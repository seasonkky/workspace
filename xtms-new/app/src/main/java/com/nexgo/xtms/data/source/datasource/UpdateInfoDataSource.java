package com.nexgo.xtms.data.source.datasource;

import android.support.annotation.NonNull;

import com.nexgo.xtms.net.update.UpdateRequestData;
import com.nexgo.xtms.net.update.UpdateResponseData;

/**
 * Created by zhouxie on 2017/7/3.
 */

public interface UpdateInfoDataSource {
    interface LoadUpdateInfoCallback {

        void onUpdateInfoLoaded(UpdateResponseData updateResponseData);

        void onDataNotAvailable();
    }

    void getUpdateInfo(@NonNull LoadUpdateInfoCallback callback);

    void getUpdateInfoLbs(@NonNull LoadUpdateInfoCallback callback, UpdateRequestData updateRequestData);

    void getUpdateInfoRes(@NonNull LoadUpdateInfoCallback callback, UpdateRequestData updateRequestData);
}
