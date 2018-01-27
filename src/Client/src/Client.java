import Utils.ClientSocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
    /**
     * No idea what is going to become of the client just yet
     */
    public static void main(String[] args) throws IOException {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);

        /*
         * Initial start up of connections
         */

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

            /*
             * End of start up of connections
             */

            while (true) {
                System.out.println("Enter Message: ");
                String incoming = br.readLine();

                // Build JSON object and add
                JsonObject json = Json.createObjectBuilder()
                        .add("test", incoming).build();

                // Send message to server and wait for receive
                mySocket.sendMessage(hostNameInet, portNumInt, json.toString());
                incoming = mySocket.receiveMessage();

                // Convert Incoming message to JSON object
                JsonReader jsonReader = Json.createReader(new StringReader(incoming));
                json = jsonReader.readObject();

                // Print out message
                System.out.println(json.getString("test"));
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
}
