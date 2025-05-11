package parse;

import com.alibaba.fastjson.JSON;

public class parse {
    public static void main(String[] args) {
        //Person1和Person2可修改，观察发现当properties只有get方法时，其get方法才被触发
        String json = "{\"@type\":\"Person.Person2\", \"name\":\"1diOt9\", \"age\":21, \"properties\":{}}";
        Object parse = JSON.parse(json);
        System.out.println("======================");
        System.out.println(parse.getClass().getName());
    }
}
