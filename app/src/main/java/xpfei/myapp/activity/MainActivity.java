package xpfei.myapp.activity;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.MainAdapter;
import xpfei.myapp.model.AlbumInfo;
import xpfei.myapp.model.BannerInfo;
import xpfei.myapp.model.CategoryInfo;
import xpfei.myapp.model.Song;
import xpfei.myapp.model.UserInfo;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.util.GlideUtils;
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
    private ACache aCache;

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
        aCache = ACache.get(this);
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
                        GlideUtils.loadImage(MainActivity.this, "http://192.168.10.20:8911" + tempPath, R.mipmap.header, imgHeader);
                    }
                }, 1000);
            }
        }
        aCache = ACache.get(this);
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
                        startActivity(new Intent(MainActivity.this, GeDanListActivity.class));
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
        list.add(new CategoryInfo("歌单", R.drawable.ic_songlist, 1));
        list.add(new CategoryInfo("排行榜", R.drawable.ic_ranking, 2));
        list.add(new CategoryInfo("歌手", R.drawable.ic_singer, 3));
        list.add(new CategoryInfo("电台", R.drawable.ic_musicstation, 4));
        return list;
    }

    @Override
    public void onRequestData() {
        MyVolley.getInstance(this).get(BaiduMusicApi.focusPic(6), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    try {
                        JSONArray jsonArray = jsonObject.optJSONArray(ContentValue.Json.Banner);
                        aCache.put(ContentValue.AcacheKey.ACACHEKEY_BANNER, jsonArray);
                        getBinner(jsonArray);
                    } catch (Exception e) {
                        AppLog.Loge("Error:" + e.getMessage());
                        onFailure("未查询到相关数据！");
                    }
                } else {
                    onFailure("服务器繁忙，请稍后再试！");
                }
                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                JSONArray jsonArray = aCache.getAsJSONArray(ContentValue.AcacheKey.ACACHEKEY_BANNER);
                getBinner(jsonArray);
                onDialogFailure(msg);
            }
        });
    }

    public void Error() {
        JSONArray jsonArray = aCache.getAsJSONArray(ContentValue.AcacheKey.ACACHEKEY_BANNER);
        getBinner(jsonArray);
    }

    private void getBinner(JSONArray jsonArray) {
        List<BannerInfo> bannerList = JSON.parseArray(jsonArray.toString(), BannerInfo.class);
        adapter.setHeaderData(bannerList);
    }

    /**
     * 获取新歌
     */
    private void getNewSong() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Song.recommendSong(6), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    try {
                        JSONArray jsonArray = jsonObject.optJSONArray(ContentValue.Json.Content);
                        JSONObject jsonSong = jsonArray.getJSONObject(0);
                        JSONArray arraySong = jsonSong.optJSONArray(ContentValue.Json.SongList);
                        aCache.put(ContentValue.AcacheKey.ACACHEKEY_NEWSONG, arraySong);
                        getSong(arraySong);
                    } catch (Exception e) {
                        AppLog.Loge("Error:" + e.getMessage());
                        onFailure("未查询到相关数据！");
                    }
                } else {
                    onFailure("服务器繁忙，请稍后再试！");
                }
                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                JSONArray jsonArray = aCache.getAsJSONArray(ContentValue.AcacheKey.ACACHEKEY_NEWSONG);
                getSong(jsonArray);
                onDialogFailure(msg);
            }
        });
    }

    private void getSong(JSONArray jsonArray) {
        List<Song> bannerList = JSON.parseArray(jsonArray.toString(), Song.class);
        adapter.setSongData(bannerList);
    }

    /**
     * 获取新的专辑
     */
    private void getAlbum() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Album.recommendAlbum(0, 6), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    try {
                        JSONObject json = jsonObject.optJSONObject(ContentValue.Json.Album);
                        JSONObject jsonRM = json.optJSONObject(ContentValue.Json.RM);
                        JSONObject AlbumList = jsonRM.optJSONObject(ContentValue.Json.AlbumList);
                        JSONArray jsonArray = AlbumList.optJSONArray(ContentValue.Json.List);
                        aCache.put(ContentValue.AcacheKey.ACACHEKEY_ALBUM, jsonArray);
                        getalbum(jsonArray);
                    } catch (Exception e) {
                        AppLog.Loge("Error:" + e.getMessage());
                        onFailure("未查询到相关数据！");
                    }
                } else {
                    onFailure("服务器繁忙，请稍后再试！");
                }

                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                JSONArray jsonArray = aCache.getAsJSONArray(ContentValue.AcacheKey.ACACHEKEY_ALBUM);
                getalbum(jsonArray);
                onDialogFailure(msg);
            }
        });
    }

    private void getalbum(JSONArray jsonArray) {
        List<AlbumInfo> bannerList = JSON.parseArray(jsonArray.toString(), AlbumInfo.class);
        adapter.setAlbumData(bannerList);
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
