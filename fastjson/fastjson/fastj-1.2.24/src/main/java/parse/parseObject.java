package parse;

import Person.Person1;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class parseObject {
    public static void main(String[] args) {
        String json = "{\"@type\":\"Person.Person1\", \"name\":\"1diOt9\", \"age\":21, \"properties\":{}}";

        //单参parseObject，会把所有getter调一遍
        JSONObject jsonObject = JSON.parseObject(json);
        System.out.println(jsonObject.getClass().getName());
        System.out.println(jsonObject.toJSONString());

        System.out.println("===========================");

        //多参parseObject，getter&setter的调用情况和parse一样
        Person1 person1 = JSON.parseObject(json, Person1.class);

    }
}
