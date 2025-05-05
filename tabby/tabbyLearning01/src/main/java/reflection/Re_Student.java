package reflection;

import pojo.Student;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Re_Student {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Student maxx = new Student(1, "maxx", 18);
        Class<? extends Student> aClass = maxx.getClass();
        Method getName = aClass.getDeclaredMethod("getName");
        getName.setAccessible(true);
        Object name = getName.invoke(maxx);
        System.out.println(name);
    }
}
