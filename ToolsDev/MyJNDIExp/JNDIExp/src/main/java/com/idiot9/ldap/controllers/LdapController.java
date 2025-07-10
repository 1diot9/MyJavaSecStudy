package com.idiot9.ldap.controllers;

import com.idiot9.ldap.exceptions.IncorrectParamsException;
import com.idiot9.ldap.exceptions.UnSupportedActionTypeException;
import com.idiot9.ldap.exceptions.UnSupportedGadgetTypeException;
import com.idiot9.ldap.exceptions.UnSupportedPayloadTypeException;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;

public interface LdapController {
    void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception;
    void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException, UnSupportedGadgetTypeException, UnSupportedActionTypeException;
}
