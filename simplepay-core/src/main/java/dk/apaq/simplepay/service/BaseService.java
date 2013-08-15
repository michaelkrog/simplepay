package dk.apaq.simplepay.service;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Assert;

/**
 * Abstract class for basic services using a
 * <code>PageableQueryDslCrudRepository</code>.
 */
public abstract class BaseService<BT extends Persistable, RepositoryType extends PagingAndSortingRepository<BT, String>> {

    protected RepositoryType repository;

    @Autowired
    public void setRepository(RepositoryType repository) {
        this.repository = repository;
    }

    public Iterable<BT> findAll() {
        return repository.findAll();
    }

    public Page<BT> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public BT findOne(String id) {
        return repository.findOne(id);
    }

    @Secured("ROLE_USER")
    public BT save(BT entity) {
        return repository.save(entity);
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public Iterable<BT> findAll(Sort sort) {
        return repository.findAll(sort);
    }
}
