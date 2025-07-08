package ysoserial.payloads.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependencies {
    //注解中通过“无形参的方法”来声明成员变量
    //当成员变量只有一个时，可以写成value，这样在使用注解时就不用表明成员变量名
    //default可以为成员变量设置默认值
	String[] value() default {};

	public static class Utils {
		public static String[] getDependencies(AnnotatedElement annotated) {
			Dependencies deps = annotated.getAnnotation(Dependencies.class);
			if (deps != null && deps.value() != null) {
				return deps.value();
			} else {
				return new String[0];
			}
		}

		public static String[] getDependenciesSimple(AnnotatedElement annotated) {
		    String[] deps = getDependencies(annotated);
		    String[] simple = new String[deps.length];
		    for (int i = 0; i < simple.length; i++) {
                simple[i] = deps[i].split(":", 2)[1];
            }
            return simple;
        }
	}
}
