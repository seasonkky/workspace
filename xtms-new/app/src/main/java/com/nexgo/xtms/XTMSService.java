package com.nexgo.xtms;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.nexgo.xtms.activity.UpdateActivity;
import com.nexgo.xtms.constant.CommonConstant;
import com.nexgo.xtms.constant.EventConstant;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.data.entity.ServerModel;
import com.nexgo.xtms.data.source.datasource.ServerDataSource;
import com.nexgo.xtms.data.source.datasource.UpdateInfoDataSource;
import com.nexgo.xtms.data.source.repository.DownloadTasksRepository;
import com.nexgo.xtms.data.source.repository.ServerRepository;
import com.nexgo.xtms.data.source.repository.UpdateInfoRepository;
import com.nexgo.xtms.eventbus.BroadCastEvent;
import com.nexgo.xtms.eventbus.DownloadRequestEvent;
import com.nexgo.xtms.eventbus.RebootEvent;
import com.nexgo.xtms.eventbus.ScreenEvent;
import com.nexgo.xtms.eventbus.UpdateEvent;
import com.nexgo.xtms.eventbus.UpdateRequestEvent;
import com.nexgo.xtms.mvp.base.App;
import com.nexgo.xtms.mvp.update.UpdateReceiver;
import com.nexgo.xtms.net.helper.RetrofitHelper;
import com.nexgo.xtms.net.update.UpdateRequestData;
import com.nexgo.xtms.net.update.UpdateResponseData;
import com.nexgo.xtms.task.ErrorCode;
import com.nexgo.xtms.task.lbs.LBSTask;
import com.nexgo.xtms.task.md5.MD5CheckListener;
import com.nexgo.xtms.task.md5.MD5CheckTask;
import com.nexgo.xtms.task.power.PowerTask;
import com.nexgo.xtms.task.reboot.RebootTask;
import com.nexgo.xtms.task.update.OnNetWorkStatusCheckListener;
import com.nexgo.xtms.task.update.UpdateTask;
import com.nexgo.xtms.task.update.app.AppUpdateTask;
import com.nexgo.xtms.task.update.ota.OTAUpdateTask;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.NetWorkUtils;
import com.nexgo.xtms.util.PreferenceUtil;
import com.nexgo.xtms.util.TimeUtils;
import com.nexgo.xtms.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.ACTION_BATTERY_CHANGED;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.nexgo.xtms.constant.BroadcastConstant.ACTION_FORCE_REBOOT;
import static com.nexgo.xtms.constant.BroadcastConstant.ACTION_UPDATE_DEVICE_INF;
import static com.nexgo.xtms.constant.CommonConstant.BASE_PARAM_SAVE_PATH;
import static com.nexgo.xtms.constant.CommonConstant.BASE_SAVE_PATH;

/**
 * Created by zhouxie on 2017/7/3.
 */

public class XTMSService extends Service {

    public static final String TAG = XTMSService.class.getSimpleName();

    private Map<String, UpdateTask> updateTaskMap = new HashMap<>();
    private ArrayList<DownloadTaskModel> downloadTaskModelList = new ArrayList<>(); // 维护的当前下载任务列表
    private String currentUpdateTaskid;// 维护的当前下载任务的id
    private UpdateTask updateTask;

    private UpdateEvent updateEvent;
    private UpdateEvent updateEventSpecial;
    private UpdateEvent freshEvent;

    private UpdateReceiver updateReceiver;

    private boolean isDownloadWithoutWifi = false;
    private int power;

    // 屏幕亮灭
    private boolean isScreenOn = false;

    // 定时器
    private PendingIntent updateSender;

    public XTMSService() {
    }

