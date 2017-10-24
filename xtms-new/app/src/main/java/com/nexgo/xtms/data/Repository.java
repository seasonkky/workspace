package com.nexgo.xtms.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nexgo.xtms.constant.DBConstant;
import com.nexgo.xtms.database.CountryModelDao;
import com.nexgo.xtms.database.DaoMaster;
import com.nexgo.xtms.database.DaoSession;
import com.nexgo.xtms.database.ServerModelDao;

/**
 * Created by xiaox on 2017/1/17.
 */

public class Repository {

    private DaoSession mDaoSession;
    private static Repository INSTANCE = null;

    public static Repository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

    public void init(Context context) {
//        SQLiteDatabase db = new DaoMaster.DevOpenHelper(new CustomContext(context, Environment.getExternalStorageDirectory().getPath()), DBConstant.DB_NAME, null).getWritableDatabase();
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, DBConstant.DB_NAME, null).getWritableDatabase();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public ServerModelDao getServerModelDao() {
        return mDaoSession.getServerModelDao();
    }
//
//    public LogModelDao getLogModelDao() {
//        return mDaoSession.getLogModelDao();
//    }
    public CountryModelDao getCountryModelDao(){
        return mDaoSession.getCountryModelDao();
    }
}
