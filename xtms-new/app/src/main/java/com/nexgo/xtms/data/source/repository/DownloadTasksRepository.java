package com.nexgo.xtms.data.source.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.data.source.datasource.DownloadTasksDataSource;
import com.nexgo.xtms.data.source.local.DownloadTasksLocalDataSource;
import com.nexgo.xtms.data.source.remote.DownloadTasksRemoteDataSource;

import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;


/**
 * Created by zhouxie on 2017/7/3.
 */

public class DownloadTasksRepository implements DownloadTasksDataSource {

    private static DownloadTasksRepository INSTANCE;
    private List<DownloadTaskModel> downloadTaskModelList;

    private final DownloadTasksDataSource mTasksRemoteDataSource;

    private final DownloadTasksDataSource mTasksLocalDataSource;

    private DownloadTasksRepository(@NonNull DownloadTasksDataSource tasksRemoteDataSource,
                                    @NonNull DownloadTasksDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = checkNotNull(tasksRemoteDataSource);
        mTasksLocalDataSource = checkNotNull(tasksLocalDataSource);
    }

    public static DownloadTasksRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DownloadTasksRepository(DownloadTasksRemoteDataSource.getInstance(), DownloadTasksLocalDataSource.getInstance(context));
        }
        return INSTANCE;
    }

    @Override
    public List<DownloadTaskModel> getTasks(String taskid) {
        mTasksLocalDataSource.getTasks(taskid);
        return null;
    }

    @Override
    public List<DownloadTaskModel> getCurrentTasks() {
        return mTasksLocalDataSource.getCurrentTasks();
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        checkNotNull(callback);
        // 先加载Remote数据，更新数据库后直接返回本地数据库
        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<DownloadTaskModel> downloadTaskModels) {

            }

            @Override
            public void onDataNotAvailable() {
                //直接返回本地数据
            }
        });
    }

    @Override
    public void getDownloadTask(@NonNull String id, @NonNull GetTaskCallback callback) {

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

    @Override
    public void deleteTask(@NonNull DownloadTaskModel downloadTaskModel) {

    }
}
