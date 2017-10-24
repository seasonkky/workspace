package com.nexgo.xtms.mvp.server.model;

/**
 * Created by zhouxie on 2017/6/26.
 */

public class ServerModel {

    private String uniqueID ;
    private String countryCode;
    private String companyName;
    private String domain;
    private String serverIP;

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    @Override
    public String toString() {
        return "ServerModel{" +
                "uniqueID='" + uniqueID + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", domain='" + domain + '\'' +
                ", serverIP='" + serverIP + '\'' +
                '}';
    }
}
