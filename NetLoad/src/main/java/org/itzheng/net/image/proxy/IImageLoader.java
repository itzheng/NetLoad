package org.itzheng.net.image.proxy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Title:<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-9.
 */
public interface IImageLoader {
    /**
     * 在什么里面添加，可以是Content ，Activity ，Fragment ，view ...
     *
     * @param context
     * @return
     */
    IImageLoader with(Object context);

    /**
     * 加载什么内容，可以是网络地址，也可以是byte[] 也可以是资源文件id
     *
     * @param imageUrl
     * @return
     */
    IImageLoader load(Object imageUrl);

    /**
     * 要显示的ImageView
     *
     * @param imageView
     */
    void into(ImageView imageView);

    /**
     * 错误时显示的图片
     *
     * @param drawable
     * @return
     */
    IImageLoader error(Drawable drawable);

    /**
     * 加载时显示的图片
     *
     * @param drawable
     * @return
     */
    IImageLoader place(Drawable drawable);

    IImageLoader setDefError(Drawable drawable);

    IImageLoader setDefPlace(Drawable drawable);
}
