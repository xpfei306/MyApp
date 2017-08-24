package xpfei.myapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;

import xpfei.myapp.R;


/**
 * 下载的ProgressDialog
 * Created by xpf on 2016/11/8.
 */

public class MyProgressDialog extends AlertDialog {
    private ProgressBar mProgress;
    private TextView mProgressNumber;
    private TextView mProgressPercent;
    private TextView mProgressTitle;
    private TextView mProgressMessage;
    private Handler mViewUpdateHandler;
    private int mMax;
    private CharSequence mMessage;
    private CharSequence mTitle;
    private boolean mHasStarted;
    private int mProgressVal;
    private String mProgressNumberFormat;
    private NumberFormat mProgressPercentFormat;

    public MyProgressDialog(Context context) {
        super(context);
        initFormats();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_progress_dialog);
        mProgress = findViewById(R.id.progress);
        mProgressNumber = findViewById(R.id.progress_number);
        mProgressPercent = findViewById(R.id.progress_percent);
        mProgressMessage = findViewById(R.id.progress_message);
        mProgressTitle = findViewById(R.id.progress_title);
        mViewUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int progress = mProgress.getProgress();
                int max = mProgress.getMax();
                double dProgress = (double) progress / (double) (1024 * 1024);
                double dMax = (double) max / (double) (1024 * 1024);
                if (mProgressNumberFormat != null) {
                    String format = mProgressNumberFormat;
                    mProgressNumber.setText(String.format(format, dProgress, dMax));
                } else {
                    mProgressNumber.setText("");
                }
                if (mProgressPercentFormat != null) {
                    double percent = (double) progress / (double) max;
                    SpannableString tmp = new SpannableString(mProgressPercentFormat.format(percent));
                    tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mProgressPercent.setText(tmp);
                } else {
                    mProgressPercent.setText("");
                }
            }
        };
        setCancelable(false);// 设置是否可以通过点击Back键取消
        setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        onProgressChanged();
        if (mTitle != null) {
            setTitle(mTitle);
        }
        if (mMessage != null) {
            setMessage(mMessage);
        }
        if (mMax > 0) {
            setMax(mMax);
        }
        if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }

    }

    private void initFormats() {
        mProgressNumberFormat = "%1.2fM/%2.2fM";
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    private void onProgressChanged() {
        mViewUpdateHandler.sendEmptyMessage(0);
    }

    public void setMax(int max) {
        if (mProgress != null) {
            mProgress.setMax(max);
            onProgressChanged();
        } else {
            mMax = max;
        }
    }


    public void setProgress(int value) {
        if (mHasStarted) {
            mProgress.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mProgressMessage != null) {
            mProgressMessage.setText(message);
        } else {
            mMessage = message;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mProgressTitle != null) {
            mProgressTitle.setText(title);
        } else {
            mTitle = title;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHasStarted = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHasStarted = false;
    }
}
