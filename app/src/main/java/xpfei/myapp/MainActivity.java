package xpfei.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xpfei.myapp.adapter.MainAdapter;
import xpfei.myapp.model.AlbumInfo;
import xpfei.myapp.model.BannerInfo;
import xpfei.myapp.model.CategoryInfo;
import xpfei.myapp.model.SongInfo;
import xpfei.myapp.model.UserInfo;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.view.MyStaggerGrildLayoutManger;
import xpfei.mylibrary.manager.ACache;
import xpfei.mylibrary.manager.AppManager;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description:首页
 * Author: xpfei
 * Date:   2017/08/08
 */
public class MainActivity extends MyBaseActivity {
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        onSetTitle("首页");
        onSetLeft(R.drawable.menu, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });
        mDrawer = (DrawerLayout) findViewById(R.id.myDrawer); //DrawerLayout
        navigationView = (NavigationView) findViewById(R.id.nav); //NavigationView导航栏
        recyclerView = (RecyclerView) findViewById(R.id.MyRv);
        adapter = new MainAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new MyStaggerGrildLayoutManger(this, 3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setCategoryData(initCategory());
                getNewSong();
                getAlbum();
            }
        }, 100);
        ACache aCache = ACache.get(this);
        final UserInfo userInfo = (UserInfo) aCache.getAsObject(ContentValue.ACACHE_USER);
        if (userInfo != null) {
            View headerView = navigationView.getHeaderView(0);
            TextView txtName = headerView.findViewById(R.id.txtName);
            txtName.setText(userInfo.getUsername());
            if (!StringUtil.isEmpty(userInfo.getHeadimage())) {
                final ImageView imgHeader = headerView.findViewById(R.id.imgHeader);
                imgHeader.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String tempPath = userInfo.getHeadimage().replace("\\", "/");
                        Glide.with(MainActivity.this).load("http://192.168.10.20:8911" + tempPath).error(R.mipmap.header).into(imgHeader);
                    }
                }, 1000);
            }
        }
    }

    private void initEvent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        startActivity(new Intent(MainActivity.this, PersonalCenterActivity.class));
                        break;
                    case R.id.item2:
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        break;
                    case R.id.item3:
                        startActivity(new Intent(MainActivity.this, SongListActivity.class));
                        break;
                    case R.id.item4:
                        CommonUtil.showToast(MainActivity.this, "退出登陆");
                        break;
                }
                navigationView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawer.closeDrawers();
                        getNewSong();
                        getAlbum();
                    }
                }, 100);
                return true;
            }
        });
        startBaseReqTask(this, null);
    }

    private List<CategoryInfo> initCategory() {
        List<CategoryInfo> list = new ArrayList<>();
        list.add(new CategoryInfo("歌单", R.drawable.ic_songlist));
        list.add(new CategoryInfo("排行榜", R.drawable.ic_ranking));
        list.add(new CategoryInfo("歌手", R.drawable.ic_singer));
        list.add(new CategoryInfo("电台", R.drawable.ic_musicstation));
        return list;
    }

    @Override
    public void onRequestData() {
        Map<String, String> params = new HashMap<>();
        params.put("version", "5.6.5.6");
        params.put("num", "6");
        params.put("method", "baidu.ting.plaza.getFocusPic");
        MyVolley.getInstance(this).get(ContentValue.Url.BaseUrl, params, new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    JSONArray jsonArray = jsonObject.optJSONArray(ContentValue.Json.Banner);
                    if (jsonArray != null && jsonArray.length() > 0) {
                        List<BannerInfo> bannerList = JSON.parseArray(jsonArray.toString(), BannerInfo.class);
                        adapter.setHeaderData(bannerList);
                    } else {
                        onFailure("未查询到相关数据！");
                    }

                } else {
                    onFailure("服务器繁忙，请稍后再试！");
                }
                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
            }
        });
    }

    /**
     * 获取新歌
     */
    private void getNewSong() {
        Map<String, String> params = new HashMap<>();
        params.put("version", "2.1.0");
        params.put("limit", "6");
        params.put("method", "baidu.ting.plaza.getNewSongs");
        MyVolley.getInstance(this).get(ContentValue.Url.BaseUrl, params, new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                JSONArray jsonArray = jsonObject.optJSONArray(ContentValue.Json.SongList);
                if (jsonArray != null && jsonArray.length() > 0) {
                    List<SongInfo> bannerList = JSON.parseArray(jsonArray.toString(), SongInfo.class);
                    adapter.setSongData(bannerList);
                } else {
                    onFailure("未查询到相关数据！");
                }
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
            }
        });
    }

    /**
     * 获取新的专辑
     */
    private void getAlbum() {
        Map<String, String> params = new HashMap<>();
        params.put("version", "2.1.0");
        params.put("offset", "0");
        params.put("limit", "6");
        params.put("method", "baidu.ting.plaza.getRecommendAlbum");
        MyVolley.getInstance(this).get(ContentValue.Url.BaseUrl, params, new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    try {
                        JSONObject json = jsonObject.optJSONObject(ContentValue.Json.Album);
                        JSONObject jsonRM = json.optJSONObject(ContentValue.Json.RM);
                        JSONObject AlbumList = jsonRM.optJSONObject(ContentValue.Json.AlbumList);
                        JSONArray jsonArray = AlbumList.optJSONArray(ContentValue.Json.List);
                        List<AlbumInfo> bannerList = JSON.parseArray(jsonArray.toString(), AlbumInfo.class);
                        adapter.setAlbumData(bannerList);
                    } catch (Exception e) {
                        AppLog.Logd("Error:" + e.getMessage());
                        onFailure("未查询到相关数据！");
                    }
                } else {
                    onFailure("服务器繁忙，请稍后再试！");
                }

                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().AppExit(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyVolley.getInstance(this).cancelAll();
    }
}
