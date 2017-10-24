package com.nexgo.xtms.data.source.local;

import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.Repository;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.data.source.datasource.ServerDataSource;
import com.nexgo.xtms.database.ServerModelDao;
import com.nexgo.xtms.util.PreferenceUtil;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by zhouxie on 2017/7/4.
 */

public class ServerLocalDataSource implements ServerDataSource {
    private static ServerLocalDataSource INSTANCE;
    private final ServerModelDao serverModelDao;

    public ServerLocalDataSource() {
        serverModelDao = Repository.getInstance().getServerModelDao();
    }

    public static ServerLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getRemoteServerBean(@NonNull LoadRemoteServerBeanCallback callback) {
        // Not Required
    }

    @Override
    public void getServers(@NonNull LoadServersCallback callback) {
        List<ServerModel> serverModels = serverModelDao.loadAll();
        callback.onServersLoaded(serverModels);
    }

    @Override
    public void getServer(@NonNull long id, @NonNull GetServerCallback callback) {
        List<ServerModel> list = serverModelDao.queryBuilder().where(ServerModelDao.Properties.Id.eq(id)).list();
        if (list != null && list.size() > 0) {
            callback.onServerLoaded(list.get(0));
        }
    }

    @Override
    public void saveServer(@NonNull ServerModel serverModel) {
        serverModelDao.insertOrReplace(serverModel);
    }

    @Override
    public void deleteServer(@NonNull long id) {
        List<ServerModel> list = serverModelDao.queryBuilder().where(ServerModelDao.Properties.Id.eq(id)).list();
        if (list != null && list.size() > 0) {
            serverModelDao.delete(list.get(0));
        }
    }

    @Override
    public void setActiveServer(@NonNull long id) {
        PreferenceUtil.putLong(SPConstant.KEY_SERVER, id);
    }

    @Override
    public ServerModel getActiveServer() {
        long id = PreferenceUtil.getLong(SPConstant.KEY_SERVER, 0L);
        List<ServerModel> list = serverModelDao.queryBuilder().where(ServerModelDao.Properties.Id.eq(id)).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public ServerModel getDefaultServer() {
        List<ServerModel> list = serverModelDao.queryBuilder().where(ServerModelDao.Properties.Id.eq(0L)).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void setDefaultServer(ServerModel serverModel) {
        serverModel.setId(0L);
        serverModelDao.insertOrReplace(serverModel);
    }
}
