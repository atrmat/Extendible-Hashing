package ExtendibleHash;

import java.util.Collection;

/**
 * User: max
 * Date: 05.01.11
 * Time: 4:28
 */

public interface Directory
{
    public void put(long hashCode, long bucketID);
    public long get(long hashCode);

    public int getDepth();
    public void extend();
}
