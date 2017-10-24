package com.nexgo.xtms.mvp.home;

import com.nexgo.xtms.mvp.base.BasePresenter;
import com.nexgo.xtms.mvp.base.BaseView;

/**
 * Created by zhouxie on 2017/6/29.
 */

public interface HomeContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void loadData();
    }
}
