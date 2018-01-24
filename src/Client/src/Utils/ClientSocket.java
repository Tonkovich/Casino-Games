package Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientSocket extends DatagramSocket {

    public ClientSocket(int portNum) throws SocketException {
        super(portNum);
    }

    public void sendMessage(InetAddress receiverHost, int receiverPort, String message) throws IOException {
        byte[] sendBuffer = message.getBytes();
        DatagramPacket datagram = new DatagramPacket(sendBuffer, sendBuffer.length, receiverHost, receiverPort);
        this.send(datagram);
    }

    public String receiveMessage() throws IOException {
        byte[] receiveBuffer = new byte[100];
        DatagramPacket datagram = new DatagramPacket(receiveBuffer, 100);
        this.receive(datagram);
        String message = new String(receiveBuffer);
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
}
