package com.p6spy.engine.modules.resultsetleak;

import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.P6Factory;
import com.p6spy.engine.spy.P6LoadableOptions;
import com.p6spy.engine.spy.option.P6OptionsRepository;

public class P6ResultSetLeakFactory implements P6Factory {

    @Override
    public JdbcEventListener getJdbcEventListener() {
        return ResultSetLeakJdbcEventListener.INSTANCE;
    }

    @Override
    public P6LoadableOptions getOptions(P6OptionsRepository optionsRepository) {
        return new P6ResultSetLeakOptions(optionsRepository);
    }

}
