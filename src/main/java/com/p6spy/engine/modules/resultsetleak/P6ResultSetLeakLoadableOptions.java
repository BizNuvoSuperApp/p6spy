package com.p6spy.engine.modules.resultsetleak;

import com.p6spy.engine.spy.P6LoadableOptions;

public interface P6ResultSetLeakLoadableOptions extends P6LoadableOptions, P6ResultSetLeakOptionsMBean {

    void setLeakDetection(String leakDetection);

    void setLeakDetectionInterval(String leakDetectionInterval);

    default long getLeakDetectionIntervalMS() {
        return getLeakDetectionInterval() * 1000L;
    }

    void setLeakDetectionThreshold(String leakDetectionThreshold);

    default long getLeakDetectionThresholdMS() {
        return getLeakDetectionThreshold() * 1000L;
    }

}
