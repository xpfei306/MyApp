package xpfei.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.model.RadioListInfo;
import xpfei.myapp.util.GlideUtils;
import xpfei.myapp.view.RoundImageView;

/**
 * Description: 电台歌手列表adapter
 * Author: xpfei
 * Date:   2017/08/28
 */
public class RadioSingerAdapter extends RecyclerView.Adapter<RadioSingerAdapter.ViewHolder> {
    private List<RadioListInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public RadioSingerAdapter(List<RadioListInfo> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_radio_singer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RadioListInfo info = list.get(position);
        GlideUtils.loadImage(context, info.getAvatar(), R.mipmap.header, holder.imgSinger);
        holder.txtRadioName.setText("歌手电台");
        holder.txtSinger.setText(info.getName());
    }

    @Override
    public int getItemCount() {
        return list != null ? 4 : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtRadioName, txtSinger;
        private RoundImageView imgSinger;

        public ViewHolder(View itemView) {
            super(itemView);
            txtRadioName = itemView.findViewById(R.id.txtRadioName);
            txtSinger = itemView.findViewById(R.id.txtSinger);
            imgSinger = itemView.findViewById(R.id.imgSinger);
        }
    }
}
