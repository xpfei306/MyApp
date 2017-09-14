package xpfei.myapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.model.AlbumInfo;

/**
 * Description:专辑分类的适配器
 * Author: xpfei
 * Date:   2017/08/22
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private Context context;
    private List<AlbumInfo> list;
    private onMyItemClickListener listener;

    public AlbumAdapter(Context context, List<AlbumInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recyclerview_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AlbumInfo info = list.get(position);
        Glide.with(context).load(info.getPic_radio()).asBitmap().error(R.drawable.noalbum).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.imgCategory.setImageBitmap(resource);
                Palette.Builder builder = Palette.from(resource);
                builder.generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrant = palette.getLightVibrantSwatch();
                        if (vibrant != null)
                            holder.llBottom.setBackgroundColor(vibrant.getRgb());
                    }
                });
            }
        });
        holder.txtName.setText(info.getTitle());
        holder.txtCountry.setText(info.getCountry());
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
        private ImageView imgCategory;
        private TextView txtName, txtCountry;
        private RelativeLayout llBottom;

        public ViewHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCountry = (TextView) itemView.findViewById(R.id.txtCountry);
            llBottom = (RelativeLayout) itemView.findViewById(R.id.llBottom);
        }
    }
}
