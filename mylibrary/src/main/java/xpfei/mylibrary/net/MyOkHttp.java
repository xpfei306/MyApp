package xpfei.mylibrary.net;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
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
import xpfei.mylibrary.net.response.IResponseHandler;
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
        if (StringUtil.isEmpty(url)) {
            return;
        }
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
     * 下载文件
     *
     * @param url                     下载地址
     * @param downloadResponseHandler 下载回调
     */
    public void download(String tag, @NonNull String url, final DownloadResponseHandler downloadResponseHandler) {
        download(tag, url, null, downloadResponseHandler);
    }

    /**
     * 下载文件
     *
     * @param url                     下载地址
     * @param downloadResponseHandler 下载回调
     */
    public void download(String tag, @NonNull String url, String filename, final DownloadResponseHandler downloadResponseHandler) {
        Request.Builder builders = new Request.Builder();
        builders.url(url);
        builders.tag(tag);
        if (StringUtil.isEmpty(filename)) {
            filename = url.substring(url.lastIndexOf("/"));
        }
        String filedir = new File(Environment.getExternalStorageDirectory().getPath() + "/MyApp").getAbsolutePath();
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
     * 取消当前context的所有请求
     *
     * @param tag
     */
    public void cancel(String tag) {
        if (client != null) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(tag))
                    call.cancel();
            }
            for (Call call : client.dispatcher().runningCalls()) {
                if (call.request().tag().equals(tag))
                    call.cancel();
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
