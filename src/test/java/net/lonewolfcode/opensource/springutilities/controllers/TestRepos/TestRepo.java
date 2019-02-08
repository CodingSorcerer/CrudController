package net.lonewolfcode.opensource.springutilities.controllers.TestRepos;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@CrudRepo(entityClass = PersonEntity.class, basePathName = "people")
public interface TestRepo extends CrudRepository<PersonEntity,String> {
}
