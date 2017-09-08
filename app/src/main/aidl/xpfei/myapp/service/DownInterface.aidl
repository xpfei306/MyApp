
package xpfei.myapp.service;
import xpfei.myapp.service.DownCallBack;
import xpfei.myapp.model.DownLoadInfo;
interface DownInterface {
     //注册回调接口
     void registerCallBack(DownCallBack cb);
     //注销回调接口
     void unregisterCallBack(DownCallBack cb);
     //开始下载
     void startDown(in DownLoadInfo info);
     //取消下载
     void cancelDown(in DownLoadInfo info);

}
