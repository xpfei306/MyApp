package xpfei.mylibrary.net;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xpfei.mylibrary.net.body.ProgressRequestBody;
import xpfei.mylibrary.net.body.ResponseProgressBody;
import xpfei.mylibrary.net.log.LoggerInterceptor;
import xpfei.mylibrary.net.response.DownloadResponseHandler;
import xpfei.mylibrary.net.response.FastJsonResponseHandler;
import xpfei.mylibrary.net.response.IResponseHandler;
import xpfei.mylibrary.net.response.JsonResponseHandler;
import xpfei.mylibrary.net.response.RawResponseHandler;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.StringUtil;


/**
 * Description: okHttp的封装基类
 * Author: xpfei
 * Date:   2017/08/01
 */
public class MyOkHttp {
    private OkHttpClient client;
    private static MyOkHttp instance;

    /**
     * client 初始化
     */
    public MyOkHttp() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor(true))
                .build();
    }

    /**
     * 单例模式获取client
     *
     * @return client的实例
     */
    public static MyOkHttp getInstance() {
        if (instance == null) {
            synchronized (MyOkHttp.class) {
                instance = new MyOkHttp();
            }
        }
        return instance;
    }

    /**
     * post方式 传递的是键值对
     *
     * @param context         标记 跟后面的取消有关
     * @param url             请求地址
     * @param params          请求的键值对
     * @param headers         头文件
     * @param responseHandler 请求的结果回调
     */
    public void post(Context context, @NonNull String url, Map<String, String> params, Headers headers, IResponseHandler responseHandler) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request.Builder builders = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            builders.headers(headers);
        }
        if (context != null) {
            builders.tag(context);
        }
        builders.url(url).post(builder.build());
        Request request = builders.build();
        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * post方式 传递的是键值对
     *
     * @param context         标记 跟后面的取消有关
     * @param url             请求地址
     * @param params          请求的json
     * @param headers         头文件
     * @param responseHandler 请求的结果回调
     */
    public void postString(Context context, @NonNull String url, String params, Headers headers, IResponseHandler responseHandler) {
        Request.Builder builders = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            builders.headers(headers);
        }
        if (context != null) {
            builders.tag(context);
        }
        if (!StringUtil.isEmpty(params)) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, params);
            builders.url(url).post(requestBody);
        }
        Request request = builders.build();
        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * get方式 传递的是键值对
     *
     * @param context         标记 跟后面的取消有关
     * @param url             请求地址
     * @param params          请求的json
     * @param headers         头文件
     * @param responseHandler 请求的结果回调
     */
    public void get(Context context, @NonNull String url, Map<String, String> params, Headers headers, IResponseHandler responseHandler) {
        //拼接url
        String get_url = url;
        if (params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }
        Request.Builder builders = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            builders.headers(headers);

        }
        if (context != null) {
            builders.tag(context);
        }
        Request request = builders.url(get_url).build();
        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * 上传文件
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param files           上传的文件files
     * @param responseHandler 回调
     */
    public void upload(Context context, @NonNull String url, Map<String, String> params, Map<String, File> files, IResponseHandler responseHandler) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }

        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                multipartBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        Request.Builder builders = new Request.Builder();
        builders.url(url)
                .post(new ProgressRequestBody(multipartBuilder.build(), responseHandler));
        if (context != null) {
            builders.tag(context);
        }
        Request request = builders.build();
        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * 下载文件
     *
     * @param url                     下载地址
     * @param downloadResponseHandler 下载回调
     */
    public void download(@NonNull String url, DownloadResponseHandler downloadResponseHandler) {
        download(null, url, null, null, downloadResponseHandler);
    }

    /**
     * 下载文件
     *
     * @param context                 context
     * @param url                     下载地址
     * @param downloadResponseHandler 下载回调
     */
    public void download(Context context, @NonNull String url, DownloadResponseHandler downloadResponseHandler) {
        download(context, url, null, null, downloadResponseHandler);
    }

    /**
     * 下载文件
     *
     * @param context                 context
     * @param url                     下载地址
     * @param filename                下载目的文件名
     * @param downloadResponseHandler 下载回调
     */
    public void download(Context context, @NonNull String url, String filename, DownloadResponseHandler downloadResponseHandler) {
        download(context, url, null, filename, downloadResponseHandler);
    }

    /**
     * 下载文件
     *
     * @param context                 发起请求的context
     * @param url                     下载地址
     * @param filedir                 下载目的目录
     * @param filename                下载目的文件名
     * @param downloadResponseHandler 下载回调
     */
    public void download(Context context, @NonNull String url, String filedir, String filename, final DownloadResponseHandler downloadResponseHandler) {
        Request.Builder builders = new Request.Builder();
        builders.url(url);
        if (context != null) {
            builders.tag(context);
        }
        if (StringUtil.isEmpty(filename)) {
            filename = url.substring(url.lastIndexOf("/"));
        }
        if (StringUtil.isEmpty(filedir)) {
            filedir = CommonUtil.getCachePath(context) + "/Myapp";
        }
        Request request = builders.build();
        client.newBuilder().addNetworkInterceptor(new Interceptor() {      //设置拦截器
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ResponseProgressBody(originalResponse.body(), downloadResponseHandler))
                        .build();
            }
        })
                .build()
                .newCall(request)
                .enqueue(new MyDownloadCallback(new Handler(), downloadResponseHandler, filedir, filename));
    }

    /**
     * 自定义的回调
     */
    private class MyCallback implements Callback {
        private Handler mHandler;
        private IResponseHandler mResponseHandler;

        public MyCallback(Handler handler, IResponseHandler responseHandler) {
            this.mHandler = handler;
            this.mResponseHandler = responseHandler;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    AppLog.Loge(e.toString());
                    String msg = "未知错误，请联系管理员！";
                    if (e instanceof SocketTimeoutException) {
                        msg = "连接超时，请检查网络设置";
                    }
                    mResponseHandler.onFailure(0, msg);
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (response.isSuccessful()) {
                final String response_body = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mResponseHandler instanceof JsonResponseHandler) {
                                JSONObject jsonBody = new JSONObject(response_body);
                                ((JsonResponseHandler) mResponseHandler).onSuccess(response.code(), jsonBody);
                            } else if (mResponseHandler instanceof FastJsonResponseHandler) {
                                ((FastJsonResponseHandler) mResponseHandler).getData(response.code(), response_body);
                            } else if (mResponseHandler instanceof RawResponseHandler) {
                                ((RawResponseHandler) mResponseHandler).onSuccess(response.code(), response_body);
                            } else {
                                mResponseHandler.onFailure(response.code(), "mResponseHandler是未知类型");
                            }
                        } catch (Exception e) {
                            AppLog.Loge("参数：" + response.code() + "fail parse jsonobject, body=" + response_body);
                            mResponseHandler.onFailure(response.code(), "未知错误，请联系管理员");
                        }
                    }
                });
            } else {
                mResponseHandler.onFailure(response.code(), "未知错误，请联系管理员");
                AppLog.Loge(response.code() + response.toString());
            }
        }
    }

    /**
     * 下载成功的回调函数
     */
    private class MyDownloadCallback implements Callback {

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
                } catch (final IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            AppLog.Logd("Error:" + e.toString());
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
    }

    /**
     * 取消当前context的所有请求
     *
     * @param context
     */
    public void cancel(Context context) {
        if (client != null) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(context))
                    call.cancel();
            }
            for (Call call : client.dispatcher().runningCalls()) {
                if (call.request().tag().equals(context))
                    call.cancel();
            }
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
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
