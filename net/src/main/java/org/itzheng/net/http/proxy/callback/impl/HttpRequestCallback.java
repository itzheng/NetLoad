package org.itzheng.net.http.proxy.callback.impl;

/**
 * Title:<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-15.
 */
public abstract class HttpRequestCallback extends HttpCallback {
    /**
     * 如果对byte结果进行截取，则不能对String进行操作
     *
     * @param bytes
     */
    @Override
    public void onSuccess(byte[] bytes) {
        onSuccess(new String(bytes));
    }

    /**
     * 结果只是将Byte转成String  ==> new String(bytes)
     *
     * @param response
     */
    public void onSuccess(String response) {

    }

    @Override
    public void onError(String error, Exception e) {

    }

    @Override
    public void onCancel(String error) {

    }
}
