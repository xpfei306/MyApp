package xpfei.myapp.manager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xpfei.myapp.model.FormFile;
import xpfei.myapp.util.ImageUtils;


/**
 * 优惠管理的批量上传功能，上传到优惠的文件夹下
 *
 * @author yjw
 */
public class MultiUploadThread implements Runnable {
    private Handler mHandler;
    private List<String> mDataList;
    private int type = 0;

    public MultiUploadThread(List<String> mDataList, Handler mHandler) {
        super();
        if (mDataList == null || mDataList.isEmpty()) {
            return;
        }
        this.mDataList = mDataList;
        this.mHandler = mHandler;

    }

    @Override
    public void run() {
        FormFile[] tempFile = new FormFile[mDataList.size()];
        List<FormFile> filesList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            if (type == 1) {
                ImageUtils.Write(new File(mDataList.get(i)), mDataList.get(i));
            }
            String imagePath = mDataList.get(i);
            String imageName = imagePath.substring(
                    imagePath.lastIndexOf("/") + 1, imagePath.length());
            File file = new File(imagePath);
            filesList.add(new FormFile(file.getName(), file, imageName,
                    "application/octet-stream"));
        }
        tempFile = filesList.toArray(tempFile);
        String result;
        result = uploadFile(tempFile);
        Message msg = mHandler.obtainMessage();
        if (msg != null) {
            Bundle bundle = msg.getData();
            if (bundle != null) {
                bundle.putString("result", result);
                msg.setData(bundle);
            } else {
                msg.obj = result;
            }
            msg.arg1 = 11;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 上传图片到服务器
     *
     * @param formFiles FormFile数组。
     */
    public String uploadFile(FormFile[] formFiles) {
        try {
            Map<String, String> params = new HashMap<>();
            String result = SocketHttpRequester.post("http://192.168.10.20:8911/Upload", params, formFiles, mHandler);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
