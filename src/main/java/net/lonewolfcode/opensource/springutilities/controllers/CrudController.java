package net.lonewolfcode.opensource.springutilities.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.errors.NotFoundException;
import net.lonewolfcode.opensource.springutilities.services.AnnotationService;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * The Generic Crud controller class. This class is the meat of this project and does all the magic.
 *
 * @author Rick Marczak
 */
@RestController
public class CrudController {
    private ObjectMapper mapper;
    private Map<String, CrudRepository> repositories;

    /**
     * This constructor gets all classes marked with @CrudRepo and loads them into a map for use later.
     * This also sets up our jaxson ObjectMapper.
     *
     * @see ObjectMapper
     * @see CrudRepo
     * @param beanLister This parameter is injected by spring and helps us search for annotations. It is
     *                   only used in this constructor and not stored.
     */
    public CrudController(ListableBeanFactory beanLister) {
        mapper = new ObjectMapper();
        repositories = new HashMap<>();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        Map<String,Object> beans = beanLister.getBeansWithAnnotation(CrudRepo.class);
        for (Map.Entry<String,Object> bean:beans.entrySet()){
            String base = AnnotationService.getCrudRepository(bean.getValue().getClass()).basePathName();
            repositories.put(base.isEmpty()?bean.getKey():base,(CrudRepository)bean.getValue());
        }

    }

    /**
     * This gets all instances of the entity that the annotated repository controls and returns them in a
     * list.
     * @param repositoryName this path variable is your set basePathName or the name of the repository class
     *                       if now basePathName was provided.
     * @return A list of all entities controlled by the repository invoked
     * @throws NotFoundException if the path you enter refers to a nonexistent repository or if your CrudRepo
     *                           annotation is set to disallow this function, this error is thrown.
     */
    @GetMapping("/{repositoryName}")
    public List<Object> doGetAll(@PathVariable String repositoryName) throws NotFoundException {
        ArrayList<Object> output = new ArrayList<>();

        if (!repositories.containsKey(repositoryName)) throw new NotFoundException(repositoryName);
        if (!AnnotationService.getCrudRepository(repositories.get(repositoryName).getClass()).allowGetAll())
            throw new NotFoundException();

        repositories.get(repositoryName).findAll().forEach(output::add);
        return output;
    }

    /**
     * This gets a specific entity from the repository.<br>
     * <b>WARNING:</b> As of this moment only entities with a single id field that is a string are supported.
     *                 More functionality will be implemented later.
     * @param repositoryName this path variable is your set basePathName or the name of the repository class
     *                       if now basePathName was provided.
     * @param id the id of the object you wish to find
     * @return the specified entity
     * @throws NotFoundException if the path you enter refers to a nonexistent repository or if your CrudRepo
     *                           annotation is set to disallow this function, this error is thrown.
     */
    @GetMapping("/{repositoryName}/{id}")
    public Object doGetStringId(@PathVariable String repositoryName,@PathVariable String id) throws NotFoundException {
        Object output = getStringId(repositoryName,id);
        if (!AnnotationService.getCrudRepository(repositories.get(repositoryName).getClass()).allowGetById())
            throw new NotFoundException();
        return output;
    }

    /**
     * This posts either a single entity or a list of entities to the specified repository.
     * @param repositoryName this path variable is your set basePathName or the name of the repository class
     *                       if now basePathName was provided.
     * @param rawJson the raw json string from the request body.
     * @throws IOException This is thrown when the request body json has an invalid format.
     * @throws NotFoundException if the path you enter refers to a nonexistent repository or if your CrudRepo
     *                           annotation is set to disallow this function, this error is thrown.
     */
    @PostMapping("/{repositoryName}")
    public void doPost(@PathVariable String repositoryName, @RequestBody String rawJson) throws IOException, NotFoundException {
        CrudRepository repo = repositories.get(repositoryName);
        if (repo == null) throw new NotFoundException();

        CrudRepo annotation = AnnotationService.getCrudRepository(repo.getClass());
        if (!annotation.allowPost()) throw new NotFoundException();

        Class entityClass = annotation.entityClass();
        repo.saveAll(mapper.readValue(rawJson,mapper.getTypeFactory().constructCollectionType(List.class,entityClass)));
    }

    /**
     * This deletes a specific entity from the repository.<br>
     * <b>WARNING:</b> As of this moment only entities with a single id field that is a string are supported.
     *                 More functionality will be implemented later.
     * @param repositoryName this path variable is your set basePathName or the name of the repository class
     *                       if now basePathName was provided.
     * @param id the id of the object you wish to delete
     * @throws NotFoundException if the path you enter refers to a nonexistent repository or if your CrudRepo
     *                           annotation is set to disallow this function, this error is thrown.
     */
    @DeleteMapping("/{repositoryName}/{id}")
    public void doDeleteStringId(@PathVariable String repositoryName,@PathVariable String id) throws NotFoundException {
        getStringId(repositoryName,id);
        if (!AnnotationService.getCrudRepository(repositories.get(repositoryName).getClass()).allowDelete())
            throw new NotFoundException();
        repositories.get(repositoryName).deleteById(id);
    }

    private Object getStringId(String repositoryName,String id) throws NotFoundException {
        if(!repositories.containsKey(repositoryName)) throw new NotFoundException();
        Optional<Object> entity = repositories.get(repositoryName).findById(id);
        if(!entity.isPresent()) throw new NotFoundException();
        return entity.get();
    }
}
