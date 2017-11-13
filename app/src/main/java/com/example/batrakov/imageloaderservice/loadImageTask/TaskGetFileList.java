package com.example.batrakov.imageloaderservice.loadImageTask;

import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Task for getting list of files name from service.
 */

public class TaskGetFileList extends Task {

    private static final String FILES_PATH_LIST = "file path list";
    private static final String FILES_NAME_LIST = "file name list";
    private static final String TAG = Task.class.getSimpleName();

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
            final ArrayList<String> filesNameList = new ArrayList<>();
            for (File file : listFiles) {
                filesPathList.add(file.getPath());
                filesNameList.add(file.getName());
            }

            Bundle bundle = new Bundle();
            bundle.putStringArrayList(FILES_PATH_LIST, filesPathList);
            bundle.putStringArrayList(FILES_NAME_LIST, filesNameList);

            msg.setData(bundle);
        } else {
            msg.setData(null);
        }
        if (!Thread.currentThread().isInterrupted()) {
            try {
                mCallback.send(msg);
                Log.i(TAG, "process: ");
            } catch (RemoteException aE) {
                aE.printStackTrace();
            }
        }
    }
}
