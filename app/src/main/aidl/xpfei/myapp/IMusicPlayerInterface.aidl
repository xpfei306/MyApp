
package xpfei.myapp;
import xpfei.myapp.IMusicCallBack;

interface IMusicPlayerInterface {
     //注册回调接口
     void registerCallBack(IMusicCallBack cb);
     //注销回调接口
     void unregisterCallBack(IMusicCallBack cb);
}
