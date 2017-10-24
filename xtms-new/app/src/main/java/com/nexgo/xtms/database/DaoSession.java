package com.nexgo.xtms.database;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.nexgo.xtms.data.entity.CountryModel;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.data.entity.LogModel;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.data.entity.TaskModel;

import com.nexgo.xtms.database.CountryModelDao;
import com.nexgo.xtms.database.DownloadTaskModelDao;
import com.nexgo.xtms.database.LogModelDao;
import com.nexgo.xtms.database.ServerModelDao;
import com.nexgo.xtms.database.TaskModelDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig countryModelDaoConfig;
    private final DaoConfig downloadTaskModelDaoConfig;
    private final DaoConfig logModelDaoConfig;
    private final DaoConfig serverModelDaoConfig;
    private final DaoConfig taskModelDaoConfig;

    private final CountryModelDao countryModelDao;
    private final DownloadTaskModelDao downloadTaskModelDao;
    private final LogModelDao logModelDao;
    private final ServerModelDao serverModelDao;
    private final TaskModelDao taskModelDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        countryModelDaoConfig = daoConfigMap.get(CountryModelDao.class).clone();
        countryModelDaoConfig.initIdentityScope(type);

        downloadTaskModelDaoConfig = daoConfigMap.get(DownloadTaskModelDao.class).clone();
        downloadTaskModelDaoConfig.initIdentityScope(type);

        logModelDaoConfig = daoConfigMap.get(LogModelDao.class).clone();
        logModelDaoConfig.initIdentityScope(type);

        serverModelDaoConfig = daoConfigMap.get(ServerModelDao.class).clone();
        serverModelDaoConfig.initIdentityScope(type);

        taskModelDaoConfig = daoConfigMap.get(TaskModelDao.class).clone();
        taskModelDaoConfig.initIdentityScope(type);

        countryModelDao = new CountryModelDao(countryModelDaoConfig, this);
        downloadTaskModelDao = new DownloadTaskModelDao(downloadTaskModelDaoConfig, this);
        logModelDao = new LogModelDao(logModelDaoConfig, this);
        serverModelDao = new ServerModelDao(serverModelDaoConfig, this);
        taskModelDao = new TaskModelDao(taskModelDaoConfig, this);

        registerDao(CountryModel.class, countryModelDao);
        registerDao(DownloadTaskModel.class, downloadTaskModelDao);
        registerDao(LogModel.class, logModelDao);
        registerDao(ServerModel.class, serverModelDao);
        registerDao(TaskModel.class, taskModelDao);
    }
    
    public void clear() {
        countryModelDaoConfig.clearIdentityScope();
        downloadTaskModelDaoConfig.clearIdentityScope();
        logModelDaoConfig.clearIdentityScope();
        serverModelDaoConfig.clearIdentityScope();
        taskModelDaoConfig.clearIdentityScope();
    }

    public CountryModelDao getCountryModelDao() {
        return countryModelDao;
    }

    public DownloadTaskModelDao getDownloadTaskModelDao() {
        return downloadTaskModelDao;
    }

    public LogModelDao getLogModelDao() {
        return logModelDao;
    }

    public ServerModelDao getServerModelDao() {
        return serverModelDao;
    }

    public TaskModelDao getTaskModelDao() {
        return taskModelDao;
    }

}
