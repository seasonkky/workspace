package com.nexgo.xtms.mvp.detail;

import android.support.annotation.NonNull;

import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.eventbus.DownloadRequestEvent;
import com.nexgo.xtms.eventbus.UpdateEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhouxie on 2017/7/17.
 */

public class DetailPresenter implements DetailContract.Presenter {

    private DownloadTaskModel downloadTaskModel;
    private int position;
    private DetailContract.View mView;
    public static final String TAG = DetailPresenter.class.getSimpleName();

    public DetailPresenter(@NonNull DetailContract.View mView, int position, DownloadTaskModel downloadTaskModel) {
        this.mView = mView;
        this.position = position;
        this.downloadTaskModel = downloadTaskModel;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        loadData();
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
    public void loadData() {
//        LogUtil.d(TAG,"load detail data = " +downloadTaskModel.toString());
        mView.reFreshView(downloadTaskModel);
    }

    @Override
    public void click() {
        switch (downloadTaskModel.getStatus()) {
            case STATUS_NOT_DOWNLOAD:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_START:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_PAUSE_EVENT, downloadTaskModel, null));
                break;
            case STATUS_CONNECTING:
                break;
            case STATUS_CONNECTTED:
                break;
            case STATUS_CONNECT_ERROR:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_DOWNLOADING:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_PAUSE_EVENT, downloadTaskModel, null));
                break;
            case STATUS_PAUSED:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_CANCELED:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_DOWNLOAD_ERROR:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
                break;
            case STATUS_COMPLETE:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_INSTALL_EVENT, downloadTaskModel, null));
                break;
            case STATUS_INSTALLED:
                break;
            case STATUS_CHECKING_ERROR:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_START_EVENT, downloadTaskModel, null));
            case STATUS_INSTALL_ERROR:
                EventBus.getDefault().post(new DownloadRequestEvent(EventConstant.DOWNLOAD_INSTALL_EVENT, downloadTaskModel, null));
                break;
        }

    }

    private long mkeyTime;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEventReceived(UpdateEvent updateEvent) {
        if (updateEvent.getTag().equals(EventConstant.UPDATE_EVENT_UPDATEFRAGMENT)) {
            // 下载进度刷新的回调 每1秒刷新一次界面
            if (mView.isVisible() && updateEvent.getDownloadTaskModelList() != null) {
                if (((System.currentTimeMillis() - mkeyTime) > 1000)) {
                    downloadTaskModel = updateEvent.getDownloadTaskModelList().get(position);
                    mkeyTime = System.currentTimeMillis();
                    mView.reFreshView(downloadTaskModel);
                }
            }
        } else if (updateEvent.getTag().equals(EventConstant.UPDATE_EVENT_UPDATEFRAGMENT_SPECIAL)) {
            // 除进度刷新之外的其他下载事件回调 不受1秒刷新影响
            downloadTaskModel = updateEvent.getDownloadTaskModelList().get(position);
            mView.reFreshView(downloadTaskModel);
        }
    }
}
