package ExtendibleHash;

import com.google.common.collect.EnumMultiset;
import com.google.common.collect.Multiset;

/**
 * User: max
 * Date: 28.01.11
 * Time: 3:37
 */
public class EHashStats
{
    public static enum Param
    {
        RECORDS,
        PAGES,
        OVERFLOWED_PAGES,
        GLOBAL_DEPTH,
        LOCAL_DEPTH_MIN,
        LOCAL_DEPTH_MAX,
        DIRECTORY_DOUBLES,
        PAGES_SPLIT,
        READS,
        WRITES,
        BYTES_READ,
        BYTES_WRITE;

        private static int maxNameLength = 0;

        static
        {
            for (Param p : values())
            {
                maxNameLength = Math.max(p.name().length(), maxNameLength);
            }
        }

        public static int getMaxNameLength()
        {
            return maxNameLength;
        }
    }

    private EnumMultiset<Param> values = EnumMultiset.create(Param.class);

    public void add(Param p)
    {
        values.add(p);
    }

    public void add(Param p, int n)
    {
        values.add(p, n);
    }

    public int get(Param p)
    {
        return values.count(p);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Multiset.Entry<Param> e : values.entrySet())
        {
            if (e.getCount() > 0)
            {
                sb.append(String.format("%1$" + Param.getMaxNameLength() + "S: %2$d%n", e.getElement().name(), e.getCount()));
            }
        }
        return sb.toString();
    }
}
