package com.nexgo.xtms.task.update;

/**
 * Created by zhouxie on 2017/10/16.
 */

public interface OnNetWorkStatusCheckListener {
    void onCheckOK();

    void onCheckCancel();

    void onChcekError();
}
