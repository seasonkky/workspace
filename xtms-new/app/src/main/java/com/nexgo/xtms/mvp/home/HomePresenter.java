package com.nexgo.xtms.mvp.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nexgo.xtms.eventbus.DataEvent;
import com.nexgo.xtms.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import javax.sql.DataSource;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * Created by zhouxie on 2017/6/29.
 */

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mView;

    public HomePresenter(@NonNull Context context, @NonNull HomeContract.View view, @NonNull DataSource repository) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    public HomePresenter(@NonNull HomeContract.View view) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        EventBus.getDefault().post(new DataEvent());
    }

    @Override
    public void restart() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void loadData() {
        LogUtil.test(" loadData ");
    }
}
