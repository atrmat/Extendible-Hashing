package ExtendibleHash;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: max
 * Date: 24.01.11
 * Time: 20:56
 */
public class MemoryLimitedPage<K, V> extends AbstractPage<K, V>
{
    public final static int PAGE_SIZE_LIMIT = 4096; //bytes
    private int spaceRequired = 0;

    private final Map<K, V> data = new TreeMap<K, V>();

    public MemoryLimitedPage(long id, int depth)
    {
        super(id, depth);
    }

    public void put(K key, V val)
    {
        data.put(key, val);
        spaceRequired = SerializablePage.forPage(this).calculateRequiredSpace();
    }

    public V get(K key)
    {
        return data.get(key);
    }

    public int size()
    {
        return data.size();
    }

    public Set<Entry<K, V>> getEntries()
    {
        Set<Entry<K, V>> set = new HashSet<Entry<K, V>>();
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

    public boolean hasSpaceFor(K key, V val)
    {
        int requiredSpace = SerializablePage.forNewPage(this, key, val).calculateRequiredSpace();
        return requiredSpace <= PAGE_SIZE_LIMIT;
    }

    public boolean isOverflowed()
    {
        return spaceRequired > PAGE_SIZE_LIMIT;
    }

    public int getSpaceRequired()
    {
        return spaceRequired;
    }

    public void serialize(OutputStream os)
    {
        SerializablePage.forPage(this).serialize(os);
    }

    public static <K, V> MemoryLimitedPage<K, V> deserialize(InputStream is)
    {
        return SerializablePage.deserialize(is).getPage();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemoryLimitedPage that = (MemoryLimitedPage) o;

        if (getSpaceRequired() != that.getSpaceRequired()) return false;
        if (getId() != that.getId()) return false;
        if (depth() != that.depth()) return false;

        return data.equals(that.data);
    }

    @Override
    public int hashCode()
    {
        throw new NotImplementedException();
    }

    private final static class SerializablePage implements Serializable
    {
        private static final long serialVersionUID = 1l;

        private final long id;
        private final int depth;
        private final Object keysVals[];

        private SerializablePage(MemoryLimitedPage page, int reserved)
        {
            this.id = page.id;
            this.depth = page.depth;
            this.keysVals = new Object[page.data.size() * 2 + reserved];
            int i = 0;
            for (Object o : page.data.entrySet())
            {
                Map.Entry e = (Map.Entry) o;
                keysVals[i++] = e.getKey();
                keysVals[i++] = e.getValue();
            }
        }

        public static SerializablePage forNewPage(MemoryLimitedPage page, Object key, Object val)
        {
            SerializablePage serializablePage = new SerializablePage(page, 2);
            int length = serializablePage.keysVals.length;
            serializablePage.keysVals[length - 1] = key;
            serializablePage.keysVals[length - 2] = val;
            return serializablePage;
        }

        public static SerializablePage forPage(MemoryLimitedPage page)
        {
            return new SerializablePage(page, 0);
        }

        public static SerializablePage deserialize(InputStream is)
        {
            try
            {
                ObjectInputStream ois = new ObjectInputStream(is);
                return (SerializablePage) ois.readObject();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        public void serialize(OutputStream os)
        {
            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(this);
                oos.flush();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        public int calculateRequiredSpace()
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            serialize(bytes);
            return bytes.size();
        }

        private <K, V> MemoryLimitedPage<K, V> getPage()
        {
            MemoryLimitedPage<K, V> page = new MemoryLimitedPage<K, V>(id, depth);
            for (int i = 0; i < keysVals.length; i += 2)
            {
                //TODO: deal with types
                page.data.put((K) keysVals[i], (V) keysVals[i + 1]);
            }
            page.spaceRequired = calculateRequiredSpace();
            return page;
        }
    }
}
