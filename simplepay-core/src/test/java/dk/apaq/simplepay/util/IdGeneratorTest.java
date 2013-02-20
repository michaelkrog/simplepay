package dk.apaq.simplepay.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        final List<String> ids = Collections.synchronizedList(new ArrayList<String>());

        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 10000; i++) {
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

        Thread.sleep(100);

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        Thread.sleep(100);

        System.out.println("ids: " + ids.size());
        List<String> dups = getDuplicate(ids);
        System.out.println("First id: " + ids.get(0));
        System.out.println("Last id: " + ids.get(ids.size() - 1));
        assertTrue("Duplicates of id found", dups.isEmpty());
    }

    private <T> List getDuplicate(Collection<T> list) {
        List<T> result = new ArrayList<T>();
        Set<T> set = new HashSet<T>();
        
        for(T t : list) {
            if(set.contains(t)) {
                result.add(t);
            } else {
                set.add(t);
            }
        }
        return result;
    }
}