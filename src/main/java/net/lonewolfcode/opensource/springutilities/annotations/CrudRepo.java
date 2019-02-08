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
}
