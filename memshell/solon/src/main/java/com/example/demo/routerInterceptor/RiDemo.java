package com.example.demo.routerInterceptor;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.core.route.PathRule;
import org.noear.solon.core.route.RouterInterceptor;
import org.noear.solon.core.route.RouterInterceptorChain;

public class RiDemo implements RouterInterceptor {
    @Override
    public PathRule pathPatterns() {
//        return new PathRule().include("/hello"); //限定路径，可以为return null;即作用于全路径
        return null;
    }

    @Override
    public void doIntercept(Context ctx, Handler mainHandler, RouterInterceptorChain chain) throws Throwable {
        System.out.println("RiDemo");
        chain.doIntercept(ctx, mainHandler);
    }
}
