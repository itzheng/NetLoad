package org.itzheng.net.image.helper;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.itzheng.net.image.proxy.IImageLoader;

/**
 * Title:<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-9.
 */
public class ImageLoaderHelper implements IImageLoader {
    private static IImageLoader mImageLoader;
    private static ImageLoaderHelper _instance;

    public static ImageLoaderHelper getInstance() {
        if (_instance == null) {
            _instance = new ImageLoaderHelper();
        }
        return _instance;
    }

    public static void init(IImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    @Override
    public IImageLoader with(Object context) {
        return mImageLoader.with(context);
    }

    @Override
    public IImageLoader load(Object imageUrl) {
        return mImageLoader.load(imageUrl);
    }

    @Override
    public void into(ImageView imageView) {
        mImageLoader.into(imageView);
    }

    @Override
    public IImageLoader error(Drawable error) {
        return mImageLoader.error(error);
    }

    @Override
    public IImageLoader place(Drawable drawable) {
        return mImageLoader.place(drawable);
    }

    @Override
    public IImageLoader setDefError(Drawable drawable) {
        return mImageLoader.setDefError(drawable);
    }

    @Override
    public IImageLoader setDefPlace(Drawable drawable) {
        return mImageLoader.setDefPlace(drawable);
    }

}
