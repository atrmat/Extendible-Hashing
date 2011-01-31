import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

/**
 * User: max
 * Date: 24.12.10
 * Time: 2:49
 */
public class TestDataGenerator
{
    static final double duplicatesCoefficient = 0.2;

    public static void main(String args[]) throws IOException
    {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

        File file = new File("data/test.txt");
//        System.out.println(file.getAbsoluteFile());
//        PrintWriter writer = new PrintWriter(new FileWriter(file));
//
//        randomDataGenerator.generateFileWithDuplicates(10, 10, 1000000, 0.1, writer);

        int linesRead = 0;
        TreeSet<String> linesSet = new TreeSet<String>();
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
        for(String line = lineNumberReader.readLine(); line != null; line = lineNumberReader.readLine(), linesRead++)
        {
            linesSet.add(line);
        }
        System.out.println(linesRead + " - " + linesSet.size());
    }
}
