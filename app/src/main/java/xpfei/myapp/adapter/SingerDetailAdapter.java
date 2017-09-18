package xpfei.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xpfei.myapp.R;
import xpfei.myapp.util.ContentValue;

/**
 * Description: 歌手详情页面adapter
 * Author: xpfei
 * Date:   2017/09/18
 */
public class SingerDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;

    public SingerDetailAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ContentValue.ViewType.ViewDetail:
                View view = inflater.inflate(R.layout.item_detail_button, parent, false);
                return new ArtInfoHolder(view);
            case ContentValue.ViewType.ViewNewSong:
                View v = inflater.inflate(R.layout.item_detail_button, parent, false);
                return new ArtSongHolder(v);
            case ContentValue.ViewType.ViewAlbum:
                View vw =  inflater.inflate(R.layout.item_detail_button, parent, false);
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
                return 0;
        }
    }

    private class ArtInfoHolder extends RecyclerView.ViewHolder {
        public ArtInfoHolder(View itemView) {
            super(itemView);
        }
    }

    private class ArtSongHolder extends RecyclerView.ViewHolder {
        public ArtSongHolder(View itemView) {
            super(itemView);
        }
    }

    private class ArtAlbumHolder extends RecyclerView.ViewHolder {
        public ArtAlbumHolder(View itemView) {
            super(itemView);
        }
    }

    private void initArtInfo(ArtInfoHolder holder) {

    }

    private void initArtSong(ArtSongHolder holder) {

    }

    private void initArtAlbum(ArtAlbumHolder holder) {

    }
}
