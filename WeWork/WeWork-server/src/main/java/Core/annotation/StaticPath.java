package Core.annotation;

import java.lang.annotation.*;

/**
 * define the directory path of the static resources
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StaticPath {
    String value() default "";
}
