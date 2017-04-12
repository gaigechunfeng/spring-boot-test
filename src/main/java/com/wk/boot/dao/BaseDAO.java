package com.wk.boot.dao;

import com.wk.boot.model.IdEntity;
import com.wk.boot.util.SqlUtil;
import com.wk.boot.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;
import java.util.List;

/**
 * Created by 005689 on 2017/4/12.
 */
@Repository
public class BaseDAO {

    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * 提供事务支持的方法
     */
    public <T> T execute(TransactionalCallBack<T> transactionalCallBack) {

        return transactionTemplate.execute(transactionalCallBack::doInTranscation);
    }

    public <T> List<T> findAll(Class<T> cls) {
        String tn = SqlUtil.getTableName(cls);
        if (Util.isStringEmpty(tn)) {
            throw new RuntimeException("cannot get tablename from " + cls);
        }

        return jdbcTemplate.queryForList("select * from " + tn, cls);
    }

    public <T extends IdEntity> void save(T t) {

        SqlUtil.SqlInfo sqlInfo;
        if (t.getId() == 0) {
            sqlInfo = SqlUtil.genAddSql(t);
        } else {
            sqlInfo = SqlUtil.genEditSql(t);
        }

        int r = jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getParams().toArray());
        if (r != 1) {
            throw new RuntimeException("result is not 1");
        }
    }

    public <T> List<T> find(String sql, Class<T> cls, Object... params) {

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(cls), params);
    }

    public <T extends IdEntity> T findById(Class<T> cls, Long id) {

        String tn = SqlUtil.getTableName(cls);
        List<T> list = find("select * from " + tn + " where id=?", cls, id);
        if (!Util.isCollectionEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public <T extends IdEntity> void deleteById(Class<T> cls, Long id) {

        String tn = SqlUtil.getTableName(cls);

        int r = jdbcTemplate.update("delete from " + tn + " where id=?", id);
        if (r != 1) {
            throw new RuntimeException("result is not 1");
        }

    }

    public <T extends IdEntity> void save(Collection<T> collection) {

        execute(status -> {
            collection.forEach(this::save);
            return null;
        });
    }

    public static interface TransactionalCallBack<T> {
        T doInTranscation(TransactionStatus status);
    }
}
