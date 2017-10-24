package com.nexgo.xtms.task.power;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by zhouxie on 2017/10/14.
 */

public class PowerTask {
    public static final String TAG = PowerTask.class.getSimpleName();
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    private static PowerTask powerTask;

    private PowerTask() {
    }

    public static PowerTask getInstance() {
        if (powerTask == null) {
            powerTask = new PowerTask();
        }
        return powerTask;
    }

    public void init(Context context) {
        // 初始化电源管理服务
        pm = (PowerManager) context.getSystemService(
                Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE,
                TAG);
    }

    public void acquire() {
        // 唤醒机器
        if (wakeLock != null && !wakeLock.isHeld())
            wakeLock.acquire();
    }

    public void release() {
        // 释放唤醒
        if (wakeLock != null && wakeLock.isHeld())
            wakeLock.release();
    }

    public boolean isScreenOn() {
        return pm.isScreenOn();
    }
}
