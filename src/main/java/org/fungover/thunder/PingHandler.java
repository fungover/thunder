package org.fungover.thunder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PingHandler {
    private InputStream inputStream;
    private OutputStream outputStream;

    public PingHandler(Socket socket) throws IOException {
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }
    public void handlePing(){
        try {
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

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
