package com.example.batrakov.imageloaderservice.loadImageTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;

import com.example.batrakov.threadtask.IServiceCallback;

/**
 * Describe task to load scaled image from external storage.
 */
public class ThumbnailTask extends Task {

    private final IServiceCallback mCallback;
    private final String mImagePath;
    private final int mTargetDensity;
    private final int mTargetWidth;

    /**
     * @param aImagePath       path to image.
     * @param aCallbackMessage callback.
     * @param aDensity         screen density.
     * @param aImageWidth      target image width in pixels.
     */
    public ThumbnailTask(String aImagePath, IServiceCallback aCallbackMessage, int aDensity, int aImageWidth) {
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
        Bitmap thumbnail = BitmapFactory.decodeFile(mImagePath, options);
        if (!Thread.currentThread().isInterrupted()) {
            try {
                mCallback.bitmapLoaded(mImagePath, thumbnail);
            } catch (RemoteException aE) {
                aE.printStackTrace();
            }
        }
    }
}
