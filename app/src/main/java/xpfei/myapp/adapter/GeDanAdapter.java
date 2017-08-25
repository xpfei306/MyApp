package xpfei.myapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.adapter.base.BaseMyReclyViewAdapter;
import xpfei.myapp.model.GeDanInfo;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description:歌单列表
 * Author: xpfei
 * Date:   2017/08/22
 */
public class GeDanAdapter extends BaseMyReclyViewAdapter<GeDanInfo, GeDanAdapter.ViewHolder> {


    public GeDanAdapter(Context context, List<GeDanInfo> data, @LayoutRes int layoutId) {
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

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        GeDanInfo info = data.get(position);
        Glide.with(context).load(info.getPic()).error(R.mipmap.nopic).into(holder.imgGeDan);
        holder.txtName.setText(info.getTitle());
        holder.txtListenum.setText(StringUtil.int2double(info.getListenum()));
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
