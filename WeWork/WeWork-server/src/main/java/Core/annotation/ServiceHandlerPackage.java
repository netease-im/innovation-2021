package Core.annotation;

import java.lang.annotation.*;

/**
 * define the package of the service handlers
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceHandlerPackage {
    String[] value() default {};
}
