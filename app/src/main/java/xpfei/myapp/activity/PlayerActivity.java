package xpfei.myapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import xpfei.myapp.NotifyCallBack;
import xpfei.myapp.RestaurantAidlInterface;
import xpfei.myapp.activity.base.MyBaseActivity;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/11
 */
public class PlayerActivity extends MyBaseActivity {

    private RestaurantAidlInterface mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (mService == null) {
                mService = RestaurantAidlInterface.Stub.asInterface(iBinder);
            }
            try {
                mService.registerCallBack(callBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                mService.unregisterCallBack(callBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mService = null;
        }
    };
    NotifyCallBack.Stub callBack = new NotifyCallBack.Stub() {
        @Override
        public void notifyMainUiThread(String name) throws RemoteException {

        }
    };

    private void bindService() {
        Intent intent = new Intent(RestaurantAidlInterface.class.getName());
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void unbindService() {
        unbindService(mServiceConnection);
    }

    @Override
    public void onRequestData() {

    }
}
