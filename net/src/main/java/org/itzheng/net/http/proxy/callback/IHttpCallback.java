package org.itzheng.net.http.proxy.callback;

/**
 * 网络请求的结果回调
 * Created by daniel on 17-4-25.
 */

public interface IHttpCallback {
    /**
     * 请求结果，默认传输bytes数组，子类可实现转成字符串，这样兼容性比较强，可以直接对文件进行读取写入操作
     * 如果返回的是String结果，那么再转成byte[]可能会因为换行符等原因造成数据错误
     *
     * @param bytes
     */
    void onSuccess(byte[] bytes);

    void onError(String error, Exception e);

    void onCancel(String error);
}
