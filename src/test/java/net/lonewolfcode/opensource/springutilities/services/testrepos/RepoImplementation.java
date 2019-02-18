package net.lonewolfcode.opensource.springutilities.services.testrepos;

import net.lonewolfcode.opensource.springutilities.controllers.TestEntities.PersonEntity;
import net.lonewolfcode.opensource.springutilities.controllers.TestRepos.TestRepo;

import java.util.Optional;

public class RepoImplementation implements TestRepo {
    @Override
    public <S extends PersonEntity> S save(S entity)
    {
        return null;
    }

    @Override
    public <S extends PersonEntity> Iterable<S> saveAll(Iterable<S> entities)
    {
        return null;
    }

    @Override
    public Optional<PersonEntity> findById(Integer s)
    {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer s)
    {
        return false;
    }

    @Override
    public Iterable<PersonEntity> findAll()
    {
        return null;
    }

    @Override
    public Iterable<PersonEntity> findAllById(Iterable<Integer> strings)
    {
        return null;
    }

    @Override
    public long count()
    {
        return 0;
    }

    @Override
    public void deleteById(Integer s)
    {

    }

    @Override
    public void delete(PersonEntity entity)
    {

    }

    @Override
    public void deleteAll(Iterable<? extends PersonEntity> entities)
    {

    }

    @Override
    public void deleteAll()
    {

    }
}
