package com.nexgo.xtms.net.update;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaox on 2017/1/19.
 */

public class UpdateResponseData {

    private String KSN;
    private String SN;
    private String status;
    private String updatePlanId;
    private UpdateOtaBean updateOta;
    private ParamInfoBean paramInfo;
    private SimBean sim;
    private String apiversion;
    private String xtmsversion;
    private List<UpdateAppBean> updateApp;
    private List<UpdateFileBean> updateFile;

    public String getApiversion() {
        return apiversion;
    }

    public void setApiversion(String apiversion) {
        this.apiversion = apiversion;
    }

    public String getXtmsversion() {
        return xtmsversion;
    }

    public void setXtmsversion(String xtmsversion) {
        this.xtmsversion = xtmsversion;
    }

    public List<UpdateFileBean> getUpdateFile() {
        return updateFile;
    }

    public void setUpdateFile(List<UpdateFileBean> updateFile) {
        this.updateFile = updateFile;
    }

    public List<UpdateAppBean> getUpdateApp() {
        return updateApp;
    }

    public void setUpdateApp(List<UpdateAppBean> updateApp) {
        this.updateApp = updateApp;
    }

    public String getKSN() {
        return KSN;
    }

    public void setKSN(String KSN) {
        this.KSN = KSN;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatePlanId() {
        return updatePlanId;
    }

    public void setUpdatePlanId(String updatePlanId) {
        this.updatePlanId = updatePlanId;
    }

    public UpdateOtaBean getUpdateOta() {
        return updateOta;
    }

    public void setUpdateOta(UpdateOtaBean updateOta) {
        this.updateOta = updateOta;
    }

    public ParamInfoBean getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(ParamInfoBean paramInfo) {
        this.paramInfo = paramInfo;
    }

    public SimBean getSim() {
        return sim;
    }

    public void setSim(SimBean sim) {
        this.sim = sim;
    }

    public static class UpdateOtaBean {

        private String status;
        private String url;
        private String md5;
        private String desc;
        private String version;
        private String filesize;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "UpdateOtaBean{" +
                    "status='" + status + '\'' +
                    ", url='" + url + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", desc='" + desc + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }

    public static class ParamInfoBean {

        private String requesttime;

        public String getRequesttime() {
            return requesttime;
        }

        public void setRequesttime(String requesttime) {
            this.requesttime = requesttime;
        }

        @Override
        public String toString() {
            return "ParamInfoBean{" +
                    "requesttime='" + requesttime + '\'' +
                    '}';
        }
    }

    public static class SimBean {

        private String imei;
        private String imsi;

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        @Override
        public String toString() {
            return "SimBean{" +
                    "imei='" + imei + '\'' +
                    ", imsi='" + imsi + '\'' +
                    '}';
        }
    }

    public static class UpdateAppBean {

        private String status;
        @SerializedName("package")
        private String packageX;
        private String url;
        private String md5;
        private String filelogo;
        private String versioncode;
        private String versionname;
        private String backFlag;
        private String desc;

        public String getFilelogo() {
            return filelogo;
        }

        public void setFilelogo(String filelogo) {
            this.filelogo = filelogo;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        private String filename;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getVersioncode() {
            return versioncode;
        }

        public void setVersioncode(String versioncode) {
            this.versioncode = versioncode;
        }

        public String getVersionname() {
            return versionname;
        }

        public void setVersionname(String versionname) {
            this.versionname = versionname;
        }

        public String getBackFlag() {
            return backFlag;
        }

        public void setBackFlag(String backFlag) {
            this.backFlag = backFlag;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "UpdateAppBean{" +
                    "status='" + status + '\'' +
                    ", packageX='" + packageX + '\'' +
                    ", url='" + url + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", filelogo='" + filelogo + '\'' +
                    ", versioncode='" + versioncode + '\'' +
                    ", versionname='" + versionname + '\'' +
                    ", backFlag='" + backFlag + '\'' +
                    ", desc='" + desc + '\'' +
                    ", filename='" + filename + '\'' +
                    '}';
        }
    }

    public static class UpdateFileBean {

        /**
         * fileowner : sys
         * apppackage : com.example.zhouxie.myapplication
         * filename : AppTest.apk.ini
         * filesize : 1696045
         * md5 : e7a38cd63b058a0fe543ccff675da611
         * filelogictype : 1
         * filetype : 1
         * status : 1
         * url : http://s.inextpos.com:8761/group1/M00/00/01/ag4frVlHcUaAVAonABnhLYnlyN0646.ini
         * versioncode : 10
         * fileresource : 1
         * savepath :
         * ifcover : 0
         * bak1 :
         * bak2 :
         * desc : 1.this is a test app.
         * 2.you can update this app by Xtms.
         * otherParam :
         */
        private String fileowner;
        private String apppackage;
        private String filename;
        private String filesize;
        private String md5;
        private String filelogictype;
        private String filetype;
        private String status;
        private String url;
        private String versioncode;
        private String fileresource;
        private String savepath;
        private String ifcover;
        private String bak1;
        private String bak2;
        private String desc;
        private String otherParam;


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

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getFilelogictype() {
            return filelogictype;
        }

        public void setFilelogictype(String filelogictype) {
            this.filelogictype = filelogictype;
        }

        public String getFiletype() {
            return filetype;
        }

        public void setFiletype(String filetype) {
            this.filetype = filetype;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersioncode() {
            return versioncode;
        }

        public void setVersioncode(String versioncode) {
            this.versioncode = versioncode;
        }

        public String getFileresource() {
            return fileresource;
        }

        public void setFileresource(String fileresource) {
            this.fileresource = fileresource;
        }

        public String getSavepath() {
            return savepath;
        }

        public void setSavepath(String savepath) {
            this.savepath = savepath;
        }

        public String getIfcover() {
            return ifcover;
        }

        public void setIfcover(String ifcover) {
            this.ifcover = ifcover;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getOtherParam() {
            return otherParam;
        }

        public void setOtherParam(String otherParam) {
            this.otherParam = otherParam;
        }
    }
}
