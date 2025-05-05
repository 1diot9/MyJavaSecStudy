package com.hessian.asServlet;

import com.caucho.hessian.client.HessianProxyFactory;
import com.hessian.pojo.Person;

import java.net.MalformedURLException;
import java.util.Properties;

public class HelloClient {
    public static void main(String[] args) throws MalformedURLException {
        HessianProxyFactory factory = new HessianProxyFactory();
        Hello hello = (Hello) factory.create(Hello.class, "http://127.0.0.1:8081/hello");

        String name = "1diOt9";
        System.out.println("test: " + hello.sayHello(name));


    }
}
