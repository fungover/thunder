package org.fungover.thunder;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClientHandler {
    private final List<Socket> clients;
    private final PackageReader packageReader;

    public ClientHandler() {
        clients = Collections.synchronizedList(new ArrayList<>());
        packageReader = new PackageReader();
    }

    public void handleConnection(Socket clientSocket) {
        try {
            if (packageReader.isValidConnection(clientSocket)) {
                System.out.println("New client: " + clientSocket.getInetAddress().getHostName());
                clients.add(clientSocket);
            } else {
                clientSocket.close();
            }
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while (!clientSocket.isClosed()) {
                bytesRead = inputStream.read(buffer);
                if (PackageReader.isDisconnectPackage(buffer,bytesRead )) {
                    System.out.println("Received MQTT DISCONNECT message from client");
                    clientSocket.close();
                } else if (PingHandler.isPingRequest(buffer, bytesRead)) {
                    System.out.println("Received MQTT PINGREQ message from client");
                    PingHandler.sendPingResponse(outputStream);
                } else {
                    // Handle other types of messages
                }
            }
            removeDisconnectedClients();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeDisconnectedClients() {
        synchronized (clients) {
            Iterator<Socket> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Socket client = iterator.next();
                if (client.isClosed()) {
                    System.out.println("Client disconnected: " + client.getInetAddress().getHostName());
                    iterator.remove();
                }
            }
        }
    }

    public List<Socket> getClients() {
        return clients;
    }
}
