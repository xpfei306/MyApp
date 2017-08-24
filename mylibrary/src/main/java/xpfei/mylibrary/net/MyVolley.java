package xpfei.mylibrary.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Map;

import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.UrlUtil;


/**
 * Description: okHttp的封装基类
 * Author: xpfei
 * Date:   2017/08/01
 */
public class MyVolley {
    private static MyVolley instance;
    private RequestQueue mQueue;
    private Context context;

    /**
     * client 初始化
     */
    public MyVolley(Context context) {
        mQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    /**
     * 单例模式获取client
     *
     * @return client的实例
     */
    public static MyVolley getInstance(Context context) {
        if (instance == null) {
            synchronized (MyVolley.class) {
                instance = new MyVolley(context);
            }
        }
        return instance;
    }

    public void get(String baseUrl, Map<String, String> params, final MyCallBack callBack) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlUtil.getUrl(baseUrl, params), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null) {
                    AppLog.Loge("responseBody's content : " + jsonObject.toString());
                    callBack.onSuccess(jsonObject);
                } else {
                    callBack.onFailure("服务器繁忙，请稍后再试！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String msg = "未知错误，请联系管理员！";
                AppLog.Loge(""+volleyError.getMessage());
                Throwable cause = volleyError.getCause();
                if (cause != null && cause instanceof SocketTimeoutException) {
                    msg = "连接超时，请检查网络设置！";
                }
                callBack.onFailure(msg);
            }
        });
        AppLog.Logd(request.getUrl());
        request.setTag(context);
        mQueue.add(request);
    }

    public interface MyCallBack {
        void onSuccess(JSONObject jsonObject);

        void onFailure(String msg);
    }

    public void cancelAll() {
        if (mQueue != null) {
            mQueue.cancelAll(context);
            mQueue = null;
        }
    }
}
