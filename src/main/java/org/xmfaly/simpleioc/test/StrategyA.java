package org.xmfaly.simpleioc.test;

import org.xmfaly.simpleioc.core.Autowired;

public class StrategyA implements Strategy{

    @Autowired(name = "a_name")
    private String name;

    public String getStrategy() {
        return name;
    }
}
