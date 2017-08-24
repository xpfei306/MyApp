
package xpfei.myapp;
import xpfei.myapp.NotifyCallBack;

interface RestaurantAidlInterface {
     //新来了一个顾客
     void join(String name);
     //注册回调接口
     void registerCallBack(NotifyCallBack cb);
     //注销回调接口
     void unregisterCallBack(NotifyCallBack cb);
}
