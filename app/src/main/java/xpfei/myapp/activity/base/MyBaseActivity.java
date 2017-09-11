package xpfei.myapp.activity.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import xpfei.myapp.R;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.util.DialogUtil;
import xpfei.myapp.view.MultiStateView;
import xpfei.mylibrary.manager.ACache;
import xpfei.mylibrary.manager.AppManager;
import xpfei.mylibrary.net.MyOkHttp;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: 所有activity的父类
 * Author: xpfei
 * Date:   2017/07/27
 */
public abstract class MyBaseActivity extends AppCompatActivity {
    private ImageView btn_left;
    private TextView txt_title;
    protected MyBaseActivity activity;
    private ProgressDialog dialog;
    protected MultiStateView mMultiStateView;
    /**
     * 保存当前使用的主题ID
     */
    protected int mCurrentThemeId;

    /**
     * 从服务端获取相关数据
     */
    public abstract void onRequestData();

    protected Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            String message;
            switch (what) {
                case ContentValue.NET.NET_LOAD:
                    if (!CommonUtil.isNetworkAvailable(activity)) {
                        CommonUtil.showToast(activity, "网路故障！请检查网络设置");
                        return;
                    }
                    message = msg.getData().getString("MSG", "正在获取信息，请稍等...");
                    dialog = DialogUtil.GetMyProgressDialog(activity, message);
                    dialog.show();
                    activity.onRequestData();
                    break;
                case ContentValue.NET.NET_MSV_LOAD:
                    if (!CommonUtil.isNetworkAvailable(activity)) {
                        mMultiStateView.setViewState(MultiStateView.STATE_NETWORK);
                        return;
                    }
                    message = msg.getData().getString("MSG", "正在获取信息，请稍等...");
                    mMultiStateView.setViewState(MultiStateView.STATE_LOADING, message);
                    activity.onRequestData();
                    break;
                case ContentValue.NET.NET_SUCCESS:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    message = msg.getData().getString("MSG", null);
                    if (!StringUtil.isEmpty(message)) {
                        CommonUtil.showToast(activity, message);
                    }
                    break;
                case ContentValue.NET.NET_MSV_SUCCESS:
                    if (mMultiStateView != null) {
                        mMultiStateView.setViewState(MultiStateView.STATE_CONTENT);
                    }
                    message = msg.getData().getString("MSG", null);
                    if (!StringUtil.isEmpty(message)) {
                        CommonUtil.showToast(activity, message);
                    }
                    break;
                case ContentValue.NET.NET_FAILURE:
                    message = msg.getData().getString("MSG", "意外错误，请联系技术人员！");
                    if (dialog != null) {
                        dialog.dismiss();
                        CommonUtil.showToast(activity, message);
                    }
                    break;
                case ContentValue.NET.NET_MSV_FAILURE:
                    message = msg.getData().getString("MSG", "意外错误，请联系技术人员！");
                    if (mMultiStateView != null) {
                        mMultiStateView.setViewState(MultiStateView.STATE_ERROR, message);
                    }
                    break;
                case ContentValue.NET.NET_EMPTY:
                    message = msg.getData().getString("MSG", "暂无相关数据");
                    if (dialog != null) {
                        dialog.dismiss();
                        CommonUtil.showToast(activity, message);
                    }
                    break;
                case ContentValue.NET.NET_MSV_EMPTY:
                    message = msg.getData().getString("MSG", "暂无相关数据");
                    if (mMultiStateView != null) {
                        mMultiStateView.setViewState(MultiStateView.STATE_EMPTY, message);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        AppManager.getAppManager().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    /**
     * 有ProgressDialog
     *
     * @param baseActivity 上下文
     */
    protected void startBaseReqTask(MyBaseActivity baseActivity, String message) {
        activity = baseActivity;
        mLoadHandler.removeMessages(ContentValue.NET.NET_LOAD);
        Message msg = mLoadHandler.obtainMessage();
        if (!StringUtil.isEmpty(message)) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", message);
            msg.setData(bundle);
        }
        msg.what = ContentValue.NET.NET_LOAD;
        mLoadHandler.sendMessage(msg);
    }

    /**
     * 有MultiStateView的页面网络请求
     *
     * @param baseActivity
     */
    protected void startBaseMSVReqTask(MyBaseActivity baseActivity, String message) {
        activity = baseActivity;
        mLoadHandler.removeMessages(ContentValue.NET.NET_MSV_LOAD);
        Message msg = mLoadHandler.obtainMessage();
        if (!StringUtil.isEmpty(message)) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", message);
            msg.setData(bundle);
        }
        msg.what = ContentValue.NET.NET_MSV_LOAD;
        mLoadHandler.sendMessage(msg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public void onDialogSuccess(String message) {
        mLoadHandler.removeMessages(ContentValue.NET.NET_SUCCESS);
        Message msg = mLoadHandler.obtainMessage();
        if (!StringUtil.isEmpty(message)) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", message);
            msg.setData(bundle);
        }
        msg.what = ContentValue.NET.NET_SUCCESS;
        mLoadHandler.sendMessage(msg);
    }

    public void onMSVSuccess(String message) {
        mLoadHandler.removeMessages(ContentValue.NET.NET_MSV_SUCCESS);
        Message msg = mLoadHandler.obtainMessage();
        if (!StringUtil.isEmpty(message)) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", message);
            msg.setData(bundle);
        }
        msg.what = ContentValue.NET.NET_MSV_SUCCESS;
        mLoadHandler.sendMessage(msg);
    }

