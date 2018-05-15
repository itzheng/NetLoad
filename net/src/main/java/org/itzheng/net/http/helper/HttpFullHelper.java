package org.itzheng.net.http.helper;

import android.util.Log;

import org.itzheng.net.http.proxy.IHttpFull;
import org.itzheng.net.http.proxy.IRequest;
import org.itzheng.net.http.proxy.callback.IHttpCallback;
import org.itzheng.net.http.proxy.callback.impl.HttpCallback;
import org.itzheng.net.http.proxy.impl.IBaseHttp;
import org.itzheng.net.http.proxy.impl.okhttp.OkHttpHttpFull;
import org.itzheng.net.thread.ThreadUtils;

import java.util.Map;

/**
 * Title:<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-10.
 */
public class HttpFullHelper implements IHttpFull {
    private static IHttpFull mHttpFull;
    private static HttpFullHelper _instance;

    /**
     * 获取对象实例
     *
     * @return
     */
    public static HttpFullHelper getInstance() {
        if (_instance == null) {
            _instance = new HttpFullHelper();
        }
        return _instance;
    }

    static {
        //设置默认工具
        if (mHttpFull == null) {
            mHttpFull = new OkHttpHttpFull();
        }
    }

    public static void init(IHttpFull httpFull) {
        mHttpFull = httpFull;
    }


    @Override
    public HttpFullHelper addHeaders(Map<String, Object> headers) {
        mHttpFull.addHeaders(headers);
        return this;
    }

    @Override
    public HttpFullHelper addHeader(String key, String name) {
        mHttpFull.addHeader(key, name);
        return this;
    }

    @Override
    public HttpFullHelper addParam(String key, String name) {
        mHttpFull.addParam(key, name);
        return this;
    }

    @Override
    public HttpFullHelper addParams(Map<String, Object> params) {
        mHttpFull.addParams(params);
        return this;
    }

    @Override
    public HttpFullHelper setTag(Object tag) {
        mHttpFull.setTag(tag);
        return this;
    }

    @Override
    public void cancel(Object tag) {
        Log.d(getClass().getName(), "cancel: ");
        mHttpFull.cancel(tag);
    }

    @Override
    public void exec() {
        mHttpFull.exec();
    }

    @Override
    public HttpFullHelper addJson(String json) {
        mHttpFull.addJson(json);
        return this;
    }

    @Override
    public HttpFullHelper setHttpCallBack(final IHttpCallback callBack) {
        mHttpFull.setHttpCallBack(new MainThreadHttpCallback(callBack));
        return this;
    }

    @Override
    public HttpFullHelper post(String url) {
        mHttpFull.post(url);
        return this;
    }

    @Override
    public HttpFullHelper get(String url) {
        mHttpFull.get(url);
        return this;
    }

    /**
     * 将回调切换到主线程
     */
    public class MainThreadHttpCallback implements IHttpCallback {
        private IHttpCallback callBack;

        public MainThreadHttpCallback(IHttpCallback callback) {
            this.callBack = callback;
        }

        @Override
        public void onSuccess(final byte[] bytes) {
            if (callBack != null) {
                if (ThreadUtils.isMainThread()) {
                    callBack.onSuccess(bytes);
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(bytes);
                        }
                    });
                }
            }
        }

        @Override
        public void onError(final String error, final Exception e) {
            if (callBack != null) {
                if (ThreadUtils.isMainThread()) {
                    callBack.onError(error, e);
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(error, e);
                        }
                    });
                }
            }
        }

        @Override
        public void onCancel(final String error) {
            if (callBack != null) {
                if (ThreadUtils.isMainThread()) {
                    callBack.onCancel(error);
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onCancel(error);
                        }
                    });
                }
            }
        }
    }
}
