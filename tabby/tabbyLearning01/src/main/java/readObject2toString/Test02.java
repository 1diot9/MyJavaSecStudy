package readObject2toString;

import pojo.Student;
import pojo.Tools;

import java.util.Hashtable;

//测试HashTable#reconstitutionPut能不能触发equals
public class Test02 {
    public static void main(String[] args) throws Exception {
        Student student1 = new Student();
        Student student2 = new Student();

        Hashtable<Object, Object> hashtable = new Hashtable<>();
        hashtable.put(student1, "aaa");
        hashtable.put(student2, "bbb");

        byte[] ser = Tools.ser(hashtable);

        Object deser = Tools.deser(ser);

    }
}