    protected void onDialogFailure(String message) {
        mLoadHandler.removeMessages(ContentValue.NET.NET_FAILURE);
        Message msg = mLoadHandler.obtainMessage();
        if (!StringUtil.isEmpty(message)) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", message);
            msg.setData(bundle);
        }
        msg.what = ContentValue.NET.NET_FAILURE;
        mLoadHandler.sendMessage(msg);
    }

    protected void onMSVFailure(String message) {
        mLoadHandler.removeMessages(ContentValue.NET.NET_MSV_FAILURE);
        Message msg = mLoadHandler.obtainMessage();
        if (!StringUtil.isEmpty(message)) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", message);
            msg.setData(bundle);
        }
        msg.what = ContentValue.NET.NET_MSV_FAILURE;
        mLoadHandler.sendMessage(msg);
    }

    protected void onDialogEmpty(String message) {
        mLoadHandler.removeMessages(ContentValue.NET.NET_EMPTY);
        Message msg = mLoadHandler.obtainMessage();
        if (!StringUtil.isEmpty(message)) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", message);
            msg.setData(bundle);
        }
        msg.what = ContentValue.NET.NET_EMPTY;
        mLoadHandler.sendMessage(msg);
    }

    protected void onMSVEmpty(String message) {
        mLoadHandler.removeMessages(ContentValue.NET.NET_MSV_EMPTY);
        Message msg = mLoadHandler.obtainMessage();
        if (!StringUtil.isEmpty(message)) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", message);
            msg.setData(bundle);
        }
        msg.what = ContentValue.NET.NET_MSV_EMPTY;
        mLoadHandler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        dialog = null;
        mLoadHandler.removeCallbacksAndMessages(null);
        MyOkHttp.getInstance().cancel(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        btn_left = (ImageView) findViewById(R.id.btn_left);
        txt_title = (TextView) findViewById(R.id.txt_title);
        mMultiStateView = (MultiStateView) findViewById(R.id.mMultiStateView);
    }

    /**
     * topview的返回按钮和点击事件
     *
     * @param isVisibility 是否可见和添加监听事件
     */
    public void onSetLeft(boolean isVisibility) {
        if (btn_left != null) {
            if (isVisibility) {
                btn_left.setVisibility(View.VISIBLE);
                btn_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            } else {
                btn_left.setVisibility(View.GONE);
            }
        }
    }

    /**
     * topview的返回按钮和点击事件
     *
     * @param listener 添加自定义事件
     */
    public void onSetLeft(View.OnClickListener listener) {
        if (btn_left != null) {
            btn_left.setVisibility(View.VISIBLE);
            btn_left.setOnClickListener(listener);
        }
    }

    /**
     * topview的返回按钮和点击事件
     *
     * @param resId    资源文件id
     * @param listener 添加自定义事件
     */
    public void onSetLeft(int resId, View.OnClickListener listener) {
        if (btn_left != null) {
            btn_left.setVisibility(View.VISIBLE);
            btn_left.setImageResource(resId);
            btn_left.setOnClickListener(listener);
        }
    }

    /**
     * 设置文件标题
     *
     * @param title 标题内容
     */

    public void onSetTitle(String title) {
        if (txt_title != null) {
            txt_title.setText(title);
        }
    }

    /**
     * 此方法会在onCreate方法之前被系统执行
     *
     * @param resid
     */
    @Override
    public void setTheme(@StyleRes int resid) {
        ACache aCache = ACache.get(this);
        int savedTheme = StringUtil.str2Int(aCache.getAsString(ContentValue.AcacheKey.ACACHEkEY_VIEWTHEME));
        if (savedTheme > 0 && savedTheme != resid) {
            resid = savedTheme;
        }
        mCurrentThemeId = resid;
        super.setTheme(resid);
    }

}
