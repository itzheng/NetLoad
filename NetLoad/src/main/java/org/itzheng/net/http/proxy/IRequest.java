package org.itzheng.net.http.proxy;

import org.itzheng.net.http.proxy.callback.IHttpCallback;
import org.itzheng.net.http.proxy.impl.IBaseHttp;

/**
 * Title:发送网络请求的一些基本接口，比如post，get，delete等等<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-10.
 */
public interface IRequest extends IBaseHttp {
    /**
     * 请求类型：get
     */
    int TYPE_GET = 0;
    /**
     * 请求类型：post
     */
    int TYPE_POST = 1;

    IRequest addJson(String json);

    /**
     * 如果单独设置callback的话，会造成引用错误，如果多次请求肯定会出现问题的
     * 这也是为什么之前回调都放在exec里面的原因
     *
     * @param callBack
     * @return
     */
    IRequest setHttpCallBack(IHttpCallback callBack);

    IRequest post(String url);

    IRequest get(String url);
}
