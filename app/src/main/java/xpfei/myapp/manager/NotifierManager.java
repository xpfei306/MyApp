package xpfei.myapp.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import xpfei.myapp.R;
import xpfei.myapp.model.Song;
import xpfei.myapp.service.MusicPlayService;
import xpfei.myapp.util.CoverLoader;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/09/20
 */
public class NotifierManager {
    private static final int NOTIFICATION_ID = 0x111;
    private static NotificationManager notificationManager;
    private static MusicPlayService playService;

    public static void init(MusicPlayService playService) {
        NotifierManager.playService = playService;
        notificationManager = (NotificationManager) playService.getSystemService(NOTIFICATION_SERVICE);
    }

    public static void showPlay(Song music) {
        playService.startForeground(NOTIFICATION_ID, buildNotification(playService, music));
    }

    public static void showPause(Song music) {
        playService.stopForeground(false);
        notificationManager.notify(NOTIFICATION_ID, buildNotification(playService, music));
    }

    public static void cancelAll() {
        notificationManager.cancelAll();
        playService = null;
    }

    private static Notification buildNotification(Context context, Song music) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_notification);
        remoteViews.setTextViewText(R.id.tv_title, music.getTitle());
        remoteViews.setTextViewText(R.id.tv_subtitle, music.getAuthor());
        if (music.getIsLocal() == 1) {
            Bitmap cover = CoverLoader.getInstance(context).loadThumbnail(music);
            remoteViews.setImageViewBitmap(R.id.iv_icon, cover);
        } else {
            Glide.with(context).load(music.getPic_small()).asBitmap().error(R.drawable.noalbum).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    remoteViews.setImageViewBitmap(R.id.iv_icon, resource);
                }
            });
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.noalbum);
        return mBuilder.build();
    }
}
