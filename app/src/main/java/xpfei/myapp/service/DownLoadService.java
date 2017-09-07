package xpfei.myapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import xpfei.myapp.DownCallBack;
import xpfei.myapp.DownInterface;
import xpfei.myapp.model.DownLoadInfo;
import xpfei.mylibrary.net.body.ResponseProgressBody;
import xpfei.mylibrary.net.log.LoggerInterceptor;
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
    private OkHttpClient client;
    private Map<Long, Call> callMap;
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

        }

        @Override
        public void pauseDown(DownLoadInfo info) throws RemoteException {

        }

        @Override
        public void delDown(DownLoadInfo info) throws RemoteException {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor(true))
                .build();
        callMap = new HashMap<>();
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
        final DownloadResponse downloadResponse = new DownloadResponse(info) {
            @Override
            public void finish(long id, File download_file) {

            }

            @Override
            public void progress(long id, long currentBytes, long totalBytes) {

            }

            @Override
            public void failure(long id, String name) {

            }
        };
        Request.Builder builders = new Request.Builder().addHeader("RANGE", "bytes=" + info.getTotalSize() + "-");
        builders.url(url);

        Request request = builders.build();
        Call call = client.newBuilder().addNetworkInterceptor(new Interceptor() {      //设置拦截器
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ResponseProgressBody(originalResponse.body(), downloadResponse))
                        .build();
            }
        })
                .build()
                .newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AppLog.Loge("downLoad error:" + e.getMessage());
                downloadResponse.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String filename = url.substring(url.lastIndexOf("/"));
                File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), filename);
                File saveFile = save(response, info.getTotalSize(), tempFile);
                downloadResponse.finish(info.getSong_id(), saveFile);
            }
        });
        callMap.put(info.getTaskId(), call);
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

    private abstract class DownloadResponse extends DownloadResponseHandler {
        private DownLoadInfo info;

        public DownloadResponse(DownLoadInfo info) {
            this.info = info;
        }

        public abstract void finish(long id, File download_file);

        public abstract void progress(long id, long currentBytes, long totalBytes);

        public abstract void failure(long id, String name);

        @Override
        public void onFinish(File download_file) {
            finish(info.getSong_id(), download_file);
        }

        @Override
        public void onProgress(long currentBytes, long totalBytes) {
            progress(info.getSong_id(), currentBytes, totalBytes);
        }

        @Override
        public void onFailure(String error_msg) {
            AppLog.Loge("download error：" + error_msg);
            failure(info.getTaskId(), info.getFileName());
        }
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

    private File save(Response response, long startsPoint, File destination) {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        // 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(destination, "rwd");
            //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return destination;
    }
}
