import java.io.PrintWriter;
import java.io.Writer;
import java.util.Random;

/**
 * User: max
 * Date: 24.12.10
 * Time: 5:33
 */

public class RandomDataGenerator
{
    public static final char[] defaultDict = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private char[] dictionary = defaultDict;

    public String getRandomWord(int wordSize)
    {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(wordSize);

        for (int i = 0; i < wordSize; i++)
        {
            char c = dictionary[random.nextInt(dictionary.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public String getRandomString(int wordSize, int wordsNum)
    {
        StringBuilder sb = new StringBuilder(wordSize);
        for (int i = 0; i < wordsNum; i++)
        {
            sb.append(getRandomWord(wordSize)).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public void generateFileWithDuplicates( int wordSize, int wordsNum, int stringsNum, double duplicatesRate, PrintWriter writer)
    {
        Random random = new Random();
        String randomString = getRandomString(wordSize, wordsNum);

        for (int i = 0; i < stringsNum; i++)
        {
            if(random.nextDouble() > duplicatesRate)
            {
                randomString = getRandomString(wordSize, wordsNum);
            }
            writer.println(randomString);
        }
        writer.flush();
    }

}
