package xpfei.mylibrary.net;

import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xpfei.mylibrary.net.response.DownloadResponseHandler;
import xpfei.mylibrary.utils.AppLog;

/**
 * Description:下载成功的回调函数
 * Author: xpfei
 * Date:   2017/09/07
 */
public class MyDownloadCallback implements Callback {

    private Handler mHandler;
    private DownloadResponseHandler mDownloadResponseHandler;
    private String mFileDir;
    private String mFilename;

    public MyDownloadCallback(Handler handler, DownloadResponseHandler downloadResponseHandler,
                              String filedir, String filename) {
        mHandler = handler;
        mDownloadResponseHandler = downloadResponseHandler;
        mFileDir = filedir;
        mFilename = filename;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AppLog.Logd("Error:" + e.toString());
                mDownloadResponseHandler.onFailure("下载失败，请稍后再试");
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if (response.isSuccessful()) {
            try {
                File file = saveFile(response, mFileDir, mFilename);
                final File newFile = file;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadResponseHandler.onFinish(newFile);
                    }
                });
            } catch (IOException e) {
                AppLog.Logd("Error:" + e.toString());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadResponseHandler.onFailure("下载失败，请稍后再试");
                    }
                });
            }
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    AppLog.Logd("Error:" + "fail status=" + response.code());
                    mDownloadResponseHandler.onFailure("下载失败，请稍后再试");
                }
            });
        }
    }


    /**
     * 保存文件
     */
    private File saveFile(Response response, String filedir, String filename) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(filedir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            if (is != null) is.close();
            if (fos != null) fos.close();
        }
    }
}
