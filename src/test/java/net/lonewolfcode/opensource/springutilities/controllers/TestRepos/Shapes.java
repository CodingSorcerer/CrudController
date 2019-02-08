package net.lonewolfcode.opensource.springutilities.controllers.TestRepos;

import net.lonewolfcode.opensource.springutilities.annotations.CrudRepo;
import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.ShapeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@CrudRepo(entityClass = ShapeEntity.class)
public interface Shapes extends CrudRepository<ShapeEntity,String> {
}
