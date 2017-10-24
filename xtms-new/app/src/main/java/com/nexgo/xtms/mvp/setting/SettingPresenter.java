package com.nexgo.xtms.mvp.setting;

import android.support.annotation.NonNull;

import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.eventbus.ServerEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * Created by zhouxie on 2017/7/4.
 */

public class SettingPresenter implements SettingContract.Presenter {
    private SettingContract.View mView;

    public SettingPresenter(@NonNull SettingContract.View view) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void restart() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadServerSettingData() {
        ServerModel activeServer = com.nexgo.xtms.data.source.repository.ServerRepository.getInstance().getActiveServer();
        mView.onServerSettingData(activeServer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerEventReceived(ServerEvent serverEvent){
        if (serverEvent.getTag().equals(EventConstant.SERVER_SETTING_EVENT)){
            loadServerSettingData();
        }
    }
}
