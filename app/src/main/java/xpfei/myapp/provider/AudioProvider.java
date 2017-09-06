package xpfei.myapp.provider;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.db.SongDbManager;
import xpfei.myapp.model.Song;

/**
 * Description: 获取本地音乐
 * Author: xpfei
 * Date:   2017/08/10
 */
public class AudioProvider implements AbstructProvider {

    private Context context;

    public AudioProvider(Context context) {
        this.context = context;
    }

    @Override
    public List<Song> getList() {
        List<Song> list = null;
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    Song song = new Song();
                    song.setSong_id(id);
                    song.setAuthor(artist);
                    song.setAlbum_title(album);
                    song.setTitle(title);
                    song.setFile_link_local(path);
                    song.setIsLocal(1);
                    list.add(song);
                }
                cursor.close();
                if (list.size() > 0) {
                    new SongDbManager().insertOrReplaceList(list);
                }
            }
        }
        return list;
    }

}
