package com.nexgo.xtms.task.update.ota;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.nexgo.xtms.R;
import com.nexgo.xtms.data.entity.DownloadTaskModel;
import com.nexgo.xtms.net.update.UpdateResponseData;
import com.nexgo.xtms.task.ErrorCode;
import com.nexgo.xtms.util.LogUtil;
import com.xgd.smartpos.manager.ICloudService;
import com.xgd.smartpos.manager.system.ISystemManager;
import com.xgd.smartpos.manager.system.ITaskCallback;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by fangqiang_001 on 2017/2/14.
 */

public class OTAUpdateTask {
    private DownloadTaskModel downloadTaskModel;
    private String TAG = "OTAUpdateTask";
    private UpdateResponseData.UpdateOtaBean otaBean;
    private Context context;
    private OnListenner onListenner;

    private Button positiveButton;
    private AlertDialog d;
    private Timer timer;
    public static final int OTA_TIME_COUNT = 1000;
    public static final int OTA_TIME_LIMIT = 30;  // 倒计时时限
    private int time = OTA_TIME_LIMIT;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == OTA_TIME_COUNT) {
                // 倒计时处理
                if (d != null && positiveButton != null && d.isShowing()) {
                    time--;
                    if (time < 0) {
                        // 倒计时结束,直接重启
                        positiveButton.performClick();
                    } else {
                        String tip = context.getString(R.string.reboot_time_tip);
                        String reboot_tip = String.format(tip, msg.arg1);
                        positiveButton.setText(reboot_tip);
                    }
                }
            }
        }
    };

    public OTAUpdateTask(Context context, DownloadTaskModel downloadTaskModel, OnListenner onListenner) {
        this.context = context.getApplicationContext();
        this.downloadTaskModel = downloadTaskModel;
        this.onListenner = onListenner;
    }

    // 请求更新ota包
    public void requestOTA(final String file) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (d != null && d.isShowing())
                    return;
                // 重置倒计时
                time = OTA_TIME_LIMIT;

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rootView = layoutInflater.inflate(R.layout.layout_ota_install_dialog, null);
                positiveButton = (Button) rootView.findViewById(R.id.positiveButton);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        onListenner.onDone();
                        installOTA(file);
                    }
                });
                Button negativeButton = (Button) rootView.findViewById(R.id.negativeButton);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onListenner.onCancel();
                        d.dismiss();
                    }
                });
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (time >= 0)
                            handler.obtainMessage(OTA_TIME_COUNT, time, 0).sendToTarget();
                    }
                };
                timer = new Timer();
                timer.schedule(task, 1000, 1000);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false)
                        .setView(rootView)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                timer.cancel();
                            }
                        });

                d = builder.create();
                d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                d.show();
            }
        });
    }

    // 安装OTA
    private String filePath;

    private void installOTA(String path) {
        this.filePath = path;
        bindAidlService();
    }

    // 回调信息
    public interface OnListenner {
        void onDone();

        void onCancel();

        void onError(ErrorCode errorCode);
    }

    // 绑定aidl服务
    private ICloudService mICloudService = null;
    private final int SYSTEM_MANAGER = 2;
    private ISystemManager mISystemManager = null;
    private static final int UPDATE_OS = 1;
    private Intent mIntent;

    private void bindAidlService() {
        LogUtil.d("------     start reboot service     ------");
        mIntent = new Intent();
        mIntent.setComponent(new ComponentName("com.xgd.possystemservice", "com.xgd.smartpos.systemservice.SystemInterfaceService"));
        context.bindService(mIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mICloudService = ICloudService.Stub.asInterface(service);
            try {
                LogUtil.d("sdk version: " + mICloudService.getServiceSdkVersion());

                try {
                    IBinder binder = mICloudService.getManager(SYSTEM_MANAGER);
                    mISystemManager = ISystemManager.Stub.asInterface(binder);
                    if (mISystemManager == null) {
                        LogUtil.d("mISystemManager == null=============================");
                        return;
                    }

                    mISystemManager.registerCallback(new ITaskCallback() {
                        @Override
                        public void actionPerformed(int actionId) throws RemoteException {
                            LogUtil.d("actionId->" + actionId);
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    });

                    // 在子线程中开启OTA更新
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mISystemManager.updateSystem(filePath, UPDATE_OS);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

//                    mISystemManager.updateSystem(filePath,UPDATE_OS);
                    //Environment.getExternalStorageDirectory().getAbsolutePath()+"/update_new.zip"
//					mISystemManager.updateSystem("123456", UPDATE_OS);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    LogUtil.d("RemoteException->" + e.toString());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtil.d("RemoteException->" + e.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("onServiceDisconnected->" + name);
        }
    };
}
