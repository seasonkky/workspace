package com.nexgo.xtms.task.update;

import com.liulishuo.filedownloader.FileDownloadListener;
import com.nexgo.xtms.data.entity.DownloadTaskModel;

/**
 * Created by zhouxie on 2017/9/27.
 */

public interface UpdateTaskController {

    void startTask(DownloadTaskModel downloadTaskModel, FileDownloadListener downloadListener);

    void startAllTasks(FileDownloadListener downloadListener);

    void pauseTask(DownloadTaskModel downloadTaskModel);

    void pauseAllTask();

    void deleteTask(DownloadTaskModel downloadTaskModel);

    void deleteAllTasks();

    boolean isTaskExist(DownloadTaskModel downloadTaskModel);

    void orderNextTask(int requsttime);


}
