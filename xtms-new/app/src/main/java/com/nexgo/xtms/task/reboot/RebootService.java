package com.nexgo.xtms.task.reboot;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.nexgo.xtms.util.LogUtil;
import com.xgd.smartpos.manager.ICloudService;
import com.xgd.smartpos.manager.system.ISystemManager;
import com.xgd.smartpos.manager.system.ITaskCallback;

/**
 * Created by zhouxie on 2017/6/8.
 */

public class RebootService {
    private static final String TAG = RebootService.class.getSimpleName();
    private Context context;

    public RebootService(Context context) {
        this.context = context;
    }

    // 绑定aidl服务
    private ICloudService mICloudService = null;
    private ISystemManager mISystemManager = null;
    private Intent mIntent;
    private final int SYSTEM_MANAGER = 2;

    public void bindAidlService() {
        LogUtil.d("start rebootService");
        mIntent = new Intent();
        mIntent.setComponent(new ComponentName("com.xgd.possystemservice", "com.xgd.smartpos.systemservice.SystemInterfaceService"));
        context.bindService(mIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d(TAG, "rebootService onServiceConnected : " + name);

            mICloudService = ICloudService.Stub.asInterface(service);
            try {
                IBinder binder = mICloudService.getManager(SYSTEM_MANAGER);
                mISystemManager = ISystemManager.Stub.asInterface(binder);
                if (mISystemManager == null) {
                    LogUtil.d(TAG, " mISystemManager == null=============================");
                    return;
                }

                mISystemManager.registerCallback(new ITaskCallback() {
                    @Override
                    public void actionPerformed(int actionId) throws RemoteException {
                        LogUtil.d(TAG, "actionId->" + actionId);
                    }

                    @Override
                    public IBinder asBinder() {
                        return null;
                    }
                });

                // 重启
                mISystemManager.reboot();

            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtil.d(TAG, "RemoteException->" + e.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(TAG, "onServiceDisconnected->" + name);
        }
    };
}
