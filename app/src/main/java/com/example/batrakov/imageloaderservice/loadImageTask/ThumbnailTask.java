package com.example.batrakov.imageloaderservice.loadImageTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Describe task to load scaled image from external storage.
 */
public class ThumbnailTask extends Task {

    private static final String IMAGE = "image";
    private static final String IMAGE_PATH = "image path";

    private final Messenger mCallback;
    private final String mImagePath;
    private final int mTargetDensity;
    private final int mTargetWidth;

    /**
     * @param aImagePath       path to image.
     * @param aCallbackMessage callback.
     * @param aDensity         screen density.
     * @param aImageWidth      target image width in pixels.
     */
    public ThumbnailTask(String aImagePath, Messenger aCallbackMessage, int aDensity, int aImageWidth) {
        mCallback = aCallbackMessage;
        mImagePath = aImagePath;
        mTargetDensity = aDensity;
        mTargetWidth = aImageWidth;
    }

    @Override
    public void process() {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inTargetDensity = mTargetDensity;
        options.inScaled = true;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImagePath, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = BitmapUtils.calculateInSampleSize(options, mTargetWidth);

        Bundle bundle = new Bundle();
        bundle.putParcelable(IMAGE, BitmapFactory.decodeFile(mImagePath, options));
        bundle.putString(IMAGE_PATH, mImagePath);

        Message msg = Message.obtain();

        msg.setData(bundle);
        try {
            mCallback.send(msg);
        } catch (RemoteException aE) {
            aE.printStackTrace();
        }
    }
}
