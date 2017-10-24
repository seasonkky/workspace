package com.nexgo.xtms.mvp.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.nexgo.xtms.R;
import com.nexgo.xtms.mvp.base.BaseActivity;
import com.nexgo.xtms.util.TimeUtils;

import butterknife.BindView;

/**
 * Created by zhouxie on 2017/9/21.
 */

public class VersionActivity extends BaseActivity {
    @BindView(R.id.version_name)
    TextView tvVersionName;
    @BindView(R.id.update_time)
    TextView tvUpdateTime;


    @Override
    public int getLayoutId() {
        return R.layout.activity_version;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        String update_version_string = getString(R.string.update_version);
        String update_time_string = getString(R.string.update_time);
        PackageManager pm = getPackageManager();
        PackageInfo pi = null;
        String versionName = getString(R.string.unknown);
        long lastUpdateTime = 0;
        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
            lastUpdateTime = pi.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVersionName.setText(String.format(update_version_string, versionName));

        String update_time = null;
        if (lastUpdateTime == 0) {
            update_time = getString(R.string.unknown);
        } else {
            update_time = TimeUtils.getTime(lastUpdateTime);
        }
        tvUpdateTime.setText(String.format(update_time_string, update_time));
    }

    @Override
    public void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.version);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
