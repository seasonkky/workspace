package com.nexgo.xtms.mvp.update;

import android.support.annotation.NonNull;

import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.eventbus.UpdateEvent;
import com.nexgo.xtms.eventbus.UpdateRequestEvent;
import com.nexgo.xtms.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * Created by zhouxie on 2017/6/29.
 */

public class UpdatePresenter implements UpdateContract.Presenter {

    private final UpdateContract.View mView;
    public static final String TAG = UpdatePresenter.class.getSimpleName();

    public UpdatePresenter(@NonNull UpdateContract.View view) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        mView.loading();
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
        LogUtil.d(TAG, "loadData post requestUpdateEvent");
        EventBus.getDefault().post(new UpdateRequestEvent(EventConstant.UPDATE_REQUEST_EVENT));
    }

    private long mkeyTime;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEventReceived(UpdateEvent updateEvent) {
        LogUtil.d(TAG, "UpdatePresenter onUpdateEventReceived ");
        List<DownloadTaskModel> downloadTaskModelList;
        if (updateEvent.getTag().equals(EventConstant.UPDATE_EVENT_UPDATEFRAGMENT)) {
            // 下载进度刷新的回调 每1秒刷新一次界面
            if (mView.isVisible() && updateEvent.getDownloadTaskModelList() != null && !mView.isRefreshing()) {
                if (((System.currentTimeMillis() - mkeyTime) > 1000)) {
                    mkeyTime = System.currentTimeMillis();
                    downloadTaskModelList = updateEvent.getDownloadTaskModelList();
                    mView.refreshRecyclerView(downloadTaskModelList);
                }
            }
        } else if (updateEvent.getTag().equals(EventConstant.UPDATE_EVENT_UPDATEFRAGMENT_SPECIAL)) {
            // 除进度刷新之外的其他下载事件回调 不受1秒刷新影响
            LogUtil.d(TAG," onUpdateEventReceived update event special");
            downloadTaskModelList = updateEvent.getDownloadTaskModelList();
            mView.refreshRecyclerView(downloadTaskModelList);
        } else if (updateEvent.getTag().equals(EventConstant.REFRESH_COMPLETE)) {
            // 刷新完成事件
            if (updateEvent.getDownloadTaskModelList() != null && updateEvent.getDownloadTaskModelList().size() > 0) {
                downloadTaskModelList = updateEvent.getDownloadTaskModelList();
                mView.refreshRecyclerView(downloadTaskModelList);
                mView.dataLoadSuccess();
            } else {
                mView.empty();
            }
        } else if (updateEvent.getTag().equals(EventConstant.REFRESH_ERROR)) {
            // 刷新失败事件
            mView.error(EventConstant.REFRESH_ERROR);
        } else if(updateEvent.getTag().equals(EventConstant.NET_4G_ERROR)){
            mView.error(EventConstant.NET_4G_ERROR);
        }
    }
}
