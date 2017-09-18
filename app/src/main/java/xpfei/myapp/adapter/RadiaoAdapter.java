package xpfei.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.model.RadioInfo;
import xpfei.myapp.model.RadioListInfo;

/**
 * Description:电台列表适配器
 * Author: xpfei
 * Date:   2017/08/28
 */
public class RadiaoAdapter extends RecyclerView.Adapter {
    private final int count = 2;
    private final int RADIO_SINGER = 1;
    private final int RADIO_PUBLIC = 2;
    private Context context;
    private LayoutInflater inflater;
    private List<RadioInfo> radioInfoList;

    public RadiaoAdapter(Context context, List<RadioInfo> list) {
        this.context = context;
        this.radioInfoList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RADIO_SINGER) {
            View view = inflater.inflate(R.layout.item_recyclerview, parent, false);
            view.setPadding(0, 20, 0, 20);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            params.setFullSpan(true);//最为重要的一个方法，占满全屏,以下同理
            view.setLayoutParams(params);
            return new SingerHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_recyclerview_wthtitle, parent, false);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            params.setFullSpan(true);//最为重要的一个方法，占满全屏,以下同理
            view.setLayoutParams(params);
            return new PublicHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SingerHolder) {
            initSinger((SingerHolder) holder);
        } else {
            initPublic((PublicHolder) holder);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return RADIO_SINGER;
        } else {
            return RADIO_PUBLIC;
        }
    }

    @Override
    public int getItemCount() {
        return radioInfoList != null ? count : 0;
    }

    public void setData(List<RadioInfo> list) {
        if (list != null) {
            this.radioInfoList = list;
            notifyDataSetChanged();
        }
    }

    private void initSinger(SingerHolder holder) {
        if (radioInfoList.size() > 0) {
            List<RadioListInfo> radioListInfos = radioInfoList.get(1).getChannellist();
            RadioSingerAdapter adapter = new RadioSingerAdapter(radioListInfos, context);
            holder.recyclerView.setAdapter(adapter);
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        }
    }

    private void initPublic(PublicHolder holder) {
        if (radioInfoList.size() > 0) {
            List<RadioListInfo> radioListInfos = radioInfoList.get(0).getChannellist();
            RadioPublicAdapter adapter = new RadioPublicAdapter(radioListInfos, context);
            holder.recyclerView.setAdapter(adapter);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.txtTitle.setText("推荐电台");
            holder.llMore.setVisibility(View.GONE);
        }
    }

    class SingerHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        public SingerHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.itemRv);
        }
    }

    class PublicHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private TextView txtTitle;
        private LinearLayout llMore;

        public PublicHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.itemRv);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            llMore = (LinearLayout) itemView.findViewById(R.id.llMore);
        }
    }
}
