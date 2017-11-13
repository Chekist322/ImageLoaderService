package com.example.batrakov.imageloaderservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.batrakov.imageloaderservice.loadImageTask.ImageLoaderTask;
import com.example.batrakov.imageloaderservice.loadImageTask.Task;
import com.example.batrakov.imageloaderservice.loadImageTask.TaskGetFileList;
import com.example.batrakov.imageloaderservice.loadImageTask.TaskManager;
import com.example.batrakov.imageloaderservice.loadImageTask.ThumbnailTask;

import java.lang.ref.WeakReference;

/**
 * Service rule ThreadManager and provide work between MainActivity to loader threads.
 */
public class ImageTaskService extends Service {

    private static final String IMAGE_PATH = "image path";
    private static final String TARGET_MSG = "target msg";
    private static final int MSG_ADD_THUMBNAIL_TASK = 0;
    private static final int MSG_ADD_BIG_TASK = 1;
    private static final int MSG_REQUEST_FILE_LIST_TASK = 2;
    private static final String TAG = ImageTaskService.class.getSimpleName();
    private final Messenger mMessenger = new Messenger(new IncomingHandler(this));

    private static final int AMOUNT_OF_THREADS = 4;

    private final TaskManager mTaskManager = new TaskManager(AMOUNT_OF_THREADS);

    static class IncomingHandler extends Handler {
        WeakReference<ImageTaskService> mReference;

        IncomingHandler(ImageTaskService aService) {
            mReference = new WeakReference<>(aService);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle msgData = msg.getData();
            switch (msg.what) {
                case MSG_ADD_THUMBNAIL_TASK:
                    if (mReference.get() != null && msgData != null) {
                        mReference.get().addTask(new ThumbnailTask(msgData.getString(IMAGE_PATH),
                                (Messenger) msg.getData().getParcelable(TARGET_MSG), msg.arg1, msg.arg2));
                    }
                    break;
                case MSG_ADD_BIG_TASK:
                    if (mReference.get() != null && msgData != null) {
                        mReference.get().addTask(new ImageLoaderTask(msgData.getString(IMAGE_PATH),
                                (Messenger) msg.getData().getParcelable(TARGET_MSG)));
                    }
                    break;
                case MSG_REQUEST_FILE_LIST_TASK:
                    if (mReference.get() != null && msgData != null) {
                        mReference.get().addTask(new TaskGetFileList((Messenger) msgData.getParcelable(TARGET_MSG)));
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent aIntent) {
        Log.i(TAG, "onBind: ");
        return mMessenger.getBinder();
    }

    /**
     * Add task to TaskManager.
     *
     * @param aTask target task.
     */
    public void addTask(Task aTask) {
        mTaskManager.addTask(aTask);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved: ");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mTaskManager.clear();
    }
}
