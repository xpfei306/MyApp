package xpfei.myapp.db;

import org.greenrobot.greendao.AbstractDao;

import xpfei.myapp.manager.AbstractDatabaseManager;
import xpfei.myapp.model.Song;

/**
 * Description: 歌曲本地管理
 * Author: xpfei
 * Date:   2017/08/30
 */
public class SongDbManager extends AbstractDatabaseManager<Song, Long> {

    @Override
    protected AbstractDao<Song, Long> getAbstractDao() {
        return daoSession.getSongDao();
    }
}
