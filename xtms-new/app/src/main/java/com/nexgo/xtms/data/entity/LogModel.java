package com.nexgo.xtms.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhouxie on 2017/6/22.
 */
@Entity
public class LogModel {

    @Unique
    private long time;

    private String logContent;

    private boolean isShow;

    @Generated(hash = 703451556)
    public LogModel(long time, String logContent, boolean isShow) {
        this.time = time;
        this.logContent = logContent;
        this.isShow = isShow;
    }

    @Generated(hash = 1039532375)
    public LogModel() {
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLogContent() {
        return this.logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public boolean getIsShow() {
        return this.isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }
}
