package org.xmfaly.simpleioc.test;

import org.xmfaly.simpleioc.core.Autowired;

public class Bread implements Good{

    @Autowired(name = "b_strategy")
    private Strategy strategy;

    public void useStrategy() {
        System.out.println("面包使用 " + strategy.getStrategy() + " 进行销售");
    }
}
