package xpfei.mylibrary.utils;

import java.util.Map;

/**
 * Description: Url的工具类
 * Author: xpfei
 * Date:   2017/08/23
 */
public class UrlUtil {
    public static String getUrl(String BaseUrl,Map<String, String> params) {
        String URL = BaseUrl;
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                URL = URL + "&" + entry.getKey() + "=" + entry.getValue();
            }
        }
        return URL;
    }
}
