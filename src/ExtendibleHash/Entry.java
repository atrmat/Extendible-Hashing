package ExtendibleHash;

/**
* User: max
* Date: 13.01.11
* Time: 22:58
*/
class Entry<K, V>
{
    public final K key;
    public final V value;

    private Entry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    static <K, V> Entry<K, V> createEntry(K key, V value)
    {
        return new Entry<K, V>(key, value);
    }
}
