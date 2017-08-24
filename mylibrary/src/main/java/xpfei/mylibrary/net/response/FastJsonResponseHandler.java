package xpfei.mylibrary.net.response;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.StringUtil;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Description: FastJson解析数据
 * Author: xpfei
 * Date:   2017/08/09
 */
public abstract class FastJsonResponseHandler<T> implements IResponseHandler {

    public abstract void onSuccess(T response);

    public void getData(int statusCode, String response) {
        if (statusCode == 200) {
            try {
                Map tempMap = parseObject(response, Map.class);
                if (isSuccessful(tempMap)) {
                    callBySuccess(successData(tempMap));
                } else {
                    String message = getMessage(tempMap);
                    if (StringUtil.isEmpty(message)) {
                        message = "未知错误，请联系管理员！";
                    }
                    onFailure(-605, message);
                }
            } catch (Exception e) {
                AppLog.Logd("Error：" + e.toString());
                onFailure(-605, "未知错误，请联系管理员！");
            }
        } else {
            AppLog.Logd("Error:" + statusCode);
            onFailure(statusCode, "网络故障，请稍后再试~");
        }
    }

    private void callBySuccess(Object data) {
        T tempResult = null;
        Type genericType = getClass().getGenericSuperclass();
        if (genericType instanceof ParameterizedType) {
            Type tempType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            tempResult = JSON.parseObject(data.toString(), tempType);
        }
        onSuccess(tempResult);
    }

    /**
     * 获取json中的code
     *
     * @param rawData json
     */
    private int getCode(Map rawData) {
        return (int) rawData.get("code");
    }

    /**
     * 获取json中的message
     *
     * @param rawData json
     */
    private String getMessage(Map rawData) {
        String message = (String) rawData.get("message");
        if (StringUtil.isEmpty(message)) {
            message = "服务器繁忙！请稍后重试";
        }
        return message;
    }

    private boolean isSuccessful(Map rawData) {
        try {
            int code = this.getCode(rawData);
            if (code > 0) {
                return true;
            }
        } catch (Exception var3) {
            return false;
        }
        return false;
    }

    /**
     * 获取json中的data
     *
     * @param rawData json
     */
    private Object successData(Map rawData) {
        return rawData.get("data");
    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
