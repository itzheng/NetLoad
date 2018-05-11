package org.itzheng.net.http.proxy.callback;

/**
 * 网络请求的结果回调
 * Created by daniel on 17-4-25.
 */

public interface IHttpCallback {
    void onSuccess(String response);

    void onError(String error, Exception e);

    void onCancel(String error);
}
