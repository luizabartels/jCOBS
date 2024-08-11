package com.myapp.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPClient {

    public static void send2Server(char[] data)
    {
        final String SERVER_ADDRESS = "localhost";
        final int SERVER_PORT = 9876;

        try (DatagramSocket socket = new DatagramSocket()) {
            String str = new String(data);
            byte[] buffer = str.getBytes(StandardCharsets.UTF_8);

            // Send the message to the com.myapp.server
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
            socket.send(packet);

            // Receive response from the com.myapp.server
            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);

            String responseMessage = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Received from com.myapp.server: " + responseMessage);

            int i = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
