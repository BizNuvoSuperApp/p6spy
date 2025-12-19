package com.p6spy.engine.modules.resultsetleak;

public interface P6ResultSetLeakOptionsMBean {

    boolean getLeakDetection();

    void setLeakDetection(boolean leakDetection);

    long getLeakDetectionInterval();

    void setLeakDetectionInterval(long leakDetectionInterval);

    long getLeakDetectionThreshold();

    void setLeakDetectionThreshold(long leakDetectionThreshold);

}
