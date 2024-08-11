import com.myapp.client.UDPClient;

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

    public static int calculateXORchecksum(char[] input)
    {
        int crc = 0;

        for (char c : input) crc ^= c;

        return crc;
    }

    public static void main(String[] args) {

        char[] testerBuffer = new char[]{1, 37}; // Buffer with at least one index and one value

        int maxIndexEncoded = encodeCOBS(testerBuffer).length;
        char[] resultEncoded = new char[maxIndexEncoded];

        System.arraycopy(encodeCOBS(testerBuffer), 0, resultEncoded, 0, maxIndexEncoded);

        char xorChecksum = (char) calculateXORchecksum(testerBuffer); // Calculate XORchecksum
        char[] xorCheckSumArray = new char[]{xorChecksum};

        char[] encodedWithXor = new char[resultEncoded.length + 1]; // Encoded length + the CheckSum byte

        System.arraycopy(resultEncoded, 0, encodedWithXor, 0, resultEncoded.length);
        System.arraycopy(xorCheckSumArray, 0, encodedWithXor, resultEncoded.length, xorCheckSumArray.length);

        int bufferLength = resultEncoded.length + 1; // For the checksum and length bytes

        char[] startByteAndLength = new char[]{0, (char) bufferLength};

        char[] bufferToSend = new char[encodedWithXor.length + startByteAndLength.length];

        System.arraycopy(startByteAndLength, 0, bufferToSend, 0, startByteAndLength.length);
        System.arraycopy(encodedWithXor, 0, bufferToSend, startByteAndLength.length, encodedWithXor.length);

        UDPClient.send2Server(bufferToSend);


        /* TODO: Figure out how UDP communication happens. If all bytes are received together or it naturally serializes each of them */

    }
}