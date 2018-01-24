import Utils.ClientSocket;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
    /**
     * No idea what is going to become of the client just yet
     */
    public static void main(String[] args) throws IOException {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        try {
            System.out.println("Please enter server IP: ");
            String hostName = br.readLine();

            if (hostName.length() == 0)
                hostName = "localhost";

            InetAddress hostNameInet = InetAddress.getByName(hostName);

            System.out.println("Please enter server port: ");
            String portNum = br.readLine();

            if (portNum.length() == 0)
                portNum = "12000";

            int portNumInt = Integer.parseInt(portNum);

            ClientSocket mySocket = new ClientSocket(1337);
            JSONObject json = new JSONObject();
            String incoming;
            while (true) {
                System.out.println("Enter Message: ");

                json.put("test", br.readLine());

                mySocket.sendMessage(hostNameInet, portNumInt, json.toJSONString());

                incoming = mySocket.receiveMessage();
                System.out.println(incoming.toString());
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
}
