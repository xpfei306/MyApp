package xpfei.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.GeDanDetailActivity;
import xpfei.myapp.adapter.base.BaseMyReclyViewAdapter;
import xpfei.myapp.model.GeDanInfo;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.util.GlideUtils;
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
        final GeDanInfo info = data.get(position);
        GlideUtils.loadImage(context, info.getPic_300(), R.mipmap.nopic, holder.imgGeDan);
        holder.txtName.setText(info.getTitle());
        holder.txtListenum.setText(StringUtil.int2double(info.getListenum()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GeDanDetailActivity.class).putExtra(ContentValue.IntentKey.IntentKeyStr, info.getListid()));
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgGeDan;
        private TextView txtName, txtListenum;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                imgGeDan = (ImageView) itemView.findViewById(R.id.imgGeDan);
                txtName = (TextView) itemView.findViewById(R.id.txtName);
                txtListenum = (TextView) itemView.findViewById(R.id.txtListenum);
            }
        }
    }
}
