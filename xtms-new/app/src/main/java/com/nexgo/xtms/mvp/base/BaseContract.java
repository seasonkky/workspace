package com.nexgo.xtms.mvp.base;

/**
 * Created by zhouxie on 2017/6/29.
 */

public interface BaseContract {

     interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter>{

    }

}
