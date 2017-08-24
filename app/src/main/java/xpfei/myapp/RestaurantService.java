package xpfei.myapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/07/28
 */
public class RestaurantService extends Service {
    private RemoteCallbackList<NotifyCallBack> mCallBacks = new RemoteCallbackList<>();


    private RestaurantAidlInterface.Stub mBinder = new RestaurantAidlInterface.Stub() {

        @Override
        public void join(String name) throws RemoteException {
            notifyCallBack(name);
        }

        @Override
        public void registerCallBack(NotifyCallBack cb) throws RemoteException {
            mCallBacks.register(cb);
        }

        @Override
        public void unregisterCallBack(NotifyCallBack cb) throws RemoteException {
            mCallBacks.unregister(cb);
        }

    };

    private void notifyCallBack(String customerName) {
        final int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).notifyMainUiThread(customerName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        //销毁回调资源否则要内存泄露
        mCallBacks.kill();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
