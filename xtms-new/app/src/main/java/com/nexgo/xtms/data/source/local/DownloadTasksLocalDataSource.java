package com.nexgo.xtms.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nexgo.xtms.data.Repository;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.data.source.datasource.DownloadTasksDataSource;
import com.nexgo.xtms.database.DownloadTaskModelDao;

import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * Created by zhouxie on 2017/6/30.
 */

public class DownloadTasksLocalDataSource implements DownloadTasksDataSource {

    private static DownloadTasksLocalDataSource INSTANCE;
    private DownloadTaskModelDao taskModelDao;

    // Prevent direct instantiation.
    private DownloadTasksLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        taskModelDao = Repository.getInstance().getmDaoSession().getDownloadTaskModelDao();
    }

    public static DownloadTasksLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DownloadTasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public List<DownloadTaskModel> getTasks(String taskid) {
        List<DownloadTaskModel> list = taskModelDao.queryBuilder()
                .where(DownloadTaskModelDao.Properties.Taskid.eq(taskid))
                .list();
        return list;
    }

    @Override
    public List<DownloadTaskModel> getCurrentTasks() {
        List<DownloadTaskModel> list = taskModelDao.queryBuilder()
                .where(DownloadTaskModelDao.Properties.Active.eq(true))
                .list();
        return list;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        List<DownloadTaskModel> downloadTaskModels = taskModelDao.loadAll();
        if (downloadTaskModels != null) {
            callback.onTasksLoaded(downloadTaskModels);
        }
    }

    @Override
    public void getDownloadTask(@NonNull String id, @NonNull GetTaskCallback callback) {
        List<DownloadTaskModel> list = taskModelDao.queryBuilder()
                .where(DownloadTaskModelDao.Properties.Id.eq(id))
                .list();
        if (list != null && list.size() > 0)
            callback.onTaskLoaded(list.get(0));
    }

    @Override
    public void deleteTask(@NonNull DownloadTaskModel downloadTaskModel) {
        taskModelDao.delete(downloadTaskModel);
    }

    @Override
    public void saveTask(@NonNull DownloadTaskModel downloadTaskModel) {

    }

    @Override
    public void completeTask(@NonNull DownloadTaskModel downloadTaskModel) {

    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull DownloadTaskModel downloadTaskModel) {

    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {

    }
}
