package com.nexgo.xtms.data.source.repository;

import android.support.annotation.NonNull;

import com.nexgo.xtms.data.source.datasource.UpdateInfoDataSource;
import com.nexgo.xtms.data.source.remote.UpdateInfoRemoteDataSource;
import com.nexgo.xtms.net.update.UpdateRequestData;

/**
 * Created by zhouxie on 2017/7/3.
 */

public class UpdateInfoRepository implements UpdateInfoDataSource {

    private static UpdateInfoRepository INSTANCE;

    private UpdateInfoRemoteDataSource updateInfoRemoteDataSource;

    private UpdateInfoRepository(UpdateInfoRemoteDataSource updateInfoRemoteDataSource) {
        this.updateInfoRemoteDataSource = updateInfoRemoteDataSource;
    }

    public static UpdateInfoRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpdateInfoRepository(UpdateInfoRemoteDataSource.getInstance());
        }
        return INSTANCE;
    }

    @Override
    public void getUpdateInfo(@NonNull LoadUpdateInfoCallback callback) {
        // 直接加载远程数据
        updateInfoRemoteDataSource.getUpdateInfo(callback);
    }

    @Override
    public void getUpdateInfoLbs(@NonNull LoadUpdateInfoCallback callback, UpdateRequestData updateRequestData) {
        // 直接加载远程数据
        updateInfoRemoteDataSource.getUpdateInfoLbs(callback, updateRequestData);
    }

    @Override
    public void getUpdateInfoRes(@NonNull LoadUpdateInfoCallback callback, UpdateRequestData updateRequestData) {
        // 直接加载远程数据
        updateInfoRemoteDataSource.getUpdateInfoRes(callback, updateRequestData);
    }
}
