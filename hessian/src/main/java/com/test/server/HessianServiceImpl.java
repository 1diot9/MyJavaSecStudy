package com.test.server;

import com.caucho.hessian.server.HessianServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "hessian", value = "/hessian")
public class HessianServiceImpl extends HessianServlet implements Service {
    @Override
    public String getCurrentTime() {
        return "test time: 1970/01/01";
    }
}
