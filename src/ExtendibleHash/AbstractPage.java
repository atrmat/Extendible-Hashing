package ExtendibleHash;

import java.util.Set;

/**
 * User: max
 * Date: 05.01.11
 * Time: 21:03
 */
public abstract class AbstractPage<K, V> implements Page<K, V>
{
    //unique bucket id
    protected final long id;

    //bucket depth(local depth)
    protected final int depth;

    public AbstractPage(long id, int depth)
    {
        this.id = id;
        this.depth = depth;
    }

    public int depth()
    {
        return depth;
    }

    public long getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(String.format("id:%1$-5d depth:%2$-2d size:%3$-2d [", id, depth, size()));
        Set<Entry<K, V>> entries = getEntries();
        if (!entries.isEmpty())
        {
            for (Entry<K, V> e : entries)
            {
                sb.append(e.key).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }
}
