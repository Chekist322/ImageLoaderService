package com.example.batrakov.imageloaderservice.loadImageTask;

import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.io.File;
import java.util.ArrayList;

/**
 * Task for getting list of files name from service.
 */

public class TaskGetFileList extends Task {

    private static final String FILES_PATH_LIST = "file path list";

    private Messenger mCallback;

    /**
     * Constructor.
     *
     * @param aCallback callback to calling app.
     */
    public TaskGetFileList(Messenger aCallback) {
        mCallback = aCallback;
    }

    @Override
    public void process() {
        File sImagesDirectory = new File(Environment.getExternalStorageDirectory() + "/images");
        Message msg = Message.obtain();
        if (sImagesDirectory.listFiles() != null) {
            File[] listFiles = sImagesDirectory.listFiles();
            final ArrayList<String> filesPathList = new ArrayList<>();
            for (File file : listFiles) {
                filesPathList.add(file.getPath());
            }

            Bundle bundle = new Bundle();
            bundle.putStringArrayList(FILES_PATH_LIST, filesPathList);

            msg.setData(bundle);
        } else {
            msg.setData(null);
        }

        try {
            mCallback.send(msg);
        } catch (RemoteException aE) {
            aE.printStackTrace();
        }
    }
}
