package dk.apaq.simplepay.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author krog
 */
public class IdGeneratorTest {

    /**
     * Test of generateUniqueId method, of class IdGenerator.
     */
    @Test
    public void testGenerateUniqueId() throws InterruptedException {
        System.out.println("generateUniqueId");
        final List<String> ids = new ArrayList<String>();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 5000; i++) {
                    list.add(IdGenerator.generateUniqueId());
                    Thread.yield();
                }
                ids.addAll(list);
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        Thread t3 = new Thread(r);
        Thread t4 = new Thread(r);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        
        List<String> dups = getDuplicate(ids);
        System.out.println(ids.get(0));
        System.out.println(ids.get(ids.size()-1));
        /*for(String id : ids) {
            System.out.println(id);
        }*/
        assertTrue(dups.isEmpty());
        //assertFalse(result.endsWith("="));
    }

    private <T> List getDuplicate(Collection<T> list) {

        final List<T> duplicatedObjects = new ArrayList<T>();
        Set<T> set = new HashSet<T>() {
            @Override
            public boolean add(T e) {
                if (contains(e)) {
                    duplicatedObjects.add(e);
                }
                return super.add(e);
            }
        };
        for (T t : list) {
            set.add(t);
        }
        return duplicatedObjects;
    }
}