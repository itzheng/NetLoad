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
        return this;
    }

    @Override
    public OkHttpHttpFull addHeader(String key, String name) {
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
    public void exec() {
        if (ThreadUtils.isMainThread()) {
            count++;
            final int currentId = count;
            builderMap.put(currentId, mBuilder);
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
            threadExec(mBuilder);
        }
        mBuilder = null;
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
    private void threadExec(final Builder builder) {
        Request.Builder requestBuilder = new Request.Builder().url(builder.url);
        switch (builder.type) {
            case TYPE_GET:
                try {
                    Request request = requestBuilder.get().build();
                    Call call = client.newCall(request);
                    //添加tag，方便取消
                    if (builder.tag != null) {
                        Log.d(TAG, "threadExec: " + "builder.tag : " + builder.tag);
                        callMap.put(builder.tag, call);
                    } else {
                        Log.d(TAG, "threadExec: " + "tag == null");
                    }
                    Response response = call.execute();
                    if (builder.callback != null) {
                        if (response != null) {
                            builder.callback.onSuccess(response.body().string());
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
                break;
            case TYPE_POST:
                try {
                    RequestBody body = RequestBody.create(JSON, builder.json);
                    Request request = requestBuilder.post(body).build();
                    Response response = client.newCall(request).execute();
                    if (builder.callback != null) {
                        if (response != null) {
                            builder.callback.onSuccess(response.body().string());
                        } else {
                            builder.callback.onError("Response is null", new NullPointerException("Response is null"));
                        }
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                    if (builder.callback != null) {
                        builder.callback.onError(e.toString(), e);
                    }
                }
                break;
        }
        if (builder.tag != null) {
            //操作完成后，就移除tag
            Log.d(TAG, "threadExec: " + "remove Call");
            callMap.remove(builder.tag);
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
    }
}
