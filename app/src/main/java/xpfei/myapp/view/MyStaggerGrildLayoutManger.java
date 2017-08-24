package xpfei.myapp.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;

/**
 * Created by ruedy on 2017/1/9.
 * 用于改变recycleview加载更多结束以后向上滑动一段距离是，平滑， 可以控制时间
 */

public class MyStaggerGrildLayoutManger extends StaggeredGridLayoutManager {


    private float MILLISECONDS_PER_INCH = 1f;
    private Context contxt;

    public MyStaggerGrildLayoutManger(Context context, int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return null;
                    }

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.density;
                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public void setSpeedSlow() {
        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.3f;
    }

    public void setSpeedFast() {
        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.03f;
    }
}
