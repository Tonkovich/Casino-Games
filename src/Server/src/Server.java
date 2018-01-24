import Utils.Message;
import Utils.ServerSocket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Server {
    /**
     * The server class will be mainly responsible for interpreting incoming client messages and sending the data to the
     * appropriate method/location
     */
    public static void main(String[] args) {

        int serverPort = 12000; // Default port
        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);   // Argument is the port number
        try {
            ServerSocket mySocket = new ServerSocket(serverPort);
            System.out.println("Server is ready");

            while (true) {
                Message request = mySocket.receiveMessageAndSender();
                String message = request.getMessage();

                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(message);

                /**
                 *
                 * switch(json.get("game"))
                 * case: "Poker"
                 *
                 * case: "Slots"
                 *
                 * call methods...do stuff
                 *
                 */
                mySocket.sendMessage(request.getAddress(), request.getPort(), json.toJSONString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
