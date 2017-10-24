package com.nexgo.xtms.mvp.setting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.eventbus.RebootEvent;
import com.nexgo.xtms.mvp.base.BaseActivity;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;
import com.nexgo.xtms.util.TimeUtils;
import com.nexgo.xtms.view.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhouxie on 2017/7/20.
 */

public class RebootSettingActivity extends BaseActivity implements OnDateSetListener {
    public static final String TAG = RebootSettingActivity.class.getSimpleName();

    @BindView(R.id.rl_time_setting)
    RelativeLayout rlTimeSetting;
    @BindView(R.id.sb_day_circulate)
    SwitchButton sbDayCirculate;
    @BindView(R.id.rl_day_circulate_setting)
    RelativeLayout rlDayCirculateSetting;
    @BindView(R.id.tv_reboot_time)
    TextView tvRebootTime;
    @BindView(R.id.sb_reboot_switch)
    SwitchButton sbRebootSwitch;
    @BindView(R.id.ll_reboot_settings)
    LinearLayout llRebootSettings;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reboot_setting;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        // 初始化重启激活设置
        boolean rebootActive = PreferenceUtil.getBoolean(SPConstant.REBOOT_ACTIVE, false);
        llRebootSettings.setVisibility(rebootActive ? View.VISIBLE : View.GONE);
        sbRebootSwitch.setChecked(rebootActive);
        sbRebootSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                PreferenceUtil.put(SPConstant.REBOOT_ACTIVE, isChecked);
                llRebootSettings.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (isChecked) {
                    EventBus.getDefault().post(new RebootEvent(EventConstant.REBOOT_SETTING_EVENT));
                } else {
                    EventBus.getDefault().post(new RebootEvent(EventConstant.REBOOT_CANCEL_EVENT));
                }
            }
        });

        // 初始化重启的时间
        long rebootTime = PreferenceUtil.getLong(SPConstant.REBOOT_TIME, 0);
        if (rebootTime == 0) {
            tvRebootTime.setText(R.string.not_set);
        } else {
            tvRebootTime.setText(TimeUtils.getTime(rebootTime));
        }

        // 初始化重启循环设置
        boolean rebootCirculate = PreferenceUtil.getBoolean(SPConstant.REBOOT_DAY_CIRCULATE, false);
        sbDayCirculate.setChecked(rebootCirculate);
        sbDayCirculate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                LogUtil.d(TAG, " set reboot circulate = " + isChecked);
                PreferenceUtil.putBoolean(SPConstant.REBOOT_DAY_CIRCULATE, isChecked);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.reboot_setting);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @OnClick({R.id.rl_time_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_time_setting:
                showTimeDialog();
                break;
        }
    }

    private long current;
    private long zero;
    private long twelve;

    private void showTimeDialog() {
        current = System.currentTimeMillis();//当前时间毫秒数
        zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        twelve = zero + 24 * 60 * 60 * 1000 + 1;//今天23点59分59秒的毫秒数
//        long yesterday=System.currentTimeMillis()-24*60*60*1000;//昨天的这一时间的毫秒数

        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getString(R.string.cancel))
                .setSureStringId(getString(R.string.ok))
                .setTitleStringId(getString(R.string.reboot_time_setting))
                .setHourText(getString(R.string.hour))
                .setMinuteText(getString(R.string.minute))
                .setCyclic(true)
                .setMinMillseconds(zero)
                .setMaxMillseconds(twelve)
                .setCurrentMillseconds(current)
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.HOURS_MINS)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(18)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "mDialogAll");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        LogUtil.test(" onDateSet = " + TimeUtils.getTime(millseconds, new SimpleDateFormat("HH:mm")));
        long settingTime;
        if (millseconds <= current) {
            // 设置时间已过，则设置为第二天的时间
            settingTime = millseconds + 24 * 60 * 60 * 1000;
        } else {
            // 今天的时间
            settingTime = millseconds;
        }
        // 存储设定的重启时间
        PreferenceUtil.putLong(SPConstant.REBOOT_TIME, settingTime);
        tvRebootTime.setText(TimeUtils.getTime(settingTime));
        // 发送设置请求
        EventBus.getDefault().post(new RebootEvent(EventConstant.REBOOT_SETTING_EVENT));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRebootEventReceived(RebootEvent rebootEvent) {
        LogUtil.d(TAG, "RebootSettingActivity onRebootEventReceived ");
        if (rebootEvent.getTag().equals(EventConstant.REBOOT_SETTING_EVENT)) {
//            initRebootAlarmManager();
        } else if (rebootEvent.getTag().equals(EventConstant.REBOOT_REQUEST_EVENT)) {
//            doForceReboot();
        } else if (rebootEvent.getTag().equals(EventConstant.REBOOT_CANCEL_EVENT)) {
            if (PreferenceUtil.getLong(SPConstant.REBOOT_TIME, 0) == 0)
                tvRebootTime.setText(R.string.not_set);
        }
    }
}
