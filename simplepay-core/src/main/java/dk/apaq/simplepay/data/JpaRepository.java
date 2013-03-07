
package dk.apaq.simplepay.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import dk.apaq.simplepay.model.BaseEntity;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * Javadoc
 */
public class JpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements org.springframework.data.jpa.repository.JpaRepository<T, ID> {

    private EntityManager entityManager;
    
    public JpaRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }

    @Override
    public <S extends T> S save(S entity) {
        if(entity instanceof BaseEntity) {
            BaseEntity be = (BaseEntity) entity;
            be.setDateChanged(new Date());
            
            Date orgDate = getOriginalCreateDate(entity.getClass(), be.getId());
            be.setDateCreated(orgDate ==null ? new Date() : orgDate);
        }
        return super.save(entity);
    }

    private Date getOriginalCreateDate(Class clazz, String id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Date> criteriaQuery = criteriaBuilder.createQuery(Date.class);
        Root from = criteriaQuery.from(clazz);
        CriteriaQuery<Date> select = criteriaQuery.select(from.get("dateCreated"));
        Predicate predicate = criteriaBuilder.equal(from.get("id"), id);
        select.where(predicate);
        TypedQuery<Date> q = entityManager.createQuery(select);
        try {
            return q.getSingleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }
    
    
    
}
