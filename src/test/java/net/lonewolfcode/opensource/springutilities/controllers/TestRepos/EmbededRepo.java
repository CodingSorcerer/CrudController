package net.lonewolfcode.opensource.springutilities.controllers.TestRepos;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.EmbeddableId;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.EmbededEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@CrudRepo(entityClass = EmbededEntity.class, basePathName = "testid")
public interface EmbededRepo extends CrudRepository<EmbededEntity, EmbeddableId> {
}
