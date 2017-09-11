package xpfei.myapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.model.DownLoadInfo;
import xpfei.mylibrary.net.MyOkHttp;
import xpfei.mylibrary.net.response.DownloadResponseHandler;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: 下载服务
 * Author: xpfei
 * Date:   2017/09/07
 */
public class DownLoadService extends Service {
    private List<CustomerClient> mClientsList = new ArrayList<>();
    private RemoteCallbackList<DownCallBack> mCallBacks = new RemoteCallbackList<>();
    private DownInterface.Stub binder = new DownInterface.Stub() {
        @Override
        public void registerCallBack(DownCallBack cb) throws RemoteException {
            mCallBacks.register(cb);
        }

        @Override
        public void unregisterCallBack(DownCallBack cb) throws RemoteException {
            mCallBacks.unregister(cb);
        }

        @Override
        public void startDown(DownLoadInfo info) throws RemoteException {
            AppLog.Logd("startDown");
            info.setState(1);
            info.setTaskId(System.currentTimeMillis());
            download(info);
            notifyStart(info);
        }

        @Override
        public void cancelDown(DownLoadInfo info) throws RemoteException {
            info.setState(3);
            notifyCancel(info);
        }
    };
    private long lastTime;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void download(final DownLoadInfo info) {
        if (info == null) {
            CommonUtil.showToast(getBaseContext(), "暂无下载链接");
            return;
        }
        final String url = info.getDownloadUrl();
        if (StringUtil.isEmpty(url)) {
            CommonUtil.showToast(getBaseContext(), "暂无下载链接");
            return;
        }
        MyOkHttp.getInstance().download(info.getTaskId() + "", info.getDownloadUrl(), new DownloadResponseHandler() {
            @Override
            public void onFinish(File download_file) {
                info.setState(5);
                info.setTotalSize(info.getFileSize());
                notifySuccess(info, download_file.getAbsolutePath());
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                long time = System.currentTimeMillis();
                if (time - lastTime > 500) {
                    lastTime = time;
                    info.setState(2);
                    info.setTotalSize(currentBytes);
                    info.setFileSize(totalBytes);
                    notifyProgress(info, currentBytes, totalBytes);
                }
            }

            @Override
            public void onFailure(String error_msg) {
                info.setState(4);
                notifyFail(info);
            }
        });
    }

    private void notifyStart(DownLoadInfo info) {
        int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).start(info);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }

    private void notifyProgress(DownLoadInfo info, long currentBytes, long totalBytes) {
        try {
            int mBroadcastCount = mCallBacks.beginBroadcast();
            for (int i = 0; i < mBroadcastCount; i++) {
                DownCallBack callBack = mCallBacks.getBroadcastItem(i);
                if (callBack != null) {
                    callBack.progress(info, currentBytes, totalBytes);
                }
            }
            mCallBacks.finishBroadcast();
        } catch (Exception e) {
        } finally {
            try {
                mCallBacks.finishBroadcast();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void notifySuccess(DownLoadInfo info, String path) {
        int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).success(info, path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }

    private void notifyFail(DownLoadInfo info) {
        int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).fail(info);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }

    private void notifyCancel(DownLoadInfo info) {
        int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).cancelDown(info);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
        MyOkHttp.getInstance().cancel(info.getTaskId() + "");
    }

    @Override
    public void onDestroy() {
        //销毁回调资源否则要内存泄露
        mCallBacks.kill();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private class CustomerClient implements IBinder.DeathRecipient {
        public final IBinder mToken;
        public final String mCustomerName;

        public CustomerClient(IBinder mToken, String mCustomerName) {
            this.mToken = mToken;
            this.mCustomerName = mCustomerName;
        }

        @Override
        public void binderDied() {
            if (mClientsList.indexOf(this) >= 0) {
                mClientsList.remove(this);
            }
        }
    }
}
