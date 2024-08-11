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

        int maxIndexEncoded = COBSEncDec.encodeCOBS(testerBuffer).length; // Is mandatory to discover buffer's length after encoder to declare the char[] used in the System.arraycopy method
        char[] resultEncoded = new char[maxIndexEncoded]; // Declaration

        System.arraycopy(COBSEncDec.encodeCOBS(testerBuffer), 0, resultEncoded, 0, maxIndexEncoded); // Correct way to copy char[]

        char xorChecksum = (char) Methods.calculateXORchecksum(testerBuffer); // Calculate XOR checksum after encoder
        char[] xorCheckSumArray = new char[]{xorChecksum}; // The xorChecksum was just and single char, now it is an element in the xorCheckSumArray

        char[] encodedWithXor = new char[resultEncoded.length + 1]; // Encoded length + the CheckSum byte, as the checksum byte will always be a single byte

        System.arraycopy(resultEncoded, 0, encodedWithXor, 0, resultEncoded.length); // Copy the resultEncoded to the encodedWithXor char[] from the first element in both arrays
        System.arraycopy(xorCheckSumArray, 0, encodedWithXor, resultEncoded.length, xorCheckSumArray.length); // Copy the xorCheckSumArray to the encodedWithXor char[] from the last element in the encodedWithXor array

        int bufferLength = resultEncoded.length + 1; // As the length we use in our buffer must count itself, the final buffer length is plus 1

        char[] startByteAndLength = new char[]{0, (char) bufferLength}; // To finally mount the final buffer, we must add the zero-delimiter and the buffer length

        char[] bufferToSend = new char[encodedWithXor.length + startByteAndLength.length]; // The total length must be calculated to be used correctly in the .arraycopy method

        System.arraycopy(startByteAndLength, 0, bufferToSend, 0, startByteAndLength.length); // Copy the first two element in the final buffer from the first element in both arrays
        System.arraycopy(encodedWithXor, 0, bufferToSend, startByteAndLength.length, encodedWithXor.length); // Copy the data frame to the bufferToSend array from the last element

        UDPClient.send2Server(bufferToSend);

        int i = 0;

    }
}