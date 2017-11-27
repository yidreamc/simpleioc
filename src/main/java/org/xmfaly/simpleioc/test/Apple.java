package org.xmfaly.simpleioc.test;

import org.xmfaly.simpleioc.core.Autowired;

public class Apple implements Good{

    @Autowired(name = "a_strategy")
    private Strategy strategy;

    public void useStrategy() {
        System.out.println("苹果使用 " + strategy.getStrategy() + " 进行销售");
    }
}
