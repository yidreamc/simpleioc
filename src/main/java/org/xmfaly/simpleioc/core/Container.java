package org.xmfaly.simpleioc.core;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class Container {

    // 保存所有bean 格式为 类名 : bean
    private Map<String, Object> beans;

    // 存储对象和类名的关系 对象名 ：bean
    private Map<String, Object> beanKeys;

    public Container() {
        beans = new ConcurrentHashMap<String, Object>();
        beanKeys = new ConcurrentHashMap<String, Object>();
    }

    /**
     * 以class的形式注册
     */
    public Object registerBean(Class<?> cls) {
        String className = cls.getName();
        Object bean = null;

        try {
            bean = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        beans.put(className, bean);

        //不指定对象名的情况下类名和对象名相同
        beanKeys.put(className, bean);
        return bean;
    }

    /**
     * 以bean的形式注册
     */
    public Object registerBean(Object bean) {
        String className = bean.getClass().getName();
        beans.put(className, bean);
        beanKeys.put(className, bean);
        return bean;
    }


    /**
     * 以带对象名的class形式注册
     */
    public Object registerBean(String name, Class<?> cls) {
        String className = cls.getName();
        Object bean = null;

        try {
            bean = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        beans.put(className, bean);
        beanKeys.put(name, bean);
        return bean;
    }

    /**
     * 注册一个带名称的Bean到容器中
     */
    public Object registerBean(String name, Object bean) {
        String className = bean.getClass().getName();
        beans.put(className, bean);
        beanKeys.put(name, bean);
        return bean;
    }

    /**
     * 通过 Class 对象获取bean
     */
    public <T> T getBean(Class<?> cls) {
        String className = cls.getName();
        Object object = beans.get(className);
        if (null != object) {
            return (T) object;
        }
        return null;
    }

    /**
     * 通过对象名获取 bean
     */
    public <T> T getBeanByName(String name) {
        Object object = beanKeys.get(name);;
        if (null != object) {
            return (T) object;
        }
        return null;
    }

    /**
     * 初始化
     */
    public void init() {
        Iterator<Map.Entry<String, Object>> it = beans.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            Object object = entry.getValue();
            injection(object);
        }
    }

    /**
     * 注入
     */
    public void injection(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        try {

            //遍历所有属性寻找@Autowired注解
            for (Field field : fields) {
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (null != autowired) {

                    // 要注入的字段
                    Object autoWritedField = null;
                    String name = autowired.name();

                    if (!name.equals("")) {
                        Object bean = beanKeys.get(name);
                        if (null != bean ) {
                            autoWritedField = bean;
                        }


                        if (null == autoWritedField) {
                            throw new RuntimeException("Unable to autoWrited " + name);
                        }
                    } else {
                        if (autowired.value() == Class.class) {
                            //该属性的Type
                            autoWritedField = recursiveAssembly(field.getType());
                        } else {
                            // 指定装配的类
                            autoWritedField = this.getBean(autowired.value());
                            if (null == autoWritedField) {
                                autoWritedField = recursiveAssembly(autowired.value());
                            }
                        }
                    }

                    if (null == autoWritedField) {
                        throw new RuntimeException("Unable to autoWrited " + field.getType().getCanonicalName());
                    }

                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(object, autoWritedField);
                    field.setAccessible(accessible);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修复没有指定注解及默认注入的情况
     */
    private Object recursiveAssembly(Class<?> cls) {
        if (null != cls) {
            return this.registerBean(cls);
        }
        return null;
    }


}
