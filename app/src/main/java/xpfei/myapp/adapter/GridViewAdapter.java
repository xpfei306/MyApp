package xpfei.myapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.databinding.ItemGridviewHomeBinding;
import xpfei.mylibrary.utils.StringUtil;


/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/07/28
 */
public class GridViewAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;

    public GridViewAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ItemGridviewHomeBinding binding;
        if (view == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_gridview_home, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (ItemGridviewHomeBinding) view.getTag();
        }
        String json = getItem(i);
        if (!StringUtil.isEmpty(json)) {

        }
        return view;
    }
}
