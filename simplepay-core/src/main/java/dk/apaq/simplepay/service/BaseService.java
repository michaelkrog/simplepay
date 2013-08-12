package dk.apaq.simplepay.service;

import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.annotation.Secured;

/**
 * Abstract class for basic services using a
 * <code>PageableQueryDslCrudRepository</code>.
 */
public abstract class BaseService<BT extends Persistable, IDTYPE extends Serializable> {

    private PagingAndSortingRepository<BT, IDTYPE> repository;

    public void setRepository(PagingAndSortingRepository<BT, IDTYPE> repository) {
        this.repository = repository;
    }

    public Iterable<BT> findAll() {
        return repository.findAll();
    }

    public Page<BT> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public BT findOne(IDTYPE id) {
        return repository.findOne(id);
    }

    @Secured("ROLE_USER")
    public BT save(BT entity) {
        return repository.save(entity);
    }

    public void delete(IDTYPE id) {
        repository.delete(id);
    }

    public Iterable<BT> findAll(Sort sort) {
        return repository.findAll(sort);
    }
}
