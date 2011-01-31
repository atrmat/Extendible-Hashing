package ExtendibleHash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: max
 * Date: 24.01.11
 * Time: 5:45
 */

public class SizeLimitedPage<K, V> extends AbstractPage<K, V>
{
    private final int PAGE_SIZE_LIMIT = 10;

    //storage for records
    protected Map<K, V> data = new HashMap<K, V>();

    public SizeLimitedPage(long id, int depth)
    {
        super(id, depth);
    }

    public void put(K key, V val)
    {
        data.put(key, val);
    }

    public Set<Entry<K, V>> getEntries()
    {
        HashSet<Entry<K, V>> set = new HashSet<Entry<K, V>>();
        for (Map.Entry<K, V> kvEntry : data.entrySet())
        {
            set.add(Entry.createEntry(kvEntry.getKey(), kvEntry.getValue()));
        }
        return set;
    }

    public boolean contains(K key)
    {
        return data.containsKey(key);
    }

    public V get(K key)
    {
        return data.get(key);
    }

    public int size()
    {
        return data.size();
    }

    public boolean hasSpaceFor(K key, V val)
    {
        return size() < PAGE_SIZE_LIMIT;
    }

    public boolean isOverflowed()
    {
        return size() > PAGE_SIZE_LIMIT;
    }
}
