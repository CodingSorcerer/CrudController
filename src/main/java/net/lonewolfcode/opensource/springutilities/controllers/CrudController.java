package net.lonewolfcode.opensource.springutilities.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.errors.NotFoundException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CrudController {
    private ObjectMapper mapper;
    private Map<String, CrudRepository> repositories;

    public CrudController(ListableBeanFactory beanLister) {
        mapper = new ObjectMapper();
        repositories = new HashMap<>();

        Map<String,Object> beans = beanLister.getBeansWithAnnotation(CrudRepo.class);
        for (Map.Entry<String,Object> bean:beans.entrySet()){
            String base = bean.getValue().getClass().getAnnotation(CrudRepo.class).basePathName();
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

    @PostMapping("/{repositoryName}")
    public void doPost(@PathVariable String repositoryName, @RequestBody String rawJson) throws IOException {
        CrudRepository repo = repositories.get(repositoryName);
        Class entityClass = repo.getClass().getAnnotation(CrudRepo.class).entityClass();
        Object entity = mapper.readValue(rawJson,entityClass);
        repo.save(entity);
    }
}
