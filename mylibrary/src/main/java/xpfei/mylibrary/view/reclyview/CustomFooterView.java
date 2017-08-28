package xpfei.mylibrary.view.reclyview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import xpfei.mylibrary.view.reclyview.callback.IFooterCallBack;
import xpfei.mylibrary.utils.Utils;


public class CustomFooterView extends LinearLayout implements IFooterCallBack {
    private Context mContext;

    private View mContentView;
    private View mProgressBar;
    private TextView mHintView;
    private TextView mClickView;
    private boolean showing = true;

    public CustomFooterView(Context context) {
        super(context);
        initView(context);
    }

    public CustomFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public void callWhenNotAutoLoadMore(final XRefreshView xRefreshView) {
        View childView = xRefreshView.getChildAt(1);
        if (childView != null && childView instanceof RecyclerView) {
            show(Utils.isRecyclerViewFullscreen((RecyclerView) childView));
            xRefreshView.enableReleaseToLoadMore(Utils.isRecyclerViewFullscreen((RecyclerView) childView));
        }
        mClickView.setText(xpfei.mylibrary.R.string.xrefreshview_footer_hint_click);
        mClickView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                xRefreshView.notifyLoadMore();
            }
        });
    }

    @Override
    public void onStateReady() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setText(xpfei.mylibrary.R.string.xrefreshview_footer_hint_click);
        mClickView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateRefreshing() {
        mHintView.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        show(true);
    }

    @Override
    public void onReleaseToLoadMore() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setText(xpfei.mylibrary.R.string.xrefreshview_footer_hint_release);
        mClickView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateFinish(boolean hideFooter) {
        if (hideFooter) {
            mHintView.setText(xpfei.mylibrary.R.string.xrefreshview_footer_hint_normal);
        } else {
            //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
            mHintView.setText(xpfei.mylibrary.R.string.xrefreshview_footer_hint_fail);
        }
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);
        mHintView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateComplete() {
        mHintView.setText(xpfei.mylibrary.R.string.xrefreshview_footer_hint_complete);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);
        mHintView.setVisibility(View.VISIBLE);
    }

    @Override
    public void show(final boolean show) {
        if (show == showing) {
            return;
        }
        showing = show;
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
        mContentView.setLayoutParams(lp);
    }

    @Override
    public boolean isShowing() {
        return showing;
    }

    private void initView(Context context) {
        mContext = context;
        ViewGroup moreView = (ViewGroup) LayoutInflater.from(mContext).inflate(xpfei.mylibrary.R.layout.xrefreshview_footer, this);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mContentView = moreView.findViewById(xpfei.mylibrary.R.id.xrefreshview_footer_content);
        mProgressBar = moreView.findViewById(xpfei.mylibrary.R.id.xrefreshview_footer_relativeLayout);
        mHintView = moreView.findViewById(xpfei.mylibrary.R.id.xrefreshview_footer_hint_textview);
        mClickView = moreView.findViewById(xpfei.mylibrary.R.id.xrefreshview_footer_click_textview);
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
