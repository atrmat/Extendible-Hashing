package ExtendibleHash;

import java.util.HashMap;
import java.util.Map;

/**
 * User: max
 * Date: 09.01.11
 * Time: 6:12
 */

public class DataStoreInRAM<K, V> implements DataStore<K, V>
{
    private int pagesCounter = 0;

    Map<Long, Page<K, V>> map = new HashMap<Long, Page<K, V>>();

    public void put(Page<K, V> page)
    {
        map.put(page.getId(), page);
    }

    public Page<K, V> get(long bucketID)
    {
        return map.get(bucketID);
    }

    public void remove(long bucketID)
    {
        map.remove(bucketID);
    }

    /**
     * @param depth local bucket depth
     * @return new AbstractPage with unique id
     */
    public Page<K, V> allocate(int depth)
    {
        return new SizeLimitedPage<K, V>(++pagesCounter, depth);
    }
}
