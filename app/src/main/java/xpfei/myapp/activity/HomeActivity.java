package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.databinding.ActivityHomeBinding;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.view.pulltorefresh.PullToRefreshBase;
import xpfei.mylibrary.view.swipemenulistview.SwipeMenu;
import xpfei.mylibrary.view.swipemenulistview.SwipeMenuCreator;
import xpfei.mylibrary.view.swipemenulistview.SwipeMenuItem;
import xpfei.mylibrary.view.swipemenulistview.SwipeMenuListView;

public class HomeActivity extends MyBaseActivity {
    private ActivityHomeBinding binding;
    /**
     * 适配器
     */
    private SimpleAdapter mAdapter;
    /**
     * 数据源
     */
    List<Map<String, Object>> datas = new ArrayList<>();
    /**
     * 信息
     */
    private String[] message = {"数据0", "数据1", "数据2", "数据3", "数据4", "数据5", "数据6", "数据7", "数据8", "数据9", "数据10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        onSetTitle("列表");
        onSetLeft(true);
        initData();
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mAdapter = new SimpleAdapter(this, datas, R.layout.item_adapter, new String[]{"message"}, new int[]{R.id.tv_message});
        binding.pslDemo.setMode(PullToRefreshBase.Mode.DISABLED);
        binding.pslDemo.setAdapter(mAdapter);
        // 创建删除滑块
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(HomeActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x20, 0x20)));
                deleteItem.setWidth(CommonUtil.dp2px(HomeActivity.this, 63));
                deleteItem.setIcon(CommonUtil.getDrawble(HomeActivity.this, R.mipmap.del));
                deleteItem.setTitleSize(14);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        ((SwipeMenuListView) binding.pslDemo.getRefreshableView()).setMenuCreator(creator);
        ((SwipeMenuListView) binding.pslDemo.getRefreshableView()).setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Toast.makeText(HomeActivity.this, "删除我了" + position, Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        binding.pslDemo.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        Toast.makeText(HomeActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                        binding.pslDemo.onRefreshComplete();
                        return false;
                    }
                }).sendEmptyMessageDelayed(1, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        Toast.makeText(HomeActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        binding.pslDemo.onRefreshComplete();
                        return false;
                    }
                }).sendEmptyMessageDelayed(2, 1000);
            }
        });
        binding.pslDemo.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < message.length; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put("message", message[i]);
            datas.add(data);
        }
    }

    @Override
    public void onRequestData() {

    }
}
