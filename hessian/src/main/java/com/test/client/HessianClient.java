package com.test.client;

import com.caucho.hessian.client.HessianProxyFactory;
import com.test.server.Service;

import java.net.MalformedURLException;

public class HessianClient {
    public static void main(String[] args) throws MalformedURLException {
        String url="http://localhost:8080/MyTomcat/hessian";
        HessianProxyFactory factory=new HessianProxyFactory();
        Service service=(Service) factory.create(Service.class, url);
        System.out.println(service.getCurrentTime());
    }
}
