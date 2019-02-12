package net.lonewolfcode.opensource.springutilities.controllers.TestRepos;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@CrudRepo(entityClass = Object.class, basePathName = "denied",
        allowDelete = false, allowGetAll = false, allowGetById = false, allowPost = false)
public interface DeniedRepo extends CrudRepository<Object,Object>
{
}
