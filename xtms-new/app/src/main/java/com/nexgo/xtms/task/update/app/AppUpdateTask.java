package com.nexgo.xtms.task.update.app;

import android.content.Context;

import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.net.helper.NEXGODriverHelper;
import com.nexgo.xtms.task.ErrorCode;

/**
 * Created by fangqiang_001 on 2017/2/14.
 */

public class AppUpdateTask {
    private DownloadTaskModel downloadTaskModel;
    private Context context;
    private OnListenner onListenner;
    private STATUES statues = STATUES.NOP;


    public AppUpdateTask(Context context, DownloadTaskModel downloadTaskModel, OnListenner onListenner) {
        this.context = context.getApplicationContext();
        this.downloadTaskModel = downloadTaskModel;
        this.onListenner = onListenner;
    }

    // 安装apk
    public void installApk(String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NEXGODriverHelper.getInstance(context).installApp(path, new NEXGODriverHelper.InstallAppLisnner() {
                    @Override
                    public void onInstallResult(int result) {
                        if (result == 0) {  // 安装成功
                            statues = STATUES.DONE;
                            onListenner.onDone();
                            return;
                        }
                        statues = STATUES.INSTALLFAIL;  // 安装失败
                        onListenner.onFail();
                    }
                });
            }
        }).start();
    }


    // 回调信息
    public interface OnListenner {
        void onDone();

        void onFail();

        void onError(ErrorCode errorCode);
    }

    // 任务状态信息
    public enum STATUES {
        NOP, READY, DOWNLOADING, INSTALLING, USERNOTPERMISION, INSTALLFAIL, DONE, DOWNLOADFAIL, MD5FAIL
    }
}
