package com.nexgo.xtms.mvp.update;

import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.mvp.base.BasePresenter;
import com.nexgo.xtms.mvp.base.BaseView;

import java.util.List;

/**
 * Created by zhouxie on 2017/6/29.
 */

interface UpdateContract {
    interface View extends BaseView<Presenter> {

        void refreshRecyclerView(List<DownloadTaskModel> downloadTaskModelList);

        boolean isVisible();

        boolean isRefreshing();

        void empty();

        void error(String error);

        void loading();

        void dataLoadSuccess();
    }

    interface Presenter extends BasePresenter {
        void loadData();
    }
}
