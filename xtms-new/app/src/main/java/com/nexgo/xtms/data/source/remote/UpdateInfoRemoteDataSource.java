package com.nexgo.xtms.data.source.remote;

import android.support.annotation.NonNull;

import com.nexgo.xtms.data.source.repository.DeviceInfoRepository;
import com.nexgo.xtms.data.source.datasource.UpdateInfoDataSource;
import com.nexgo.xtms.mvp.base.App;
import com.nexgo.xtms.net.helper.RetrofitHelper;
import com.nexgo.xtms.net.update.UpdateRequestData;
import com.nexgo.xtms.net.update.UpdateResponseData;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.NetWorkUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhouxie on 2017/7/3.
 */

public class UpdateInfoRemoteDataSource implements UpdateInfoDataSource {
    private static UpdateInfoRemoteDataSource INSTANCE;

    private UpdateInfoRemoteDataSource() {
    }

    public static UpdateInfoRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpdateInfoRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getUpdateInfo(@NonNull LoadUpdateInfoCallback callback) {
        // 加载服务器远程数据
        if (NetWorkUtils.getNetworkTypeName(App.getInstance()).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT)) {
            LogUtil.test("    ****************************   ");
            callback.onDataNotAvailable();
        } else {
            RetrofitHelper.getTasksService()
                    .getUpdateInfo(DeviceInfoRepository.getInstance().getDeviceInfo(App.getInstance()))
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<UpdateResponseData>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull UpdateResponseData updateResponseData) {
                            callback.onUpdateInfoLoaded(updateResponseData);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            LogUtil.test("****************************   " +e.getMessage());
                            callback.onDataNotAvailable();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void getUpdateInfoLbs(@NonNull LoadUpdateInfoCallback callback, UpdateRequestData updateRequestData) {
        // 加载服务器远程数据
        if (NetWorkUtils.getNetworkTypeName(App.getInstance()).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT)) {
            callback.onDataNotAvailable();
        } else {
            RetrofitHelper.getTasksService()
                    .getUpdateInfo(DeviceInfoRepository.getInstance().getDeviceInfoLbs(App.getInstance(),updateRequestData))
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<UpdateResponseData>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull UpdateResponseData updateResponseData) {
                            callback.onUpdateInfoLoaded(updateResponseData);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            callback.onDataNotAvailable();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void getUpdateInfoRes(@NonNull LoadUpdateInfoCallback callback, UpdateRequestData updateRequestData) {
        // 加载服务器远程数据
        if (NetWorkUtils.getNetworkTypeName(App.getInstance()).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT)) {
            callback.onDataNotAvailable();
        } else {
            RetrofitHelper.getTasksService()
                    .getUpdateInfo(DeviceInfoRepository.getInstance().getDeviceInfoRes(App.getInstance(),updateRequestData))
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<UpdateResponseData>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull UpdateResponseData updateResponseData) {
                            callback.onUpdateInfoLoaded(updateResponseData);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            callback.onDataNotAvailable();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
