package com.hessian.asServlet;

import com.caucho.hessian.server.HessianServlet;
import com.hessian.pojo.Person;

public class HelloServer extends HessianServlet implements Hello{
    @Override
    public String sayHello(String name) {
        System.out.println("Hello " + name);
        return "Hello " + name;
    }

    @Override
    public Person changeAge(Person person, int age) {
        person.setAge(age);
        return person;
    }
}
