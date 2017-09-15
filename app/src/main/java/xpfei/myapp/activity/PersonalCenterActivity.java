package xpfei.myapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.SongAdapter;
import xpfei.myapp.databinding.ActivityPersonalcenterBinding;
import xpfei.myapp.model.Song;
import xpfei.myapp.model.UserInfo;
import xpfei.myapp.provider.AudioProvider;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.util.GlideUtils;
import xpfei.myapp.view.MultiStateView;
import xpfei.myapp.view.SelectPicturePopupWindow;
import xpfei.mylibrary.manager.ACache;
import xpfei.mylibrary.net.MyOkHttp;
import xpfei.mylibrary.net.response.FastJsonResponseHandler;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.imageutil.ImageUtils;
import xpfei.mylibrary.utils.imageutil.OnCompressListener;
import xpfei.mylibrary.view.reclyview.RecyclerViewDivider;

/**
 * Description: 个人中心
 * Author: xpfei
 * Date:   2017/08/10
 */
public class PersonalCenterActivity extends MyBaseActivity {
    private ActivityPersonalcenterBinding binding;
    private SelectPicturePopupWindow mSelectPicturePopupWindow;
    public static final int TAKE_PICTURE = 0x000001;
    private String path;
    private List<File> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personalcenter);
        onSetTitle("个人中心");
        onSetLeft(true);
        initView();
        initEvent();
    }

    private void initView() {
        mSelectPicturePopupWindow = new SelectPicturePopupWindow(this);
        binding.mMultiStateView.setViewState(MultiStateView.STATE_LOADING);
        binding.mRecvclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                AudioProvider provider = new AudioProvider(PersonalCenterActivity.this);
                if (provider.getList() != null && provider.getList().size() > 0) {
                    final ArrayList<Song> songs = provider.getList();
                    SongAdapter adapter = new SongAdapter(PersonalCenterActivity.this, provider.getList());
                    binding.mRecvclerview.setLayoutManager(new LinearLayoutManager(PersonalCenterActivity.this));
                    binding.mRecvclerview.addItemDecoration(new RecyclerViewDivider(PersonalCenterActivity.this,
                            LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.colorGray)));
                    binding.mRecvclerview.setAdapter(adapter);
                    binding.mMultiStateView.setViewState(MultiStateView.STATE_CONTENT);
                    adapter.setOnMyItemClickListener(new SongAdapter.onMyItemClickListener() {
                        @Override
                        public void onMyItemClick(Song info, int postion) {
                            Intent intent = new Intent(PersonalCenterActivity.this, PlayerActivity.class);
                            intent.putExtra(ContentValue.IntentKey.IntentKeyInt, 2);
                            intent.putExtra(ContentValue.IntentKey.IntentKeyIndex, postion);
                            intent.putParcelableArrayListExtra(ContentValue.IntentKey.IntentKeyList, songs);
                            startActivity(intent);
                        }
                    });
                } else {
                    binding.mMultiStateView.setViewState(MultiStateView.STATE_EMPTY, "暂无本地歌曲");
                }
            }
        }, 1000);
    }

    private void initEvent() {
        mSelectPicturePopupWindow.setOnSelectedListener(new SelectPicturePopupWindow.OnSelectedListener() {
            @Override
            public void OnSelected(View v, int position) {
                switch (position) {
                    case 0://拍照
                        photo();
                        break;
                    case 1://从相册选择

                        break;
                    case 2:
                        mSelectPicturePopupWindow.dismissPopupWindow();
                        break;
                }
            }
        });
        binding.setOnMyClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imgHeader:
                        mSelectPicturePopupWindow.showPopupWindow(PersonalCenterActivity.this);
                        break;
                    case R.id.llMyDown:
                        startActivity(new Intent(PersonalCenterActivity.this, DownLoadActivity.class));
                        break;
                }
            }
        });
    }

    /**
     * 调用系统摄像头拍照
     */
    private void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String tempPath = CommonUtil.getCachePath(this);
        File vFile = new File(tempPath, String.valueOf(System.currentTimeMillis()) + ".jpg");
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                files.add(new File(path));
                break;

        }
    }

    @Override
    public void onRequestData() {
        ImageUtils.with(PersonalCenterActivity.this)
                .load(files)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onSuccess(List<String> file) {
                        ACache aCache = ACache.get(PersonalCenterActivity.this);
                        UserInfo userInfo = (UserInfo) aCache.getAsObject(ContentValue.AcacheKey.ACACHE_USER);
                        JSONObject params = new JSONObject();
                        params.put("userid", userInfo.getUserid() + "");
                        params.put("imgheader", file);
                        MyOkHttp.getInstance().postString(PersonalCenterActivity.this, "http://192.168.10.20:8911/MyTest/MyTestService.svc/Upload", params.toString(), null, new FastJsonResponseHandler<Boolean>() {
                            @Override
                            public void onSuccess(Boolean response) {
                                if (response) {
                                    onDialogSuccess("上传成功");
                                    GlideUtils.loadImage(PersonalCenterActivity.this, path, R.mipmap.nopic, binding.imgHeader);
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                onDialogFailure(error_msg);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        onDialogFailure(e.getMessage());
                    }
                }).launch();
    }

}
