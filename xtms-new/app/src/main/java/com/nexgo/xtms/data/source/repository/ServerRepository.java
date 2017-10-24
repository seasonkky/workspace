package com.nexgo.xtms.data.source.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nexgo.xtms.R;
import com.nexgo.xtms.data.Repository;
import com.nexgo.xtms.data.entity.CountryModel;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.data.source.datasource.ServerDataSource;
import com.nexgo.xtms.data.source.local.ServerLocalDataSource;
import com.nexgo.xtms.data.source.remote.ServerRemoteDataSource;
import com.nexgo.xtms.mvp.base.App;
import com.nexgo.xtms.net.helper.NEXGODriverHelper;
import com.nexgo.xtms.net.server.ServerBean;
import com.nexgo.xtms.util.CountryUtil;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.ResourceUtils;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by zhouxie on 2017/7/4.
 */

public class ServerRepository implements ServerDataSource {
    public static final String TAG = ServerRepository.class.getSimpleName();

    private static ServerRepository INSTANCE;
    private ServerRemoteDataSource serverRemoteDataSource;
    private ServerLocalDataSource serverLocalDataSource;

    private ServerRepository(ServerRemoteDataSource serverRemoteDataSource, ServerLocalDataSource serverLocalDataSource) {
        this.serverRemoteDataSource = serverRemoteDataSource;
        this.serverLocalDataSource = serverLocalDataSource;
        initServerData();
    }

    // 初始化更新服务器
    private void initServerData() {
        //  初始化国家代码和名称
        String json = ResourceUtils.geFileFromAssets(App.getInstance(), "country.json");
        if (json != null) {
            List<CountryModel> countryModelList = new Gson().fromJson(json, new TypeToken<List<CountryModel>>() {
            }.getType());

            Repository.getInstance().getCountryModelDao().insertOrReplaceInTx(countryModelList);
        }
        // 获取机器的默认地区和地址并匹配
        String otaName = NEXGODriverHelper.getInstance(App.getInstance()).getOTAName();
        switch (otaName){
            case "pab":
                serverLocalDataSource.setDefaultServer(new ServerModel(0L, "平安", App.getInstance().getString(R.string.default_), "CN", "https://218.17.132.167:6643/", null, false));
                LogUtil.d(TAG," setDefaultServer = pab");
                break;
            case "exadigm":
                serverLocalDataSource.setDefaultServer(new ServerModel(0L, "ExaDigm", App.getInstance().getString(R.string.default_), "USA", "https://74.212.223.230:6634/", null, false));
                LogUtil.d(TAG," setDefaultServer = exadigm");
                break;
            default:
                serverLocalDataSource.setDefaultServer(new ServerModel(0L, "NEXGO", App.getInstance().getString(R.string.default_), "CN", "https://s.inextpos.com:6643/", null, false));
                LogUtil.d(TAG," setDefaultServer = nexgo");
                break;
        }
//        mDaoSession.getServerModelDao().insertOrReplace(new ServerModel(1L, getString(R.string.america), "1", null, "https://74.212.223.230:6634/"));

    }

    public static ServerRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerRepository(ServerRemoteDataSource.getInstance(), ServerLocalDataSource.getInstance());
        }
        return INSTANCE;
    }

    @Override
    public void getServers(@NonNull LoadServersCallback callback) {
        // 拉取远程原始数据
        serverRemoteDataSource.getRemoteServerBean(new LoadRemoteServerBeanCallback() {
            @Override
            public void onServersLoaded(ServerBean[] serverBeenArr) {
                fetchDBData(serverBeenArr);
                serverLocalDataSource.getServers(new LoadServersCallback() {
                    @Override
                    public void onServersLoaded(List<ServerModel> serverModelList) {
                        callback.onServersLoaded(serverModelList);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
                // 仅显示本地数据
                serverLocalDataSource.getServers(new LoadServersCallback() {
                    @Override
                    public void onServersLoaded(List<ServerModel> serverModelList) {
                        callback.onServersLoaded(serverModelList);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    public void fetchDBData(ServerBean[] serverBeenArr) {
        // 将服务器拉取的数据加入数据库
        if (serverBeenArr != null && serverBeenArr.length > 0) {
            LogUtil.d(TAG, " serverBeenArr length " + serverBeenArr.length);

            for (ServerBean serverBean : serverBeenArr) {
                LogUtil.d(TAG, " serverModelList addUpgradeServer " + serverBean.getCountryCode() + " serverBeanId = " + serverBean.getUniqueID());
                // 服务器数据及默认URL不显示
                ServerModel serverModel = new ServerModel(Long.parseLong(serverBean.getUniqueID()), serverBean.getCompanyName(), CountryUtil.getCountryNameFromCountryCode(App.getInstance(), serverBean.getCountryCode()), serverBean.getCountryCode(), serverBean.getDomain(), serverBean.getServerIP(), false);
                saveServer(serverModel);
            }
        }
    }

    @Override
    public void getServer(@NonNull long id, @NonNull GetServerCallback callback) {
        serverLocalDataSource.getServer(id, callback);
    }

    @Override
    public void saveServer(@NonNull ServerModel serverModel) {
        serverLocalDataSource.saveServer(serverModel);
    }

    @Override
    public void deleteServer(@NonNull long id) {
        serverLocalDataSource.deleteServer(id);
    }

    @Override
    public void setActiveServer(@NonNull long id) {
        serverLocalDataSource.setActiveServer(id);
    }

    @Override
    public ServerModel getActiveServer() {
        return serverLocalDataSource.getActiveServer();
    }

    @Override
    public ServerModel getDefaultServer() {
        return serverLocalDataSource.getDefaultServer();
    }

    @Override
    public void setDefaultServer(ServerModel serverModel) {
        serverLocalDataSource.setDefaultServer(serverModel);
    }

    @Override
    public void getRemoteServerBean(@NonNull LoadRemoteServerBeanCallback callback) {

    }
}
