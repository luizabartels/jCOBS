package com.myapp.server;
import com.myapp.COBS.COBSEncDec;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UDPServer {

    public static char[] processBuffer2Decode(char[] buffer){
        int bufferLength = buffer.length;
        char[] dataFrame = new char[bufferLength - 2];

        System.arraycopy(buffer, 2, dataFrame, 0, dataFrame.length);

        return COBSEncDec.decodeCOBS(dataFrame);
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
                char[] bufferDecoded = processBuffer2Decode(buffer2Decode);

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
