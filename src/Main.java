import com.myapp.client.UDPClient;
import com.myapp.COBS.COBSEncDec;
import com.myapp.Utils.Methods;

public class Main {

    public static void main(String[] args) {

        /* The Consistent Overhead Byte Stuffing (COBS) algorithm (https://en.wikipedia.org/wiki/Consistent_Overhead_Byte_Stuffing) used in this script.
            The packet framing is basically a char vector/array, consisting of:
            0 (delimiter), data frame length (including itself), encoded data frame, checksum (calculated after encoder)
         */

        char[] testerBuffer = new char[]{90, 53, 0, 47, 0}; // Buffer with at least one index and one value

        char xorChecksum = (char) Methods.calculateXORchecksum(testerBuffer); // Calculate XOR checksum after encoder
        char[] xorCheckSumArray = new char[]{xorChecksum}; // The xorChecksum was just and single char, now it is an element in the xorCheckSumArray

        char[] bufferToEncode = new char[testerBuffer.length + 1];

        System.arraycopy(testerBuffer, 0, bufferToEncode, 0, testerBuffer.length);
        System.arraycopy(xorCheckSumArray, 0, bufferToEncode, testerBuffer.length, xorCheckSumArray.length);

        int maxIndexEncoded = COBSEncDec.encodeCOBS(bufferToEncode).length; // Is mandatory to discover buffer's length after encoder to declare the char[] used in the System.arraycopy method
        char[] resultEncoded = new char[maxIndexEncoded]; // Declaration

        System.arraycopy(COBSEncDec.encodeCOBS(bufferToEncode), 0, resultEncoded, 0, maxIndexEncoded); // Correct way to copy char[]

        int bufferLength = resultEncoded.length + 1; // As the length we use in our buffer must count itself, the final buffer length is plus 1

        char[] startByteAndLength = new char[]{0, (char) bufferLength}; // To finally mount the final buffer, we must add the zero-delimiter and the buffer length

        char[] bufferToSend = new char[resultEncoded.length + startByteAndLength.length]; // The total length must be calculated to be used correctly in the .arraycopy method

        System.arraycopy(startByteAndLength, 0, bufferToSend, 0, startByteAndLength.length); // Copy the first two element in the final buffer from the first element in both arrays
        System.arraycopy(resultEncoded, 0, bufferToSend, startByteAndLength.length, resultEncoded.length); // Copy the data frame to the bufferToSend array from the last element

        UDPClient.send2Server(bufferToSend);

        int i = 0;

    }
}