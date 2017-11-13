package com.example.batrakov.imageloaderservice.loadImageTask;

import android.os.Environment;
import android.os.RemoteException;

import java.io.File;
import java.util.ArrayList;

import com.example.batrakov.threadtask.IServiceCallback;

/**
 * Task for getting list of files name from service.
 */

public class TaskGetFileList extends Task {

    private IServiceCallback mCallback;

    /**
     * Constructor.
     *
     * @param aCallback callback to main app.
     */
    public TaskGetFileList(IServiceCallback aCallback) {
        mCallback = aCallback;
    }

    @Override
    public void process() {
        File sImagesDirectory = new File(Environment.getExternalStorageDirectory() + "/images");

        ArrayList<String> filesNameList = new ArrayList<>();
        ArrayList<String> filesPathList = new ArrayList<>();

        if (sImagesDirectory.listFiles() != null) {
            File[] listFiles = sImagesDirectory.listFiles();
            for (File file : listFiles) {
                filesPathList.add(file.getPath());
                filesNameList.add(file.getName());
            }

            if (!Thread.currentThread().isInterrupted()) {
                try {
                    mCallback.listsLoaded(filesPathList, filesNameList);
                } catch (RemoteException aE) {
                    aE.printStackTrace();
                }
            }
        } else {
            try {
                mCallback.listsLoaded(null, null);
            } catch (RemoteException aE) {
                aE.printStackTrace();
            }
        }

    }
}
