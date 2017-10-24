package com.nexgo.xtms.data.source.datasource;

import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.net.server.ServerBean;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by zhouxie on 2017/7/4.
 */

public interface ServerDataSource {

    interface LoadServersCallback {

        void onServersLoaded(List<ServerModel> serverModelList);

        void onDataNotAvailable();
    }

    interface GetServerCallback {

        void onServerLoaded(ServerModel serverModel);

        void onDataNotAvailable();
    }

    interface LoadRemoteServerBeanCallback {

        void onServersLoaded(ServerBean[] serverBeenArr);

        void onDataNotAvailable();
    }

    void getRemoteServerBean(@NonNull LoadRemoteServerBeanCallback callback);

    void getServers(@NonNull LoadServersCallback callback);

    void getServer(@NonNull long id, @NonNull GetServerCallback callback);

    void saveServer(@NonNull ServerModel serverModel);

    void deleteServer(@NonNull long id);

    void setActiveServer(@NonNull long id);

    ServerModel getActiveServer();

    ServerModel getDefaultServer();

    void setDefaultServer(ServerModel serverModel);
}
