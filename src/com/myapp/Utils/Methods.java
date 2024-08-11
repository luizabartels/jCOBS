package com.myapp.Utils;

public class Methods {
    public static int calculateXORchecksum(char[] input)
    {
        int crc = 0;

        for (char c : input) crc ^= c;

        return crc;
    }
}
