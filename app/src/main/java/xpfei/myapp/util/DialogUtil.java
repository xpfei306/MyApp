package xpfei.myapp.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import xpfei.mylibrary.utils.StringUtil;

/**
 * dialog工具类
 * Created by xpf on 2016/9/23.
 */

public class DialogUtil {
    /**
     * 创建ProgressDialog不带进度条
     *
     * @param context
     * @param message ProgressDialog显示内容
     * @return
     */
    public static ProgressDialog GetMyProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        return dialog;
    }

    /**
     * 创建一个dialog
     *
     * @param context
     * @param message  dialog提示框内容
     * @param listener 确定按钮的监听事件
     * @return
     */
    public static AlertDialog GetMyDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(message).setTitle("提示").setPositiveButton("确认", listener).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.setCancelable(false); // 是否可以按返回键消失
        dialog.setCanceledOnTouchOutside(false); //点击加载框以外的区域
        return dialog;
    }

    /**
     * 创建一个dialog
     *
     * @param context
     * @param message dialog提示框内容
     * @return
     */
    public static AlertDialog GetMyDialogNoNegative(Context context, String message) {
        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(message).setTitle("提示").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        return dialog;
    }

    /**
     * 创建一个dialog
     *
     * @param context
     * @param message dialog提示框内容
     * @return
     */
    public static AlertDialog GetMyDialogNoNegative(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(message).setTitle("提示").
                setPositiveButton("确认", listener).create();
        dialog.setCancelable(false); // 是否可以按返回键消失
        dialog.setCanceledOnTouchOutside(false); //点击加载框以外的区域
        return dialog;
    }

    /**
     * 创建一个dialog
     *
     * @param context
     * @param message  dialog提示框内容
     * @param listener 确定按钮的监听事件
     * @return
     */
    public static AlertDialog GetMyDialog(Context context, @Nullable String message, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener1) {
        if (StringUtil.isEmpty(message)) {
            message = "下载新版本";
        }
        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(message).setTitle("提示").setPositiveButton("确认", listener).setNegativeButton("取消", listener1).create();
        dialog.setCancelable(false); // 是否可以按返回键消失
        dialog.setCanceledOnTouchOutside(false); //点击加载框以外的区域
        return dialog;
    }
}
