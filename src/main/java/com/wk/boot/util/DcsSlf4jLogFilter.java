package com.wk.boot.util;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * 自定义的Slf4jLogerFilter，1：将日志打印的模式改为debug，2：重写statementExecuteAfter方法
 * Created by 005689 on 2017/1/9.
 */
public class DcsSlf4jLogFilter extends Slf4jLogFilter {

    protected Logger logger = LoggerFactory.getLogger(statementLoggerName);
    private boolean statementExecuteAfterLogEnable = true;
    private boolean statementExecuteQueryAfterLogEnable = true;
    private boolean statementExecuteUpdateAfterLogEnable = true;
    private boolean statementExecuteBatchAfterLogEnable = true;

    public DcsSlf4jLogFilter() {
        setConnectionLogEnabled(false);
        setResultSetLogEnabled(false);
        setStatementCloseAfterLogEnabled(false);
        setStatementCreateAfterLogEnabled(false);
        setStatementParameterSetLogEnabled(false);
        setStatementParameterClearLogEnable(false);
        setStatementPrepareAfterLogEnabled(false);
        setStatementPrepareCallAfterLogEnabled(false);
    }

    public void setStatementExecuteAfterLogEnable(boolean statementExecuteAfterLogEnable) {
        this.statementExecuteAfterLogEnable = statementExecuteAfterLogEnable;
    }

    public void setStatementExecuteQueryAfterLogEnable(boolean statementExecuteQueryAfterLogEnable) {
        this.statementExecuteQueryAfterLogEnable = statementExecuteQueryAfterLogEnable;
    }

    public void setStatementExecuteUpdateAfterLogEnable(boolean statementExecuteUpdateAfterLogEnable) {
        this.statementExecuteUpdateAfterLogEnable = statementExecuteUpdateAfterLogEnable;
    }

    public void setStatementExecuteBatchAfterLogEnable(boolean statementExecuteBatchAfterLogEnable) {
        this.statementExecuteBatchAfterLogEnable = statementExecuteBatchAfterLogEnable;
    }

    @Override
    public boolean isStatementLogEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    protected void statementLog(String message) {
        logger.info(message);
    }

    private void logAfter(StatementProxy statement, String sql) {
        if (this.isStatementLogEnabled()) {
            statement.setLastExecuteTimeNano();
            double nanos = (double) statement.getLastExecuteTimeNano();
            double millis = nanos / 1000000.0D;
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + "} executed. " + millis + " millis. \n" + getFormattedSql(statement, sql));
        }
    }

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean firstResult) {
        if (this.statementExecuteAfterLogEnable) {
            logAfter(statement, sql);
        }
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        if (this.statementExecuteQueryAfterLogEnable) {
            logAfter(statement, sql);
        }
    }

    @Override
    protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
        if (this.statementExecuteUpdateAfterLogEnable) {
            logAfter(statement, sql);
        }
    }

    @Override
    protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
        String sql;
        if (statement instanceof PreparedStatementProxy) {
            sql = ((PreparedStatementProxy) statement).getSql();
        } else {
            sql = statement.getBatchSql();
        }

        if (this.statementExecuteBatchAfterLogEnable) {
            logAfter(statement, sql);
        }
    }

    private String getFormattedSql(StatementProxy statement, String sql) {
        int parametersSize = statement.getParametersSize();
        String s;
        if (parametersSize == 0) {
            s = sql;
        } else {
            ArrayList parameters = new ArrayList(parametersSize);

            for (int dbType = 0; dbType < parametersSize; ++dbType) {
                JdbcParameter formattedSql = statement.getParameter(dbType);
                parameters.add(formattedSql != null ? formattedSql.getValue() : null);
            }

            String var7 = statement.getConnectionProxy().getDirectDataSource().getDbType();
            s = SQLUtils.format(sql, var7, parameters);
        }

        return s;
    }
}
