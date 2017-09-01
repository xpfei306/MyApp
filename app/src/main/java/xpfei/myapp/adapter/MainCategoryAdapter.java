package xpfei.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.model.CategoryInfo;
import xpfei.myapp.util.GlideUtils;

/**
 * Description:首页分类的适配器
 * Author: xpfei
 * Date:   2017/08/22
 */
public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {
    private Context context;
    private List<CategoryInfo> list;
    private onMyItemClickListener listener;

    public MainCategoryAdapter(Context context, List<CategoryInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recyclerview_maincategory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CategoryInfo info = list.get(position);
        GlideUtils.loadImage(context, info.getDrawableId(), R.mipmap.nopic, holder.imgCategory);
        holder.txtCategory.setText(info.getName());
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
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface onMyItemClickListener {
        void onMyItemClick(int position);
    }

    public void setOnMyItemClickListener(onMyItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView txtCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            txtCategory = itemView.findViewById(R.id.txtCategory);
        }
    }
}
