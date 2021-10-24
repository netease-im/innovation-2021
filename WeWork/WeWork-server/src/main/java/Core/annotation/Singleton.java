package Core.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Singleton {
    //id
    String[]id() default "";

    //propertyListPath
    String property() default "";


}
