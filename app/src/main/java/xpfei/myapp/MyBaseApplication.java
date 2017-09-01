package xpfei.myapp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import xpfei.myapp.manager.AbstractDatabaseManager;
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

    public IMusicPlayerInterface getmService() {
        return mService;
    }

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

    private void bindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
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
        aCache.put(ContentValue.ACACHEkEY_VERSION, versionCode);
        bindService();
//        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unbindService(connection);
        mService = null;
        connection = null;
        application = null;
    }
}
