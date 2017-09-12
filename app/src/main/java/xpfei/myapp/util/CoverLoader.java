package xpfei.myapp.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

import xpfei.myapp.R;
import xpfei.myapp.model.Song;
import xpfei.mylibrary.net.MyOkHttp;

/**
 * 专辑封面图片加载器
 * Created by wcy on 2015/11/27.
 */
public class CoverLoader {
    // 封面缓存
    private LruCache<String, Bitmap> mCoverCache;
    private Context mContext;
    private static CoverLoader instance;

    private CoverLoader(Context mContext) {
        this.mContext = mContext;
        // 获取当前进程的可用内存（单位KB）
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 缓存大小为当前进程可用内存的1/8
        int cacheSize = maxMemory / 8;
        mCoverCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return bitmap.getAllocationByteCount() / 1024;
                } else {
                    return bitmap.getByteCount() / 1024;
                }
            }
        };
    }

    public static CoverLoader getInstance(Context mContext) {
        if (instance == null) {
            synchronized (MyOkHttp.class) {
                instance = new CoverLoader(mContext);
            }
        }
        return instance;
    }

    public Bitmap loadThumbnail(Song music) {
        return loadCover(music);
    }

    private Bitmap loadCover(Song music) {
        Bitmap bitmap;
        if (music == null || music.getIsLocal() != 1) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.noalbum);
            return bitmap;
        }
        String key = getKey(music);
        if (TextUtils.isEmpty(key)) {
            bitmap = mCoverCache.get(String.valueOf(music.getAlbum_id()));
            if (bitmap != null) {
                return bitmap;
            }
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.noalbum);
            mCoverCache.put(String.valueOf(music.getAlbum_id()), bitmap);
            return bitmap;
        }
        bitmap = mCoverCache.get(key);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = loadCoverFromMediaStore(music.getAlbum_id());
        if (bitmap != null) {
            mCoverCache.put(key, bitmap);
            return bitmap;
        }
        return loadCover(null);
    }

    private String getKey(Song music) {
        if (music == null) {
            return null;
        }
        if (music.getIsLocal() == 1 && music.getAlbum_id() > 0) {
            return String.valueOf(music.getAlbum_id());
        } else {
            return null;
        }
    }

    /**
     * 从媒体库加载封面<br>
     * 本地音乐
     */
    private Bitmap loadCoverFromMediaStore(long albumId) {
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = getMediaStoreAlbumCoverUri(albumId);
        InputStream is;
        try {
            is = resolver.openInputStream(uri);
        } catch (FileNotFoundException ignored) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(is, null, options);
    }

    private Uri getMediaStoreAlbumCoverUri(long albumId) {
        Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(artworkUri, albumId);
    }
}
