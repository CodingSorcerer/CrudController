package net.lonewolfcode.opensource.springutilities.controllers;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.EmbeddableId;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.EmbededEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.PersonEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.ShapeEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestRepos.DeniedRepo;
import net.lonewolfcode.opensource.springutilities.controllers.TestRepos.EmbededRepo;
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
    @Mock
    private EmbededRepo embededRepo;

    private static final String TESTREPO1 = "shapes";
    private static final String TESTREPO2 = "people";
    private static final String UNUSEDNAME = "testrepo";
    private static final String DENIED = "denied";
    private static final String EMBEDDED = "testid";
    private static final ShapeEntity RECTANGLE = new ShapeEntity("rectangle");
    private static final PersonEntity KRYSTAL = new PersonEntity(123,"Krystal");
    private static final EmbeddableId EMBEDDABLE_ID = new EmbeddableId("abcd",1234);
    private static final EmbededEntity EMBEDED_ENTITY = new EmbededEntity(EMBEDDABLE_ID, "test");
    private CrudController testController;
    private ArrayList<ShapeEntity> expectedShapes;
    private ArrayList<PersonEntity> expectedPersons;
    private ArrayList<EmbededEntity> expectedEntites;

    @Before
    public void setup(){
        expectedShapes = new ArrayList<>();
        expectedShapes.add(RECTANGLE);
        expectedShapes.add(new ShapeEntity("circle"));

        expectedPersons = new ArrayList<>();
        expectedPersons.add(KRYSTAL);

        expectedEntites = new ArrayList<>();
        expectedEntites.add(EMBEDED_ENTITY);

        ArrayList<Object> deniedObjects = new ArrayList<>();
        deniedObjects.add(DENIED);

        Mockito.when(shapes.findAll()).thenReturn(expectedShapes);
        Mockito.when(shapes.saveAll(Mockito.any())).thenReturn(null);
        Mockito.when(shapes.findById(RECTANGLE.getName())).thenReturn(Optional.of(RECTANGLE));
        Mockito.when(testRepo.findAll()).thenReturn(expectedPersons);
        Mockito.when(testRepo.findById(KRYSTAL.getId())).thenReturn(Optional.of(KRYSTAL));
        Mockito.when(embededRepo.findById(EMBEDDABLE_ID)).thenReturn(Optional.of(EMBEDED_ENTITY));

        Map<String,Object> CrudObjects = new HashMap<>();
        CrudObjects.put(TESTREPO1, shapes);
        CrudObjects.put(UNUSEDNAME,testRepo);
        CrudObjects.put(DENIED,deniedRepo);
        CrudObjects.put(EMBEDDED, embededRepo);

        Mockito.when(beanLister.getBeansWithAnnotation(CrudRepo.class)).thenReturn(CrudObjects);

        testController = new CrudController(beanLister);
    }

    @Test
    public void doGetAllDefault() throws Exception {
        Object actual = testController.doGet(TESTREPO1,null);
        Assert.assertEquals(expectedShapes,actual);
    }

    @Test
    public void doGetAllSpecifiedPathName() throws Exception {
        Object actual = testController.doGet(TESTREPO2,null);
        Assert.assertEquals(expectedPersons,actual);
    }

    @Test(expected = NotFoundException.class)
    public void doGetAllNonexistantRepo() throws Exception {
        testController.doGet(UNUSEDNAME,null);
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
    public void doGetByIdDefault() throws Exception {
        Object actual = testController.doGetById(TESTREPO1,RECTANGLE.getName());
        Assert.assertEquals(RECTANGLE,actual);
    }

    @Test
    public void doGetByIdSpecifiedPathName() throws Exception {
        Object actual = testController.doGetById(TESTREPO2,KRYSTAL.getId().toString());
        Assert.assertEquals(KRYSTAL,actual);
    }

    @Test(expected = NotFoundException.class)
    public void doGetByIdNonexistantRepo() throws Exception {
        testController.doGetById(UNUSEDNAME,KRYSTAL.getId().toString());
    }

    @Test(expected = NotFoundException.class)
    public void doGetByIdNonExistantId() throws Exception {
        testController.doGetById(TESTREPO1,"triangle");
    }


    @Test
    public void doDeleteByIdDefault() throws Exception {
        testController.doDeleteById(TESTREPO1,RECTANGLE.getName());
        Mockito.verify(shapes).deleteById(RECTANGLE.getName());
    }

    @Test
    public void doDeleteByIdSpecifiedPathName() throws Exception {
        testController.doDeleteById(TESTREPO2,KRYSTAL.getId().toString());
        Mockito.verify(testRepo).deleteById(KRYSTAL.getId());
    }

    @Test(expected = NotFoundException.class)
    public void doDeleteByIdNonexistantRepo() throws Exception {
        testController.doDeleteById(UNUSEDNAME,KRYSTAL.getId().toString());
    }

    @Test(expected = NotFoundException.class)
    public void doDeleteByIdNonExistantId() throws Exception {
        testController.doDeleteById(TESTREPO1,"triangle");
    }

    @Test(expected = NotFoundException.class)
    public void doDeleteByIdDenied() throws Exception {
        testController.doDeleteById(DENIED,KRYSTAL.getId().toString());
    }

    @Test(expected = NotFoundException.class)
    public void doPostDenied() throws NotFoundException,IOException {
        testController.doPost(DENIED,KRYSTAL.getId().toString());
    }

    @Test(expected = NotFoundException.class)
    public void doGetAllDenied() throws Exception{
        testController.doGet(DENIED,null);
    }

    @Test(expected = NotFoundException.class)
    public void doGetByIdDenied() throws Exception{
        testController.doGetById(DENIED,KRYSTAL.getId().toString());
    }

    @Test
    public void doGetSuccessOneOff() throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("key1",EMBEDDABLE_ID.getKey1());
        params.put("key2", EMBEDDABLE_ID.getKey2().toString());
        Object actual = testController.doGet(EMBEDDED, params);

        Mockito.verify(embededRepo).findById(EMBEDDABLE_ID);
        Assert.assertEquals(EMBEDED_ENTITY,actual);
    }

    @Test
    public void doGetSuccessAll() throws Exception {
        Object actual = testController.doGet(TESTREPO1,null);
        Mockito.verify(shapes).findAll();
        Assert.assertEquals(expectedShapes,actual);
    }

    @Test(expected = NotFoundException.class)
    public void doGetWrongParams() throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("key1","hi!!!!!");
        params.put("key2", EMBEDDABLE_ID.getKey2().toString());
        testController.doGet(EMBEDDED,params);
    }

    @Test
    public void doGetIdQuery() throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("id",RECTANGLE.getName());
        Object actual = testController.doGet(TESTREPO1,params);
        Assert.assertEquals(RECTANGLE,actual);
    }
}
