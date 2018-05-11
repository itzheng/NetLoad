package org.itzheng.net.http.helper;

import android.util.Log;

import org.itzheng.net.http.proxy.IHttpFull;
import org.itzheng.net.http.proxy.IRequest;
import org.itzheng.net.http.proxy.callback.IHttpCallback;
import org.itzheng.net.http.proxy.impl.IBaseHttp;

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

    public static HttpFullHelper getInstance() {
        if (_instance == null) {
            _instance = new HttpFullHelper();
        }
        return _instance;
    }

    public static void init(IHttpFull httpFull) {
        mHttpFull = httpFull;
    }


    @Override
    public IBaseHttp addHeaders(Map<String, Object> headers) {
        return mHttpFull.addHeaders(headers);
    }

    @Override
    public IBaseHttp addHeader(String key, String name) {
        return mHttpFull.addHeader(key, name);
    }

    @Override
    public IBaseHttp setTag(Object tag) {
        return mHttpFull.setTag(tag);
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
    public IRequest addJson(String json) {
        return mHttpFull.addJson(json);
    }

    @Override
    public IRequest setHttpCallBack(final IHttpCallback callBack) {
        return mHttpFull.setHttpCallBack(callBack);
    }

    @Override
    public IRequest post(String url) {
        return mHttpFull.post(url);
    }

    @Override
    public IRequest get(String url) {
        return mHttpFull.get(url);
    }
}
