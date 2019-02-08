package net.lonewolfcode.opensource.springutilities.controllers;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.PersonEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.ShapeEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestRepos.Shapes;
import net.lonewolfcode.opensource.springutilities.controllers.TestRepos.TestRepo;
import net.lonewolfcode.opensource.springutilities.errors.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCrudController {
    @Mock
    private ListableBeanFactory beanLister;
    @Mock
    private Shapes shapes;
    @Mock
    private TestRepo testRepo;

    private static final String TESTREPO1 = "shapes";
    private static final String TESTREPO2 = "people";
    private static final String UNUSEDNAME = "testrepo";
    private CrudController testController;
    private ArrayList<ShapeEntity> expectedShapes;
    private ArrayList<PersonEntity> expectedPersons;

    @Before
    public void setup(){
        expectedShapes = new ArrayList<>();
        expectedShapes.add(new ShapeEntity("rectangle"));
        expectedShapes.add(new ShapeEntity("circle"));

        expectedPersons = new ArrayList<>();
        expectedPersons.add(new PersonEntity("123","Krystal"));

        Mockito.when(shapes.findAll()).thenReturn(expectedShapes);
        Mockito.when(shapes.save(Mockito.any())).thenReturn(null);
        Mockito.when(testRepo.findAll()).thenReturn(expectedPersons);

        Map<String,Object> CrudObjects = new HashMap<>();
        CrudObjects.put(TESTREPO1, shapes);
        CrudObjects.put(UNUSEDNAME,testRepo);

        Mockito.when(beanLister.getBeansWithAnnotation(CrudRepo.class)).thenReturn(CrudObjects);

        testController = new CrudController(beanLister);
    }

    @Test
    public void doGetAllDefault() throws NotFoundException {
        List<Object> actual = testController.doGetAll(TESTREPO1);
        Assert.assertEquals(expectedShapes,actual);
    }

    @Test
    public void doGetAllSpecifiedPathName() throws NotFoundException {
        List<Object> actual = testController.doGetAll(TESTREPO2);
        Assert.assertEquals(expectedPersons,actual);
    }

    @Test(expected = NotFoundException.class)
    public void doGetAllNonexistantRepo() throws NotFoundException {
        testController.doGetAll(UNUSEDNAME);
    }

    @Test
    public void doPostSuccess() throws Exception {
        testController.doPost(TESTREPO1, "{\"name\":\"triangle\"}");
        Mockito.verify(shapes).save(new ShapeEntity("triangle"));
    }
}
