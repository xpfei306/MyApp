package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import xpfei.myapp.MyBaseApplication;
import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.DownLoadAdapter;
import xpfei.myapp.databinding.ActivityRecyclerviewBinding;
import xpfei.myapp.db.DownManager;
import xpfei.myapp.model.DownLoadInfo;
import xpfei.myapp.model.DownLoadInfoDao;
import xpfei.myapp.service.DownCallBack;
import xpfei.myapp.service.DownInterface;
import xpfei.myapp.view.MultiStateView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview);
        onSetLeft(true);
        onSetTitle("下载管理");
        mDownService = MyBaseApplication.application.getmDownService();
        initView();
    }

    private void initView() {
        binding.mRecvclerview.post(new Runnable() {
            @Override
            public void run() {
                QueryBuilder builder = new DownManager().getQueryBuilder();
                builder.where(DownLoadInfoDao.Properties.State.eq(2)).orderAsc(DownLoadInfoDao.Properties.TaskId);
                List<DownLoadInfo> list = builder.list();
                adapter = new DownLoadAdapter(DownLoadActivity.this, list, R.layout.item_download);
                binding.mRecvclerview.setAdapter(adapter);
                binding.mRecvclerview.setLayoutManager(new LinearLayoutManager(DownLoadActivity.this));
                if (list == null || list.size() == 0) {
                    binding.mMultiStateView.setViewState(MultiStateView.STATE_EMPTY, "暂无下载内容");
                } else {
                    binding.mMultiStateView.setViewState(MultiStateView.STATE_CONTENT);
                }
                if (mDownService != null) {
                    try {
                        mDownService.registerCallBack(callBack);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mDownService.unregisterCallBack(callBack);
            callBack = null;
            mDownService = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestData() {
    }
}
