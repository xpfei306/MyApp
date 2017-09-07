package xpfei.myapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import xpfei.myapp.model.DaoMaster;
import xpfei.myapp.model.DownLoadInfoDao;
import xpfei.myapp.model.SongDao;
import xpfei.myapp.model.UserInfoDao;
import xpfei.mylibrary.db.MigrationHelper;


public class HMROpenHelper extends DaoMaster.OpenHelper {

    public HMROpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper helper = new MigrationHelper();
        helper.migrate(db, UserInfoDao.class, SongDao.class, DownLoadInfoDao.class);
    }
}
