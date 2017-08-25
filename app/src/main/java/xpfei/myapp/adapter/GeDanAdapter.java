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
import xpfei.myapp.model.GeDanInfo;
import xpfei.mylibrary.utils.StringUtil;
import xpfei.mylibrary.view.reclyview.recyclerview.BaseRecyclerAdapter;

/**
 * Description:歌单列表
 * Author: xpfei
 * Date:   2017/08/22
 */
public class GeDanAdapter extends BaseRecyclerAdapter<GeDanAdapter.ViewHolder> {
    private Context context;
    private List<GeDanInfo> list;
    private onMyItemClickListener listener;

    public GeDanAdapter(Context context, List<GeDanInfo> list) {
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
        View view = inflater.inflate(R.layout.item_recyclerview_gedan, parent, false);
        return new ViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position, boolean isItem) {
        GeDanInfo info = list.get(position);
        Glide.with(context).load(info.getPic()).error(R.mipmap.nopic).into(holder.imgGeDan);
        holder.txtName.setText(info.getTitle());
        holder.txtListenum.setText(StringUtil.int2double(info.getListenum()));
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
    public int getAdapterItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface onMyItemClickListener {
        void onMyItemClick(int position);
    }

    public void setOnMyItemClickListener(onMyItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<GeDanInfo> list) {
        if (list != null) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgGeDan;
        private TextView txtName, txtListenum;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                imgGeDan = itemView.findViewById(R.id.imgGeDan);
                txtName = itemView.findViewById(R.id.txtName);
                txtListenum = itemView.findViewById(R.id.txtListenum);
            }
        }
    }
}
