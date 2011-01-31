import ExtendibleHash.BinUtils;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;


/**
 * User: max
 * Date: 14.01.11
 * Time: 2:41
 */
public class BinUtilsTest
{
    @Test
    public void testEnumerate()
    {
        //201 - 00000000 11001001
        //457 - 00000001 11001001
        assertArrayEquals(new long[]{201, 457}, BinUtils.enumerateValues(9, 8, 201));
        //assertEquals(new long[]{201, 457}, BinUtils.enumerateValues(2, 1, 201));
    }
}
