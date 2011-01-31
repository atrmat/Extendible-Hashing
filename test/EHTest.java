import ExtendibleHash.*;
import junit.extensions.TestDecorator;
import org.junit.Test;

import java.awt.image.Kernel;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: max
 * Date: 05.01.11
 * Time: 3:59
 */

public class EHTest
{
    @Test
    public void test()
    {
        EHash<String, String> ehash = new EHash<String, String>(new DirectoryInRAM(), new DataStoreInRAM<String, String>());

        ehash.put("12", "12");
        ehash.put("13", "13");
        ehash.put("a", "a");
        ehash.put("b", "b");
        dumpFull(ehash);

        assertTrue(ehash.contains("12"));
        assertTrue(ehash.contains("13"));
        assertTrue(ehash.contains("a"));
        assertTrue(ehash.contains("b"));
    }

    @Test
    public void test2()
    {
        EHash<String, String> ehash = new EHash<String, String>(new DirectoryInRAM(), new DataStoreInRAM<String, String>());

        ehash.put("12", "");
        ehash.put("13", "");
        ehash.put("12", null);
        ehash.put("13", null);

        dumpFull(ehash);

        assertTrue(ehash.size() == 2);
        assertTrue(ehash.contains("12"));
        assertTrue(ehash.contains("13"));
    }

    @Test
    public void test3()
    {
        EHash<String, String> ehash = new EHash<String, String>(new DirectoryInRAM(2), new DataStoreInRAM<String, String>());

        int size = 100;
        String[][] vals = new String[size][2];
        for (int i = 0; i < size; i++)
        {
            vals[i][0] = String.valueOf(i);
            vals[i][1] = String.valueOf(i);
        }

        addAll(ehash, vals);

        dumpFull(ehash);

        assertTrue(ehash.size() == size);
        assertContainsEach(ehash, vals);
    }


    @Test
    public void complexTest()
    {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        Set<String> set = new HashSet<String>();
        EHash<String, String> eset = new EHash<String, String>(new DirectoryInRAM(), new DataStoreInRAM<String, String>());
        for(int i = 0; i < 200000; i++)
        {
            String randomString = randomDataGenerator.getRandomString(4, 4);
            set.add(randomString);
            eset.put(randomString, randomString);
        }

        dump(eset);
        assertEquals(set.size(), eset.size());
        assertContainsEach(eset, set);
    }


    /*
    Auxiliary code
     */

    public <K, V> void addAll(EHash<K, V> eh, Object... recs)
    {
        for (Object rec : recs)
        {
            K key = (K)((Object[])rec)[0];
            V val = (V)((Object[])rec)[1];
            eh.put(key, val);
        }
    }

    public <K, V> void assertContainsEach(EHash<K, V> eh, Object... recs)
    {
        for (Object rec : recs)
        {
            K key = (K)((Object[])rec)[0];
            V val = (V)((Object[])rec)[1];
            assertTrue(eh.contains(key));
            assertEquals(eh.get(key), val);
        }
    }

    public <K, V> void assertContainsEach(EHash<K, V> eh, Collection recs)
    {
        for (Object r : recs)
        {
            assertTrue(eh.contains((K)r));
        }
    }

    private void dumpFull(EHash<String, String> ehash)
    {
        new EHashDump(ehash).dump(new PrintWriter(System.out), true);
    }

    private void dump(EHash<String, String> ehash)
    {
        new EHashDump(ehash).dump(new PrintWriter(System.out), false);
    }
}
