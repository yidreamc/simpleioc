项目结构如下  
![11](/doc/1.png)
其中`core`文件夹中是ioc容器代码`test`  文件夹中是该框架的使用示例代码。  
以下内容我将介绍该框架提供的接口方法。  

该框架提供一个`@Autowired`注解用于实现依赖注入。  
如果你想要注入值到某个属性中，可以使用该注解来修饰该属性。例如在`test\StrategyA.java`文件中的  
```
    @Autowired(name = "name")
    private String name;
```
我给`@Autowired`提供了三种注入方式  
1. 第一种是设置名字，如上例，这样就会在容器中找到名字为`name`的bean进行注入。  
2. 第二种是  
```
    @Autowired
    private String name;

```
如果使用该方式进行修饰，该框架会自动扫描属性的`Type`（该例是`String.class`进行注入。）  
3. 第三种
```
	@Autowired(value = String.class)	
	private String name;
```
还是以`String`类为例，为注解提供`value`属性，那么容器就会按照`value`的值进行注入。  
请注意如果使用2、3种方式进行注入的时候，如果注入的类型为自定义类型，如果重载过构造函数，请务必提供空参数的构造函数。  

容器类`Container`提供了以下几个方法用于注册`bean`  
```
    // 以bean的形式注册
    public Object registerBean(Object bean);


    // 以class的形式注册
    public Object registerBean(Class<?> cls);

    // 以带对象名的class形式注册
    public Object registerBean(String name, Class<?> cls);


    // 以带对象名的bean形式注册
    public Object registerBean(String name, Object bean);

```

以下我以一个例子的形式介绍如果使用该框架。该例子的完整代码见项目下的`test`文件夹  

```
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

```
运行结果  
![22](/doc/2.png)
