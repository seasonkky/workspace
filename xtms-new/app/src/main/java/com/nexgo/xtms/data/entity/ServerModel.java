package com.nexgo.xtms.data.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import static com.nexgo.xtms.constant.ApiConstant.DEFAULT_URL;

/**
 * Created by zhouxie on 2017/6/21.
 */
@Entity
public class ServerModel {

    @Id(autoincrement = true)
    private Long id;

    private String companyName;

    private String countryname;

    private String countryCode;

    private String domain;

    private String ip;

    private boolean isShowIp;

    @Generated(hash = 1934206865)
    public ServerModel(Long id, String companyName, String countryname,
            String countryCode, String domain, String ip, boolean isShowIp) {
        this.id = id;
        this.companyName = companyName;
        this.countryname = countryname;
        this.countryCode = countryCode;
        this.domain = domain;
        this.ip = ip;
        this.isShowIp = isShowIp;
    }

    @Generated(hash = 160183274)
    public ServerModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountryname() {
        return this.countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean getIsShowIp() {
        return this.isShowIp;
    }

    public void setIsShowIp(boolean isShowIp) {
        this.isShowIp = isShowIp;
    }

    public String getUrlByModel() {
        if (getDomain() != null && !TextUtils.isEmpty(getDomain())) {
            return getDomain();
        } else if (getIp() != null && !TextUtils.isEmpty(getIp())) {
            return getIp();
        } else {
            return DEFAULT_URL;
        }
    }
}
