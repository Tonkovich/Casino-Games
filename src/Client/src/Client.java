import Parsers.ParseFactory;
import Utils.ClientSocket;
import Utils.Login;

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

    private static ClientSocket mySocket;
    private static Login userLogin = new Login();

    public static void main(String[] args) throws IOException {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        ParseFactory p = ParseFactory.getInstance(); // Singleton

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

            mySocket = new ClientSocket(1337);
            mySocket.setReceiverHost(hostNameInet);
            mySocket.setReceiverPort(Integer.parseInt(portNum));

            /*
             * End of start up of connections
             */

            // Login
            userLogin.login(mySocket);

            // Start displaying menus
            // TODO: Separate class and methods for exiting and etc

            while (true) {
                System.out.println("Enter Message: ");
                String incoming = br.readLine();

                // Build JSON object and add
                JsonObject json = Json.createObjectBuilder()
                        .add("test", incoming).build();

                // Send message to server and wait for receive
                mySocket.sendMessage(json.toString());

                // ReceiveMessage
                incoming = mySocket.receiveMessage();
                JsonReader jsonReader = Json.createReader(new StringReader(incoming));
                json = jsonReader.readObject();

                p.parse(json); // Interpret message
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
}
