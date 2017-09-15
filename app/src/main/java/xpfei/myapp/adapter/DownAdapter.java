package xpfei.myapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.adapter.base.BaseMyReclyViewAdapter;
import xpfei.myapp.model.DownInfo;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/09/15
 */
public class DownAdapter extends BaseMyReclyViewAdapter<DownInfo, DownAdapter.ViewHolder> {


    public DownAdapter(Context context, List<DownInfo> data, @LayoutRes int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void onBindData(ViewHolder holder, int position, boolean isItem) {
        DownInfo info = data.get(position);
        int quality = info.getFile_bitrate();
        String strQuality;
        if (quality > 0 && quality <= 24) {
            strQuality = "低品质";
        } else if (quality > 24 && quality <= 64) {
            strQuality = "普通品质";
        } else if (quality > 64 && quality <= 128) {
            strQuality = "标准品质";
        } else if (quality > 128 && quality <= 256) {
            strQuality = "高品质";
        } else if (quality > 256 && quality <= 320) {
            strQuality = "HQ品质";
        } else if (quality > 320) {
            strQuality = "无损品质";
        } else {
            strQuality = "未知";
        }
        holder.text1.setText(strQuality + " ( " + StringUtil.int2double(info.getFile_size()) + " )");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
