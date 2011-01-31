package ExtendibleHash;

/**
 * User: max
 * Date: 11.01.11
 * Time: 16:10
 */
public class BinUtils
{
    /**
     * Converts an word to string representation of its binary value.
     * @param word - set of bits, a word to be printed
     * @param length - number of bits required to print
     * @return - set of 1 and 0 grouped by bytes.
     */
    public static String toBinary(long word, int length)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= length; i++)
        {
            if ((word & 1) == 1)
            {
                sb.insert(0, '1');
            }
            else
            {
                sb.insert(0, '0');
            }

            if (i % 8 == 0)
            {
                sb.insert(0, ' ');
            }
            word >>>= 1;
        }
        return sb.toString().trim();
    }

    /**
     * Generates all possible values with constant least significant bits and variable most significant bits.
     * generated value = variable bits + constant bits.
     * for example for this values:
     * 201 - 00000000 11001001
     * 457 - 00000001 11001001
     * BinUtils.enumerateValues(9, 8, 201) returns {201, 457}
     * @param bitsForVariable - size of result value in bits
     * @param bitsForConstant - size of constant part - number of least significant bits.
     * @param constant - predefined constant part of result value.
     * @return - an arrays of generated values.
     */
    public static long[] enumerateValues(int bitsForVariable, int bitsForConstant, long constant)
    {
        int variableBits = bitsForVariable - bitsForConstant;
        int size = 1 << variableBits;
        long[] values = new long[size];
        for(int i = 0; i < size; i++)
        {
            values[i] = (i << bitsForConstant) | constant;
        }
        return values;
    }

}
