package com.nexgo.xtms.task.md5;

import com.liulishuo.filedownloader.FileDownloader;
import com.nexgo.xtms.R;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.net.helper.MD5Helper;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.ToastUtil;


/**
 * Created by zhouxie on 2017/10/13.
 */

public class MD5CheckTask {
    public static final String TAG = MD5CheckTask.class.getSimpleName();

    private static MD5CheckTask md5CheckTask;

    private MD5CheckTask() {
    }

    public static MD5CheckTask getInstance() {
        if (md5CheckTask == null) {
            md5CheckTask = new MD5CheckTask();
        }
        return md5CheckTask;
    }

    public void checkMd5(DownloadTaskModel downloadTaskModel, MD5CheckListener md5CheckListener) {
        if (downloadTaskModel == null || md5CheckListener == null) {
            LogUtil.d(TAG, " md5 check task or md5CheckListener is null !");
            return;
        }
        //已经在校验或者安装状态则直接返回
        LogUtil.d(TAG, "check MD5 task status = " + downloadTaskModel.getStatus().name());
        if (downloadTaskModel.getStatus().equals(DownloadTaskModel.DownloadStatus.STATUS_CHECKING) || downloadTaskModel.getStatus().equals(DownloadTaskModel.DownloadStatus.STATUS_INSTALLING)) {
            ToastUtil.ShortToast(R.string.check_install_tip);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开始校验
                LogUtil.d(TAG, " DownloadTaskModel md5 check start  = " + downloadTaskModel.getSavePath());
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_CHECKING);
                md5CheckListener.start();

                LogUtil.d(TAG, " DownloadTaskModel savePath = " + downloadTaskModel.getSavePath());
                String md5Sum = MD5Helper.md5sum(downloadTaskModel.getSavePath());
                if (downloadTaskModel.getMd5() == null || md5Sum == null) {
                    LogUtil.d(TAG, "server MD5 is null or file path error");
                    boolean clear = FileDownloader.getImpl().clear(downloadTaskModel.getId(), downloadTaskModel.getSavePath());
                    LogUtil.d(TAG, " FileDownloader clear ok  = " + clear);
                    // 校验失败
                    downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_CHECKING_ERROR);
                    md5CheckListener.error();
                    return;
                }
                if (downloadTaskModel.getMd5() != null && !downloadTaskModel.getMd5().toUpperCase().equals(md5Sum.toUpperCase())) {
                    // MD5校验出错
                    LogUtil.d(TAG, "MD5 check error，redownload task");
                    boolean clear = FileDownloader.getImpl().clear(downloadTaskModel.getId(), downloadTaskModel.getSavePath());
                    LogUtil.d(TAG, " FileDownloader clear ok  = " + clear);
                    // 校验失败
                    downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_CHECKING_ERROR);
                    md5CheckListener.error();
                    //TODO 校验失败后的上送处理
                    return;
                }
                LogUtil.d(TAG, "----- MD5 check ok -----");
                md5CheckListener.success();
            }
        }).start();
    }

}
