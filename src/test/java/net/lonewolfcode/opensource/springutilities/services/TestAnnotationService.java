package net.lonewolfcode.opensource.springutilities.services;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.PersonEntity;
import net.lonewolfcode.opensource.springutilities.services.testrepos.RepoImplementation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Entity;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@SpringBootTest
public class TestAnnotationService
{
    @Test
    public void getAnnotationFromClassDefault() {
        Annotation entityTag = AnnotationService.getAnnotationFromClass(PersonEntity.class, Entity.class);
        Assert.assertNotNull(entityTag);
        Assert.assertTrue(entityTag instanceof Entity);
    }

    @Test
    public void getAnnotationFromClassOnInterface() {
        Annotation crudRepo = AnnotationService.getAnnotationFromClass(RepoImplementation.class, CrudRepo.class);
        Assert.assertNotNull(crudRepo);
        Assert.assertTrue(crudRepo instanceof CrudRepo);
        Assert.assertEquals("people",((CrudRepo)crudRepo).basePathName());
    }

    @Test
    public void getIdFieldSuccess() throws NoSuchFieldException{
        Field actual = AnnotationService.getIdField(PersonEntity.class);
        Field expected = PersonEntity.class.getDeclaredField("id");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getIdFieldNotFound(){
        Assert.assertNull(AnnotationService.getIdField(RepoImplementation.class));
    }
}
