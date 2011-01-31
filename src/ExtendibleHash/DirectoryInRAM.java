package ExtendibleHash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * User: max
 * Date: 05.01.11
 * Time: 17:42
 */


/**
 * Class that maps hash codes to data pages.
 */
public class DirectoryInRAM implements Directory
{
    /*
    Initially, all local depths are equal to the global depth (which is the number of
    bits needed to express the total number of buckets).
    */
    //global depth
    private int depth;
    private long[] directory;

    public DirectoryInRAM(int depth)
    {
        this.depth = depth;
        this.directory = new long[1 << depth];
    }

    public DirectoryInRAM()
    {
        this(8);
    }

    public void put(long hashCode, long bucketID)
    {
        if(depth >= 32)
        {
            throw new IllegalStateException("Directory index is too big. As long as directory is just an array your max. dir. size = 32.");
        }

        int depthMask = ((1 << depth) - 1);
        int index = (int) hashCode & depthMask;
        directory[index] = bucketID;
    }

    public long get(long hashCode)
    {
        int depthMask = ((1 << depth) - 1);
        return directory[(int) hashCode & depthMask];
    }

    public void extend()
    {
        int oldSize = 1 << depth;
        depth++;
        int newSize = 1 << depth;

        directory = Arrays.copyOf(directory, newSize);
        System.arraycopy(directory, 0, directory, oldSize, oldSize);
    }

    public int getDepth()
    {
        return depth;
    }
}
