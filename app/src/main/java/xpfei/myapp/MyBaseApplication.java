package xpfei.myapp;

import android.app.Application;

import xpfei.myapp.manager.AbstractDatabaseManager;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.manager.ACache;
import xpfei.mylibrary.utils.CommonUtil;


/**
 * 基础的Application
 * Created by xpf on 2016/9/27.
 */

public class MyBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.getInstance().init(getApplicationContext());
        AbstractDatabaseManager.initOpenHelper(getApplicationContext());
//        int code = SharedPreferencesUtils.GetCode(getApplicationContext());
        int versionCode = CommonUtil.GetCode(getApplicationContext());
//        if (code != versionCode) {
        AbstractDatabaseManager.UpdateDb();
        ACache aCache = ACache.get(this);
        aCache.put(ContentValue.ACACHEkEY_VERSION, versionCode);
//        }
    }
}
