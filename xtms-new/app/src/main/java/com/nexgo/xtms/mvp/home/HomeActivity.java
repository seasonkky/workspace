package com.nexgo.xtms.mvp.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.nexgo.xtms.R;
import com.nexgo.xtms.mvp.base.BaseActivity;
import com.nexgo.xtms.util.ActivityUtils;
import com.nexgo.xtms.util.LogUtil;

/**
 * Created by zhouxie on 2017/6/29.
 */

public class HomeActivity extends BaseActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private HomePresenter mhomePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        HomeFragment homeFragment =
                (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (homeFragment == null) {
            // Create the fragment
            homeFragment = HomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, R.id.contentFrame);
        }

        mhomePresenter = new HomePresenter(
                homeFragment);
    }

    @Override
    public void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.home);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, HomeActivity.class);
    }

}
