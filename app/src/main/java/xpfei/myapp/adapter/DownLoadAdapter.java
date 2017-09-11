package xpfei.myapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.adapter.base.BaseMyReclyViewAdapter;
import xpfei.myapp.model.DownLoadInfo;
import xpfei.myapp.util.GlideUtils;
import xpfei.myapp.view.ProgressLayout;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/09/07
 */
public class DownLoadAdapter extends BaseMyReclyViewAdapter<DownLoadInfo, DownLoadAdapter.ViewHolder> {


    public DownLoadAdapter(Context context, List<DownLoadInfo> data, @LayoutRes int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void onBindData(ViewHolder holder, int position, boolean isItem) {
        DownLoadInfo info = data.get(position);
        GlideUtils.loadImage(context, info.getImgPath(), R.drawable.noalbum, holder.imgSong);
        holder.txtName.setText(info.getFileName());
        holder.mProgressLayout.setMaxProgress((int) info.getFileSize());
        //0未知状态1开始下载2下载中3取消下载4下载错误5下载完成
        switch (info.getState()) {
            case 1:
                holder.txtState.setText("开始下载");
                holder.mProgressLayout.setCurrentProgress(0);
                break;
            case 2:
                String tempStr = StringUtil.int2double(info.getTotalSize());
                String tempStr1 = StringUtil.int2double(info.getFileSize());
                holder.txtState.setText(tempStr + "/" + tempStr1);
                holder.mProgressLayout.setCurrentProgress(info.getTotalSize());
                holder.mProgressLayout.setMaxProgress(info.getFileSize());
                holder.mProgressLayout.start();
                break;
            case 3:
                holder.txtState.setText("下载已取消，点击重新下载");
                holder.mProgressLayout.setCurrentProgress(info.getTotalSize());
                holder.mProgressLayout.cancel();
                break;
            case 4:
                holder.txtState.setText("下载出错");
                holder.mProgressLayout.setCurrentProgress(0);
                holder.mProgressLayout.cancel();
                break;
            case 5:
                holder.txtState.setText("下载完成");
                holder.mProgressLayout.setCurrentProgress(info.getFileSize());
                holder.mProgressLayout.stop();
                break;
            default:
                holder.txtState.setText("未知状态");
                holder.mProgressLayout.setCurrentProgress(0);
                break;
        }
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

    public void setInfo(DownLoadInfo info) {
        if (data == null || data.size() == 0) {
            data.add(0, info);
        } else {
            if (data.contains(info)) {
                int index = data.indexOf(info);
                data.remove(info);
                data.add(index, info);
            } else {
                data.add(info);
            }
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ProgressLayout mProgressLayout;
        private ImageView imgSong;
        private TextView txtName, txtState;

        public ViewHolder(View itemView) {
            super(itemView);
            mProgressLayout = (ProgressLayout) itemView.findViewById(R.id.mProgressLayout);
            imgSong = (ImageView) itemView.findViewById(R.id.imgSong);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtState = (TextView) itemView.findViewById(R.id.txtState);
        }
    }
}
