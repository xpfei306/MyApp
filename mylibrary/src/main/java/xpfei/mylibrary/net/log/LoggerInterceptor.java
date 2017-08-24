package xpfei.mylibrary.net.log;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import xpfei.mylibrary.utils.AppLog;

/**
 * Description: okhttp请求的log日志管理类
 * Author: xpfei
 * Date:   2017/08/01
 */
public class LoggerInterceptor implements Interceptor {
    private boolean showResponse;

    public LoggerInterceptor(boolean showResponse) {
        this.showResponse = showResponse;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            AppLog.Loge("code : " + clone.code());
            if (!TextUtils.isEmpty(clone.message()))
                AppLog.Loge("message : " + clone.message());
            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        if (isText(mediaType)) {
                            String resp = body.string();
                            AppLog.Loge("responseBody's content : " + resp);
                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            AppLog.Loge("responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            AppLog.Loge("method : " + request.method());
            AppLog.Loge("url : " + url);
            if (headers != null && headers.size() > 0) {
                AppLog.Loge("headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        AppLog.Loge("requestBody's content : " + bodyToString(request));
                    } else {
                        AppLog.Loge("requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml"))
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
