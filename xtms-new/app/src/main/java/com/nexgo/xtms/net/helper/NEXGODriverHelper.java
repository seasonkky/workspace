package com.nexgo.xtms.net.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.OnAppOperatListener;
import com.nexgo.xtms.net.update.UpdateRequestData;
import com.nexgo.xtms.util.TimeUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by fangqiang_001 on 2017/2/10.
 */

public class NEXGODriverHelper {

    private static NEXGODriverHelper INSTANCE;

    public static NEXGODriverHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NEXGODriverHelper(context);
        }
        return INSTANCE;
    }

    private Context context;
    private DeviceEngine deviceEngine;

    private NEXGODriverHelper(Context context) {
        this.context = context;
        deviceEngine = APIProxy.getDeviceEngine();
    }

    private NEXGODriverHelper() {

    }

    // 获取SN
    public String getSN() {
//        return "N500000059";
        return deviceEngine.getDeviceInfo().getSn();
    }

    // 获取KSN
    public String getKSN() {
//        return "N500000059";
        return deviceEngine.getDeviceInfo().getKsn();
    }

    // 获取应用信息列表
    public List<UpdateRequestData.AppBean> getAppInfList() {
        List<UpdateRequestData.AppBean> appBeanList = new ArrayList<UpdateRequestData.AppBean>();

        PackageManager packageManager = null;
        packageManager = context.getPackageManager();
        List<PackageInfo> mAllPackages = new ArrayList<PackageInfo>();
        mAllPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : mAllPackages) {
            // 系统自身自带应用，不进行统计
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//                System.out.println("系统应用");
                continue;
            }
            if (packageInfo.applicationInfo.packageName.equals("com.nexgo.xtms")) {
                continue;

            }

            UpdateRequestData.AppBean appBean = new UpdateRequestData.AppBean();
            appBean.setPackageX(packageInfo.applicationInfo.packageName);
            appBean.setInstallTime("" + TimeUtils.getTime(packageInfo.firstInstallTime));
            appBean.setVersioncode("" + packageInfo.versionCode);
//            appBean.setVersioncode("0");
            appBean.setVersionname(packageInfo.versionName);

            appBeanList.add(appBean);
        }

        if (appBeanList.size() == 0) {
            System.out.println("没有需要传送的app");
            return null;
        }

