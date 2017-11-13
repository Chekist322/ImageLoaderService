package com.example.batrakov.imageloaderservice.loadImageTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;

import com.example.batrakov.imageloaderservice.ImageTaskService;
import com.example.batrakov.threadtask.IServiceCallback;

/**
 * Describe task to load image in it base configuration.
 */
public class ImageLoaderTask extends Task {

    private final IServiceCallback mCallback;
    private final String mImagePath;

    /**
     * Constructor.
     *
     * @param aImageName       path to image from external storage.
     * @param aCallbackMessage callback to parent.
     */
    public ImageLoaderTask(String aImageName, IServiceCallback aCallbackMessage) {
        mCallback = aCallbackMessage;
        mImagePath = ImageTaskService.PATH_TO_IMAGES + aImageName;
    }

    @Override
    public void process() {
        Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
        if (!Thread.currentThread().isInterrupted()) {
            try {
                mCallback.bitmapLoaded(mImagePath, bitmap);
            } catch (RemoteException aE) {
                aE.printStackTrace();
            }
        }
    }
}
