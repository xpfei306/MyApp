package xpfei.myapp.db;

import org.greenrobot.greendao.AbstractDao;

import xpfei.myapp.manager.AbstractDatabaseManager;
import xpfei.myapp.model.UserInfo;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/04
 */
public class UserManager extends AbstractDatabaseManager<UserInfo, Long> {
    @Override
    protected AbstractDao<UserInfo, Long> getAbstractDao() {
        return daoSession.getUserInfoDao();
    }
}
