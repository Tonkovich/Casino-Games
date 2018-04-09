package Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientSocket extends DatagramSocket {

    InetAddress receiverHost;
    int receiverPort;
    private static ClientSocket instance;

    public static ClientSocket getInstance() {
        if (instance == null) {
            try {
                instance = new ClientSocket(0);
            } catch (SocketException ex) {

            }
        }
        return instance;
    }

    private ClientSocket(int portNum) throws SocketException {
        super(portNum);
    }

    public void sendMessage(String message) {
        try {
            byte[] sendBuffer = message.getBytes();
            DatagramPacket datagram = new DatagramPacket(sendBuffer, sendBuffer.length, receiverHost, receiverPort);
            this.send(datagram);
        } catch (IOException io) {

        }
    }

    public String receiveMessage() {
        String message = "";
        try {
            byte[] receiveBuffer = new byte[100];
            DatagramPacket datagram = new DatagramPacket(receiveBuffer, 100);
            this.receive(datagram);
            message = new String(receiveBuffer);
        } catch (IOException io) {

        }
        return message;
    }

    public Message receiveMessageAndSender() throws IOException {
        byte[] receiveBuffer = new byte[100];
        DatagramPacket datagram = new DatagramPacket(receiveBuffer, 100);
        this.receive(datagram);
        Message m = new Message();
        m.putVal(new String(receiveBuffer), datagram.getAddress(), datagram.getPort());
        return m;
    }

    public void setReceiverHost(InetAddress receiverHost) {
        this.receiverHost = receiverHost;
    }

    public void setReceiverPort(int num) {
        receiverPort = num;
    }
}
