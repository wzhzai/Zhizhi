package com.cleverzone.zhizhi.application;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by WANGZHENGZE on 2015/5/4.
 */
public class ZApp extends Application {
    private static ZApp instance = null;
    public ImageLoader mImageLoader;


    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
    }

    public static void setInstance(ZApp instance) {
        ZApp.instance = instance;
    }

    public static ZApp getInstance() {
        return instance;
    }

}
