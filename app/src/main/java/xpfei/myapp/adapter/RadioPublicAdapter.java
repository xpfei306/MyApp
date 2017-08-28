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
import xpfei.myapp.model.RadioListInfo;

/**
 * Description: 电台公共
 * Author: xpfei
 * Date:   2017/08/28
 */
public class RadioPublicAdapter extends RecyclerView.Adapter<RadioPublicAdapter.ViewHolder> {
    private List<RadioListInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public RadioPublicAdapter(List<RadioListInfo> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_radio_public, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RadioListInfo info = list.get(position);
        Glide.with(context).load(info.getThumb()).error(R.mipmap.header).into(holder.imgRadio);
        holder.txtRadioName.setText(info.getName());
}

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtRadioName;
        private ImageView imgRadio;

        public ViewHolder(View itemView) {
            super(itemView);
            txtRadioName = itemView.findViewById(R.id.txtRadioName);
            imgRadio = itemView.findViewById(R.id.imgRadio);
        }
    }
}
