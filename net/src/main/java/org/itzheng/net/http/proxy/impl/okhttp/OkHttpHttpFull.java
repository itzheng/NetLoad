package org.itzheng.net.http.proxy.impl.okhttp;

import android.util.Log;

import org.itzheng.net.http.proxy.IHttpFull;
import org.itzheng.net.http.proxy.callback.IHttpCallback;
import org.itzheng.net.thread.ThreadUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Title:<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-10.
 */
public class OkHttpHttpFull implements IHttpFull {
    private static final String TAG = "OkHttpHttpFull";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private Builder mBuilder;
    /**
     * 把参数放在Map里面是因为线程切换回造成延时，如果同时执行多个请求会造成数据错乱
     */
    Map<Integer, Builder> builderMap = new HashMap<>();
    Map<Object, Call> callMap = new HashMap<>();
    private int count = 0;

    @Override
    public OkHttpHttpFull addHeaders(Map<String, Object> headers) {
        initBuilder();
        if (mBuilder.headers == null) {
            mBuilder.headers = new HashMap<>();
        }
        if (headers != null) {
            mBuilder.headers.putAll(headers);
        }
        return this;
    }

    @Override
    public OkHttpHttpFull addHeader(String key, String name) {
        initBuilder();
        if (mBuilder.headers == null) {
            mBuilder.headers = new HashMap<>();
        }
        if (key != null) {
            mBuilder.headers.put(key, name);
        }
        return this;
    }

    @Override
    public OkHttpHttpFull addParam(String key, String name) {
        initBuilder();
        if (mBuilder.params == null) {
            mBuilder.params = new HashMap<>();
        }
        if (key != null) {
            mBuilder.params.put(key, name);
        }
        return this;
    }

    @Override
    public OkHttpHttpFull addParams(Map<String, Object> params) {
        initBuilder();
        if (mBuilder.params == null) {
            mBuilder.params = new HashMap<>();
        }
        if (params != null) {
            mBuilder.params.putAll(params);
        }
        return this;
    }

    @Override
    public OkHttpHttpFull setTag(Object tag) {
        initBuilder();
        mBuilder.tag = tag;
        return this;
    }

    @Override
    public void cancel(Object tag) {
        Log.d(TAG, "cancel: ");
        if (tag != null) {
            Call call = callMap.get(tag);
            if (call != null) {
                Log.d(TAG, "call.cancel: ");
                call.cancel();
            } else {
                Log.d(TAG, "call == null: callMap.size" + callMap.size());
            }
        }
    }


    @Override
    public synchronized void exec() {
        //将builder数据存在内存中，避免数据覆盖
        final Builder finalBuilder = mBuilder;
        mBuilder = null;
        //如果是主线程，直接切换到子线程，如果是子线程，会不会造成多个方法执行时，请求等待？
        if (ThreadUtils.isMainThread()) {
            count++;
            final int currentId = count;
            builderMap.put(currentId, finalBuilder);
            //如果在主线程，则切换到子线程执行
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    Builder httpCallback = builderMap.get(currentId);
                    builderMap.remove(currentId);
                    threadExec(httpCallback);
                }
            });
        } else {
            threadExec(finalBuilder);
        }
    }

    /**
     * 初始化构造器
     */
    private void initBuilder() {
        if (mBuilder == null) {
            mBuilder = new Builder();
        }
    }

    /**
     * 在子线程执行
     *
     * @param builder
     */
    private void threadExec(Builder builder) {
        Request.Builder requestBuilder = new Request.Builder();
        Request request = null;
        switch (builder.type) {
            case TYPE_GET:
                if (builder.params == null || builder.params.isEmpty()) {
                    requestBuilder.url(builder.url);
                } else {
                    StringBuffer params = new StringBuffer();
                    for (Map.Entry<String, Object> entry : builder.params.entrySet()) {
                        if (params.length() != 0) {
                            params.append("&");
                        }
                        params.append(entry.getKey() + "=" + entry.getValue());
                    }
                    requestBuilder.url(builder.url + "?" + params.toString());
                }
                request = requestBuilder.get().build();
                execRequest(builder, request);
                break;
            case TYPE_POST:
                requestBuilder.url(builder.url);
                if (builder.json == null || "".equals(builder.json)) {
                    request = requestBuilder.get().build();
                } else {
                    RequestBody body = RequestBody.create(JSON, builder.json);
                    request = requestBuilder.post(body).build();
                }
                if (!(builder.headers == null || builder.headers.isEmpty())) {
                    //设置头部信息，如果有的话
                    for (Map.Entry<String, Object> entry : builder.headers.entrySet()) {
                        requestBuilder.addHeader(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
                    }
                }
                execRequest(builder, request);
                break;
        }
        //操作完成后，就移除tag
        if (builder.tag != null) {
            Log.d(TAG, "threadExec: " + "remove Call");
            callMap.remove(builder.tag);
        }
        builder = null;
    }

    /**
     * 执行请求操作
     *
     * @param builder
     * @param request
     */
    private void execRequest(Builder builder, Request request) {
        try {
            Call call = client.newCall(request);
            //给call添加tag
            addTagToCall(builder, call);
            Response response = call.execute();
            if (builder.callback != null) {
                if (response != null) {
                    MediaType type = response.body().contentType();
                    Log.d(TAG, "threadExec: type:" + type.type());
//                    builder.callback.onSuccess(response.body().bytes());
                    //如果直接回调Byte[]兼容性更强，但是
                    builder.callback.onSuccess(response.body().bytes());
                } else {
                    builder.callback.onError("Response is null", new NullPointerException("Response is null"));
                }
            }
        } catch (IOException e) {
//                    e.printStackTrace();
            if (builder.callback != null) {
                if ("Canceled".equals(e.getMessage())) {
                    builder.callback.onCancel(e.getMessage());
                } else {
                    builder.callback.onError(e.toString(), e);
                }

            }
        }
    }

    /**
     * 给call添加tag
     *
     * @param builder
     * @param call
     */
    private void addTagToCall(Builder builder, Call call) {
        //添加tag，方便取消
        if (builder.tag != null) {
            Log.d(TAG, "threadExec: " + "builder.tag : " + builder.tag);
            callMap.put(builder.tag, call);
        } else {
            Log.d(TAG, "threadExec: " + "tag == null");
        }
    }

    @Override
    public OkHttpHttpFull addJson(String json) {
        initBuilder();
        mBuilder.json = json;
        return this;
    }

    @Override
    public OkHttpHttpFull setHttpCallBack(IHttpCallback callBack) {
        initBuilder();
        mBuilder.callback = callBack;
        return this;
    }

    @Override
    public OkHttpHttpFull post(String url) {
        initBuilder();
        mBuilder.type = TYPE_POST;
        mBuilder.url = url;
        return this;
    }

    @Override
    public OkHttpHttpFull get(String url) {
        initBuilder();
        mBuilder.type = TYPE_GET;
        mBuilder.url = url;
        return this;
    }

    /**
     * 构造器，用于数据传递，避免线程切换造成的数据错误
     */
    protected class Builder {
        public int type;
        public String url;
        public IHttpCallback callback;
        public String json;
        public Object tag;
        public Map<String, Object> params;
        public Map<String, Object> headers;
    }
}
