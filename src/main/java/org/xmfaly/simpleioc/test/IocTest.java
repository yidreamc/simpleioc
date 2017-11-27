package org.xmfaly.simpleioc.test;

import org.xmfaly.simpleioc.core.Container;

public class IocTest {

    public static void main(String[] args) {

        //新建一个容器
        Container c = new Container();

        //配置
        //该步骤相当于spring的配置xml文件步骤
        //因为考虑到xml文件的可读性不是很好，因此我使用java代码的形式进行配置

        //可以使用class的形式注册bean
        c.registerBean("apple",Apple.class);

        //也可以使用bean的形式注册
        c.registerBean("bread",new Bread());

        //对苹果使用策略a
        c.registerBean("a_strategy",StrategyA.class);

        //对面包使用策略b
        c.registerBean("b_strategy",StrategyB.class);

        //策略a的名字
        c.registerBean("a_name","策略A");

        //策略b的名字
        c.registerBean("b_name","策略B");

        //初始化容器
        c.init();


        //测试
        //该步骤来测试是否注入成功

        Apple apple = c.getBeanByName("apple");
        apple.useStrategy();

        Bread bread = c.getBeanByName("bread");
        bread.useStrategy();

    }
}
