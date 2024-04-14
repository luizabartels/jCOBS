import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import static java.util.Arrays.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static char[] encodeCOBS(char[] input)
    {
        int len = input.length + 1;
        char[] encoded = new char[len];
        int zero = 1;
        int value;
        for (int i = len - 1; i > 0; i--)
        {
            value = input[i - 1];

            if(value == 0)
            {
                encoded[i] = (char) zero;
                zero = 1;
            }
            else
            {
                encoded[i] = (char) value;
                zero++;
            }
        }
        encoded[0] = (char) zero;

        return encoded;
    }

    /* Explaining COBS decoder: the first element from char[] input point where the first zero is.
    Basically COBS decoder looks for the zero, replace them with real zeros, while the others elements are simply
    stored in the new char[]
     */
    public static char[] decodeCOBS(char[] input)
    {
        int len = input.length;
        char[] decoded = new char[len];

        int zeroPosition = input[0]; // first element of the encoded input will points to the first zero position

        for(int i = 1; i < len; i++)
        {
            int decodedIndex = i - 1;

            if(input[i] == 0) continue;

            if(i == zeroPosition)
            {
                //System.out.println("Zero");
                zeroPosition += input[i];
                decoded[decodedIndex] = 0;
            }
            else
            {
                //System.out.println("Não zero");
                decoded[decodedIndex] = input[i];
            }
        }
        return decoded;
    }

    public static char[] calculateXORchecksum(char[] input)
    {
        int crc = 0;
        char[] result = new char[1];

        for(int i = 0; i < input.length; i++) crc ^= input[i];

        result[0] = (char) crc;

        return result;
    }

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        //System.out.println("Hello and welcome!");

        char[] testerBuffer = new char[]{1, 37}; // Buffer with at least one index and one value

        int maxIndexEncoded = encodeCOBS(testerBuffer).length;
        char[] resultEncoded = new char[maxIndexEncoded];

        System.arraycopy(encodeCOBS(testerBuffer), 0, resultEncoded, 0, maxIndexEncoded);

        char[] xorChecksum = new char[1];
        calculateXORchecksum(testerBuffer); // Calculate XORchecksum
        char[] encodedWithXor = new char[resultEncoded.length + 1]; // Encoded length + the CheckSum byte

        System.arraycopy(resultEncoded, 0, encodedWithXor, 0, resultEncoded.length);
        System.arraycopy(xorChecksum, 0, encodedWithXor, resultEncoded.length, 1);

        int bufferLength = resultEncoded.length + 2; // Plus two is for the checksum and length bytes
//
//        char[] bufferToSend = new char[2];
//        bufferToSend[0] = 0;
//        bufferToSend[1] = (char) bufferLength;
//
//
//        int maxIndexDecoded = resultEncoded.length - 1;
//        char[] resultDecoded = new char[maxIndexDecoded];
//
//        for(int i = 0; i < maxIndexDecoded; i++)
//        {
//            resultDecoded[i] = decodeCOBS(resultEncoded)[i];
//        }
//
//
//
//        int i = 0;

    }
}