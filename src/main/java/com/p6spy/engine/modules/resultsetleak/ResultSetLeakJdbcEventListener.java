package com.p6spy.engine.modules.resultsetleak;

import com.p6spy.engine.common.ResultSetInformation;
import com.p6spy.engine.event.SimpleJdbcEventListener;

import java.sql.SQLException;

public class ResultSetLeakJdbcEventListener extends SimpleJdbcEventListener {

    public static final ResultSetLeakJdbcEventListener INSTANCE = new ResultSetLeakJdbcEventListener();

    private ResultSetLeakJdbcEventListener() {
    }

    @Override
    public void onAfterGetResultSet(ResultSetInformation resultSetInformation) {
        if (P6ResultSetLeakOptions.getActiveInstance().getLeakDetection()) {
            P6ResultSetLeakDetector.INSTANCE.registerInvocation(resultSetInformation.getResultSet(), System.nanoTime(), "rsleak", resultSetInformation.getSqlWithValues(), resultSetInformation.getSql(), resultSetInformation.getConnectionInformation().getUrl());
        }
    }

    @Override
    public void onAfterResultSetClose(ResultSetInformation resultSetInformation, long closeTimeNanos, SQLException e) {
        if (P6ResultSetLeakOptions.getActiveInstance().getLeakDetection()) {
            P6ResultSetLeakDetector.INSTANCE.unregisterInvocation(resultSetInformation.getResultSet());
        }
    }

}
