package xpfei.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.model.AlbumInfo;
import xpfei.myapp.model.ArtInfo;
import xpfei.myapp.model.Song;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.util.GlideUtils;

/**
 * Description: 歌手详情页面adapter
 * Author: xpfei
 * Date:   2017/09/18
 */
public class SingerDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;
    private ArtInfo artInfo;
    private List<Song> songList;
    private List<AlbumInfo> albumInfoList;

    public SingerDetailAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        songList = new ArrayList<>();
        albumInfoList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ContentValue.ViewType.ViewDetail:
                View view = inflater.inflate(R.layout.item_singerdetail_header, parent, false);
                return new ArtInfoHolder(view);
            case ContentValue.ViewType.ViewNewSong:
                View v = inflater.inflate(R.layout.item_singerdetail_songandalbum, parent, false);
                return new ArtSongHolder(v);
            case ContentValue.ViewType.ViewAlbum:
                View vw = inflater.inflate(R.layout.item_singerdetail_songandalbum, parent, false);
                return new ArtAlbumHolder(vw);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArtInfoHolder) {
            initArtInfo((ArtInfoHolder) holder);
        } else if (holder instanceof ArtSongHolder) {
            initArtSong((ArtSongHolder) holder);
        } else if (holder instanceof ArtAlbumHolder) {
            initArtAlbum((ArtAlbumHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ContentValue.ViewType.ViewDetail;
            case 1:
                return ContentValue.ViewType.ViewNewSong;
            case 2:
                return ContentValue.ViewType.ViewAlbum;
            default:
                return -1;
        }
    }

    private class ArtInfoHolder extends RecyclerView.ViewHolder {
        private ImageView ivSinger;
        private TextView txtSingerName, txtSongNum, txtAlbumNum;

        public ArtInfoHolder(View itemView) {
            super(itemView);
            ivSinger = (ImageView) itemView.findViewById(R.id.ivSinger);
            txtSingerName = (TextView) itemView.findViewById(R.id.txtSingerName);
            txtSongNum = (TextView) itemView.findViewById(R.id.txtSongNum);
            txtAlbumNum = (TextView) itemView.findViewById(R.id.txtAlbumNum);
        }
    }

    private class ArtSongHolder extends RecyclerView.ViewHolder {
        private TextView btnMore, txtTitle;
        private RecyclerView rvSong;

        public ArtSongHolder(View itemView) {
            super(itemView);
            btnMore = (TextView) itemView.findViewById(R.id.btnMore);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            rvSong = (RecyclerView) itemView.findViewById(R.id.rvSong);
        }
    }

    private class ArtAlbumHolder extends RecyclerView.ViewHolder {
        private TextView btnMore, txtTitle;
        private RecyclerView rvSong;

        public ArtAlbumHolder(View itemView) {
            super(itemView);
            btnMore = (TextView) itemView.findViewById(R.id.btnMore);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            rvSong = (RecyclerView) itemView.findViewById(R.id.rvSong);
        }
    }

    private void initArtInfo(ArtInfoHolder holder) {
        if (artInfo != null) {
            GlideUtils.loadImage(context, artInfo.getAvatar_big(), R.mipmap.nopic, holder.ivSinger);
            holder.txtSingerName.setText(artInfo.getName());
            holder.txtSongNum.setText(artInfo.getSongs_total() + "首歌曲");
            holder.txtAlbumNum.setText(artInfo.getAlbums_total() + "张专辑");
        }
    }

    private void initArtSong(ArtSongHolder holder) {
        if (songList.size() > 0) {
            holder.txtTitle.setText("热门歌曲");
            holder.btnMore.setText("更多歌曲");
            SongAdapter adapter = new SongAdapter(context, songList, 1);
            holder.rvSong.setAdapter(adapter);
            holder.rvSong.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    private void initArtAlbum(ArtAlbumHolder holder) {
        if (albumInfoList.size() > 0) {
            holder.txtTitle.setText("最新专辑");
            holder.btnMore.setText("更多专辑");
            AlbumAdapter albumAdapter = new AlbumAdapter(context, albumInfoList);
            holder.rvSong.setAdapter(albumAdapter);
            holder.rvSong.setLayoutManager(new GridLayoutManager(context, 2));
        }
    }

    public void setArtInfo(ArtInfo info) {
        if (info != null) {
            this.artInfo = info;
            notifyDataSetChanged();
        }
    }

    public void setArtSongInfo(List<Song> list) {
        if (list != null) {
            this.songList = list;
            notifyDataSetChanged();
        }
    }

    public void setArtAlbumInfo(List<AlbumInfo> list) {
        if (list != null) {
            this.albumInfoList = list;
            notifyDataSetChanged();
        }
    }
}
