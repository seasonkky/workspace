package com.nexgo.xtms.mvp.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.nexgo.xtms.constant.LanguageConstant;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.Repository;
import com.nexgo.xtms.net.helper.OkHttp3Connection;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;

import java.util.Locale;


/**
 * Created by zhouxie on 2017/6/29.
 */

public class App extends Application {
    public static final String TAG = "xtms";

    private static App mInstance;

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LogUtil.init();
        Repository.getInstance().init(this);
        initDownloader();
        changeLan(this,PreferenceUtil.getString(SPConstant.START_LANGUAGE,""));
        // 测试环境
        PreferenceUtil.put(SPConstant.SSL_AUTH,"1");
    }

    private void initDownloader() {
        // Init the FileDownloader with the OkHttp3Connection.Creator.
        FileDownloader.setupOnApplicationOnCreate(this);
        FileDownloader.init(this, new DownloadMgrInitialParams.InitCustomMaker()
                .connectionCreator(new OkHttp3Connection.Creator()));
        // 设置默认存储根目录
//        FileDownloadUtils.setDefaultSaveRootPath(BASE_SAVE_PATH);
    }

    public static void changeLan(Context context, String language) {
        if (language == null||language.length() == 0){
            return;
        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        Locale locale = null;
        // 应用用户选择语言
        switch (language) {
            case LanguageConstant.FARSI:
                locale = new Locale("fa");
                PreferenceUtil.put(SPConstant.START_LANGUAGE,"fa");
                break;
            case LanguageConstant.CHINESE:
                locale = Locale.CHINESE;
                PreferenceUtil.put(SPConstant.START_LANGUAGE,"zh");
                break;
            case LanguageConstant.ENGLISH:
                locale = Locale.ENGLISH;
                PreferenceUtil.put(SPConstant.START_LANGUAGE,"en");
                break;
        }
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLayoutDirection(config.locale);
        }
        resources.updateConfiguration(config, dm);
    }
}
