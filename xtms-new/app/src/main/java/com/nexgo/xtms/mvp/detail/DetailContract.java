package com.nexgo.xtms.mvp.detail;

import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.mvp.base.BasePresenter;
import com.nexgo.xtms.mvp.base.BaseView;

/**
 * Created by zhouxie on 2017/7/17.
 */

public interface DetailContract {
    interface View extends BaseView<Presenter> {
        boolean isVisible();
        void reFreshView(DownloadTaskModel downloadTaskModel);
    }

    interface Presenter extends BasePresenter {
        void loadData();
        void click();
    }
}
