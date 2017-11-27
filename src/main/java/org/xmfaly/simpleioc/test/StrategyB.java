package org.xmfaly.simpleioc.test;

import org.xmfaly.simpleioc.core.Autowired;

public class StrategyB implements Strategy {

    @Autowired(name = "b_name")
    private String name;

    public String getStrategy() {
        return name;
    }
}
