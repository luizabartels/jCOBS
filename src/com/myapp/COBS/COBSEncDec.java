package com.myapp.COBS;

public class COBSEncDec {

    /* Explaining COBS decoder: the first element from char[] input point where the first zero is.
    Basically COBS decoder looks for the zero, replace them with real zeros, while the others elements are simply
    stored in the new char[]
     */
    public static char[] decodeCOBS(char[] input)
    {
        int len = input.length;
        char[] decoded = new char[len];

        int zeroPosition = input[0]; // first element of the encoded input will point to the first zero position

        for(int i = 1; i < len; i++)
        {
            int decodedIndex = i - 1;

            if(input[i] == 0) continue;

            System.out.println("Zero position");
            System.out.println(zeroPosition);
            System.out.println("i");
            System.out.println(i);

            if(i == zeroPosition)
            {
                System.out.println("Zero - if: valor buffer");
                System.out.println((int) input[i]);
                zeroPosition += input[i];
                decoded[decodedIndex] = 0;
            }
            else
            {
                System.out.println("Não zero - if: valor buffer");
                System.out.println((int) input[i]);
                decoded[decodedIndex] = input[i];
            }

        }
        return decoded;
    }


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


}
