package com.hessian.asServlet;

import com.hessian.pojo.Person;

public interface Hello {
    public String sayHello(String name);

    public Person changeAge(Person person, int age);
}
