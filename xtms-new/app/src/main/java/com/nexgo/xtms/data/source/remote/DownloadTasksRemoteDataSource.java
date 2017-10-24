package com.nexgo.xtms.data.source.remote;

import android.support.annotation.NonNull;

import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.data.source.datasource.DownloadTasksDataSource;

import java.util.List;

/**
 * Created by zhouxie on 2017/6/30.
 */

public class DownloadTasksRemoteDataSource implements DownloadTasksDataSource {

    private static DownloadTasksRemoteDataSource INSTANCE;
    private List<DownloadTaskModel> downloadTaskModelList;

    public static DownloadTasksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DownloadTasksRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<DownloadTaskModel> getTasks(String taskid) {
        // Not Required
        return null;
    }

    @Override
    public List<DownloadTaskModel> getCurrentTasks() {
        // Not Required
        return null;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        // Not Required
    }

    @Override
    public void getDownloadTask(@NonNull String id, @NonNull GetTaskCallback callback) {
        // Not Required
    }

    @Override
    public void saveTask(@NonNull DownloadTaskModel downloadTaskModel) {
        // Not Required
    }

    @Override
    public void completeTask(@NonNull DownloadTaskModel downloadTaskModel) {
        // Not Required
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // Not Required
    }

    @Override
    public void activateTask(@NonNull DownloadTaskModel downloadTaskModel) {
        // Not Required
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Not Required
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
