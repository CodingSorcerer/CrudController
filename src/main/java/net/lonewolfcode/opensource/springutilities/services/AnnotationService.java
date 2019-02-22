package net.lonewolfcode.opensource.springutilities.services;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;

/**
 * Aids in getting annotation objects from classes and their interfaces. This was created because getting annotated
 * classes from spring dose not always return the class you created, but instead usually an implementation of it.
 *
 * @author Rick Marczak
 */
public class AnnotationService
{
    /**
     * Gets the specified annotation from a class, if that annotation exists on that class or any of it's
     * interfaces.
     * @param annotated the class that supposedly has the annotation you're looking for
     * @param annotationClass the class of the annotation you're looking for
     * @return returns null if no annotation was found, but returns the class object if it is found. Some casting
     *         required to get properties.
     */
    public static Annotation getAnnotationFromClass(Class annotated, Class annotationClass) {
        Annotation output = annotated.getAnnotation(annotationClass);

        if (output== null){
            for(AnnotatedType type:annotated.getAnnotatedInterfaces()) {
                output = ((Class)type.getType()).getAnnotation(annotationClass);
                if (output!=null) break;
            }
        }

        return output;
    }

    /**
     * Specifically fetches a CrudRepo annotation to avoid complicated casting later in my CrudController.
     * @see net.lonewolfcode.opensource.springutilities.controllers.CrudController
     * @see CrudRepo
     * @param annotated A class supposedly annotated with @CrudRepo
     * @return null if it's not found, a CrudRepo annotation if it is found.
     */
    public static CrudRepo getCrudRepository(Class annotated) {
        return (CrudRepo) getAnnotationFromClass(annotated,CrudRepo.class);
    }

    /**
     * Gets the Id annotated field from a JPA Entity object
     * @param annotated the class which we wish to get the id field from
     * @return the field if present, or null if not
     */
    public static Field getIdField(Class annotated) {
        Field output = getAnnotatedField(annotated,Id.class);
        if (output==null) output = getAnnotatedField(annotated, EmbeddedId.class);
        return output;
    }

    /**
     * retries a field from a class, that has the specified annotation.
     * @param annotated the annotated class
     * @param fieldAnnotation the annotation you're looking for
     * @return the field if present, or null if not
     */
    public static Field getAnnotatedField(Class annotated, Class fieldAnnotation){
        Field output = null;
        Field[] fields = annotated.getDeclaredFields();
        for (Field field:fields){
            if (field.getAnnotation(fieldAnnotation)!=null){
                output = field;
                break;
            }
        }
        return output;
    }
}
