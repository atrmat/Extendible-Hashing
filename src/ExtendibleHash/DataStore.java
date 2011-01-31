package ExtendibleHash;

/**
 * User: max
 * Date: 09.01.11
 * Time: 6:08
 */

public interface DataStore<K, V>
{
    public void put(Page<K, V> page);
    public Page<K, V> get(long pageID);


    public Page<K, V> allocate(int depth);
    public void remove (long pageID);
}
