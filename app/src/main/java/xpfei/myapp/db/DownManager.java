package xpfei.myapp.db;

import org.greenrobot.greendao.AbstractDao;

import xpfei.myapp.manager.AbstractDatabaseManager;
import xpfei.myapp.model.DownLoadInfo;


/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/09/07
 */
public class DownManager extends AbstractDatabaseManager<DownLoadInfo, Long> {
    @Override
    protected AbstractDao<DownLoadInfo, Long> getAbstractDao() {
        return daoSession.getDownLoadInfoDao();
    }
}
