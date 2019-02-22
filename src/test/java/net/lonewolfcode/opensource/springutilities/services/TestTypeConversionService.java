package net.lonewolfcode.opensource.springutilities.services;

import net.lonewolfcode.opensource.springutilities.errors.TypeConversionError;
import net.lonewolfcode.opensource.springutilities.services.testData.NonPrimField;
import net.lonewolfcode.opensource.springutilities.services.testData.TestId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestTypeConversionService {
    private static final int INTFIELD = 123;
    private static final Integer INTEGERFIELD = 123;
    private static final byte BYTEFIELD = 10;
    private static final Byte BYTEOBJFIELD = 10;
    private static final short SHORTFIELD = 10;
    private static final Short SHORTOBJFIELD = 10;
    private static final char CHARFIELD = 'a';
    private static final Character CHARACTERFIELD = 'a';
    private static final float FLOATFIELD = 111.22f;
    private static final Float FLOATOBJFIELD = 111.22f;
    private static final String STRINGFIELD = "this is a string";
    private static final boolean BOOLEANFIELD = false;
    private static final Boolean BOOLEANOBJFIELD = false;
    private static final long LONGFIELD = 6789;
    private static final Long LONGOBJFIELD = 6789l;
    private static final double DOUBLFIELD = 123.1;
    private static final Double DOUBLEOBJFIELD = 123.1;
    private static final NonPrimField NONPRIMFIELD = new NonPrimField("test");

    @Test
    public void convertToFieldTypePrimitiveSuccess() throws IllegalAccessException, TypeConversionError {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field:fields) {
            Object value = field.get(this);
            Object actual = TypeConversionService.convertToFieldType(value.toString(),field);
            Assert.assertEquals(value,actual);
        }
    }

    @Test(expected = TypeConversionError.class)
    public void convertToFieldTypePrimitiveFail() throws TypeConversionError, NoSuchFieldException {
        try {
            TypeConversionService.convertToFieldType("this is not an int",this.getClass().getDeclaredField("INTEGERFIELD"));
        }catch (TypeConversionError e) {
            Assert.assertEquals("Error converting string \"this is not an int\" to type \"java.lang.Integer\"",e.getMessage());
            throw e;
        }
    }

    @Test
    public void convertMapToObjectSuccess() throws TypeConversionError {
        Map<String,String> params = new HashMap<>();
        params.put("key1","info");
        params.put("key2","123");
        params.put("key3","true");
        TestId expected = new TestId("info",123,true);

        Object actual = TypeConversionService.convertMapToObject(params, TestId.class);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void convertMapToObjectFieldsMissing() throws TypeConversionError {
        Map<String,String> params = new HashMap<>();
        params.put("key1","info");
        TestId expected = new TestId("info",0,false);

        Object actual = TypeConversionService.convertMapToObject(params, TestId.class);
        Assert.assertEquals(expected,actual);
    }

    @Test(expected = TypeConversionError.class)
    public void convertMapToObjectBadType() throws TypeConversionError {
        Map<String,String> params = new HashMap<>();
        params.put("key2","this cant be parsed to int. oopse!");

        TypeConversionService.convertMapToObject(params,TestId.class);
    }
}
