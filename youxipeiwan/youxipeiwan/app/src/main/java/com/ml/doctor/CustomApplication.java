package com.ml.doctor;

import android.app.Application;

import com.ml.doctor.call2.NimInitHelper;
import com.ml.doctor.utils.LocalShared;
import com.ml.doctor.utils.T;


public class CustomApplication extends Application {
    private static CustomApplication mInstance;
    public String userToken;
    public int userId;

    public static CustomApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LocalShared mLocalShared = LocalShared.getInstance(this);
        userToken = mLocalShared.getUserToken();
        userId = mLocalShared.getUserId();
        T.init(this);
        NimInitHelper.getInstance().init(this, true);
    }

}
