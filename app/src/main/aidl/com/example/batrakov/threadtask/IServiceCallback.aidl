// ServiceAidlInteface.aidl
package com.example.batrakov.threadtask;

// Declare any non-default types here with import statements

interface IServiceCallback {

    oneway void bitmapLoaded(in String aPath, in Bitmap aBitmap);

    oneway void listsLoaded(in List<String> aPathList, in List<String> aNameList);

}
