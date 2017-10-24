package com.nexgo.xtms.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by zhouxie on 2017/6/23.
 */
@Entity
public class CountryModel {
    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String countryCode;

    private String countryId;

    private String en_name;

    private String cn_name;

    @Generated(hash = 1147191395)
    public CountryModel(Long id, String countryCode, String countryId,
            String en_name, String cn_name) {
        this.id = id;
        this.countryCode = countryCode;
        this.countryId = countryId;
        this.en_name = en_name;
        this.cn_name = cn_name;
    }

    @Generated(hash = 625625300)
    public CountryModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryId() {
        return this.countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getEn_name() {
        return this.en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getCn_name() {
        return this.cn_name;
    }

    public void setCn_name(String cn_name) {
        this.cn_name = cn_name;
    }
}
