import Graphics.GameMenu;
import Parsers.ParseFactory;
import Utils.ClientSocket;
import Utils.HeartBeatSocket;
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
    private static HeartBeatSocket hbs = HeartBeatSocket.getInstance();
    private static Login userLogin = new Login();
    private static GameMenu menu = new GameMenu();

    public static void main(String[] args) throws IOException {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        ParseFactory p = ParseFactory.getInstance(); // Singleton

        /*
         * Initial start up of connections
         */

        try {
            System.out.print("Please enter server IP: ");
            String hostName = br.readLine();

            if (hostName.length() == 0)
                hostName = "localhost";

            InetAddress hostNameInet = InetAddress.getByName(hostName);

            System.out.print("Please enter server port: ");
            String portNum = br.readLine();

            if (portNum.length() == 0)
                portNum = "12000";

            mySocket = ClientSocket.getInstance();
            mySocket.setReceiverHost(hostNameInet);
            mySocket.setReceiverPort(Integer.parseInt(portNum));

            /*
             * End of start up of connections
             * TODO: Test for connection, send, and wait
             */

            // Login
            userLogin.login();

            // Initial display of games to play
            menu.display();

            while (true) {
                // ReceiveMessage
                String incoming = mySocket.receiveMessage();
                JsonReader jsonReader = Json.createReader(new StringReader(incoming));
                JsonObject json = jsonReader.readObject();
                p.parse(json);
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
}
