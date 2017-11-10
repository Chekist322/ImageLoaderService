package com.example.batrakov.imageloaderservice.loadImageTask;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Describe task to load image in it base configuration.
 */
public class ImageLoaderTask extends Task {

    private static final String IMAGE = "image";
    private final Messenger mCallback;
    private final String mImagePath;

    /**
     * Constructor.
     *
     * @param aImagePath       path to image from external storage.
     * @param aCallbackMessage callback to parent.
     */
    public ImageLoaderTask(String aImagePath, Messenger aCallbackMessage) {
        mCallback = aCallbackMessage;
        mImagePath = aImagePath;
    }

    @Override
    public void process() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(IMAGE, BitmapFactory.decodeFile(mImagePath));

        Message msg = Message.obtain();
        msg.setData(bundle);
        try {
            mCallback.send(msg);
        } catch (RemoteException aE) {
            aE.printStackTrace();
        }
    }
}
