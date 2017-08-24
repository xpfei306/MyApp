package xpfei.mylibrary.utils.imageutil;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils implements Handler.Callback {
    private static final String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";

    private static final int MSG_COMPRESS_SUCCESS = 0;
    private static final int MSG_COMPRESS_START = 1;
    private static final int MSG_COMPRESS_ERROR = 2;
    private List<File> fileList;
    private OnCompressListener onCompressListener;

    private Handler mHandler;

    private ImageUtils(Builder builder) {
        this.fileList = builder.fileList;
        this.onCompressListener = builder.onCompressListener;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    /**
     * Returns a file with a cache audio name in the private cache directory.
     *
     * @param context A context.
     */
    private File getImageCacheFile(Context context) {
        if (getImageCacheDir(context) != null) {
            return new File(getImageCacheDir(context) + "/" + System.currentTimeMillis() + (int) (Math.random() * 1000) + ".jpg");
        }
        return null;
    }

    /**
     * Returns a directory with a default name in the private cache directory of the application to
     * use to store retrieved audio.
     *
     * @param context A context.
     * @see #getImageCacheDir(Context, String)
     */
    @Nullable
    private File getImageCacheDir(Context context) {
        return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to
     * use to store retrieved media and thumbnails.
     *
     * @param context   A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @see #getImageCacheDir(Context)
     */
    @Nullable
    private File getImageCacheDir(Context context, String cacheName) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                return null;
            }
            return result;
        }
        return null;
    }

    /**
     * start asynchronous compress thread
     */
    @UiThread
    private void launch(final Context context) {
        if (fileList == null && onCompressListener != null) {
            onCompressListener.onError(new NullPointerException("image file cannot be null"));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_START));
                    List<String> stringList = new ArrayList<>();
                    Engine engine = new Engine();
                    for (File file : fileList) {
                        engine.setEngine(file, getImageCacheFile(context));
                        String result = engine.compress1();
                        stringList.add(result);
                    }
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_SUCCESS, stringList));
                } catch (IOException e) {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_ERROR, e));
                }
            }
        }).start();
    }
//
//    /**
//     * start compress and return the file
//     */
//    @WorkerThread
//    private File get(final Context context) throws IOException {
//        Engine engine = new Engine();
//        engine.setEngine(file, getImageCacheFile(context));
//        return engine.compress();
//    }

    @Override
    public boolean handleMessage(Message msg) {
        if (onCompressListener == null) return false;
        switch (msg.what) {
            case MSG_COMPRESS_START:
                break;
            case MSG_COMPRESS_SUCCESS:
                onCompressListener.onSuccess((List<String>) msg.obj);
                break;
            case MSG_COMPRESS_ERROR:
                onCompressListener.onError((Throwable) msg.obj);
                break;
        }
        return false;
    }

    public static class Builder {
        private Context context;
        private List<File> fileList;
        private OnCompressListener onCompressListener;

        Builder(Context context) {
            this.context = context;
        }

        private ImageUtils build() {
            return new ImageUtils(this);
        }

        public Builder load(List<File> fileList) {
            this.fileList = fileList;
            return this;
        }

        public Builder setCompressListener(OnCompressListener listener) {
            this.onCompressListener = listener;
            return this;
        }

        public void launch() {
            build().launch(context);
        }

//        public File get() throws IOException {
//            return build().get(context);
//        }
    }
}