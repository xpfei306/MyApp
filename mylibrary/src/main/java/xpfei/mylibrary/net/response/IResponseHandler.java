package xpfei.mylibrary.net.response;

/**
 * Description: 请求之后的回调 父类
 * Author: xpfei
 * Date:   2017/08/01
 */
public interface IResponseHandler  {
    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
