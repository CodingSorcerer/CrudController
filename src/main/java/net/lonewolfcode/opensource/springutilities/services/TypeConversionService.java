package net.lonewolfcode.opensource.springutilities.services;

import net.lonewolfcode.opensource.springutilities.errors.TypeConversionError;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is made to convert different types of objects to other types of objects.
 *
 * @author Rick Marczak
 */
public class TypeConversionService {
    public final static Map<Class<?>, Class<?>> WRAPPER_MAP = new HashMap<>();
    static {
        WRAPPER_MAP.put(boolean.class, Boolean.class);
        WRAPPER_MAP.put(byte.class, Byte.class);
        WRAPPER_MAP.put(short.class, Short.class);
        WRAPPER_MAP.put(char.class, Character.class);
        WRAPPER_MAP.put(int.class, Integer.class);
        WRAPPER_MAP.put(long.class, Long.class);
        WRAPPER_MAP.put(float.class, Float.class);
        WRAPPER_MAP.put(double.class, Double.class);
    }

    /**
     * This converts a string to a primitive type or an object type that accepts a String constructor. This function is mainly
     * used to convert a string into a JPA Entity's id field type.
     * @param input the input string to convert
     * @param target the Id field of the Entity
     * @return an object of the same type as the Id field that's represented by the input string
     * @throws TypeConversionError throws this error if the given string cannot be converted to the field's type
     */
    public static Object convertToFieldType (String input, Field target) throws TypeConversionError {
        Object output;
        Class targetClass = target.getType();

        try{
            if (targetClass.isPrimitive()){
                targetClass = WRAPPER_MAP.get(targetClass);
            }
            if(targetClass==Character.class&&input.length()==1) {
                output = Character.valueOf(input.charAt(0));
            } else {
                output = targetClass.getDeclaredConstructor(String.class).newInstance(input);
            }
        }catch(Exception e){
            throw new TypeConversionError(input,targetClass.getName());
        }

        return output;
    }
}
