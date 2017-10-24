package com.nexgo.xtms.data.source.local;

import android.content.Context;

import com.nexgo.xtms.data.Repository;
import com.nexgo.xtms.data.source.datasource.DeviceInfoDataSource;
import com.nexgo.xtms.database.DownloadTaskModelDao;
import com.nexgo.xtms.net.helper.NEXGODriverHelper;
import com.nexgo.xtms.net.update.UpdateRequestData;
import com.nexgo.xtms.util.LogUtil;

/**
 * Created by zhouxie on 2017/6/30.
 */

public class DeviceInfoLocalDataSource implements DeviceInfoDataSource {

    private static DeviceInfoLocalDataSource INSTANCE;
    private DownloadTaskModelDao taskModelDao;

    // Prevent direct instantiation.
    private DeviceInfoLocalDataSource() {
        taskModelDao = Repository.getInstance().getmDaoSession().getDownloadTaskModelDao();
    }

    public static DeviceInfoLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DeviceInfoLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public UpdateRequestData getDeviceInfo(Context context) {
        NEXGODriverHelper nexgoDriverHelper = NEXGODriverHelper.getInstance(context);
        LogUtil.test(" --- getDeviceInfo ---");
        UpdateRequestData deviceInf = new UpdateRequestData();
        deviceInf.setSN(nexgoDriverHelper.getSN());
        deviceInf.setKSN(nexgoDriverHelper.getKSN());
        deviceInf.setApp(nexgoDriverHelper.getAppInfList());
        deviceInf.setOta(nexgoDriverHelper.getOTAVersion());
        deviceInf.setParamInfo(nexgoDriverHelper.getParamInf());
        deviceInf.setLocation(nexgoDriverHelper.getLocation());
        deviceInf.setSim(nexgoDriverHelper.getSim());
        deviceInf.setUpdateType(UpdateRequestData.TYPE_SIGN_IN);
        return deviceInf;
    }

    @Override
    public UpdateRequestData getDeviceInfoLbs(Context context,UpdateRequestData updateRequestData) {
        NEXGODriverHelper nexgoDriverHelper = NEXGODriverHelper.getInstance(context);
        LogUtil.test(" --- getDeviceInfo LBS ---");
        UpdateRequestData.LocationBean location = updateRequestData.getLocation();
        location.setSim(nexgoDriverHelper.getSim());
        UpdateRequestData deviceInf = getDeviceInfo(context);
        deviceInf.setUpdateType(UpdateRequestData.TYPE_ADDRESS);
        deviceInf.setLocation(location);
        return deviceInf;
    }

    @Override
    public UpdateRequestData getDeviceInfoRes(Context context,UpdateRequestData updateRequestData) {
        UpdateRequestData deviceInf = getDeviceInfo(context);
        deviceInf.setUpdateType(UpdateRequestData.TYPE_RESULT);
        return deviceInf;
    }
}
