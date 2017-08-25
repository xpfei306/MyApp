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
import xpfei.myapp.model.ArtInfo;
import xpfei.mylibrary.view.reclyview.recyclerview.BaseRecyclerAdapter;

/**
 * Description:歌手列表
 * Author: xpfei
 * Date:   2017/08/22
 */
public class ArtAdapter extends BaseRecyclerAdapter<ArtAdapter.ViewHolder> {
    private Context context;
    private List<ArtInfo> list;

    public ArtAdapter(Context context, List<ArtInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recyclerview_art, parent, false);
        return new ViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position, boolean isItem) {
        ArtInfo info = list.get(position);
        Glide.with(context).load(info.getAvatar_big()).error(R.mipmap.header).into(holder.imgArt);
        holder.txtArtName.setText(info.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return list != null ? list.size() : 0;
    }


    public void setData(List<ArtInfo> list) {
        if (list != null) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgArt;
        private TextView txtArtName;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                imgArt = itemView.findViewById(R.id.imgArt);
                txtArtName = itemView.findViewById(R.id.txtArtName);
            }
        }
    }
}
