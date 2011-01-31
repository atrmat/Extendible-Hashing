package ExtendibleHash;

import java.util.Set;

/**
 * User: max
 * Date: 24.01.11
 * Time: 5:36
 */
public interface Page<K, V>
{
    void put(K key, V val);

    V get(K key);

    Set<Entry<K, V>> getEntries();

    int depth();

    int size();

    long getId();

    boolean contains(K key);

    boolean hasSpaceFor(K key, V val);

    boolean isOverflowed();
}
