
package dk.apaq.simplepay.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;

import org.springframework.data.repository.CrudRepository;

/**
 * Javadoc
 */
public class MockRepositoryBase<BT extends Persistable<String>> implements CrudRepository<BT, String> {
    protected Map<String, BT> map = new HashMap<String, BT>();

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public void delete(String id) {
        map.remove(id);
    }

    @Override
    public void delete(BT entity) {
        map.remove(entity.getId());
    }

    @Override
    public void delete(Iterable<? extends BT> entities) {
        for (BT e : entities) {
            delete(e);
        }
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public boolean exists(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BT> findAll() {
        return new ArrayList<BT>(map.values());
    }



    @Override
    public Iterable<BT> findAll(Iterable<String> ids) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BT findOne(String id) {
        return map.get(id);
    }

    @Override
    public <S extends BT> List<S> save(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends BT> S save(S entity) {
        if (entity.getId() == null) {
            boolean success = false;
            Class clazz = entity.getClass();
            try {
                while(clazz!=null) {
                    try{
                        Field field = clazz.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity, UUID.randomUUID().toString());
                        success = true;
                        break;
                    } catch(NoSuchFieldException ex) {
                        clazz = clazz.getSuperclass();
                    }
                }
                if(!success) {
                    throw new IllegalArgumentException("The entity must have a field by the name 'id'");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        map.put(entity.getId(), entity);
        return entity;
    }

}
