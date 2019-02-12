package net.lonewolfcode.opensource.springutilities.controllers;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.PersonEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.ShapeEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestRepos.DeniedRepo;
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

import java.io.IOException;
import java.util.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCrudController {
    @Mock
    private ListableBeanFactory beanLister;
    @Mock
    private Shapes shapes;
    @Mock
    private TestRepo testRepo;
    @Mock
    private DeniedRepo deniedRepo;

    private static final String TESTREPO1 = "shapes";
    private static final String TESTREPO2 = "people";
    private static final String UNUSEDNAME = "testrepo";
    private static final String DENIED = "denied";
    private static final ShapeEntity RECTANGLE = new ShapeEntity("rectangle");
    private static final PersonEntity KRYSTAL = new PersonEntity("123","Krystal");
    private CrudController testController;
    private ArrayList<ShapeEntity> expectedShapes;
    private ArrayList<PersonEntity> expectedPersons;

    @Before
    public void setup(){
        expectedShapes = new ArrayList<>();
        expectedShapes.add(RECTANGLE);
        expectedShapes.add(new ShapeEntity("circle"));

        expectedPersons = new ArrayList<>();
        expectedPersons.add(KRYSTAL);

        ArrayList<Object> deniedObjects = new ArrayList<>();
        deniedObjects.add(DENIED);

        Mockito.when(shapes.findAll()).thenReturn(expectedShapes);
        Mockito.when(shapes.saveAll(Mockito.any())).thenReturn(null);
        Mockito.when(shapes.findById(RECTANGLE.getName())).thenReturn(Optional.of(RECTANGLE));
        Mockito.when(testRepo.findAll()).thenReturn(expectedPersons);
        Mockito.when(testRepo.findById(KRYSTAL.getId())).thenReturn(Optional.of(KRYSTAL));
        Mockito.when(deniedRepo.findById(DENIED)).thenReturn(Optional.of(DENIED));

        Map<String,Object> CrudObjects = new HashMap<>();
        CrudObjects.put(TESTREPO1, shapes);
        CrudObjects.put(UNUSEDNAME,testRepo);
        CrudObjects.put(DENIED,deniedRepo);

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
        ArrayList<ShapeEntity> expected = new ArrayList<>();
        expected.add(new ShapeEntity("triangle"));
        Mockito.verify(shapes).saveAll(expected);
    }

    @Test
    public void doPostListSuccess() throws Exception {
        testController.doPost(TESTREPO1, "[{\"name\":\"rectangle\"},{\"name\":\"circle\"}]");
        Mockito.verify(shapes).saveAll(expectedShapes);
    }

    @Test(expected = NotFoundException.class)
    public void doPostNotFound() throws Exception {
        testController.doPost(UNUSEDNAME,"");
    }

    @Test(expected = IOException.class)
    public void doPostBadBody() throws Exception {
        testController.doPost(TESTREPO1,"");
    }

    @Test
    public void doGetStringIdDefault() throws NotFoundException {
        Object actual = testController.doGetStringId(TESTREPO1,RECTANGLE.getName());
        Assert.assertEquals(RECTANGLE,actual);
    }

    @Test
    public void doGetStringIdSpecifiedPathName() throws NotFoundException {
        Object actual = testController.doGetStringId(TESTREPO2,KRYSTAL.getId());
        Assert.assertEquals(KRYSTAL,actual);
    }

    @Test(expected = NotFoundException.class)
    public void doGetStringIdNonexistantRepo() throws NotFoundException {
        testController.doGetStringId(UNUSEDNAME,KRYSTAL.getId());
    }

    @Test(expected = NotFoundException.class)
    public void doGetStringIdNonExistantId() throws NotFoundException {
        testController.doGetStringId(TESTREPO1,"triangle");
    }


    @Test
    public void doDeleteStringIdDefault() throws NotFoundException {
        testController.doDeleteStringId(TESTREPO1,RECTANGLE.getName());
        Mockito.verify(shapes).deleteById(RECTANGLE.getName());
    }

    @Test
    public void doDeleteStringIdSpecifiedPathName() throws NotFoundException {
        testController.doDeleteStringId(TESTREPO2,KRYSTAL.getId());
        Mockito.verify(testRepo).deleteById(KRYSTAL.getId());
    }

    @Test(expected = NotFoundException.class)
    public void doDeleteStringIdNonexistantRepo() throws NotFoundException {
        testController.doDeleteStringId(UNUSEDNAME,KRYSTAL.getId());
    }

    @Test(expected = NotFoundException.class)
    public void doDeleteStringIdNonExistantId() throws NotFoundException {
        testController.doDeleteStringId(TESTREPO1,"triangle");
    }

    @Test(expected = NotFoundException.class)
    public void doDeleteStringIdDenied() throws NotFoundException {
        testController.doDeleteStringId(DENIED,DENIED);
    }

    @Test(expected = NotFoundException.class)
    public void doPostDenied() throws NotFoundException,IOException {
        testController.doPost(DENIED,DENIED);
    }

    @Test(expected = NotFoundException.class)
    public void doGetAllDenied() throws NotFoundException{
        testController.doGetAll(DENIED);
    }

    @Test(expected = NotFoundException.class)
    public void doGetStringIdDenied() throws NotFoundException{
        testController.doGetStringId(DENIED,DENIED);
    }
}
