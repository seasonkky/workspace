package com.nexgo.xtms.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;

/**
 * Created by xiaox on 2017/2/16.
 * <p>
 * 更换DB路径方便测试
 */

public class CustomContext extends ContextWrapper {
    private String mDbPath;

    public CustomContext(Context base, String dbPath) {
        super(base);
        mDbPath = dbPath;
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象对象
     *
     * @param name
     */
    @Override
    public File getDatabasePath(String name) {
        String dbPath = mDbPath + File.separator + name;//数据库路径
        //判断目录是否存在，不存在则创建该目录
        File dirFile = new File(mDbPath);
        if (!dirFile.exists())
            dirFile.mkdirs();

        //数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        //判断文件是否存在，不存在则创建该文件
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();//创建文件
                dbFile.setReadable(true, false);
                dbFile.setWritable(true, false);
                dbFile.setExecutable(true, false);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            isFileCreateSuccess = true;

        //返回数据库文件对象
        if (isFileCreateSuccess)
            return dbFile;
        else
            return null;

    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see ContextWrapper#openOrCreateDatabase(String, int,
     * SQLiteDatabase.CursorFactory,
     * DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}
