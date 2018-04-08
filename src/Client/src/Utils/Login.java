package Utils;

import Models.Player;
import Utils.JSONMesssages.LoginMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;


public class Login {

    private static LoginMessages lm = new LoginMessages();
    private static final Logger log = LogManager.getLogger(Login.class);
    private Player p = Player.getInstance();

    public void login(ClientSocket mySocket) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please login \n");

        System.out.print("Username: ");
        String username = scan.next();

        System.out.print("Password: ");
        String password = scan.next();
        System.out.print("\n\n");

        boolean resp = false;

        try {
            mySocket.sendMessage(lm.login(username, password));
            JsonReader jsonReader = Json.createReader(new StringReader(mySocket.receiveMessage()));
            // TODO Timer to disconnect, assume server crashed
            JsonObject obj = jsonReader.readObject();

            String login = obj.getString("login");
            switch (login) {
                case "reject":
                    break;
                case "success":
                    resp = true;
                    break;
                default:
                    break;
            }
            p.setUserID(obj.getInt("userID"));
            p.setPlayerWallet(obj.getInt("wallet"));


        } catch (IOException ex) {
            ex.getMessage();
        }

        if (!resp) {
            System.out.println("Username or password incorrect!");
            login(mySocket); // Recurse, try again

            // TODO: Possible login limiter here
        } else {
            log.info("Successful login!");
            log.info("Welcome to Casino Games!");
        }
    }
}
