package com.nexgo.xtms.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.CommonConstant;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.mvp.base.BaseActivity;
import com.nexgo.xtms.mvp.update.UpdateFragment;
import com.nexgo.xtms.mvp.update.UpdatePresenter;
import com.nexgo.xtms.util.ActivityUtils;

/**
 * Created by zhouxie on 2017/6/29.
 */

public class UpdateActivity extends BaseActivity {

    private static final String TAG = UpdateActivity.class.getSimpleName();
    private UpdatePresenter mTasksPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        // 发送启动更新服务的通知
        Intent intent = new Intent(CommonConstant.UPDATE_SERVICE_ACTION);
        sendBroadcast(intent);

        UpdateFragment tasksFragment =
                (UpdateFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = UpdateFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }
        mTasksPresenter = new UpdatePresenter(
                tasksFragment);
    }

    @Override
    public void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.update);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, UpdateActivity.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        changeLanguage(intent.getStringExtra(SPConstant.START_LANGUAGE),UpdateActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
