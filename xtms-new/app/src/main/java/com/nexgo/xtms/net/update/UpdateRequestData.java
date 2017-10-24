package com.nexgo.xtms.net.update;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaox on 2017/1/19.
 */

public class UpdateRequestData {

    public static final String TYPE_SIGN_IN = "0";
    public static final String TYPE_ADDRESS = "1";
    public static final String TYPE_RESULT = "2";
    private String KSN;
    private String SN;
    private OtaBean ota;
    private ParamInfoBean paramInfo;
    private LocationBean location;
    private SimBean sim;
    private String apiversion;
    private String xtmsversion;
    private String updateType;
    private List<AppBean> app;
    private List<FileBean> fileinfo;

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
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

    public OtaBean getOta() {
        return ota;
    }

    public void setOta(OtaBean ota) {
        this.ota = ota;
    }

    public ParamInfoBean getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(ParamInfoBean paramInfo) {
        this.paramInfo = paramInfo;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public SimBean getSim() {
        return sim;
    }

    public void setSim(SimBean sim) {
        this.sim = sim;
    }

    public List<AppBean> getApp() {
        return app;
    }

    public void setApp(List<AppBean> app) {
        this.app = app;
    }

    public static class OtaBean {

        private String otaVersion;

        private String otaName;

        public String getOtaName() {
            return otaName;
        }

        public void setOtaName(String otaName) {
            this.otaName = otaName;
        }

        public OtaBean() {
        }

        public OtaBean(String otaVersion) {
            this.otaVersion = otaVersion;
        }

        public String getOtaVersion() {
            return otaVersion;
        }

        public void setOtaVersion(String otaVersion) {
            this.otaVersion = otaVersion;
        }
    }

    public static class ParamInfoBean {

        private String requesttime;
        private int partitionAvailSizeM;

        public int getPartitionAvailSizeM() {
            return partitionAvailSizeM;
        }

        public void setPartitionAvailSizeM(int partitionAvailSizeM) {
            this.partitionAvailSizeM = partitionAvailSizeM;
        }

        public ParamInfoBean() {
        }

        public ParamInfoBean(String requesttime) {
            this.requesttime = requesttime;
        }

        public String getRequesttime() {
            return requesttime;
        }

        public void setRequesttime(String requesttime) {
            this.requesttime = requesttime;
        }
    }

    public static class LocationBean {

        private String longitude;
        private String latitude;
        private String citycode;
        private String city;
        private String address;
        private SimBean sim;

        public LocationBean() {

        }

        public LocationBean(String longitude, String latitude, String citycode, String city, String address, SimBean sim) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.citycode = citycode;
            this.city = city;
            this.address = address;
            this.sim = sim;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public SimBean getSim() {
            return sim;
        }

        public void setSim(SimBean sim) {
            this.sim = sim;
        }
    }

    public static class SimBean {

        private String imei;
        private String imsi;

        public SimBean() {
        }

        public SimBean(String imei, String imsi) {
            this.imei = imei;
            this.imsi = imsi;
        }

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
    }

    public static class AppBean {

        @SerializedName("package")
        private String packageX;
        private String versioncode;
        private String installTime;
        private String versionname;
        private String name;

        public AppBean(String packageX, String versioncode, String installTime, String versionname) {
            this.packageX = packageX;
            this.versioncode = versioncode;
            this.installTime = installTime;
            this.versionname = versionname;
        }

        public AppBean() {

        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getVersioncode() {
            return versioncode;
        }

        public void setVersioncode(String versioncode) {
            this.versioncode = versioncode;
        }

        public String getInstallTime() {
            return installTime;
        }

        public void setInstallTime(String installTime) {
            this.installTime = installTime;
        }

        public String getVersionname() {
            return versionname;
        }

        public void setVersionname(String versionname) {
            this.versionname = versionname;
        }
    }

    public static class FileBean {


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
}
