package xpfei.myapp.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.adapter.DownAdapter;
import xpfei.myapp.adapter.base.BaseMyReclyViewAdapter;
import xpfei.myapp.model.DownInfo;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.view.reclyview.RecyclerViewDivider;

/**
 * Description: 歌曲列表pop
 * Author: xpfei
 * Date:   2017/09/04
 */
public class DownListPopupWindow {
    private Context mContext;
    private PopupWindow popupWindow;
    private DownAdapter adapter;
    private RecyclerView itemRv;
    private onPopItemClickListener listener;

    public interface onPopItemClickListener {
        void popItemClick(int position);
    }


    public void setOnPopItemClickListener(onPopItemClickListener listener) {
        this.listener = listener;
    }

    public DownListPopupWindow(Context context) {
        this.mContext = context;
        popupWindow = new PopupWindow(context);
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_downlistpopupwindow, null);
        itemRv = (RecyclerView) contentView.findViewById(R.id.itemRv);
        adapter = new DownAdapter(mContext, new ArrayList<DownInfo>(), android.R.layout.simple_list_item_1);
        itemRv.setAdapter(adapter);
        itemRv.setLayoutManager(new LinearLayoutManager(mContext));
        itemRv.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL,
                1, mContext.getResources().getColor(R.color.colorGray)));
        adapter.setOnMyItemClickListener(new BaseMyReclyViewAdapter.onMyItemClickListener() {
            @Override
            public void onMyItemClick(int position) {
                if (listener != null) {
                    listener.popItemClick(position);
                }
            }
        });
        //设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(contentView);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog); // 设置一个动画。
    }

    public void show(View view, List<DownInfo> list) {
        if (list == null || list.size() == 0) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                CommonUtil.showToast(mContext, "暂无下载链接");
                return;
            }
        }
        adapter.setData(list);
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }
}
