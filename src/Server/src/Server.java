import Parsers.ParseStore;
import Utils.Database;
import Utils.DatabaseException;
import Utils.Message;
import Utils.ServerSocket;
import org.apache.commons.io.FileUtils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;


public class Server {
    /**
     * The server class will be mainly responsible for interpreting incoming client messages and sending the data to the
     * appropriate method/location
     */
    public static void main(String[] args) throws DatabaseException, IOException {
        ParseStore ps = ParseStore.getInstance();

        // Parse config
        File file = new File("src/Server/config.json");
        String jsonConfig = FileUtils.readFileToString(file, "UTF-8");
        JsonReader configReader = Json.createReader(new StringReader(jsonConfig));
        JsonObject config = configReader.readObject();
        String url = config.getString("JDBC-URL");
        String user = config.getString("JDBC-USER");
        String pass = config.getString("JDBC-PASS");

        // Connect to database
        Database db = new Database(url, user, pass);

        int serverPort = 12000; // Default port
        if (args.length == 1)
            serverPort = Integer.parseInt(args[0]);   // Argument is the port number
        try {
            ServerSocket mySocket = new ServerSocket(serverPort);
            System.out.println("Server is ready");

            // Game loop waiting for messages
            while (true) {
                Message request = mySocket.receiveMessageAndSender();
                String message = request.getMessage();

                // Take incoming message and convert to JSON object
                JsonReader jsonReader = Json.createReader(new StringReader(message));
                JsonObject json = jsonReader.readObject();

                ps.parse(json); // Parse
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
