package xpfei.myapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.adapter.base.BaseMyReclyViewAdapter;
import xpfei.myapp.model.ArtInfo;
import xpfei.myapp.util.GlideUtils;

/**
 * Description:歌手列表
 * Author: xpfei
 * Date:   2017/08/22
 */
public class ArtAdapter extends BaseMyReclyViewAdapter<ArtInfo, ArtAdapter.ViewHolder> {

    public ArtAdapter(Context context, List data, @LayoutRes int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = inflater.inflate(layoutId, parent, false);
        return new ViewHolder(view, true);
    }

    @Override
    public void onBindData(ViewHolder holder, int position, boolean isItem) {
        ArtInfo info = data.get(position);
        GlideUtils.loadImage(context, info.getAvatar_big(), R.mipmap.header, holder.imgArt);
        holder.txtArtName.setText(info.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
