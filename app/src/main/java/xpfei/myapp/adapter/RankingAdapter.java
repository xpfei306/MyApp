package xpfei.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.adapter.base.BaseMyReclyViewAdapter;
import xpfei.myapp.model.RankingInfo;
import xpfei.myapp.model.Song;
import xpfei.myapp.util.GlideUtils;

/**
 * Description: 排行榜adapter
 * Author: xpfei
 * Date:   2017/08/25
 */
public class RankingAdapter extends BaseMyReclyViewAdapter<RankingInfo, RankingAdapter.ViewHolder> {

    public RankingAdapter(Context context, List<RankingInfo> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void onBindData(ViewHolder holder, int position, boolean isItem) {
        RankingInfo info = data.get(position);
        GlideUtils.loadImage(context, info.getPic_s260(), R.mipmap.nopic, holder.imgRanking);
        holder.txtRankingName.setText(info.getName());
        Song songinfo = info.getContent().get(0);
        Song songinfo1 = info.getContent().get(1);
        Song songinfo2 = info.getContent().get(2);
        Song songinfo3 = info.getContent().get(3);
        holder.txtName.setText("1 " + songinfo.getTitle() + " — " + songinfo.getAuthor());
        holder.txtName1.setText("2 " + songinfo1.getTitle() + " — " + songinfo1.getAuthor());
        holder.txtName2.setText("3 " + songinfo2.getTitle() + " — " + songinfo2.getAuthor());
        holder.txtName3.setText("4 " + songinfo3.getTitle() + " — " + songinfo3.getAuthor());
    }

    @Override
    public RankingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = inflater.inflate(layoutId, parent, false);
        return new ViewHolder(view, true);
    }

    @Override
    public RankingAdapter.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgRanking;
        private TextView txtRankingName, txtName, txtName1, txtName2, txtName3;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                imgRanking = itemView.findViewById(R.id.imgRanking);
                txtRankingName = itemView.findViewById(R.id.txtRankingName);
                txtName = itemView.findViewById(R.id.txtName);
                txtName1 = itemView.findViewById(R.id.txtName1);
                txtName2 = itemView.findViewById(R.id.txtName2);
                txtName3 = itemView.findViewById(R.id.txtName3);
            }
        }
    }
}
