package com.nexgo.xtms.data.source.remote;

import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.data.source.datasource.ServerDataSource;
import com.nexgo.xtms.net.helper.RetrofitHelper;
import com.nexgo.xtms.net.server.ServerBean;
import com.nexgo.xtms.util.LogUtil;

import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhouxie on 2017/7/4.
 */

public class ServerRemoteDataSource implements ServerDataSource {
    public static final String TAG = ServerRemoteDataSource.class.getSimpleName();

    private static ServerRemoteDataSource INSTANCE ;

    public static ServerRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getRemoteServerBean(@NonNull LoadRemoteServerBeanCallback callback) {
        RetrofitHelper.getServerListService()
                .getServerList()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ServerBean[]>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ServerBean[] serverBeenArr) {
                        LogUtil.d(TAG , " ServerBean Arr = " + Arrays.toString(serverBeenArr));
                        callback.onServersLoaded(serverBeenArr);
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

    @Override
    public void getServers(@NonNull LoadServersCallback callback) {

    }

    @Override
    public void getServer(@NonNull long id, @NonNull GetServerCallback callback) {

    }

    @Override
    public void saveServer(@NonNull ServerModel serverModel) {

    }

    @Override
    public void deleteServer(@NonNull long id) {


    }

    @Override
    public void setActiveServer(@NonNull long id) {

    }

    @Override
    public ServerModel getActiveServer() {
        return null;
    }

    @Override
    public ServerModel getDefaultServer() {
        return null;
    }

    @Override
    public void setDefaultServer(ServerModel serverModel) {

    }
}
