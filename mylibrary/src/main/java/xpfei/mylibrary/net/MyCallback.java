package xpfei.mylibrary.net;

import android.os.Handler;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xpfei.mylibrary.net.response.FastJsonResponseHandler;
import xpfei.mylibrary.net.response.IResponseHandler;
import xpfei.mylibrary.net.response.JsonResponseHandler;
import xpfei.mylibrary.net.response.RawResponseHandler;
import xpfei.mylibrary.utils.AppLog;

/**
 * Description:  请求成功回调函数
 * Author: xpfei
 * Date:   2017/09/07
 */
public class MyCallback implements Callback {
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