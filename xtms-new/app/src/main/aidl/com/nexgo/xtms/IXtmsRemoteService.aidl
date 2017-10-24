// IXtmsRemoteService.aidl
package com.nexgo.xtms;

// Declare any non-default types here with import statements

interface IXtmsRemoteService {
    // country 国家 company 客户公司名称 domain 域名 ip IP地址（域名和IP填写其中一个即可，格式参照 https://192.168.0.1:8080/ ）
    void setUpdateAddressCountry(String country ,String company ,String domain ,String ip);

    // 设置xtms客户端的语言，目前只支持"zh","en","fa";
    void setLanguage(String language);

    // 跳转至xtms的更新主界面
    void jumpToUpdateActivity();
}
