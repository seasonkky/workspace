package com.nexgo.xtms.mvp.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.eventbus.BroadCastEvent;
import com.nexgo.xtms.eventbus.RebootEvent;
import com.nexgo.xtms.eventbus.ScreenEvent;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.NetWorkUtils;

import org.greenrobot.eventbus.EventBus;

import static android.content.Intent.ACTION_BATTERY_CHANGED;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.nexgo.xtms.constant.BroadcastConstant.ACTION_FORCE_REBOOT;
import static com.nexgo.xtms.constant.BroadcastConstant.ACTION_UPDATE_DEVICE_INF;

/**
 * Created by zhouxie on 2017/7/6.
 */

public class UpdateReceiver extends BroadcastReceiver {
    public static final String TAG = UpdateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case CONNECTIVITY_ACTION:
                // 网络连接广播
                if (!NetWorkUtils.isNetworkAvailable(context)) {
                    LogUtil.d(TAG, "network is not available");
                    return;
                }
                if (!NetWorkUtils.isWifi(context)) {
                    // 如果是移动网络，发送更新请求事件，在Service内处理弹窗
                    EventBus.getDefault().post(new BroadCastEvent(EventConstant.BROADCAST_REQUEST_DOWNLOAD));
                    return;
                }
                LogUtil.d(TAG, "net work connnected，continue download ");
                EventBus.getDefault().post(new BroadCastEvent(EventConstant.BROADCAST_RESTART_DOWNLOAD));
                break;
            case ACTION_BATTERY_CHANGED:
                // 电池电量变化
                int power = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                EventBus.getDefault().post(new BroadCastEvent(EventConstant.BROADCAST_POWER_CHANGE, power));
//                LogUtil.d(TAG, "power change = " + power);
                break;
            case ACTION_SCREEN_OFF:
                // 屏幕灭
                EventBus.getDefault().post(new ScreenEvent(EventConstant.SCREEN_EVENT,false));
                break;
            case ACTION_SCREEN_ON:
                // 屏幕亮
                EventBus.getDefault().post(new ScreenEvent(EventConstant.SCREEN_EVENT,true));
                break;
            case ACTION_UPDATE_DEVICE_INF:
                // 申请更新
                EventBus.getDefault().post(new BroadCastEvent(EventConstant.BROADCAST_UPDATE_DEVICE_INF));
                break;
            case ACTION_FORCE_REBOOT:
                // 强制重启
                LogUtil.d(TAG,"broadcast received action_force_reboot ");
                EventBus.getDefault().post(new RebootEvent(EventConstant.REBOOT_REQUEST_EVENT));
                break;
        }
    }
}
