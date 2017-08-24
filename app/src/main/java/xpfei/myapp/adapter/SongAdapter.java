package xpfei.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.model.SongInfo;

/**
 * Description:歌曲分类的适配器
 * Author: xpfei
 * Date:   2017/08/22
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context context;
    private List<SongInfo> list;
    private onMyItemClickListener listener;

    public SongAdapter(Context context, List<SongInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recyclerview_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SongInfo info = list.get(position);
        Glide.with(context).load(info.getPic_small()).error(R.mipmap.nopic).into(holder.imgSong);
        holder.txtSong.setText(info.getTitle());
        holder.txtAlbum.setText(info.getAuthor() + "-" + info.getAlbum_title());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onMyItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface onMyItemClickListener {
        void onMyItemClick(int position);
    }

    public void setOnMyItemClickListener(onMyItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSong;
        TextView txtAlbum, txtSong;

        public ViewHolder(View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.imgSong);
            txtAlbum = itemView.findViewById(R.id.txtAlbum);
            txtSong = itemView.findViewById(R.id.txtSong);
        }
    }
}
