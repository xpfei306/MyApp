package xpfei.mylibrary.utils;

import android.content.Intent;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * 字符串的相关工具类
 * Created by xpf on 2016/9/23.
 */
public class StringUtil {

    public static final String IP_REG_EXPRESSION = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
    public static final String PORT_REG_EXPRESSION = "([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])";

    /**
     * 判断该字符串是否符合IP地址格式
     *
     * @param s
     * @return
     */
    public static boolean isIp(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        return Pattern.matches(IP_REG_EXPRESSION, s);
    }

    /**
     * 判断该字符串是否符合port地址格式
     *
     * @param s
     * @return
     */
    public static boolean isPort(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        return Pattern.matches(PORT_REG_EXPRESSION, s);
    }

    /**
     * 判断字符串是否为空
     *
     * @return
     */
    public static boolean isEmpty(String txt) {
        if (!TextUtils.isEmpty(txt) && !"".equals(txt.trim()))
            return false;
        else
            return true;
    }

    /**
     * 获取当前时间，再将其转化为"yyyy-MM-dd"的格式
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(System.currentTimeMillis());
    }


    public static String getStringExtra(Intent intent, String key, String defaultValue) {
        String value = intent.getStringExtra(key);
        if (!isEmpty(value))
            return value;
        return defaultValue;
    }

    public static int str2Int(String value) {
        if (!isEmpty(value)) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    public static String int2double(int value) {
        if (value > 10000) {
            double tempvalue = value / 10000.00;
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(tempvalue) + " 万";
        } else {
            return String.valueOf(value);
        }
    }

    public static String timeParse(long duration) {
        SimpleDateFormat times = new SimpleDateFormat("mm:ss");
        return times.format(duration);
    }
}
