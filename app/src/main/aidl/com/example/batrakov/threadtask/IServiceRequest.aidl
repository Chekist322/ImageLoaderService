// AidlServiceListenerInterface.aidl
package com.example.batrakov.threadtask;

// Declare any non-default types here with import statements

import com.example.batrakov.threadtask.IServiceCallback;

interface IServiceRequest {

    oneway void addThumbnailTask(in String aPath,in IServiceCallback aCallback, int aDensity, int aWidth);

    oneway void addBigTask(in String aPath,in IServiceCallback aCallback);

    oneway void addListTask(in IServiceCallback aCallback);

}
