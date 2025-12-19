package com.p6spy.engine.modules.resultsetleak;

import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.option.P6OptionsRepository;

import javax.management.StandardMBean;
import java.util.HashMap;
import java.util.Map;

public class P6ResultSetLeakOptions extends StandardMBean implements P6ResultSetLeakLoadableOptions {

    public static final String LEAKDETECTION = "leakdetection";
    public static final String LEAKDETECTIONINTERVAL = "leakdetectioninterval";
    public static final String LEAKDETECTIONTHRESHOLD = "leakdetectionthreshold";

    protected static final Map<String, String> defaults;

    static {
        defaults = new HashMap<>();
        defaults.put(LEAKDETECTION, Boolean.toString(false));
        defaults.put(LEAKDETECTIONINTERVAL, Long.toString(60));
        defaults.put(LEAKDETECTIONTHRESHOLD, Long.toString(180));
    }

    private final P6OptionsRepository optionsRepository;

    public P6ResultSetLeakOptions(final P6OptionsRepository optionsRepository) {
        super(P6ResultSetLeakOptionsMBean.class, false);
        this.optionsRepository = optionsRepository;
    }

    public static P6ResultSetLeakOptions getActiveInstance() {
        return P6ModuleManager.getInstance().getOptions(P6ResultSetLeakOptions.class);
    }

    @Override
    public Map<String, String> getDefaults() {
        return defaults;
    }

    @Override
    public void load(Map<String, String> options) {
        setLeakDetection(options.get(LEAKDETECTION));
        setLeakDetectionInterval(options.get(LEAKDETECTIONINTERVAL));
        setLeakDetectionThreshold(options.get(LEAKDETECTIONTHRESHOLD));
    }

    @Override
    public boolean getLeakDetection() {
        return optionsRepository.get(Boolean.class, LEAKDETECTION);
    }

    @Override
    public void setLeakDetection(boolean leakDetection) {
        optionsRepository.set(Boolean.class, LEAKDETECTION, leakDetection);
    }

    @Override
    public void setLeakDetection(String leakDetection) {
        optionsRepository.set(Boolean.class, LEAKDETECTION, leakDetection);
    }

    @Override
    public long getLeakDetectionInterval() {
        return optionsRepository.get(Long.class, LEAKDETECTIONINTERVAL);
    }

    @Override
    public void setLeakDetectionInterval(long leakDetectionInterval) {
        optionsRepository.set(Long.class, LEAKDETECTIONINTERVAL, leakDetectionInterval);
    }

    @Override
    public void setLeakDetectionInterval(String leakDetectionInterval) {
        optionsRepository.set(Long.class, LEAKDETECTIONINTERVAL, leakDetectionInterval);
    }

    @Override
    public long getLeakDetectionThreshold() {
        return optionsRepository.get(Long.class, LEAKDETECTIONTHRESHOLD);
    }

    @Override
    public void setLeakDetectionThreshold(long leakDetectionThreshold) {
        optionsRepository.set(Long.class, LEAKDETECTIONTHRESHOLD, leakDetectionThreshold);
    }

    @Override
    public void setLeakDetectionThreshold(String leakDetectionThreshold) {
        optionsRepository.set(Long.class, LEAKDETECTIONTHRESHOLD, leakDetectionThreshold);
    }
}
