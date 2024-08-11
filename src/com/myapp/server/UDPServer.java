package com.myapp.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UDPServer {

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

    public static void main(String[] args) {
        final int PORT = 9876;

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[1024];

            System.out.println("Server is listening on port " + PORT);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + Arrays.toString(receivedMessage.toCharArray()));
                System.out.println("Length(): " + receivedMessage.length());

                char[] buffer2Decode = receivedMessage.toCharArray();
                char[] bufferDecoded = decodeCOBS(buffer2Decode);


                String str = new String(bufferDecoded);
                byte[] responseBuffer = str.getBytes(StandardCharsets.UTF_8);

                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length,
                        packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
