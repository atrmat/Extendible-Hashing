package ExtendibleHash;

import java.util.Collection;

/**
 * User: max
 * Date: 05.01.11
 * Time: 17:39
 * Extendible hashing implementation
 * Details: http://en.wikipedia.org/wiki/Extendible_hashing
 */

public class EHash<K, V>
{
    private Directory directory;
    private DataStore<K, V> dataStore;
    private EHashStats stats = new EHashStats();
    private long size = 0;

    public EHash(Directory directory, DataStore<K, V> dataStore)
    {
        this.directory = directory;
        this.dataStore = dataStore;
    }

    public boolean contains(K key)
    {
        long hashCode = key.hashCode();
        long bucketID = directory.get(hashCode);
        if (bucketID != 0)
        {
            Page<K, V> page = dataStore.get(bucketID);
            return page.contains(key);
        }
        return false;
    }

    public V get(K key)
    {
        long hashCode = key.hashCode();
        long bucketID = directory.get(hashCode);
        if (bucketID != 0)
        {
            Page<K, V> page = dataStore.get(bucketID);
            return page.get(key);
        }
        return null;
    }

    public void put(K key, V value)
    {
        long hashCode = key.hashCode();
        long bucketID = directory.get(hashCode);

        Page<K, V> page;
        if (bucketID == 0)
        {
            page = dataStore.allocate(directory.getDepth());
        }
        else
        {
            page = dataStore.get(bucketID);
            if (page.contains(key))
            {
                return;
            }
        }

        // in case if all values of spitting bucket have been rehashed into one bucket, than there is nothing we ca do, only accept it.
        // but this is very rare case.
        if (!page.hasSpaceFor(key, value))
        {
            int localDepth = page.depth();
            if (localDepth == directory.getDepth())
            {
                //depth of directory will be increased
                directory.extend();
            }

            int newBucketsDepth = localDepth + 1;
            Page<K, V> bucket1 = dataStore.allocate(newBucketsDepth);
            Page<K, V> bucket2 = dataStore.allocate(newBucketsDepth);

            Collection<Entry<K, V>> swapElements = page.getEntries();
            swapElements.add(Entry.createEntry(key, value));
            for (Entry<K, V> e : swapElements)
            {
                long h = e.key.hashCode();
                if (((h >> (newBucketsDepth - 1)) & 1) == 1)
                {
                    bucket2.put(e.key, e.value);
                }
                else
                {
                    bucket1.put(e.key, e.value);
                }
            }

            //get the indexes of directory which are points to split page
            long[] bucketCodes = BinUtils.enumerateValues(directory.getDepth(), localDepth, hashCode & ((1 << localDepth) - 1));
            for (long i : bucketCodes)
            {
                if (((i >> (newBucketsDepth - 1)) & 1) == 1)
                {
                    directory.put(i, bucket2.getId());
                }
                else
                {
                    directory.put(i, bucket1.getId());
                }
            }

            dataStore.remove(page.getId());
            dataStore.put(bucket1);
            dataStore.put(bucket2);
        }
        else
        {
            page.put(key, value);
            directory.put(hashCode, page.getId());
            dataStore.put(page);
        }
        size++;
    }

    public long size()
    {
        return size;
    }

    public Directory getDirectory()
    {
        return directory;
    }

    public DataStore<K, V> getDataStore()
    {
        return dataStore;
    }

    public EHashStats getStats()
    {
        return stats;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("EXTENDIBLE HASH { directory = ").append(directory.getClass().getSimpleName())
                .append(", dataStore = ").append(dataStore.getClass().getSimpleName())
                .append(", size = ").append(size).append(" }");
        sb.append(stats);
        return sb.toString();
    }
}
