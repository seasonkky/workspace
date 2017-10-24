package com.nexgo.xtms.mvp.server;

import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.mvp.base.BasePresenter;
import com.nexgo.xtms.mvp.base.BaseView;

import java.util.List;

/**
 * Created by zhouxie on 2017/6/29.
 */

public class ServerContract {
    interface View extends BaseView<Presenter> {
        void disMiss();

        void refreshListOnUIThread(List<ServerModel> serverModelList);

        void setLoading(boolean isLoading);

        void delete(int position);

    }

    interface Presenter extends BasePresenter {
        void loadData();

        void onAddServerConfirm(ServerModel serverModel);

        void onItemClick(int position);

        void onItemLongClick(int position);

        void onDeleteServerConfirm(int position);

        void sendServerSettingUpdateEvent();
    }
}
