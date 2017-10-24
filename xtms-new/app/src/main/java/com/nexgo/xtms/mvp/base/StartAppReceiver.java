package com.nexgo.xtms.mvp.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nexgo.xtms.XTMSService;
import com.nexgo.xtms.util.LogUtil;

public class StartAppReceiver extends BroadcastReceiver {
    public static final String TAG = StartAppReceiver.class.getSimpleName();

    public StartAppReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        LogUtil.d(TAG,"StartAppReceiver onReceive action" +intent.getAction());
        Intent serviceIntent = new Intent(context, XTMSService.class);
        context.startService(serviceIntent);
    }
}
