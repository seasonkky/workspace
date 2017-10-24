package com.nexgo.xtms.task.reboot;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.WindowManager;

import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.eventbus.RebootEvent;
import com.nexgo.xtms.mvp.base.App;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;
import com.nexgo.xtms.util.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;
import static com.nexgo.xtms.constant.BroadcastConstant.ACTION_FORCE_REBOOT;
import static com.nexgo.xtms.constant.SPConstant.REBOOT_ACTIVE;
import static com.nexgo.xtms.constant.SPConstant.REBOOT_DAY_CIRCULATE;
import static com.nexgo.xtms.constant.SPConstant.REBOOT_TIME;
import static com.nexgo.xtms.mvp.server.ServerFragment.TAG;

/**
 * Created by zhouxie on 2017/10/14.
 */

public class RebootTask {
    private static RebootTask rebootTask ;
    private PendingIntent rebootSender;
    private Context context;

    private RebootTask() {
    }

    public static RebootTask getInstance() {
        if (rebootTask == null) {
            rebootTask = new RebootTask();
        }
        return rebootTask;
    }

    public void init(Context context){
        this.context = context;
    }

    /**
     * 重启设置
     */
    // 取消重启设置
    public void cancelRebootAlarmManager() {
        if (rebootSender != null) {
            AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            manager.cancel(rebootSender);
        }
    }

    // 重启初始化
    public void initRebootAlarmManager() {
        // 获取重启激活状态
        boolean rebootActive = PreferenceUtil.getBoolean(REBOOT_ACTIVE, false);
        LogUtil.d(TAG, "get reboot active = " + rebootActive);
        if (!rebootActive) {
            //未激活则直接返回
            return;
        }
        // 获取重启时间
        long rebootTime = PreferenceUtil.getLong(REBOOT_TIME, 0);
        LogUtil.d(TAG, "get reboot time = " + TimeUtils.getTime(rebootTime));
        // 获取重启循环设置
        boolean rebootCirculate = PreferenceUtil.getBoolean(REBOOT_DAY_CIRCULATE, false);
        LogUtil.d(TAG, "get reboot circulate = " + rebootCirculate);

        if (rebootTime == 0) {
            // 未设置重启时间，直接返回
            LogUtil.d(TAG, " reboot time not set!");
        } else {
            // 已设置过重启时间
            if (rebootTime <= System.currentTimeMillis()) {
                // 重启设定时间少于等于当前时间，则是已重启过
                if (rebootCirculate) {
                    // 设置下一次重启时间为当前设置时间的24小时后
                    rebootTime += (24 * 3600 * 1000);
                    PreferenceUtil.putLong(REBOOT_TIME, rebootTime);
                    LogUtil.d(TAG, " set reboot time = " + TimeUtils.getTime(rebootTime));
                    setRebootTime(Integer.parseInt(TimeUtils.getHour(rebootTime)), Integer.parseInt(TimeUtils.getMinute(rebootTime)));
                } else {
                    // 不循环则直接返回，并清除当前重启时间设置
                    PreferenceUtil.remove(REBOOT_TIME);
                    EventBus.getDefault().post(new RebootEvent(EventConstant.REBOOT_CANCEL_EVENT));
                    LogUtil.d(TAG, " do not circulate ,clear reboot time set! ");
                }
            } else {
                // 重启时间大于当前时间，则是未重启过，设置重启
                LogUtil.d(TAG, " set reboot time =  " + TimeUtils.getTime(rebootTime));
                setRebootTime(Integer.parseInt(TimeUtils.getHour(rebootTime)), Integer.parseInt(TimeUtils.getMinute(rebootTime)));
            }
        }
    }

    public void setRebootTime(int hour, int minute) {
        cancelRebootAlarmManager();
        Intent intent = new Intent(ACTION_FORCE_REBOOT);
        rebootSender = PendingIntent.getBroadcast(context, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getDefault());

        // 设定每天定时重启的时间
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime >= selectTime) {
//            Toast.makeText(this, "设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }

        // 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;

        LogUtil.d(TAG, "time ==== " + time + ", selectTime ===== "
                + selectTime + ", systemTime ==== " + systemTime + ", firstTime === " + firstTime + "   " + TimeUtils.getTime(firstTime));
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                firstTime, rebootSender);
    }

    public void requestForceReboot() {
        LogUtil.d(TAG, " requestForceReboot ");
        RebootService rebootTask = new RebootService(App.getInstance());
        rebootTask.bindAidlService();
    }

    public void showRebootRequestDialog() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LogUtil.d(TAG, " screen is on ,show dialog do reboot !");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage(R.string.reboot_now_tip);
                builder.setTitle(R.string.prompt);
                builder.setNegativeButton(R.string.reboot, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.d(TAG, " reboot now ");
                        RebootTask.getInstance().requestForceReboot();
                    }
                });
                builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.d(TAG, " cancel reboot ");
                        // 设置下一次重启时间
                        RebootTask.getInstance().initRebootAlarmManager();
                    }
                });

                AlertDialog d = builder.create();
                d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                d.show();
            }
        });
    }
}
