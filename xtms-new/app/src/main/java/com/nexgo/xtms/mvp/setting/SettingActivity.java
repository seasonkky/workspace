package com.nexgo.xtms.mvp.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.mvp.base.BaseActivity;
import com.nexgo.xtms.mvp.server.ServerFragment;
import com.nexgo.xtms.mvp.server.ServerPresenter;
import com.nexgo.xtms.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhouxie on 2017/7/4.
 */

public class SettingActivity extends BaseActivity implements SettingContract.View {
    @BindView(R.id.tv_countryname)
    TextView tvCountryname;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.tv_reboot_active)
    TextView tvRebootActive;
    private SettingPresenter mSettingPresenter;
    @BindView(R.id.rl_server_setting)
    RelativeLayout rlServerSetting;
    @BindView(R.id.rl_download_setting)
    RelativeLayout rlDownloadSetting;
    @BindView(R.id.rl_other_func)
    RelativeLayout rlOtherFunc;
    @BindView(R.id.rl_install_setting)
    RelativeLayout rlInstallSetting;
    private ServerFragment serverFragment;
    private ServerPresenter serverPresenter;
    private SettingContract.Presenter presenter;
    private DownloadSettingFragment downloadSettingFragment;
    private LanguageSettingFragment languageSettingFragment;


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        presenter = new SettingPresenter(this);
        presenter.start();
        presenter.loadServerSettingData();

        serverFragment = ServerFragment.newInstance();
        serverPresenter = new ServerPresenter(this, serverFragment);

        downloadSettingFragment = DownloadSettingFragment.newInstance();
        languageSettingFragment = LanguageSettingFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvRebootActive.setText(PreferenceUtil.getBoolean(SPConstant.REBOOT_ACTIVE, false) ? R.string.open : R.string.close);
    }

    @Override
    public void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.setting);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @OnClick({R.id.rl_server_setting, R.id.rl_download_setting, R.id.rl_other_func, R.id.rl_install_setting, R.id.rl_version, R.id.rl_language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_server_setting:
                if (!serverFragment.isAdded()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(serverFragment, "serverFragment");
                    ft.show(serverFragment);
                    ft.commit();
                }
                break;
            case R.id.rl_download_setting:
                if (!downloadSettingFragment.isAdded()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(downloadSettingFragment, "serverFragment");
                    ft.show(downloadSettingFragment);
                    ft.commit();
                }
                break;
            case R.id.rl_other_func:
                Intent intent1 = new Intent(this, RebootSettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_install_setting:
                break;
            case R.id.rl_version:
                Intent intent2 = new Intent(this, VersionActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_language:
//                showLanguageSelect();
                if (!languageSettingFragment.isAdded()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(languageSettingFragment, "languageSettingFragment");
                    ft.show(languageSettingFragment);
                    ft.commit();
                }
                break;
        }
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onServerSettingData(ServerModel serverModel) {
        tvCountryname.setText(serverModel.getCountryname() + "   " + serverModel.getCompanyName());
        if (serverModel.getIsShowIp()) {
            tvUrl.setVisibility(View.VISIBLE);
            tvUrl.setText(serverModel.getUrlByModel());
        } else {
            tvUrl.setVisibility(View.GONE);
        }
    }
}
