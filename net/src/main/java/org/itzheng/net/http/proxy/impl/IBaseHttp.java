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
    /**
     * 添加头部集合，如果已经有头部集合数据将会进行合并，如果相同key，则后面的会覆盖之前数据
     *
     * @param headers
     * @return
     */
    IBaseHttp addHeaders(Map<String, Object> headers);

    /**
     * 添加单个头部信息，和之前的头部信息进行合并
     *
     * @param key
     * @param name
     * @return
     */
    IBaseHttp addHeader(String key, String name);

    /**
     * 添加单个请求参数，和之前的请求参数进行合并
     *
     * @param key
     * @param name
     * @return
     */
    IBaseHttp addParam(String key, String name);

    /**
     * 添加请求参数集合，会和之前的请求参数进行合并，如果相同key，则后面添加的会覆盖前面的数据
     *
     * @param params
     * @return
     */
    IBaseHttp addParams(Map<String, Object> params);

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
