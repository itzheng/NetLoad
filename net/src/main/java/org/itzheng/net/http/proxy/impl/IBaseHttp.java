package org.itzheng.net.http.proxy.impl;

import java.util.Map;

/**
 * Title:<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-10.
 */
public interface IBaseHttp {
    IBaseHttp addHeaders(Map<String, Object> headers);

    IBaseHttp addHeader(String key, String name);

    /**
     * 设置tag
     *
     * @param tag
     * @return
     */
    IBaseHttp setTag(Object tag);

    /**
     * 根据tag取消操作，有些可能未实现
     *
     * @param tag
     */
    void cancel(Object tag);

    /**
     * 执行操作
     */
    void exec();
}
