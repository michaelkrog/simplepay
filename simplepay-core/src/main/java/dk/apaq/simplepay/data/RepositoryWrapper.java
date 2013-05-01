
package dk.apaq.simplepay.data;

import java.io.Serializable;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.Repository;
import org.springframework.data.domain.Persistable;

/**
 * Javadoc
 */
public class RepositoryWrapper<BEANTYPE extends Persistable<ID>, ID extends Serializable> implements Repository<BEANTYPE, ID> {

    private final Repository<BEANTYPE, ID> repository;

    public RepositoryWrapper(Repository<BEANTYPE, ID> repository) {
        this.repository = repository;
    }

    @Override
    public BEANTYPE findOne(Criteria criteria) {
        return repository.findOne(criteria);
    }

    @Override
    public Iterable<BEANTYPE> findAll(Criteria criteria) {
        return repository.findAll(criteria);
    }

    @Override
    public long count(Criteria criteria) {
        return repository.count(criteria);
    }

    @Override
    public <S extends BEANTYPE> S save(S s) {
        return repository.save(s);
    }

    @Override
    public <S extends BEANTYPE> Iterable<S> save(Iterable<S> itrbl) {
        return repository.save(itrbl);
    }

    @Override
    public BEANTYPE findOne(ID id) {
        return repository.findOne(id);
    }

    @Override
    public boolean exists(ID id) {
        return repository.exists(id);
    }

    @Override
    public Iterable<BEANTYPE> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<BEANTYPE> findAll(Iterable<ID> itrbl) {
        return repository.findAll(itrbl);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void delete(ID id) {
        repository.delete(id);
    }

    @Override
    public void delete(BEANTYPE t) {
        repository.delete(t);
    }

    @Override
    public void delete(Iterable<? extends BEANTYPE> itrbl) {
        repository.delete(itrbl);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
    
    
}
