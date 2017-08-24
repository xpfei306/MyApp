package xpfei.mylibrary.db;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Description: 数据库实现接口
 * Author: xpfei
 * Date:   2017/4/25
 */
public interface IDatabase<M, K> {
    /**
     * 插入
     */
    boolean insert(M m);

    /**
     * 删除
     */
    boolean delete(M m);

    /**
     * 通过key删除
     */
    boolean deleteByKey(K key);

    /**
     * 删除集合
     */
    boolean deleteList(List<M> mList);

    /**
     * 删除多个key
     */
    boolean deleteByKeyInTx(K... key);

    /**
     * 删除所有
     */
    boolean deleteAll();

    /**
     * 插入或替换
     */
    boolean insertOrReplace(@NonNull M m);

    /**
     * 更新
     */
    boolean update(M m);

    /**
     * 更新多个
     */
    boolean updateInTx(M... m);

    /**
     * 更新集合
     */
    boolean updateList(List<M> mList);

    /**
     * 通过key来查找对象
     */
    M selectByPrimaryKey(K key);

    /**
     * 查找所有
     */
    List<M> loadAll();

    /**
     * 刷新当前对象的值
     */
    boolean refresh(M m);

    /**
     * 清理缓存
     */
    void clearDaoSession();

    /**
     * Delete all tables and content from our database
     */
    boolean dropDatabase();

    /**
     * 事务
     */
    void runInTx(Runnable runnable);

    /**
     * 添加集合
     *
     * @param mList
     */
    boolean insertList(List<M> mList);

    /**
     * 添加集合
     *
     * @param mList
     */
    boolean insertOrReplaceList(List<M> mList);

    /**
     * 自定义查询
     *
     * @return
     */
    QueryBuilder<M> getQueryBuilder();

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    List<M> queryRaw(String where, String... selectionArg);

}
