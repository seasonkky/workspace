package com.nexgo.xtms.data.source.datasource;

import android.content.Context;

import com.nexgo.xtms.net.update.UpdateRequestData;

/**
 * Created by xiaox on 2017/1/17.
 */

public interface DeviceInfoDataSource {

    UpdateRequestData getDeviceInfo(Context context);

    UpdateRequestData getDeviceInfoLbs(Context context ,UpdateRequestData updateRequestData);

    UpdateRequestData getDeviceInfoRes(Context context, UpdateRequestData updateRequestData);
}
