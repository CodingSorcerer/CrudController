package net.lonewolfcode.opensource.springutilities.annotations;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CrudRepo {
    @NotNull
    Class entityClass();
    String basePathName() default "";
    boolean allowGetAll() default true;
    boolean allowPost() default true;
    boolean allowDelete() default true;
    boolean allowGetById() default true;
}
