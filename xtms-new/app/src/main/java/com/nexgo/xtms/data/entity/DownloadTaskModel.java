package com.nexgo.xtms.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;


/**
 * Created by zhouxie on 2017/6/29.
 */
@Entity
public class DownloadTaskModel implements Serializable {

    static final long serialVersionUID = 42L;

    public enum DownloadStatus {
        STATUS_NOT_DOWNLOAD,
        STATUS_START,
        STATUS_CONNECTING,
        STATUS_CONNECTTED,
        STATUS_CONNECT_ERROR,
        STATUS_DOWNLOADING,
        STATUS_PAUSED,
        STATUS_CANCELED,
        STATUS_DOWNLOAD_ERROR,
        STATUS_COMPLETE,
        STATUS_CHECKING,
        STATUS_CHECKING_ERROR,
        STATUS_INSTALLING,
        STATUS_INSTALLED,
        STATUS_INSTALL_ERROR
    }

    public enum Type {
        App, OTA, File
    }

    public enum UpdateType {
        ForceUpdate, NotForceUpdate
    }

    private String name;
    private String packageName;
    private int id;
    private String image;
    private String url;
    private int progress;
    private long downloadSize;
    private long totalSize;
    private String savePath;
    private String md5;
    private String version;
    private String desc;
    private String taskid;
    private boolean active;
    private String fileowner;
    private String filesize;
    private String filelogictype;
    private String filetype;
    private String versioncode;
    private String fileresource;
    private String ifcover;
    private String bak1;
    private String bak2;
    private String otherParam;
    private String updatestatus;


    @Transient
    private int speed;
    @Transient
    private long time;
    private String downloadPerSize;
    @Transient
    private int retryCount;
    @Transient
    private DownloadStatus status;
    @Transient
    private Type type;
    @Transient
    private UpdateType updateType;

    @Generated(hash = 634828738)
    public DownloadTaskModel(String name, String packageName, int id, String image,
                             String url, int progress, long downloadSize, long totalSize,
                             String savePath, String md5, String version, String desc, String taskid,
                             boolean active, String fileowner, String filesize, String filelogictype,
                             String filetype, String versioncode, String fileresource,
                             String ifcover, String bak1, String bak2, String otherParam,
                             String updatestatus, String downloadPerSize) {
        this.name = name;
        this.packageName = packageName;
        this.id = id;
        this.image = image;
        this.url = url;
        this.progress = progress;
        this.downloadSize = downloadSize;
        this.totalSize = totalSize;
        this.savePath = savePath;
        this.md5 = md5;
        this.version = version;
        this.desc = desc;
        this.taskid = taskid;
        this.active = active;
        this.fileowner = fileowner;
        this.filesize = filesize;
        this.filelogictype = filelogictype;
        this.filetype = filetype;
        this.versioncode = versioncode;
        this.fileresource = fileresource;
        this.ifcover = ifcover;
        this.bak1 = bak1;
        this.bak2 = bak2;
        this.otherParam = otherParam;
        this.updatestatus = updatestatus;
        this.downloadPerSize = downloadPerSize;
    }

    @Generated(hash = 1045657427)
    public DownloadTaskModel() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getFileowner() {
        return this.fileowner;
    }

    public void setFileowner(String fileowner) {
        this.fileowner = fileowner;
    }

    public String getFilesize() {
        return this.filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFilelogictype() {
        return this.filelogictype;
    }

    public void setFilelogictype(String filelogictype) {
        this.filelogictype = filelogictype;
    }

    public String getFiletype() {
        return this.filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getVersioncode() {
        return this.versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getFileresource() {
        return this.fileresource;
    }

    public void setFileresource(String fileresource) {
        this.fileresource = fileresource;
    }

    public String getIfcover() {
        return this.ifcover;
    }

    public void setIfcover(String ifcover) {
        this.ifcover = ifcover;
    }

    public String getBak1() {
        return this.bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return this.bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public String getOtherParam() {
        return this.otherParam;
    }

    public void setOtherParam(String otherParam) {
        this.otherParam = otherParam;
    }

    public String getUpdatestatus() {
        return this.updatestatus;
    }

    public void setUpdatestatus(String updatestatus) {
        this.updatestatus = updatestatus;
    }

    public String getDownloadPerSize() {
        return this.downloadPerSize;
    }

    public void setDownloadPerSize(String downloadPerSize) {
        this.downloadPerSize = downloadPerSize;
    }

    public boolean isActive() {
        return active;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }
}
