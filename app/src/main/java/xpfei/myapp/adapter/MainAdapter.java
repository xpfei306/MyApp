package xpfei.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.AlbumDetailActivity;
import xpfei.myapp.activity.GeDanListActivity;
import xpfei.myapp.activity.PlayerActivity;
import xpfei.myapp.activity.RadioListActivity;
import xpfei.myapp.activity.RankingActivity;
import xpfei.myapp.activity.SingerListActivity;
import xpfei.myapp.activity.WebActivity;
import xpfei.myapp.model.AlbumInfo;
import xpfei.myapp.model.BannerInfo;
import xpfei.myapp.model.CategoryInfo;
import xpfei.myapp.model.Song;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.util.GlideUtils;
import xpfei.myapp.view.banner.Banner;
import xpfei.myapp.view.banner.BannerAdapter;

/**
 * Description: 首页的RecyclerView的adapter
 * Author: xpfei
 * Date:   2017/08/21
 */
public class MainAdapter extends RecyclerView.Adapter {
    private int count = 4;
    private LayoutInflater inflater;
    private Context context;
    private List<BannerInfo> bannerInfos;
    private List<CategoryInfo> categoryInfos;
    private List<Song> songInfos;
    private List<AlbumInfo> albumInfos;
    private Banner banner;

    public MainAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        bannerInfos = new ArrayList<>();
        categoryInfos = new ArrayList<>();
        songInfos = new ArrayList<>();
        albumInfos = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ContentValue.ViewType.ViewHeader) {
            View viewTop = inflater.inflate(R.layout.item_recyclerview_header, parent, false);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewTop.getLayoutParams();
            params.setFullSpan(true);//最为重要的一个方法，占满全屏,以下同理
            viewTop.setLayoutParams(params);
            return new HeaderHold(viewTop);
        } else if (viewType == ContentValue.ViewType.ViewCategory) {
            View viewCategory = inflater.inflate(R.layout.item_recyclerview_category, parent, false);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewCategory.getLayoutParams();
            params.setFullSpan(true);//最为重要的一个方法，占满全屏,以下同理
            viewCategory.setLayoutParams(params);
            return new CategoryHold(viewCategory);
        } else if (viewType == ContentValue.ViewType.ViewNewSong) {
            View viewNewSong = inflater.inflate(R.layout.item_recyclerview_wthtitle, parent, false);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewNewSong.getLayoutParams();
            params.setFullSpan(true);//最为重要的一个方法，占满全屏,以下同理
            viewNewSong.setLayoutParams(params);
            return new SongHold(viewNewSong);
        } else {
            View viewalbum = inflater.inflate(R.layout.item_recyclerview_wthtitle, parent, false);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewalbum.getLayoutParams();
            params.setFullSpan(true);//最为重要的一个方法，占满全屏,以下同理
            viewalbum.setLayoutParams(params);
            return new AlbumHold(viewalbum);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHold) {
            initHeader((HeaderHold) holder, bannerInfos);
        } else if (holder instanceof CategoryHold) {
            initCategory((CategoryHold) holder, categoryInfos);
        } else if (holder instanceof SongHold) {
            initSong((SongHold) holder, songInfos);
        } else {
            initAlbum((AlbumHold) holder, albumInfos);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ContentValue.ViewType.ViewHeader;
        } else if (position == 1) {
            return ContentValue.ViewType.ViewCategory;
        } else if (position == 2) {
            return ContentValue.ViewType.ViewNewSong;
        } else {
            return ContentValue.ViewType.ViewAlbum;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //如果快速滑动， 不加载图片
                if (newState == 2) {
                    Glide.with(context).pauseRequests();
                    if (banner != null) {
                        banner.pauseScroll();
                    }
                } else {
                    Glide.with(context).resumeRequests();
                    if (banner != null) {
                        banner.goScroll();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }

    /**
     * 插入头部文件布局
     *
     * @param headerHold viewhold
     * @param list       绑定的数据
     */
    private void initHeader(HeaderHold headerHold, final List<BannerInfo> list) {
        banner = headerHold.banner;
        BannerAdapter adapter = new BannerAdapter<BannerInfo>(list) {
            @Override
            protected void bindTips(TextView tv, BannerInfo bannerInfo) {
                tv.setVisibility(View.GONE);
            }

            @Override
            public void bindImage(ImageView imageView, BannerInfo bannerInfo) {
                GlideUtils.loadImage(context, bannerInfo.getRandpic(), R.mipmap.nopic, imageView);
            }
        };
        headerHold.banner.setBannerAdapter(adapter);
        headerHold.banner.goScroll();
        headerHold.banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                BannerInfo bannerInfo = list.get(position);
                if (bannerInfo.getType() == 2) {
                    intent.setClass(context, AlbumDetailActivity.class);
                    intent.putExtra(ContentValue.IntentKey.IntentKeyStr, bannerInfo.getCode());
                    context.startActivity(intent);
                } else {
                    if (bannerInfo.getCode().contains("http")) {
                        intent.setClass(context, WebActivity.class);
                        intent.putExtra(ContentValue.IntentKey.IntentKeyStr, bannerInfo.getCode());
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * 中间分类赋值
     *
     * @param hold viewhold
     * @param list 绑定的数据
     */
    private void initCategory(CategoryHold hold, final List<CategoryInfo> list) {
        MainCategoryAdapter adapter = new MainCategoryAdapter(context, list);
        hold.recyclerView.setAdapter(adapter);
        adapter.setOnMyItemClickListener(new MainCategoryAdapter.onMyItemClickListener() {
            @Override
            public void onMyItemClick(int position) {
                CategoryInfo info = list.get(position);
                Intent intent = new Intent();
                intent.putExtra(ContentValue.IntentKey.IntentKeyStr, info.getName());
                switch (info.getType()) {
                    case 1:
                        intent.setClass(context, GeDanListActivity.class);
                        context.startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(context, RankingActivity.class);
                        context.startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(context, SingerListActivity.class);
                        context.startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(context, RadioListActivity.class);
                        context.startActivity(intent);
                        break;
                }

            }
        });
        hold.recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
    }

    /**
     * 底部List赋值
     *
     * @param hold viewhold
     * @param list 绑定的数据
     */
    private void initSong(SongHold hold, List<Song> list) {
        hold.txtTitle.setText("新歌速递");
        SongAdapter adapter = new SongAdapter(context, list,0);
        hold.recyclerView.setAdapter(adapter);
        adapter.setOnMyItemClickListener(new SongAdapter.onMyItemClickListener() {
            @Override
            public void onMyItemClick(Song info, int postion) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra(ContentValue.IntentKey.IntentKeyStr, info.getSong_id());
                intent.putExtra(ContentValue.IntentKey.IntentKeyInt, 1);
                context.startActivity(intent);
            }
        });
        hold.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        hold.llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    /**
     * 底部List赋值
     *
     * @param hold viewhold
     * @param list 绑定的数据
     */
    private void initAlbum(AlbumHold hold, final List<AlbumInfo> list) {
        hold.txtTitle.setText("新碟上架");
        AlbumAdapter adapter = new AlbumAdapter(context, list);
        hold.recyclerView.setAdapter(adapter);
        adapter.setOnMyItemClickListener(new AlbumAdapter.onMyItemClickListener() {
            @Override
            public void onMyItemClick(int position) {
                Intent intent = new Intent(context, AlbumDetailActivity.class);
                AlbumInfo info = list.get(position);
                intent.putExtra(ContentValue.IntentKey.IntentKeyStr, info.getAlbum_id());
                context.startActivity(intent);
            }
        });
        hold.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        hold.llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    /**
     * 头部数据赋值
     *
     * @param list 数据源
     */
    public void setHeaderData(List<BannerInfo> list) {
        if (list != null) {
            this.bannerInfos = list;
            notifyDataSetChanged();
        }
    }

    /**
     * 中间分类数据赋值
     *
     * @param list 数据源
     */
    public void setCategoryData(List<CategoryInfo> list) {
        this.categoryInfos = list;
        notifyDataSetChanged();
    }

    /**
     * 底部List赋值
     *
     * @param list 绑定的数据
     */
    public void setSongData(List<Song> list) {
        this.songInfos = list;
        notifyDataSetChanged();
    }

    /**
     * 底部List赋值
     *
     * @param list 绑定的数据
     */
    public void setAlbumData(List<AlbumInfo> list) {
        this.albumInfos = list;
        notifyDataSetChanged();
    }

    private class HeaderHold extends RecyclerView.ViewHolder {
        Banner banner;

        HeaderHold(View view) {
            super(view);
            banner = (Banner) view.findViewById(R.id.banner);
        }
    }

    private class CategoryHold extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        CategoryHold(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.itemRv);
        }
    }

    private class SongHold extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private TextView txtTitle;
        private LinearLayout llMore;

        SongHold(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.itemRv);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            llMore = (LinearLayout) view.findViewById(R.id.llMore);
        }
    }

    private class AlbumHold extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private TextView txtTitle;
        private LinearLayout llMore;

        AlbumHold(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.itemRv);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            llMore = (LinearLayout) view.findViewById(R.id.llMore);
        }
    }
}
