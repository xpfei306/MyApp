package xpfei.mylibrary.utils;

import android.util.Log;

/**
 * @author 许鹏飞
 * @Description: log日志类
 * @date 2016-3-9
 */
public class AppLog {
    public static final String TAG = "networkEngine";
    private static final boolean DEBUG = true;

    public static void Logi(String content) {
        Logi(TAG, content);
    }

    public static void Loge(String content) {
        Loge(TAG, content);
    }

    public static void Logd(String content) {
        Logd(TAG, content);
    }

    private static void Logi(String tag, String content) {
        if (DEBUG) {
            Log.i(tag, content);
        }
    }

    private static void Loge(String tag, String content) {
        if (DEBUG) {
            Log.e(tag, content);
        }
    }

    private static void Logd(String tag, String content) {
        if (DEBUG) {
            Log.d(tag, content);
        }
    }

}