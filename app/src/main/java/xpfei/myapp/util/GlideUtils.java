package xpfei.myapp.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/09/01
 */
public class GlideUtils {
    public static void loadImage(Context context, String path, int errorPath, ImageView view) {
        Glide.with(context)
                .load(path)
                .error(errorPath)
                .skipMemoryCache(false)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    public static void loadImage(Context context, int path, int errorPath, ImageView view) {
        Glide.with(context)
                .load(path)
                .error(errorPath)
                .skipMemoryCache(false)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }
}
