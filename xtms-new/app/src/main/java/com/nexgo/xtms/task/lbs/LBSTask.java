package com.nexgo.xtms.task.lbs;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nexgo.xtms.data.source.datasource.UpdateInfoDataSource;
import com.nexgo.xtms.data.source.repository.UpdateInfoRepository;
import com.nexgo.xtms.mvp.base.App;
import com.nexgo.xtms.net.update.UpdateRequestData;
import com.nexgo.xtms.net.update.UpdateResponseData;
import com.nexgo.xtms.task.power.PowerTask;
import com.nexgo.xtms.util.LogUtil;

/**
 * Created by zhouxie on 2017/9/29.
 */

public class LBSTask {
    public static final String TAG = LBSTask.class.getSimpleName();

    private static LBSTask lbsTask;
    private LocationClient mLocationClient;

    private LBSTask() {
    }

    public static LBSTask getInstance() {
        if (lbsTask == null) {
            lbsTask = new LBSTask();
        }
        return lbsTask;
    }

    public void initBaiduLBSService() {
        //声明LocationClient类
        mLocationClient = new LocationClient(App.getInstance());
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("GCJ02");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 500;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        setBdLocationListener(bdLocationListener);
    }

    private void setBdLocationListener(BDLocationListener bdLocationListener) {
        if (bdLocationListener != null) {
            this.bdLocationListener = bdLocationListener;
            mLocationClient.registerLocationListener(bdLocationListener);
        }
    }

    public void startLocate() {
        if (mLocationClient.isStarted()) {
            LogUtil.d(TAG, " *** requestLocation ***");
            mLocationClient.requestLocation();
        } else {
            LogUtil.d(TAG, " *** startLocate ***");
            mLocationClient.start();
        }
    }

    public void stopLocate() {
        if (mLocationClient.isStarted()) {
            LogUtil.d(TAG, " *** stopLocate ***");
            mLocationClient.stop();
        }
    }

    public void cancelBdLocationListener() {
        if (bdLocationListener != null) {
            mLocationClient.unRegisterLocationListener(bdLocationListener);
        }
    }

    private BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null)
                return;
            if (bdLocation.getLatitude() > 0 || bdLocation.getLongitude() > 0) {
                stopLocate();
            } else {
                mLocationClient.requestLocation();
            }
            LogUtil.d(TAG, "\ntime : " + bdLocation.getTime() + "\nerror code : " + bdLocation.getLocType() + "\nlatitude : " + bdLocation.getLatitude() +
                    "\nlontitude : " + bdLocation.getLongitude() + "\naddress : " + bdLocation.getAddrStr() + "\ncity : " + bdLocation.getCity() + "\ncityCode : " + bdLocation.getCityCode());

            UpdateRequestData updateRequestData = new UpdateRequestData();
            UpdateRequestData.LocationBean locationBean = new UpdateRequestData.LocationBean();
            locationBean.setLatitude("" + bdLocation.getLatitude());
            locationBean.setLongitude("" + bdLocation.getLongitude());
            locationBean.setAddress(bdLocation.getAddrStr());
            locationBean.setCity(bdLocation.getCity());
            locationBean.setCitycode(bdLocation.getCityCode());
            updateRequestData.setLocation(locationBean);

            // 上送地理位置数据
            UpdateInfoRepository.getInstance().getUpdateInfoLbs(new UpdateInfoDataSource.LoadUpdateInfoCallback() {
                @Override
                public void onUpdateInfoLoaded(UpdateResponseData updateResponseData) {
                    // 上送完成
                    LogUtil.d(TAG, "Lbs data upload success !");
                    PowerTask.getInstance().release();
                    stopLocate();
                }

                @Override
                public void onDataNotAvailable() {
                    // 上送失败
                    LogUtil.d(TAG, "Lbs data upload fail !");
                    PowerTask.getInstance().release();
                    stopLocate();
                }
            }, updateRequestData);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };
}
