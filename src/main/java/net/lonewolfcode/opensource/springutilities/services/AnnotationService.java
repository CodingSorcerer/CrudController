package net.lonewolfcode.opensource.springutilities.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

public class AnnotationService
{
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
}
