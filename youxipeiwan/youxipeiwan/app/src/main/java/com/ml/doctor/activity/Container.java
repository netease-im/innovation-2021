package com.ml.doctor.activity;

import android.app.Activity;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by zhoujianghua on 2015/7/6.
 */
public class Container {

    public final Activity activity;

    public final String account;

    public final SessionTypeEnum sessionType;

    public final boolean proxySend;

    public Container(Activity activity, String account, SessionTypeEnum sessionType) {
        this.activity = activity;
        this.account = account;
        this.sessionType = sessionType;
        this.proxySend = false;
    }

    public Container(Activity activity, String account, SessionTypeEnum sessionType,
                     boolean proxySend) {
        this.activity = activity;
        this.account = account;
        this.sessionType = sessionType;
        this.proxySend = proxySend;
    }
}
