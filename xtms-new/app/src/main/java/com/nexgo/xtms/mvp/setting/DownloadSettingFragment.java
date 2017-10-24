package com.nexgo.xtms.mvp.setting;

import android.view.View;

import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.mvp.base.BaseDialogFragment;
import com.nexgo.xtms.util.PreferenceUtil;
import com.nexgo.xtms.view.widget.SwitchButton;

import butterknife.BindView;

/**
 * Created by zhouxie on 2017/7/5.
 */

public class DownloadSettingFragment extends BaseDialogFragment {

    @BindView(R.id.sb_silent_download)
    SwitchButton sbSilentInstall;
    @BindView(R.id.sb_no_wifi_download)
    SwitchButton sbNoWifiDownload;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_download_setting;
    }

    public static DownloadSettingFragment newInstance() {
        return new DownloadSettingFragment();
    }

    @Override
    public void initView(View view) {
        setTitle(R.string.download_setting);
        sbSilentInstall.setChecked(PreferenceUtil.getBoolean(SPConstant.KEY_SILENT_DOWNLOAD, true));
        sbSilentInstall.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                PreferenceUtil.putBoolean(SPConstant.KEY_SILENT_DOWNLOAD, isChecked);
            }
        });
        sbNoWifiDownload.setChecked(PreferenceUtil.getBoolean(SPConstant.KEY_NO_WIFI_DOWNLOAD, false));
        sbNoWifiDownload.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                PreferenceUtil.putBoolean(SPConstant.KEY_NO_WIFI_DOWNLOAD, isChecked);
            }
        });
    }
}
