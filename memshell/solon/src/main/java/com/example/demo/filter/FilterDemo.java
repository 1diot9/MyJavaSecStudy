package com.example.demo.filter;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;

@Component(index = 0)
public class FilterDemo implements Filter {

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        System.out.println(ctx.path());
        chain.doFilter(ctx);
    }
}
