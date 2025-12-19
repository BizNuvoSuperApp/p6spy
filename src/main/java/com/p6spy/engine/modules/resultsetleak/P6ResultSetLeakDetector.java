package com.p6spy.engine.modules.resultsetleak;

import com.p6spy.engine.common.P6LogQuery;
import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.modules.logging.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;

public enum P6ResultSetLeakDetector implements Runnable {

    INSTANCE;

    private final ConcurrentMap<ResultSet, InvocationInfo> pendingResultSets;
    private final ScheduledExecutorService leakDetectorThread = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread t = new Thread(P6Util.getP6ThreadGroup(), runnable, "P6LeakDetector");
        t.setDaemon(true);
        return t;
    });

    // flag that indicates that the thread should stop running
    private boolean haltThread;

    P6ResultSetLeakDetector() {
        pendingResultSets = new ConcurrentHashMap<>();

        leakDetectorThread.schedule(this, P6ResultSetLeakOptions.getActiveInstance().getLeakDetectionIntervalMS(), TimeUnit.MILLISECONDS);

        P6LogQuery.debug("P6Spy - P6ResultsSetLeakDetector has been invoked.");
        P6LogQuery.debug("P6Spy - P6ResultsSetLeakDetector.getLeakDetectionIntervalMS() = " + P6ResultSetLeakOptions.getActiveInstance().getLeakDetectionIntervalMS());
        P6LogQuery.debug("P6Spy - P6ResultsSetLeakDetector.getLeakDetectionThresholdMS() = " + P6ResultSetLeakOptions.getActiveInstance().getLeakDetectionThresholdMS());
    }

    @Override
    public void run() {
        if (!haltThread) {
            detectLeak();

            leakDetectorThread.schedule(this, P6ResultSetLeakOptions.getActiveInstance().getLeakDetectionIntervalMS(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Tells the auxillary thread to stop executing. Thread will exit upon waking next.
     */
    public void shutdown() {
        haltThread = true;
    }

    /**
     * Registers the execution of a statement. This should be called just before the statement is
     * passed to the real driver.
     */
    public void registerInvocation(ResultSet jdbcObject, long startTime, String category, String ps, String sql, String url) {
        pendingResultSets.put(jdbcObject, new InvocationInfo(startTime, category, ps, sql, url));
    }

    /**
     * Unregisters the execution of a statement. This should be called just after the statement is
     * passed to the real driver.
     */
    public void unregisterInvocation(ResultSet jdbcObject) {
        pendingResultSets.remove(jdbcObject);
    }

    private void detectLeak() {
        int listSize = pendingResultSets.size();

        if (listSize == 0) {
            return;
        }

        P6LogQuery.debug("P6Spy - resultSetLeak.pendingResultSets.size = " + listSize);

        long currentTime = System.nanoTime();
        long threshold = TimeUnit.MILLISECONDS.toNanos(P6ResultSetLeakOptions.getActiveInstance().getLeakDetectionThresholdMS());

        for (ResultSet jdbcObject : pendingResultSets.keySet()) {
            // here is a thread hazard that we'll be lazy about. Another thread
            // might have already removed the entry from the messages map, so we
            // need to check if the result is null
            InvocationInfo ii = pendingResultSets.get(jdbcObject);
            if (ii == null) {
                continue;
            }

            try {
                if (jdbcObject.isClosed()) {
                    P6LogQuery.debug("P6Spy - resultSetLeak.jdbcObject.isClosed() = true, but not removed from pendingResultSets.");
                    unregisterInvocation(jdbcObject);
                }
                // has this statement exceeded the threshold?
                else if ((currentTime - ii.startTime) > threshold) {
                    logOutage(ii);
                }
            } catch (SQLException e) {
                P6LogQuery.debug("");
            }
        }
    }

    private void logOutage(InvocationInfo ii) {
        P6LogQuery.log(Category.RSLEAK, System.nanoTime() - ii.startTime,  ii.preparedStmt, ii.sql, ii.url);
    }

}

// inner class to hold the info about a specific statement invocation
class InvocationInfo {
    final long startTime;

    final String category;

    final String preparedStmt;

    final String sql;

    final String url;

    public InvocationInfo(long startTime, String category, String ps, String sql, String url) {
        this.startTime = startTime;
        this.category = category;
        this.preparedStmt = ps;
        this.sql = sql;
        this.url = url;
    }
}
