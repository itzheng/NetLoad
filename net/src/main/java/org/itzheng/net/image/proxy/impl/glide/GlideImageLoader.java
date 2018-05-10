package org.itzheng.net.image.proxy.impl.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import org.itzheng.net.image.proxy.IImageLoader;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Title:<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-9.
 */
public class GlideImageLoader implements IImageLoader {
    /**
     * 默认的错误图标
     */
    public static Drawable defError;
    /**
     * 默认的占位图标
     */
    public static Drawable defPlace;

    /**
     * 基于哪里的加载
     */
    Object mContext;
    /**
     * 加载的资源
     */
    Object mLoad;
    /**
     * 加载错误的图案
     */
    Drawable mError;
    /**
     * 加载占位符
     */
    Drawable mPlace;

    @Override
    public IImageLoader with(Object context) {
        mContext = context;
        return this;
    }

    @Override
    public IImageLoader load(Object imageUrl) {
        mLoad = imageUrl;
        return this;
    }


    @Override
    public void into(ImageView imageView) {
        if (imageView == null) {
            //ImageView 没有，就不需要插入
            throw new NullPointerException("imageView is null object");
        }
        RequestManager requestManager = null;
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(mPlace)
                .error(mError);
        if (mContext == null) {
            mContext = imageView.getContext();
        }
        if (mContext instanceof Context) {
            requestManager = Glide.with((Context) mContext);
        }
        requestManager = requestManager.applyDefaultRequestOptions(options);
        RequestBuilder<Drawable> requestBuilder = null;
        //根据加载不同资源文件调用不同方法
        if (mLoad == null) {

        } else if (mLoad instanceof String) {
            requestBuilder = requestManager.load((String) mLoad);
        } else if (mLoad instanceof Integer) {
            requestBuilder = requestManager.load((Integer) mLoad);
        } else if (mLoad instanceof byte[]) {
            requestBuilder = requestManager.load((byte[]) mLoad);
        }
        //插入imageView中
        if (requestBuilder != null) {
            requestBuilder.transition(withCrossFade());
            requestBuilder.into(imageView);
        } else {
            //如果有错误，显示错误图片，如果没有错误图片则显示加载图片，或者不显示图片
            if (imageView != null) {
                if (mError != null) {
                    imageView.setImageDrawable(mError);
                } else if (mPlace != null) {
                    imageView.setImageDrawable(mPlace);
                }
            }
        }
        //into 后还原默认值
        restore();
    }

    private void restore() {
        mContext = null;
        mLoad = null;
        mError = defError;
        mPlace = defPlace;
    }

    @Override
    public IImageLoader error(Drawable drawable) {
        mError = drawable;
        return this;
    }

    @Override
    public IImageLoader place(Drawable drawable) {
        mPlace = drawable;
        return this;
    }

    @Override
    public IImageLoader setDefError(Drawable drawable) {
        defError = drawable;
        mError = defError;
        return this;
    }

    @Override
    public IImageLoader setDefPlace(Drawable drawable) {
        defPlace = drawable;
        mPlace = defPlace;
        return this;
    }

}
