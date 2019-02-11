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

@RestController
public class CrudController {
    private ObjectMapper mapper;
    private Map<String, CrudRepository> repositories;

    public CrudController(ListableBeanFactory beanLister) {
        mapper = new ObjectMapper();
        repositories = new HashMap<>();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        Map<String,Object> beans = beanLister.getBeansWithAnnotation(CrudRepo.class);
        for (Map.Entry<String,Object> bean:beans.entrySet()){
            String base = ((CrudRepo)AnnotationService.getAnnotationFromClass(bean.getValue().getClass(),CrudRepo.class)).basePathName();
            repositories.put(base.isEmpty()?bean.getKey():base,(CrudRepository)bean.getValue());
        }

    }

    @GetMapping("/{repositoryName}")
    public List<Object> doGetAll(@PathVariable String repositoryName) throws NotFoundException {
        ArrayList<Object> output = new ArrayList<>();

        if (!repositories.containsKey(repositoryName)) throw new NotFoundException(repositoryName);

        repositories.get(repositoryName).findAll().forEach(output::add);
        return output;
    }

    @GetMapping("/{repositoryName}/{id}")
    public Object doGetStringId(@PathVariable String repositoryName,@PathVariable String id) throws NotFoundException {
        if(!repositories.containsKey(repositoryName)) throw new NotFoundException();
        Optional<Object> entity = repositories.get(repositoryName).findById(id);
        if(!entity.isPresent()) throw new NotFoundException();
        return entity.get();
    }

    @PostMapping("/{repositoryName}")
    public void doPost(@PathVariable String repositoryName, @RequestBody String rawJson) throws IOException, NotFoundException {
        CrudRepository repo = repositories.get(repositoryName);
        if (repo == null) throw new NotFoundException();

        Class entityClass = ((CrudRepo)AnnotationService.getAnnotationFromClass(repo.getClass(),CrudRepo.class)).entityClass();
        repo.saveAll(mapper.readValue(rawJson,mapper.getTypeFactory().constructCollectionType(List.class,entityClass)));
    }

    @DeleteMapping("/{repositoryName}/{id}")
    public void doDeleteStringId(@PathVariable String repositoryName,@PathVariable String id) throws NotFoundException {
        doGetStringId(repositoryName,id);
        repositories.get(repositoryName).deleteById(id);
    }
}
