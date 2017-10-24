package com.nexgo.xtms.data.entity;

/**
 * Created by zhouxie on 2017/10/18.
 */

public class FileModel {

    private String fileowner;
    private String apppackage;
    private String filename;
    private String filesize;
    private String filelogictype;
    private String versioncode;
    private String savepath;
    private String bak1;
    private String bak2;

    public String getFileowner() {
        return fileowner;
    }

    public void setFileowner(String fileowner) {
        this.fileowner = fileowner;
    }

    public String getApppackage() {
        return apppackage;
    }

    public void setApppackage(String apppackage) {
        this.apppackage = apppackage;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFilelogictype() {
        return filelogictype;
    }

    public void setFilelogictype(String filelogictype) {
        this.filelogictype = filelogictype;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getSavepath() {
        return savepath;
    }

    public void setSavepath(String savepath) {
        this.savepath = savepath;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }
}
