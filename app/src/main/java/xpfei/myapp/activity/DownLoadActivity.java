package xpfei.myapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.DownLoadAdapter;
import xpfei.myapp.databinding.ActivityRecyclerviewBinding;
import xpfei.myapp.model.DownLoadInfo;
import xpfei.myapp.service.DownCallBack;
import xpfei.myapp.service.DownInterface;
import xpfei.myapp.service.DownLoadService;

/**
 * Description: 下载管理
 * Author: xpfei
 * Date:   2017/09/07
 */
public class DownLoadActivity extends MyBaseActivity {
    private ActivityRecyclerviewBinding binding;
    private DownLoadAdapter adapter;
    private DownInterface mDownService;
    private DownCallBack callBack = new DownCallBack.Stub() {
        @Override
        public void progress(final DownLoadInfo info, long currentBytes, long totalBytes) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setInfo(info);
                }
            });
        }

        @Override
        public void start(DownLoadInfo info) throws RemoteException {
            adapter.setInfo(info);
        }

        @Override
        public void success(DownLoadInfo info, String path) throws RemoteException {
            adapter.setInfo(info);
        }

        @Override
        public void fail(DownLoadInfo info) throws RemoteException {
            adapter.setInfo(info);
        }

        @Override
        public void cancelDown(DownLoadInfo info) throws RemoteException {
            adapter.setInfo(info);
        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                if (mDownService == null) {
                    mDownService = DownInterface.Stub.asInterface(iBinder);
                }
                mDownService.registerCallBack(callBack);
                DownLoadInfo info = new DownLoadInfo();
                info.setDownloadUrl("http://p1.exmmw.cn/p1/pp/zksh.apk");
                mDownService.startDown(info);
                DownLoadInfo info1 = new DownLoadInfo();
                info1.setDownloadUrl("http://p1.exmmw.cn/p1/pp/jyyd.apk");
                mDownService.startDown(info1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                mDownService.unregisterCallBack(callBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private void bindService() {
        Intent intent = new Intent(this, DownLoadService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview);
        onSetLeft(true);
        onSetTitle("下载管理");
        initView();
        bindService();
    }

    private void initView() {
        adapter = new DownLoadAdapter(this, new ArrayList<DownLoadInfo>(), R.layout.item_download);
        binding.mRecvclerview.setAdapter(adapter);
        binding.mRecvclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        mDownService = null;
        connection = null;
    }

    @Override
    public void onRequestData() {
    }
}
