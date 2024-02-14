package org.fungover.thunder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public class PingHandler {
    private static final Logger logger = Logger.getLogger(PingHandler.class.getName());

    private PingHandler() {
    }
    public static boolean isPingRequest( byte[] buffer, int bytesRead) throws IOException {
        return bytesRead > 0 && buffer[0] == (byte) 0xC0;
    }

    public static Boolean sendPingResponse(OutputStream outputStream) throws IOException {
        try {
            byte[] pingrespMessage = new byte[]{(byte) 0xD0, (byte) 0x00};
            outputStream.write(pingrespMessage);
            outputStream.flush();
            logger.info("Sent MQTT PINGRESP message to client");
            return true;
        } catch (IOException e) {
            logger.severe("Error sending MQTT PINGRESP message to client: " + e.getMessage());
            return false;
        }
    }
}
