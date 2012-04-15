package dk.apaq.simplepay.crud;

import dk.apaq.crud.jpa.EntityManagerCrud;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class extends EntityManagerCrud which holds all the logic. This instance makes sure that when intantiated via Spring the
 * create, update and delete methods are marked as <code>Transactional</code>.
 * @author michaelzachariassenkrog
 */
public class EntityManagerCrudForSpring<IDTYPE, BEANTYPE> extends EntityManagerCrud<IDTYPE, BEANTYPE> {

    public EntityManagerCrudForSpring(EntityManager em, Class clazz) {
        super(em, clazz);
    }

    @Override
    @Transactional(readOnly=true)
    public BEANTYPE read(IDTYPE id) {
        return super.read(id);
    }

    @Override
    @Transactional
    public BEANTYPE update(BEANTYPE entity) {
        return super.update(entity);
    }

    @Override
    @Transactional
    public void delete(IDTYPE id) {
        super.delete(id);
    }

    @Override
    @Transactional
    public IDTYPE create() {
        return super.create();
    }

    @Override
    @Transactional
    public <T extends BEANTYPE> IDTYPE create(T entity) {
        return super.create(entity);
    }

    @Override
    @Transactional
    public <T extends BEANTYPE> T createAndRead(T entity) {
        return super.createAndRead(entity);
    }

    
    
}
