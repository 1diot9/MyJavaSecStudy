package com.idiot9.ldap.templates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用于给内存马打标签，方便统计有哪些内存马
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemShell {
}