    private Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "XTMSService onCreate ");

        EventBus.getDefault().register(this);

        // 创建UI通知事件
        freshEvent = new UpdateEvent(EventConstant.REFRESH_COMPLETE, downloadTaskModelList);
        updateEvent = new UpdateEvent(EventConstant.UPDATE_EVENT_UPDATEFRAGMENT, downloadTaskModelList);
        updateEventSpecial = new UpdateEvent(EventConstant.UPDATE_EVENT_UPDATEFRAGMENT_SPECIAL, downloadTaskModelList);

        // 初始化电源管理服务
        PowerTask.getInstance().init(this);
        registerBroadCast();
        // 初始化百度地图服务
        LBSTask.getInstance().initBaiduLBSService();

        // 重启后初始化下一次重启的时间
        RebootTask.getInstance().init(this);
        RebootTask.getInstance().initRebootAlarmManager();
        requestUpdateInfo();
    }

    private IXtmsRemoteService.Stub mBinder = new IXtmsRemoteService.Stub() {
        @Override
        public void setUpdateAddressCountry(String country, String company, String domain, String ip) throws RemoteException {
            ServerModel serverModel = new ServerModel(null, company, country, null, domain, ip, true);
            ServerRepository.getInstance().saveServer(serverModel);
            ServerRepository.getInstance().getServer(serverModel.getId(), new ServerDataSource.GetServerCallback() {
                @Override
                public void onServerLoaded(ServerModel serverModel) {
                    PreferenceUtil.putLong(SPConstant.KEY_SERVER, serverModel.getId());
                }

                @Override
                public void onDataNotAvailable() {
                    PreferenceUtil.putLong(SPConstant.KEY_SERVER, 0L);
                }
            });

            // 设置新的服务器HOST地址
            RetrofitHelper.setBaseUrl(serverModel.getUrlByModel());
        }

        @Override
        public void setLanguage(String language) throws RemoteException {
            App.changeLan(XTMSService.this, language);
        }

        @Override
        public void jumpToUpdateActivity() throws RemoteException {
            Intent intent = new Intent(XTMSService.this, UpdateActivity.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 暂停所有的下载任务
        FileDownloader.getImpl().pauseAll();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(updateReceiver);
        LBSTask.getInstance().cancelBdLocationListener();
    }

    /**
     * 监听广播事件
     */
    private void registerBroadCast() {
        updateReceiver = new UpdateReceiver();
        IntentFilter mFilter = new IntentFilter(CONNECTIVITY_ACTION);
        mFilter.addAction(ACTION_BATTERY_CHANGED);
        mFilter.addAction(ACTION_SCREEN_OFF);
        mFilter.addAction(ACTION_SCREEN_ON);
        mFilter.addAction(ACTION_FORCE_REBOOT);
        mFilter.addAction(ACTION_UPDATE_DEVICE_INF);
        registerReceiver(updateReceiver, mFilter);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onUpdateRequestEventReceived(UpdateRequestEvent updateRequestEvent) {
        LogUtil.d(TAG, "onUpdateRequestReceived ");
        // 首先对已完成的任务进行检测
        for (DownloadTaskModel downloadTaskModel : downloadTaskModelList) {
            if (downloadTaskModel.getStatus().equals(DownloadTaskModel.DownloadStatus.STATUS_COMPLETE) || downloadTaskModel.getStatus().equals(DownloadTaskModel.DownloadStatus.STATUS_INSTALL_ERROR)) {
                checkMD5(downloadTaskModel);
            }
        }

        if (updateRequestEvent.getTag().equals(EventConstant.UPDATE_REQUEST_EVENT)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestUpdateInfo();
                }
            }, updateRequestEvent.getDelayTime());
        }
    }

    /**
     * 安装OTA和APP
     *
     * @param downloadTaskModel
     */

    private void requestInstall(DownloadTaskModel downloadTaskModel) {
        if (downloadTaskModel.getType().equals(DownloadTaskModel.Type.App)) {
            requestAppInstall(downloadTaskModel);
        } else if (downloadTaskModel.getType().equals(DownloadTaskModel.Type.OTA)) {
            requestOTAInstall(downloadTaskModel);
        }
    }

    /**
     * 安装OTA
     *
     * @param downloadTaskModel
     */

    private void requestOTAInstall(DownloadTaskModel downloadTaskModel) {
        LogUtil.d(TAG, "--- requestOTAInstall ----");

        // 检查电量
        if (power < 50) {
            LogUtil.d(TAG, "power less than 50%， forbid OTA");
            ToastUtil.ShortToast(getString(R.string.low_power_tip));
            downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_INSTALL_ERROR);
            EventBus.getDefault().post(updateEventSpecial);
            PowerTask.getInstance().release();
            return;
        }

        downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_INSTALLING);
        EventBus.getDefault().post(updateEventSpecial);

        new OTAUpdateTask(XTMSService.this, downloadTaskModel, new OTAUpdateTask.OnListenner() {
            @Override
            public void onDone() {
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_CHECKING);
                EventBus.getDefault().post(updateEventSpecial);
            }

            @Override
            public void onCancel() {
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_COMPLETE);
                EventBus.getDefault().post(updateEventSpecial);
                PowerTask.getInstance().release();
            }

            @Override
            public void onError(ErrorCode errorCode) {
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_INSTALL_ERROR);
                EventBus.getDefault().post(updateEventSpecial);
                PowerTask.getInstance().release();
            }
        }).requestOTA(downloadTaskModel.getSavePath());
    }

    /**
     * 安装APP
     *
     * @param downloadTaskModel
     */

    private void requestAppInstall(DownloadTaskModel downloadTaskModel) {
        downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_INSTALLING);
        EventBus.getDefault().post(updateEventSpecial);
        LogUtil.d(TAG, "--- requestAppInstall ----");
        new AppUpdateTask(XTMSService.this, downloadTaskModel, new AppUpdateTask.OnListenner() {
            @Override
            public void onDone() {
                // 安装成功
                LogUtil.d(TAG, " Install complete " + downloadTaskModel.getName());
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_INSTALLED);
                FileDownloader.getImpl().clear(downloadTaskModel.getId(), downloadTaskModel.getSavePath());
                if (downloadTaskModelList.contains(downloadTaskModel))
                    downloadTaskModelList.remove(downloadTaskModel);
                EventBus.getDefault().post(updateEventSpecial);
                // 刷新界面
                EventBus.getDefault().post(freshEvent);

                // 安装完成则进行一次数据上送
                UpdateRequestData updateRequestData = new UpdateRequestData();
                UpdateInfoRepository.getInstance().getUpdateInfoRes(new UpdateInfoDataSource.LoadUpdateInfoCallback() {
                    @Override
                    public void onUpdateInfoLoaded(UpdateResponseData updateResponseData) {
                        // 上送完成
                        LogUtil.d(TAG, "data upload success !");
                        PowerTask.getInstance().release();
                    }

                    @Override
                    public void onDataNotAvailable() {
                        // 上送失败
                        LogUtil.d(TAG, "data upload fail !");
                        PowerTask.getInstance().release();
                    }
                }, updateRequestData);
            }

            @Override
            public void onFail() {
                LogUtil.d(TAG, " Install fail" + downloadTaskModel.getName());
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_INSTALL_ERROR);
                EventBus.getDefault().post(updateEventSpecial);
            }

            @Override
            public void onError(ErrorCode errorCode) {
                LogUtil.d(TAG, " Install error" + downloadTaskModel.getName());
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_INSTALL_ERROR);
                EventBus.getDefault().post(updateEventSpecial);
            }
        }).installApk(downloadTaskModel.getSavePath());
    }

    /**
     * 开始更新的入口
     */

    private void requestUpdateInfo() {
        LogUtil.d(TAG, "--- requestUpdateInfo ----");
        PowerTask.getInstance().acquire();
        LBSTask.getInstance().startLocate();

        UpdateInfoRepository.getInstance().getUpdateInfo(new UpdateInfoDataSource.LoadUpdateInfoCallback() {

            @Override
            public void onUpdateInfoLoaded(UpdateResponseData updateResponseData) {
                // 预约下一次更新请求
                orderNextTask(updateResponseData);

                // 取得数据
                LogUtil.d(TAG, "update service requestUpdateInfo = " + updateResponseData.toString());
                if ((updateResponseData.getStatus() == null) || (!updateResponseData.getStatus().equals("1"))) {  // 没有需要升级的话，就直接结束整个流程
                    LogUtil.d(TAG, "--- No Update ,Finish Task ---");
                    // 通知UpdateFragment 无更新事件
                    EventBus.getDefault().post(new UpdateEvent(EventConstant.REFRESH_COMPLETE, null));
                    if (updateTask != null) {
                        updateTask.clearTasks();
                    } else {
                        // 将全部进行中的任务删除
                        List<DownloadTaskModel> currentTasks = DownloadTasksRepository.getInstance(XTMSService.this).getCurrentTasks();
                        if (currentTasks != null && currentTasks.size() > 0) {
                            UpdateTask updateTask = new UpdateTask(currentTasks.get(0).getTaskid(), currentTasks, downloadListener);
                            updateTask.clearTasks();
                        }
                    }
                    // 释放唤醒
                    PowerTask.getInstance().release();

                    return;
                } else if (updateResponseData.getStatus() != null && "1".equals(updateResponseData.getStatus())) {
                    // 有更新任务

                    // 判断网络类型
                    checkNetStatus(new OnNetWorkStatusCheckListener() {
                        @Override
                        public void onCheckOK() {
                            // 网络状况正常则继续进行

                            // 判断SDCard容量
                            if (!checkSdcardIsEnough()) {
                                // 继续未完成的任务
                                LogUtil.d(TAG, " sdcard is not enough !");
                                if (updateTask != null) {
                                    updateTask.startAllTasks(downloadListener);
                                } else {
                                    List<DownloadTaskModel> currentTasks = DownloadTasksRepository.getInstance(XTMSService.this).getCurrentTasks();
                                    if (currentTasks != null && currentTasks.size() > 0) {
                                        UpdateTask updateTask = new UpdateTask(currentTasks.get(0).getTaskid(), currentTasks, downloadListener);
                                        updateTask.startAllTasks(downloadListener);
                                    }
                                }
                                EventBus.getDefault().post(freshEvent);
                                return;
                            }
                            // 添加下载任务
                            transformDownloadTasks(updateResponseData);
                        }

                        @Override
                        public void onCheckCancel() {
                            // 用户取消
                            EventBus.getDefault().post(new UpdateEvent(EventConstant.NET_4G_ERROR));

                            // 释放唤醒
                            PowerTask.getInstance().release();
                        }

                        @Override
                        public void onChcekError() {
                            // 网络异常
                            EventBus.getDefault().post(new UpdateEvent(EventConstant.REFRESH_ERROR));

                            // 释放唤醒
                            PowerTask.getInstance().release();
                        }
                    });
                }
            }

            /**
             * 判断网络状态
             * @param checkListener
             */

            private void checkNetStatus(OnNetWorkStatusCheckListener checkListener) {
                if (!NetWorkUtils.isNetworkAvailable(XTMSService.this)) {
                    LogUtil.d(TAG, "network is not available");
                    checkListener.onChcekError();
                    return;
                }
                if (NetWorkUtils.isWifi(XTMSService.this)) {
                    LogUtil.d(TAG, "network is Wifi");
                    checkListener.onCheckOK();
                } else {
                    LogUtil.d(TAG, "network is not Wifi");
                    if (PreferenceUtil.getBoolean(SPConstant.KEY_NO_WIFI_DOWNLOAD, false) || isDownloadWithoutWifi) {
                        checkListener.onCheckOK();
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showContinueDownloadDialog(checkListener);
                            }
                        });
                    }
                }
            }

            /**
             * 判断磁盘容量大小
             * @return
             */
            private boolean checkSdcardIsEnough() {
                //取得SD卡文件路径
                File path = Environment.getExternalStorageDirectory();
                StatFs sf = new StatFs(path.getPath());
                //获取单个数据块的大小(Byte)
                long blockSize = sf.getBlockSizeLong();
                //获取所有数据块数
                long allBlocks = sf.getBlockSizeLong();
                long available = sf.getAvailableBlocksLong();
                //返回SD卡大小
                //return allBlocks * blockSize; //单位Byte
                //return (allBlocks * blockSize)/1024; //单位KB
                int freeMemery = (int) ((available * blockSize) / 1024 / 1024);
                LogUtil.d(TAG, "checkSdcardIsEnough blockSize->" + blockSize + ", available->" + available + ", freeMemery->" + freeMemery);
                if (freeMemery > CommonConstant.SDCARDMIN) {
                    return true;
                }
                ToastUtil.ShortToast(R.string.sdcardnotenough);
                return false;
            }

            private void transformDownloadTasks(UpdateResponseData updateResponseData) {
                currentUpdateTaskid = updateResponseData.getUpdatePlanId();
                LogUtil.d(TAG, "  transformDownloadTasks start planid = " + currentUpdateTaskid);
                List<DownloadTaskModel> downloadTaskModelList = new ArrayList<DownloadTaskModel>();
                // 添加OTA下载任务
                if (updateResponseData.getUpdateOta() != null) {
                    UpdateResponseData.UpdateOtaBean updateOta = updateResponseData.getUpdateOta();
                    DownloadTaskModel downloadTaskModel = new DownloadTaskModel();
                    // TODO 设置了测试图片
//                    downloadTaskModel.setImage("");
                    downloadTaskModel.setName(updateOta.getDesc() + updateOta.getVersion());
                    downloadTaskModel.setUrl(updateOta.getUrl());
                    downloadTaskModel.setMd5(updateOta.getMd5());
                    downloadTaskModel.setDesc(updateOta.getDesc());
                    downloadTaskModel.setVersion(updateOta.getVersion());
                    downloadTaskModel.setType(DownloadTaskModel.Type.OTA);
                    downloadTaskModel.setSavePath(BASE_SAVE_PATH + "/" + getFileNameByUrl(updateOta.getUrl()));
                    downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_NOT_DOWNLOAD);
                    downloadTaskModel.setUpdateType(updateOta.getStatus().equals("1") ? DownloadTaskModel.UpdateType.ForceUpdate : DownloadTaskModel.UpdateType.NotForceUpdate);
                    downloadTaskModelList.add(downloadTaskModel);
                    LogUtil.d(TAG, " add OTA downloadTask = " + downloadTaskModel.getUrl());
                }
                // 添加App下载任务
                if (updateResponseData.getUpdateApp() != null) {
                    for (UpdateResponseData.UpdateAppBean updateAppBean : updateResponseData.getUpdateApp()
                            ) {
                        DownloadTaskModel downloadTaskModel = new DownloadTaskModel();
                        downloadTaskModel.setImage(updateAppBean.getFilelogo());
                        downloadTaskModel.setName(updateAppBean.getFilename());
                        downloadTaskModel.setUrl(updateAppBean.getUrl());
                        downloadTaskModel.setSavePath(BASE_SAVE_PATH + "/" + updateAppBean.getFilename());
                        downloadTaskModel.setMd5(updateAppBean.getMd5());
                        downloadTaskModel.setDesc(updateAppBean.getDesc());
                        downloadTaskModel.setVersion(updateAppBean.getVersionname());
                        downloadTaskModel.setPackageName(updateAppBean.getPackageX());
                        downloadTaskModel.setType(DownloadTaskModel.Type.App);
                        downloadTaskModel.setUpdateType(updateAppBean.getStatus().equals("1") ? DownloadTaskModel.UpdateType.ForceUpdate : DownloadTaskModel.UpdateType.NotForceUpdate);
                        downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_NOT_DOWNLOAD);
                        downloadTaskModelList.add(downloadTaskModel);
                        LogUtil.d(TAG, " add App downloadTask = " + downloadTaskModel.getUrl());
                    }
                }

                // 添加File下载任务
                if (updateResponseData.getUpdateFile() != null) {
                    for (UpdateResponseData.UpdateFileBean updateFileBean : updateResponseData.getUpdateFile()
                            ) {
                        DownloadTaskModel downloadTaskModel = new DownloadTaskModel();
                        downloadTaskModel.setFileowner(updateFileBean.getFileowner());
                        downloadTaskModel.setName(updateFileBean.getFilename());
                        downloadTaskModel.setPackageName(updateFileBean.getApppackage());
                        downloadTaskModel.setMd5(updateFileBean.getMd5());
                        downloadTaskModel.setUrl(updateFileBean.getUrl());
                        downloadTaskModel.setFilelogictype(updateFileBean.getFilelogictype());
                        downloadTaskModel.setFiletype(updateFileBean.getFiletype());
                        downloadTaskModel.setSavePath(BASE_PARAM_SAVE_PATH + "/" + updateFileBean.getApppackage() + "/" + updateFileBean.getFilename());
                        downloadTaskModel.setDesc(updateFileBean.getDesc());
                        downloadTaskModel.setFileresource(updateFileBean.getFileresource());
                        downloadTaskModel.setIfcover(updateFileBean.getIfcover());
                        downloadTaskModel.setOtherParam(updateFileBean.getOtherParam());
                        downloadTaskModel.setVersioncode(updateFileBean.getVersioncode());
                        downloadTaskModel.setType(DownloadTaskModel.Type.File);
                        downloadTaskModel.setUpdateType(updateFileBean.getStatus().equals("1") ? DownloadTaskModel.UpdateType.ForceUpdate : DownloadTaskModel.UpdateType.NotForceUpdate);
                        downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_NOT_DOWNLOAD);
                        downloadTaskModelList.add(downloadTaskModel);
                        LogUtil.d(TAG, " add App downloadTask = " + downloadTaskModel.getUrl());
                    }
                }


                // 处理旧任务
                if (updateTask == null) {
                    // 开启下载任务
                    updateTask = new UpdateTask(currentUpdateTaskid, downloadTaskModelList, downloadListener);
                } else {
                    if (currentUpdateTaskid.equals(updateTask.getTaskId())) {
                    } else {
                        updateTask.clearTasks();
                        updateTask = new UpdateTask(currentUpdateTaskid, downloadTaskModelList, downloadListener);
                    }
                }
                updateTask.addDownloadTasks(downloadTaskModelList);

                // 通知界面刷新
                EventBus.getDefault().post(freshEvent);
            }

            @Override
            public void onDataNotAvailable() {
                LogUtil.d(TAG, " update onDataNotAvailable");
                // 请求失败
//                ToastUtil.ShortToast(getString(R.string.net_error));
                EventBus.getDefault().post(new UpdateEvent(EventConstant.REFRESH_ERROR));
                // 预约下一次更新请求
                orderNextTask(null);
            }
        });
    }

    // 获取下载文件夹，不存在则创建
    private File getDownloadFolder() {
        File folder = new File(BASE_SAVE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    // 通过url获取文件名称
    private String getFileNameByUrl(String url) {
        int index = -1;
        for (int i = url.length() - 1; i >= 0; i--) {
            if (url.charAt(i) == '/') {
                index = i;
                break;
            }
        }
        return url.substring(index + 1);
    }

    /**
     * MD5验证
     *
     * @param downloadTaskModel
     */
    private void checkMD5(DownloadTaskModel downloadTaskModel) {
        MD5CheckTask.getInstance().checkMd5(downloadTaskModel, new MD5CheckListener() {
            @Override
            public void start() {
                EventBus.getDefault().post(updateEventSpecial);
            }

            @Override
            public void success() {
                requestInstall(downloadTaskModel);
            }

            @Override
            public void error() {
                EventBus.getDefault().post(updateEventSpecial);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDownloadRequestReceived(DownloadRequestEvent downloadRequest) {
        LogUtil.d(TAG, "onDownloadRequestReceived ");
        if (downloadRequest.getTag().equals(EventConstant.DOWNLOAD_START_EVENT)) {
            // 开始任务
            if (downloadRequest.getDownloadTaskModelList() != null && downloadRequest.getDownloadTaskModelList().size() > 1) {
                updateTask.addDownloadTasks(downloadRequest.getDownloadTaskModelList());
            } else if (downloadRequest.getDownloadTaskModel() != null) {
                updateTask.startDownloadTask(downloadRequest.getDownloadTaskModel(), downloadListener);
            }
        } else if (downloadRequest.getTag().equals(EventConstant.DOWNLOAD_PAUSE_EVENT)) {
            // 暂停任务
            if (downloadRequest.getDownloadTaskModel() != null) {
                updateTask.pauseDownloadTask(downloadRequest.getDownloadTaskModel());
            } else if (downloadRequest.getDownloadTaskModelList() != null && downloadRequest.getDownloadTaskModelList().size() > 0) {
                for (DownloadTaskModel downloadTaskModel : downloadRequest.getDownloadTaskModelList()) {
                    updateTask.pauseDownloadTask(downloadTaskModel);
                }
            }
        } else if (downloadRequest.getTag().equals(EventConstant.DOWNLOAD_INSTALL_EVENT)) {
            // 开始安装
            if (downloadRequest.getDownloadTaskModel() != null) {
                checkMD5(downloadRequest.getDownloadTaskModel());
            } else if (downloadRequest.getDownloadTaskModelList() != null && downloadRequest.getDownloadTaskModelList().size() > 0) {
                for (DownloadTaskModel downloadTaskModel : downloadRequest.getDownloadTaskModelList()) {
                    checkMD5(downloadTaskModel);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBroadCastEventReceived(BroadCastEvent broadCastEvent) {
//        LogUtil.d(TAG, "onBroadCastEventReceived " + broadCastEvent.getTag());
        if (broadCastEvent.getTag().equals(EventConstant.BROADCAST_RESTART_DOWNLOAD)) {
            // 监听网络重连后任务重启事件
            requestUpdateInfo();
        } else if (broadCastEvent.getTag().equals(EventConstant.BROADCAST_REQUEST_DOWNLOAD)) {
            // 移动网络需要弹窗提示
            requestUpdateInfo();
        } else if (broadCastEvent.getTag().equals(EventConstant.BROADCAST_POWER_CHANGE)) {
            // 更新电池电量
            power = broadCastEvent.getPower();
        } else if (broadCastEvent.getTag().equals(EventConstant.BROADCAST_UPDATE_DEVICE_INF)) {
            requestUpdateInfo();
        }
    }

    private DownloadTaskModel getTaskFromBaseTask(BaseDownloadTask task) {
        for (DownloadTaskModel downloadTaskModel : downloadTaskModelList
                ) {
            if (downloadTaskModel.getUrl().equals(task.getUrl())) {
                return downloadTaskModel;
            }
        }
        return null;
    }

    private FileDownloadListener downloadListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            DownloadTaskModel downloadTaskModel = getTaskFromBaseTask(task);
            if (downloadTaskModel == null)
                return;
            downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_START);
            downloadTaskModel.setId(task.getId());
            downloadTaskModel.setTotalSize(totalBytes);
            downloadTaskModel.setDownloadSize(soFarBytes);
            downloadTaskModel.setProgress(totalBytes == 0 ? 0 : soFarBytes * 100 / totalBytes);
            downloadTaskModel.setTime(SystemClock.currentThreadTimeMillis());
            EventBus.getDefault().post(updateEventSpecial);
            LogUtil.d(TAG, "download pending  url = " + task.getUrl() + " total size = " + totalBytes);
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            DownloadTaskModel downloadTaskModel = getTaskFromBaseTask(task);
            if (downloadTaskModel == null)
                return;
            downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_CONNECTTED);
            downloadTaskModel.setId(task.getId());
            downloadTaskModel.setRetryCount(0); // 重置重试次数
            downloadTaskModel.setTotalSize(totalBytes);
            downloadTaskModel.setDownloadSize(soFarBytes);
            downloadTaskModel.setProgress(totalBytes == 0 ? 0 : soFarBytes * 100 / totalBytes);
            downloadTaskModel.setTime(SystemClock.currentThreadTimeMillis());
            EventBus.getDefault().post(updateEventSpecial);
            LogUtil.d(TAG, "download pending  url = " + task.getUrl() + " total size = " + totalBytes + "   progress = " + (totalBytes == 0 ? 0 : soFarBytes * 100 / totalBytes));
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            DownloadTaskModel downloadTaskModel = getTaskFromBaseTask(task);
            if (downloadTaskModel == null)
                return;
            downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_DOWNLOADING);
            downloadTaskModel.setId(task.getId());
            downloadTaskModel.setTotalSize(totalBytes);
            downloadTaskModel.setDownloadSize(soFarBytes);
            downloadTaskModel.setSpeed(task.getSpeed());
            downloadTaskModel.setProgress(totalBytes == 0 ? 0 : (int) ((long) soFarBytes * 100 / (long) totalBytes));
            downloadTaskModel.setTime(SystemClock.currentThreadTimeMillis());
            EventBus.getDefault().post(updateEvent);
            LogUtil.d(TAG, "download path = " + task.getPath() + "   soFarBytes = " + soFarBytes + " total size = " + totalBytes + "   progress = " + (totalBytes == 0 ? 0 : (long) soFarBytes * 100 / (long) totalBytes));
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            DownloadTaskModel downloadTaskModel = getTaskFromBaseTask(task);
            if (downloadTaskModel == null)
                return;
            // 正在校验或者安装了，则不改变其状态
            if (!downloadTaskModel.getStatus().equals(DownloadTaskModel.DownloadStatus.STATUS_CHECKING) && !downloadTaskModel.getStatus().equals(DownloadTaskModel.DownloadStatus.STATUS_INSTALLING))
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_COMPLETE);

            downloadTaskModel.setId(task.getId());
            downloadTaskModel.setProgress(100);
            downloadTaskModel.setTime(SystemClock.currentThreadTimeMillis());
            EventBus.getDefault().post(updateEventSpecial);
            LogUtil.d(TAG, "download completed  = " + task.getPath());

            // MD5校验,必须在子线程中进行(大文件MD5校验为耗时操作)
            checkMD5(downloadTaskModel);
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            DownloadTaskModel downloadTaskModel = getTaskFromBaseTask(task);
            if (downloadTaskModel == null)
                return;
            downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_PAUSED);
            downloadTaskModel.setId(task.getId());
            downloadTaskModel.setTotalSize(totalBytes);
            downloadTaskModel.setDownloadSize(soFarBytes);
            downloadTaskModel.setProgress(totalBytes == 0 ? 0 : (int) ((long) soFarBytes * 100 / (long) totalBytes));
            downloadTaskModel.setTime(SystemClock.currentThreadTimeMillis());
            EventBus.getDefault().post(updateEventSpecial);
            LogUtil.d(TAG, "download pause = " + task.getPath() + "   soFarBytes = " + soFarBytes + " total size = " + totalBytes);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            DownloadTaskModel downloadTaskModel = getTaskFromBaseTask(task);
            if (downloadTaskModel == null)
                return;
            downloadTaskModel.setId(task.getId());
            downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_DOWNLOAD_ERROR);
            downloadTaskModel.setTime(SystemClock.currentThreadTimeMillis());
            LogUtil.d(TAG, "download error  = " + e.getMessage() + "    " + task.getUrl());

            // 下载失败后
            int retryCount = downloadTaskModel.getRetryCount();
            retryCount++;
            if (retryCount > 5) {
                // 重试次数超过5次则放弃重试
                retryCount = 0;
                downloadTaskModel.setRetryCount(retryCount);
                downloadTaskModel.setStatus(DownloadTaskModel.DownloadStatus.STATUS_DOWNLOAD_ERROR);
                EventBus.getDefault().post(updateEventSpecial);
                // TODO 上报错误
                return;
            }
            downloadTaskModel.setRetryCount(retryCount);
            LogUtil.d(TAG, "download onFailed = " + e.getMessage() + " retryCount = " + retryCount + " task = " + downloadTaskModel.getUrl());

            // 3秒后重新开始任务
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateTask.startDownloadTask(downloadTaskModel, downloadListener);
                }
            }, 10000);
        }

        @Override
        protected void warn(BaseDownloadTask task) {
        }
    };

    private boolean isTaskExist(DownloadTaskModel downloadTaskModel) {
        for (DownloadTaskModel task : downloadTaskModelList) {
            if (downloadTaskModel.getUrl() != null && downloadTaskModel.getUrl().equals(task.getUrl())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 预约下一次更新请求
     *
     * @param updateResponseData
     */
    private void orderNextTask(UpdateResponseData updateResponseData) {
        int requsttime;
        // 参数错误的情况
        if (updateResponseData == null || (updateResponseData.getParamInfo() == null) || (updateResponseData.getParamInfo().getRequesttime() == null)) {
            // 下一个小时更新
            requsttime = 3600 * 1000;
        } else {
            requsttime = Integer.parseInt(updateResponseData.getParamInfo().getRequesttime());
            requsttime = requsttime * 60 * 1000;
        }

        LogUtil.d(TAG, "orderNextTask = " + requsttime + "  time = " + TimeUtils.getTime(requsttime));
        if (requsttime < 1000) {
            requsttime = 3600 * 1000;
        }

        initUpdateAlarmManager(requsttime);
    }

    private void initUpdateAlarmManager(long firstTime) {
        cancelUpdateAlarmManager();
        LogUtil.d(TAG, " initUpdateAlarmManager firsttime = " + firstTime + "    time = " + TimeUtils.getTime(System.currentTimeMillis() + SystemClock.elapsedRealtime() + firstTime));
        Intent intent = new Intent(ACTION_UPDATE_DEVICE_INF);
        updateSender = PendingIntent.getBroadcast(this, 0, intent, 0);
        long triggerAtTime = SystemClock.elapsedRealtime() + firstTime;
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, updateSender);//开启提醒
    }

    /**
     * 取消原预约事件
     */
    private void cancelUpdateAlarmManager() {
        if (updateSender != null) {
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            manager.cancel(updateSender);
        }
    }

    /**
     * 非WiFi状态下下载弹窗
     *
     * @param checkListener
     */

    private void showContinueDownloadDialog(OnNetWorkStatusCheckListener checkListener) {
        // 如不允许非WiFi状态下下载则弹窗
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.not_wifi_if_continue_dowload);
        builder.setTitle(R.string.prompt);
        builder.setNegativeButton(R.string.download_with_gprs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.d(TAG, "download without wifi");
                checkListener.onCheckOK();
                isDownloadWithoutWifi = true;
            }
        });
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.d(TAG, "cancel download");
                checkListener.onCheckCancel();
                isDownloadWithoutWifi = false;
            }
        });

        AlertDialog d = builder.create();
        d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        d.show();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onRebootEventReceived(RebootEvent rebootEvent) {
        LogUtil.d(TAG, "onRebootEventReceived ");
        if (rebootEvent.getTag().equals(EventConstant.REBOOT_SETTING_EVENT)) {
            RebootTask.getInstance().initRebootAlarmManager();
        } else if (rebootEvent.getTag().equals(EventConstant.REBOOT_CANCEL_EVENT)) {
            RebootTask.getInstance().cancelRebootAlarmManager();
        } else if (rebootEvent.getTag().equals(EventConstant.REBOOT_REQUEST_EVENT)) {
            doForceReboot();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onScreenEventReceived(ScreenEvent screenEvent) {
        if (screenEvent.getTag().equals(EventConstant.SCREEN_EVENT)) {
//            isScreenOn = screenEvent.isScreenOn();
            LogUtil.d(TAG, "onScreenEventReceived isScreenOn = " + isScreenOn);
        }
    }

    private void doForceReboot() {
        isScreenOn = PowerTask.getInstance().isScreenOn();
        LogUtil.d(TAG, "------ device request reboot --------   isScreenOn = " + isScreenOn);
        if (isScreenOn) {
            // 亮屏状态下则弹窗提示
            RebootTask.getInstance().showRebootRequestDialog();
        } else {
            // 息屏状态直接重启
            LogUtil.d(TAG, " screen is off ,requestForceReboot !");
            RebootTask.getInstance().requestForceReboot();
        }
    }

}

