package xpfei.mylibrary.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

import xpfei.mylibrary.utils.CommonUtil;


/**
 * @Description:   Activity管理类：用于管理Activity和退出程序 
 * @author:  xpfei   
 * @date:    2016/12/23 
 *  
 */
public class AppManager {

    // Activity栈
    private static Stack<Activity> activityStack;
    // 单例模式
    private static AppManager instance;
    private long exitTime = 0;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            for (Activity activity : activityStack) {
                if (null != activity) {
                    activity.finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            CommonUtil.showToast(currentActivity(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        }
    }
}
