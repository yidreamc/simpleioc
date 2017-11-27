import org.junit.Test;
import org.xmfaly.simpleioc.core.Autowired;
import org.xmfaly.simpleioc.core.Container;

public class TestIoc {

    class A{

        @Autowired(name = "myvalue")
        private Integer value;

        @Autowired(name = "str")
        private String myStr;

        @Autowired(value = String.class)
        private String myStr2 = null;

        @Autowired
        public String myStr3 = null;

        public void show(){
            System.out.println("value: " + value);
            System.out.println("str: " + myStr);
            System.out.println(myStr2 == null);
            System.out.println(myStr3 == null);
        }
    }

    @Test
    public void test(){
        Container c = new Container();
        c.registerBean("a",new A());
        c.registerBean("str","注入成功");
        c.registerBean("myvalue",2333);
        c.init();
        A a = c.getBeanByName("a");
        a.show();
    }

















}
