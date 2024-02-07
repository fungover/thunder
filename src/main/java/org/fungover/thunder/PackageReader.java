package org.fungover.thunder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PackageReader {
    private final Map<InetAddress, Boolean> connectPackageSent = new HashMap<>();
    private static final int CONNECTION_TIMEOUT = 30000;
    public boolean isValidConnection(Socket socket) throws IOException {
        InetAddress client = socket.getInetAddress();
        byte[] buffer = new byte[1024];
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        int bytesRead = inputStream.read(buffer);

        if (connectPackageSent.containsKey(client) && Boolean.TRUE.equals(connectPackageSent.get(client)) && buffer[0] == 0x10) {
            return false;
        }

        socket.setSoTimeout(CONNECTION_TIMEOUT);

        if (isConnectPackage(bytesRead, buffer)) {
            System.out.println("Received MQTT CONNECT message from client");
            connectPackageSent.put(socket.getInetAddress(), true);
            sendConnackToClient(outputStream);
            System.out.println("Sent MQTT CONNACK message to client");
            return true;
        }

        return true;
    }

   private static void sendConnackToClient(OutputStream outputStream) throws IOException {
        byte[] connackMessage = new byte[]{(byte) 0x20, (byte) 0x02, (byte) 0x00, (byte) 0x00};
        outputStream.write(connackMessage);
        outputStream.flush();
    }

    private static boolean isConnectPackage(int bytesRead, byte[] buffer) {
        return bytesRead > 0 && buffer[0] == (byte) 0x10;
    }
}
