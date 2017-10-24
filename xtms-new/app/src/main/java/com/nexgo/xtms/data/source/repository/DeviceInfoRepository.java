package com.nexgo.xtms.data.source.repository;

import android.content.Context;

import com.nexgo.xtms.data.source.datasource.DeviceInfoDataSource;
import com.nexgo.xtms.data.source.local.DeviceInfoLocalDataSource;
import com.nexgo.xtms.net.update.UpdateRequestData;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * Created by zhouxie on 2017/7/3.
 */

public class DeviceInfoRepository implements DeviceInfoDataSource {

    private static DeviceInfoRepository INSTANCE;
    private  DeviceInfoLocalDataSource deviceInfoLocalDataSource;

    private DeviceInfoRepository(DeviceInfoLocalDataSource deviceInfoLocalDataSource) {
        this.deviceInfoLocalDataSource = deviceInfoLocalDataSource;
    }

    public static DeviceInfoRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DeviceInfoRepository(DeviceInfoLocalDataSource.getInstance());
        }
        return INSTANCE;
    }

    // 暂时不做持久化存储，只在内存中存储
    @Override
    public UpdateRequestData getDeviceInfo(Context context) {
        checkNotNull(context);
        checkNotNull(deviceInfoLocalDataSource);
        return deviceInfoLocalDataSource.getDeviceInfo(context);
    }

    @Override
    public UpdateRequestData getDeviceInfoLbs(Context context, UpdateRequestData updateRequestData) {
        checkNotNull(context);
        checkNotNull(deviceInfoLocalDataSource);
        return deviceInfoLocalDataSource.getDeviceInfoLbs(context,updateRequestData);
    }

    @Override
    public UpdateRequestData getDeviceInfoRes(Context context, UpdateRequestData updateRequestData) {
        checkNotNull(context);
        checkNotNull(deviceInfoLocalDataSource);
        return deviceInfoLocalDataSource.getDeviceInfoRes(context,updateRequestData);
    }

}
