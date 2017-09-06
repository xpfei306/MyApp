package xpfei.myapp.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.adapter.SongAdapter;
import xpfei.myapp.model.Song;

/**
 * Description: 歌曲列表pop
 * Author: xpfei
 * Date:   2017/09/04
 */
public class MusicListPopupWindow {
    private Context mContext;
    private PopupWindow popupWindow;
    private ImageView ivLeft;
    private TextView txtPlayMode, txtMusicNum, btnClear, txtEmpty;
    private RecyclerView itemRv;
    private SongAdapter adapter;
    private onPopItemClickListener listener;
    private onClearClickListener clearClickListener;

    public interface onPopItemClickListener {
        void popItemClick(Song info);
    }

    public interface onClearClickListener {
        void clearClick();
    }

    public void setonClearClickListener(onClearClickListener clearClickListener) {
        this.clearClickListener = clearClickListener;
    }

    public void setOnPopItemClickListener(onPopItemClickListener listener) {
        this.listener = listener;
    }

    public MusicListPopupWindow(Context context) {
        this.mContext = context;
        popupWindow = new PopupWindow(context);
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_musiclistpopupwindow, null);
        ivLeft = (ImageView) contentView.findViewById(R.id.ivLeft);
        txtPlayMode = (TextView) contentView.findViewById(R.id.txtPlayMode);
        txtMusicNum = (TextView) contentView.findViewById(R.id.txtMusicNum);
        txtEmpty = (TextView) contentView.findViewById(R.id.txtEmpty);
        btnClear = (TextView) contentView.findViewById(R.id.btnClear);
        itemRv = (RecyclerView) contentView.findViewById(R.id.itemRv);
        adapter = new SongAdapter(mContext, new ArrayList<Song>());
        itemRv.setAdapter(adapter);
        itemRv.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.setOnMyItemClickListener(new SongAdapter.onMyItemClickListener() {
            @Override
            public void onMyItemClick(Song info) {
                if (listener != null) {
                    listener.popItemClick(info);
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearClickListener.clearClick();
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

    public void show(View view, List<Song> list, int mode) {
        if (mode == 1) {
            ivLeft.setImageResource(R.drawable.dqxh);
            txtPlayMode.setText("单曲循环");
        } else if (mode == 2) {
            ivLeft.setImageResource(R.drawable.sjbf);
            txtPlayMode.setText("随机播放");
        } else {
            ivLeft.setImageResource(R.drawable.lbxh);
            txtPlayMode.setText("列表循环");
        }
        if (list != null && list.size() > 0) {
            txtEmpty.setVisibility(View.GONE);
            txtMusicNum.setVisibility(View.VISIBLE);
            itemRv.setVisibility(View.VISIBLE);
            txtMusicNum.setText("(" + list.size() + ")");
            adapter.setData(list);
        } else {
            txtMusicNum.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.VISIBLE);
            itemRv.setVisibility(View.GONE);
            txtMusicNum.setText("(0)");
        }
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }
}
