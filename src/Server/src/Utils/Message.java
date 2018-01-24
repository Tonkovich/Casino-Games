package Utils;

import java.net.InetAddress;

public class Message {
    private String message;
    private InetAddress senderAddress;
    private int senderPort;

    public void putVal(String message, InetAddress addr, int port) {
        this.message = message;
        this.senderAddress = addr;
        this.senderPort = port;
    }

    public String getMessage() {
        return message;
    }

    public InetAddress getAddress() {
        return senderAddress;
    }

    public int getPort() {
        return senderPort;
    }
}
