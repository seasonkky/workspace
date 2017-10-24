package com.nexgo.xtms.mvp.server;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.ApiConstant;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.data.source.repository.ServerRepository;
import com.nexgo.xtms.data.source.datasource.ServerDataSource;
import com.nexgo.xtms.eventbus.ServerEvent;
import com.nexgo.xtms.net.helper.RetrofitHelper;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;
import com.nexgo.xtms.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;
import static com.nexgo.xtms.constant.EventConstant.SERVER_SETTING_EVENT;

/**
 * Created by zhouxie on 2017/6/29.
 */

public class ServerPresenter implements ServerContract.Presenter {

    private final ServerContract.View mView;
    private List<ServerModel> serverModelList;

    public static final String TAG = ServerPresenter.class.getSimpleName();

    public ServerPresenter(@NonNull Context context, @NonNull ServerContract.View view) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {

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
        mView.setLoading(true);
        ServerRepository.getInstance().getServers(new ServerDataSource.LoadServersCallback() {
            @Override
            public void onServersLoaded(List<ServerModel> serverModels) {
                serverModelList = serverModels;
                mView.refreshListOnUIThread(serverModelList);
                mView.setLoading(false);
            }

            @Override
            public void onDataNotAvailable() {
                mView.refreshListOnUIThread(serverModelList);
                mView.setLoading(false);
            }
        });
    }

    @Override
    public void onAddServerConfirm(ServerModel serverModel) {
        ServerRepository.getInstance().saveServer(serverModel);
        ServerRepository.getInstance().getServer(serverModel.getId(), new ServerDataSource.GetServerCallback() {
            @Override
            public void onServerLoaded(ServerModel serverModel) {
                PreferenceUtil.putLong(SPConstant.KEY_SERVER, serverModel.getId());
            }

            @Override
            public void onDataNotAvailable() {
                PreferenceUtil.putLong(SPConstant.KEY_SERVER, 0L);
            }
        });

        // 设置新的服务器HOST地址
        RetrofitHelper.setBaseUrl(serverModel.getUrlByModel());
        // 发起更新请求
        loadData();
    }

    @Override
    public void onItemClick(int position) {
        if (serverModelList != null && serverModelList.size() != 0) {
            if (serverModelList.get(position).getId() == PreferenceUtil.getLong(SPConstant.KEY_SERVER, 0L)) {
                mView.disMiss();
                return;
            }
            PreferenceUtil.putLong(SPConstant.KEY_SERVER, serverModelList.get(position).getId());

            // 设置新的服务器HOST地址
            RetrofitHelper.setBaseUrl(serverModelList.get(position).getUrlByModel());
            LogUtil.d(TAG, "------- Set new ServerModel Host Url = " + serverModelList.get(position).getUrlByModel());
            // 发起更新请求
            mView.setLoading(true);
            ServerRepository.getInstance().getServers(new ServerDataSource.LoadServersCallback() {
                @Override
                public void onServersLoaded(List<ServerModel> serverModels) {
                    serverModelList = serverModels;
                    mView.refreshListOnUIThread(serverModelList);
                    mView.setLoading(false);
                    mView.disMiss();
                }

                @Override
                public void onDataNotAvailable() {
                    mView.refreshListOnUIThread(serverModelList);
                    mView.setLoading(false);
                    mView.disMiss();
                }
            });

        }
    }

    @Override
    public void onItemLongClick(int position) {
        // 弹出确认框
        mView.delete(position);
    }

    @Override
    public void onDeleteServerConfirm(int position) {
        if (serverModelList != null && serverModelList.size() != 0) {
            // 不允许删除默认条目
            if (position == 0) {
                ToastUtil.ShortToast(R.string.delete_default_server_tip);
                mView.disMiss();
                return;
            }

            // 删除条目是否是当前选中的条目
            if (serverModelList.get(position).getId() == PreferenceUtil.getLong(SPConstant.KEY_SERVER, 0L)) {
                PreferenceUtil.putLong(SPConstant.KEY_SERVER, 0L);
                RetrofitHelper.setBaseUrl(ApiConstant.DEFAULT_URL);
            }
            // 删除数据库条目
            ServerRepository.getInstance().deleteServer(serverModelList.get(position).getId());
            // 重新加载数据
            loadData();
        }
    }

    @Override
    public void sendServerSettingUpdateEvent() {
        LogUtil.test("sendServerSettingUpdateEvent");
        EventBus.getDefault().post(new ServerEvent(SERVER_SETTING_EVENT));
    }
}
