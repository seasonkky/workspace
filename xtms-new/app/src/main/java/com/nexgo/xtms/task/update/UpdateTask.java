package com.nexgo.xtms.task.update;

import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.CommonConstant;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.data.source.repository.DownloadTasksRepository;
import com.nexgo.xtms.mvp.base.App;
import com.nexgo.xtms.util.FileUtils;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;
import com.nexgo.xtms.util.ToastUtil;

import java.util.List;

/**
 * Created by zhouxie on 2017/9/27.
 */

public class UpdateTask {
    public static final String TAG = UpdateTask.class.getSimpleName();

    private List<DownloadTaskModel> downloadTaskModelList;
    private FileDownloadListener fileDownloadListener;
    private String taskId;

    private UpdateTask() {
    }

    public UpdateTask(String taskId, List<DownloadTaskModel> downloadTaskModelList, FileDownloadListener fileDownloadListener) {
        this.taskId = taskId;
        this.downloadTaskModelList = downloadTaskModelList;
        this.fileDownloadListener = fileDownloadListener;
    }

    public String getTaskId() {
        return taskId;
    }

    public void addDownloadTasks(List<DownloadTaskModel> downloadTaskModelList) {
        // 先过滤并删除掉旧任务
        if (this.downloadTaskModelList.size() > 0) {
            LogUtil.d(TAG, " start task clear this.downloadTaskModelList.size() =  " + this.downloadTaskModelList.size());
            for (int i = this.downloadTaskModelList.size() - 1; i >= 0; i--) {
                DownloadTaskModel downloadTaskModel1 = this.downloadTaskModelList.get(i);
                boolean delete = true;
                if (downloadTaskModelList == null || downloadTaskModelList.size() == 0) {
                    delete = true;
                } else {
                    for (DownloadTaskModel downloadTaskModel2 : downloadTaskModelList) {
                        if (downloadTaskModel1.getUrl().equals(downloadTaskModel2.getUrl())) {
                            delete = false;
                        }
                    }
                }
                if (delete) {
                    this.downloadTaskModelList.remove(downloadTaskModel1);
                    boolean success = FileDownloader.getImpl().clear(downloadTaskModel1.getId(), downloadTaskModel1.getSavePath());
                    LogUtil.d(TAG, " downloadTaskModelList clear task = " + downloadTaskModel1.getUrl() + "  delete success = " + success);
                }
            }
        }

        LogUtil.d(TAG, " downloadTaskModelList clear task complete downloadTaskModelList.size() = " + this.downloadTaskModelList.size());
        for (DownloadTaskModel downloadTaskModel : downloadTaskModelList) {
            addDownloadTask(downloadTaskModel);
        }
    }


    public void addDownloadTask(DownloadTaskModel downloadTaskModel) {
        if (!isTaskExist(downloadTaskModel)) {
            LogUtil.d(TAG, " downloadTaskModelList.add = " + downloadTaskModel.getUrl());
            downloadTaskModelList.add(downloadTaskModel);
        }
        // 检查默认下载设置
        LogUtil.d(TAG, " KEY_SILENT_DOWNLOAD = " + PreferenceUtil.getBoolean(SPConstant.KEY_SILENT_DOWNLOAD, true));

        // 静默升级和强制升级不需要询问用户
        if (PreferenceUtil.getBoolean(SPConstant.KEY_SILENT_DOWNLOAD, true) || downloadTaskModel.getUpdateType().equals(DownloadTaskModel.UpdateType.ForceUpdate)) {
            startDownloadTask(downloadTaskModel, fileDownloadListener);
        } else {
            // 非静默升级提示用户去升级
            ToastUtil.ShortToast(R.string.check_update_task);
        }
    }

    public void startDownloadTask(DownloadTaskModel downloadTaskModel, FileDownloadListener downloadListener) {
        FileDownloader.getImpl().create(downloadTaskModel.getUrl())
                .setPath(downloadTaskModel.getSavePath())
                .setCallbackProgressTimes(1000)
                .setMinIntervalUpdateSpeed(400)
                .setTag(downloadTaskModel.getUrl())
                .setListener(downloadListener)
                .start();
        LogUtil.d(TAG, "download task start " + downloadTaskModel.getSavePath());
    }

    public void startAllTasks(FileDownloadListener downloadListener) {
        for (DownloadTaskModel downloadTaskModel : downloadTaskModelList) {
            startDownloadTask(downloadTaskModel, downloadListener);
        }
    }

    public void pauseDownloadTask(DownloadTaskModel downloadTaskModel) {
        FileDownloader.getImpl().pause(downloadTaskModel.getId());
    }


    public void pauseAllTask() {
        for (DownloadTaskModel downloadTaskModel : downloadTaskModelList) {
            pauseDownloadTask(downloadTaskModel);
        }
    }

    public void deleteTask(DownloadTaskModel downloadTaskModel) {
        FileDownloader.getImpl().clear(downloadTaskModel.getId(), downloadTaskModel.getSavePath());
    }

    public void deleteAllTasks() {
        for (DownloadTaskModel downloadTaskModel : downloadTaskModelList) {
            deleteTask(downloadTaskModel);
        }
        FileDownloader.getImpl().clearAllTaskData();
    }

    public void clearTasks() {
        LogUtil.d(TAG, "clear task  = " + downloadTaskModelList.size());
        if (downloadTaskModelList.size() == 0) {
            FileDownloader.getImpl().clearAllTaskData();
            // TODO  删除文件和缓存
            for (DownloadTaskModel downloadTaskModel : downloadTaskModelList) {
                deleteTask(downloadTaskModel);
                DownloadTasksRepository.getInstance(App.getInstance()).deleteTask(downloadTaskModel);
            }
            FileUtils.deleteDir(CommonConstant.BASE_SAVE_PATH, false);
            LogUtil.d(TAG, "task is empty , start clear all tasks and files!");
        }
    }

    public boolean isTaskExist(DownloadTaskModel downloadTaskModel) {
        return downloadTaskModelList.contains(downloadTaskModel);
    }

    public void finishTask() {

    }

    public void confirmTask() {

    }
}
