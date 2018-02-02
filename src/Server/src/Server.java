import Models.Games.Poker;
import Utils.Message;
import Utils.ServerSocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;


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

                // Take incoming message and convert to JSON object
                JsonReader jsonReader = Json.createReader(new StringReader(message));
                JsonObject json = jsonReader.readObject();

                // Read from the JSON Object, in this case key test
                System.out.print(json.getString("test"));

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


                mySocket.sendMessage(request.getAddress(), request.getPort(), json.toString() + " Return");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void pokerLogic(Poker poker, int userID) {

    }

    public void slotLogic() {

    }
}
