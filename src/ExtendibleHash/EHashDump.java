package ExtendibleHash;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * User: max
 * Date: 28.01.11
 * Time: 16:53
 */
public class EHashDump
{
    private EHash ehash;

    public EHashDump(EHash ehash)
    {
        this.ehash = ehash;
    }

    public void dump(PrintWriter printWriter, boolean structureDump)
    {
        Directory directory = ehash.getDirectory();
        DataStore dataStore = ehash.getDataStore();

        printWriter.print(ehash + ":\n");

        Map<Page, Long> bucks = new HashMap<Page, Long>();
        int depth = directory.getDepth();
        int numWidth = String.valueOf(1 << depth).length();
        int minLocDepth = 32;
        int maxLocDepth = 0;
        int pages = 0;
        int overflowedBuckets = 0;
        for (int i = 0; i < 1 << depth; i++)
        {
            long bucketID = directory.get(i);
            if (bucketID != 0)
            {
                long localHash = ((1 << depth) - 1) & i;
                Page page = dataStore.get(bucketID);

                bucks.put(page, localHash);
                minLocDepth = Math.min(minLocDepth, page.depth());
                maxLocDepth = Math.max(maxLocDepth, page.depth());
                if (page.isOverflowed())
                {
                    overflowedBuckets++;
                }
                pages++;
            }
        }

        printWriter.printf("PAGE INFO: " +
                "%n    local depth between %d, %d;" +
                "%n    overflowed pages = %d;" +
                "%n    page size mean = %2.1f%n", minLocDepth, maxLocDepth, overflowedBuckets, (float)ehash.size() / (float)pages);

        if (structureDump)
        {
            //printWriter.printf("%1$" + numWidth + "S %2$" + (depth + 1) + "s  %3$7s  %4$s%n", "d.i|", "bin|", "depth|", "|bucket");
            for (Map.Entry<Page, Long> entry : bucks.entrySet())
            {
                Page page = entry.getKey();
                Long localHash = entry.getValue();
                String bucketString = page.toString();
                long[] bucketCodes = BinUtils.enumerateValues(directory.getDepth(), page.depth(), localHash & ((1 << page.depth()) - 1));
                for (Long index : bucketCodes)
                {
                    printWriter.printf("%1$" + numWidth + "d %2$s  %3$7." + (depth - page.depth()) + "s  %4$s%n", index, BinUtils.toBinary(index, depth), "----------", bucketString);
                }
            }
        }
        printWriter.flush();
    }
}

