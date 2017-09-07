package xpfei.myapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;

import xpfei.myapp.DownCallBack;
import xpfei.myapp.DownInterface;
import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.DownLoadAdapter;
import xpfei.myapp.databinding.ActivityRecyclerviewBinding;
import xpfei.myapp.db.DownManager;
import xpfei.myapp.service.MusicPlayService;

/**
 * Description: 下载管理
 * Author: xpfei
 * Date:   2017/09/07
 */
public class DownLoadActivity extends MyBaseActivity {
    private ActivityRecyclerviewBinding binding;
    private DownLoadAdapter adapter;
    private DownCallBack callBack = new DownCallBack.Stub() {
        @Override
        public void progress(long id, long currentBytes, long totalBytes) throws RemoteException {

        }

        @Override
        public void success(long id, String path) throws RemoteException {

        }

        @Override
        public void fail(long id) throws RemoteException {

        }

        @Override
        public void del(long id) throws RemoteException {

        }
    };
    private DownInterface mService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                if (mService == null) {
                    mService = DownInterface.Stub.asInterface(iBinder);
                }
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
        }
    };

    private void bindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview);
        onSetLeft(true);
        onSetTitle("下载管理");
        bindService();
        initView();
    }

    private void initView() {
        adapter = new DownLoadAdapter(this, new DownManager().loadAll(), R.layout.item_download);
        binding.mRecvclerview.setAdapter(adapter);
        binding.mRecvclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        mService = null;
        connection = null;
    }

    @Override
    public void onRequestData() {

    }
}
