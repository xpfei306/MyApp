package xpfei.myapp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import xpfei.myapp.manager.AbstractDatabaseManager;
import xpfei.myapp.service.DownInterface;
import xpfei.myapp.service.DownLoadService;
import xpfei.myapp.service.IMusicPlayerInterface;
import xpfei.myapp.service.MusicPlayService;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.manager.ACache;
import xpfei.mylibrary.utils.CommonUtil;


/**
 * 基础的Application
 * Created by xpf on 2016/9/27.
 */
public class MyBaseApplication extends Application {
    public static MyBaseApplication application;
    private IMusicPlayerInterface mService;
    private DownInterface mDownService;
    private ServiceConnection mDownconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (mDownService == null) {
                mDownService = DownInterface.Stub.asInterface(iBinder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (mService == null) {
                mService = IMusicPlayerInterface.Stub.asInterface(iBinder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public IMusicPlayerInterface getmService() {
        return mService;
    }

    public DownInterface getmDownService() {
        return mDownService;
    }

    private void bindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void bindDownService() {
        Intent intent = new Intent(this, DownLoadService.class);
        bindService(intent, mDownconnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
//        CrashHandler.getInstance().init(getApplicationContext());
        AbstractDatabaseManager.initOpenHelper(getApplicationContext());
//        int code = SharedPreferencesUtils.GetCode(getApplicationContext());
        int versionCode = CommonUtil.GetCode(getApplicationContext());
//        if (code != versionCode) {
        AbstractDatabaseManager.UpdateDb();
        ACache aCache = ACache.get(this);
        aCache.put(ContentValue.AcacheKey.ACACHEkEY_VERSION, versionCode);
        bindService();
        bindDownService();
//        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unbindService(connection);
        unbindService(mDownconnection);
        mDownService = null;
        mDownconnection = null;
        mService = null;
        connection = null;
        application = null;
    }
}