//        return null;

        return appBeanList;
    }

    // 安装app
    private boolean installing = false;
    private List<AppPackeg> appPackegs = new ArrayList<AppPackeg>();

    public void installApp(String path, InstallAppLisnner listener) {
//        deviceEngine.installApp(context, path, listener);

        AppPackeg appPackeg = new AppPackeg();
        appPackeg.setPath(path);
        appPackeg.setListener(new OnAppOperatListener() {
            @Override
            public void onOperatResult(int i) {
                System.out.println("=============================onOperatResult->" + i);
                AppPackeg appPackeg_tmp = null;
                for (AppPackeg ap : appPackegs) {
                    if (ap.getListener() == this) {
                        appPackeg_tmp = ap;
                    }
                }

                if (appPackeg_tmp == null) {
                    System.out.println("不存在的apk");
                    return;
                }

                appPackeg_tmp.getLisnner2().onInstallResult(i);
                appPackegs.remove(0);

                // 继续安装下一个
                if (appPackegs.size() == 0) {  // 全部安装完成
                    System.out.println("安装结束");
                    installing = false;
                    return;
                }
                System.out.println("继续安装队列");
                deviceEngine.installApp(context, appPackegs.get(0).getPath(), appPackegs.get(0).getListener());
            }
        });
        appPackeg.setLisnner2(listener);

        appPackegs.add(appPackeg);

        if (installing) {  // 正在安装的话，就不需要执行
            return;
        }

        // 开始启动安装队列
        System.out.println("开始启动安装队列");
        installing = true;
        deviceEngine.installApp(context, appPackegs.get(0).getPath(), appPackegs.get(0).getListener());
    }

    // 把apk文件和listener打包一下，变成队列，排队安装
    private static class AppPackeg {
        private String path;
        private OnAppOperatListener listener;
        private InstallAppLisnner lisnner2;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public OnAppOperatListener getListener() {
            return listener;
        }

        public void setListener(OnAppOperatListener listener) {
            this.listener = listener;
        }

        public InstallAppLisnner getLisnner2() {
            return lisnner2;
        }

        public void setLisnner2(InstallAppLisnner lisnner2) {
            this.lisnner2 = lisnner2;
        }
    }

    public interface InstallAppLisnner {
        public void onInstallResult(int result);
    }

    // 获取OTA版本号
    public UpdateRequestData.OtaBean getOTAVersion() {
        UpdateRequestData.OtaBean otaBean = new UpdateRequestData.OtaBean();

        String otaVersion = "";

        String firmware = getProperty("ro.product.firmware");
        String customname = getProperty("ro.xgd.custom.name");
        String customversion = getProperty("ro.custom.version");

        if ((firmware == null) || (firmware.length() == 0)) {
            firmware = "v1.0.0";
        }
        if ((customname == null) || (customname.length() == 0)) {
            customname = "N5";
        }
        if ((customversion == null) || (customversion.length() == 0)) {
            customversion = "000001";
        }
        // 客户名称
        otaBean.setOtaName(customname);

        otaVersion += firmware;
        otaVersion += "_";

        otaVersion += customname;
        otaVersion += customversion;
        System.out.println("customversion->" + otaVersion);
//        otaVersion = "v1.3.0_Q8000003";
        otaBean.setOtaVersion(otaVersion);

        return otaBean;
    }

    public String getOTAName(){
        return getOTAVersion().getOtaName();
    }

    // 获取参数信息
    public UpdateRequestData.ParamInfoBean getParamInf() {
        UpdateRequestData.ParamInfoBean paramInfoBean = new UpdateRequestData.ParamInfoBean();

        paramInfoBean.setRequesttime("" + (System.currentTimeMillis() / 1000));  // 当前请求时间戳
        paramInfoBean.setPartitionAvailSizeM(getPartitionAvailSizeM(Environment.getDataDirectory().getPath()));
        return paramInfoBean;
    }

    // 获取当前地理位置信息
    public UpdateRequestData.LocationBean getLocation() {
        UpdateRequestData.LocationBean locationBean = new UpdateRequestData.LocationBean("0", "0", "", "", "", new UpdateRequestData.SimBean("", ""));
        return locationBean;
    }

    // 获取msi
    public UpdateRequestData.SimBean getSim() {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        String msi = tm.getSubscriberId();
        if (imei == null) {
            imei = "";
        }
        if (msi == null) {
            msi = "";
        }

        UpdateRequestData.SimBean simBean = new UpdateRequestData.SimBean();
        simBean.setImei(imei);
        simBean.setImsi(msi);
        return simBean;
    }

    //  获取固件版本号，参数：ro.product.firmware
    //  获取设备名称，参数：ro.xgd.custom.name
    //  获取客户版本号，参数：ro.custom.version
    public String getProperty(String key) {
        String result = null;
        try {
            Class<?> spCls = Class.forName("android.os.SystemProperties");
            Class<?>[] typeArgs = new Class[2];
            typeArgs[0] = String.class;
            typeArgs[1] = String.class;
            Constructor<?> spcs = spCls.getConstructor();

            Object[] valueArgs = new Object[2];
            valueArgs[0] = key;
            valueArgs[1] = null;
            Object sp = spcs.newInstance();

            Method method = spCls.getMethod("get", typeArgs);
            result = (String) method.invoke(sp, valueArgs);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取Data目录可用大小
     *
     * @param partition
     * @return
     */
    private int getPartitionAvailSizeM(String partition) {
        String path = partition;

        File file = new File(path);
        StatFs sf = new StatFs(file.getPath());

        long blockSize = sf.getBlockSize();
//        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        return (int) (availCount * blockSize / 1024 / 1024);
    }

}
