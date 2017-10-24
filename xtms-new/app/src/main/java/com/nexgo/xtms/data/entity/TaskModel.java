package com.nexgo.xtms.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhouxie on 2017/10/17.
 */
@Entity
public class TaskModel {
    private String taskid;
    private boolean active;
    private boolean finish;
    private String urls;

    @Generated(hash = 1225460216)
    public TaskModel(String taskid, boolean active, boolean finish, String urls) {
        this.taskid = taskid;
        this.active = active;
        this.finish = finish;
        this.urls = urls;
    }

    @Generated(hash = 648620828)
    public TaskModel() {
    }

    public String getTaskid() {
        return this.taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getFinish() {
        return this.finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getUrls() {
        return this.urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
