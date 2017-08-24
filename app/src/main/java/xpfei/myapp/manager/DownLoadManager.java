package xpfei.myapp.manager;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import xpfei.myapp.view.MyProgressDialog;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.StringUtil;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Description: 下载manager
 * Author: xpfei
 * Date:   2017/5/2
 */
public class DownLoadManager {
    private Activity activity;
    private long id;
    private TimerTask task;
    private String downloadUrl;
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private DownloadManager.Query query;
    private MyProgressDialog progressDialog;

    public DownLoadManager(Activity activity, String downloadUrl) {
        this.activity = activity;
        this.downloadUrl = downloadUrl;
        init();
    }

    private void init() {
        downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle("版本更新");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        request.setMimeType("application/vnd.android.package-archive");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        //创建目录
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
        //设置文件存放路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "MyApp.apk");
        query = new DownloadManager.Query();
        Timer timer = new Timer();
        progressDialog = new MyProgressDialog(activity);
        progressDialog.setTitle("更新提示");
        progressDialog.setMax(100);
        task = new TimerTask() {
            @Override
            public void run() {
                Cursor cursor = downloadManager.query(query.setFilterById(id));
                if (cursor != null && cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        case DownloadManager.STATUS_PAUSED:
                            break;
                        case DownloadManager.STATUS_PENDING:
                            if (!progressDialog.isShowing())
                                progressDialog.show();

                            break;
                        case DownloadManager.STATUS_RUNNING:
                            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            progressDialog.setMax(bytes_total);
                            progressDialog.setProgress(bytes_downloaded);
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            install(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/MyApp.apk");
                            progressDialog.setMax(100);
                            progressDialog.dismiss();
                            task.cancel();
                            break;
                        case DownloadManager.STATUS_FAILED:
                            downloadManager.remove(id);
                            CommonUtil.showToast(activity, "下载失败，请稍后在试");
                            progressDialog.dismiss();
                            task.cancel();
                            break;
                    }
                }
                if (cursor != null)
                    cursor.close();
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public void startDownLoad(String msg) {
        id = downloadManager.enqueue(request);
        if (StringUtil.isEmpty(msg))
            progressDialog.setMessage("正在下载...");
        else
            progressDialog.setMessage(msg);
        task.run();
    }

    private void install(@Nullable String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//4.0以上系统弹出安装成功打开界面
        activity.startActivity(intent);
    }
}
