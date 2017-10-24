package com.nexgo.xtms.mvp.setting;

import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.mvp.base.BasePresenter;
import com.nexgo.xtms.mvp.base.BaseView;

/**
 * Created by zhouxie on 2017/7/4.
 */

public class SettingContract {
    interface View extends BaseView<Presenter> {
        void onServerSettingData(ServerModel serverModel);
    }

    interface Presenter extends BasePresenter {
        void loadServerSettingData();
    }
}
