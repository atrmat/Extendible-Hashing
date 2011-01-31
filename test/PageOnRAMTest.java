import ExtendibleHash.MemoryLimitedPage;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: max
 * Date: 28.01.11
 * Time: 20:43
 */
public class PageOnRAMTest
{
    @Test
    public void requiredSpace()
    {
        MemoryLimitedPage<String, String> page = new MemoryLimitedPage<String, String>(1, 1);
        assertThat(page.getSpaceRequired(), is(0));
        page.put("a", "a");
        assertThat(page.getSpaceRequired(), is(not(0)));
    }

    @Test
    public void hasEnoughSpace()
    {
        MemoryLimitedPage<Integer, Integer> page = new MemoryLimitedPage<Integer, Integer>(1, 1);

        assertThat(page.size(), is(0));
        assertThat(page.isOverflowed(), is(false));

        int i = 0;
        for (; page.hasSpaceFor(i, i); i++)
        {
            page.put(i, i);
        }

        assertThat(page.size(), is(i));
        assertThat(page.isOverflowed(), is(false));
        page.put(i + 1, i);
        assertThat(page.isOverflowed(), is(true));
    }

    @Test
    public void serialization()
    {
        MemoryLimitedPage<Integer, Integer> page = new MemoryLimitedPage<Integer, Integer>(121, 1);

        for (int i = 0; page.hasSpaceFor(i, i); i++)
        {
            page.put(i, i);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        page.serialize(os);
        byte[] bytes = os.toByteArray();
        MemoryLimitedPage<Integer, Integer> page2 = MemoryLimitedPage.deserialize(new ByteArrayInputStream(bytes));

        assertThat(page, is(not(sameInstance(page2))));
        assertThat(page, is(equalTo(page2)));
    }
}
