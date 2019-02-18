package net.lonewolfcode.opensource.springutilities.annotations;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation tells the crud controller how to behave. This annotation also denotes what repositories
 * that the crud controller reads from
 * @author Rick Marczak
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CrudRepo {

    /**
     * This parameter cannot be null and contains the entity class for the JPA repository this tag is on.
     * @return the entity class
     */
    @NotNull
    Class entityClass();

    /**
     * This parameter sets the base path. for example, if this was set to "Dog" your endpoint for
     * this part of the API would be localhost:8080/Dog. Please note that if this is left null then
     * the name of the class will be used as the base path.
     * @return a string representing the desired base path
     */
    String basePathName() default "";

    /**
     * This parameter tells the CrudController weather or not the Get All function should be performed on this
     * repository. this defaults to true.
     * @see net.lonewolfcode.opensource.springutilities.controllers.CrudController#doGetAll(String)
     * @return a boolean representing this permission
     */
    boolean allowGetAll() default true;

    /**
     * This parameter tells the CrudController weather or not the Post function should be performed on this
     * repository. this defaults to true.
     * @see net.lonewolfcode.opensource.springutilities.controllers.CrudController#doPost(String, String)
     * @return a boolean representing this permission
     */
    boolean allowPost() default true;

    /**
     * This parameter tells the CrudController weather or not the Delete function should be performed on this
     * repository. this defaults to true.
     * @see net.lonewolfcode.opensource.springutilities.controllers.CrudController#doDeleteById(String, String)
     * @return a boolean representing this permission
     */
    boolean allowDelete() default true;

    /**
     * This parameter tells the CrudController weather or not the Get single object function should be performed
     * on this repository. this defaults to true.
     * @see net.lonewolfcode.opensource.springutilities.controllers.CrudController#doGetById(String, String)
     * @return a boolean representing this permission
     */
    boolean allowGetById() default true;
}
