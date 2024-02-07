package org.fungover.thunder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PingHandler {

    public PingHandler() {
    }

    public void handlePing(Socket socket) throws IOException {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            System.out.println("Bytes read: " + bytesRead);
            System.out.printf("First byte: 0x%02X\n", buffer[0]);
            if (bytesRead > 0 && buffer[0] == (byte) 0xC0) {
                System.out.println("Received MQTT PINGREQ message from client");
                byte[] pingrespMessage = new byte[]{(byte) 0xD0, (byte) 0x00};
                outputStream.write(pingrespMessage);
                outputStream.flush();
                System.out.println("Sent MQTT PINGRESP message to client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
