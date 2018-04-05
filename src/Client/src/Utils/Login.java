package Utils;

import Utils.JSONMesssages.LoginMessages;

import java.util.Scanner;


public class Login {

    private static LoginMessages lm = new LoginMessages();

    public void login(ClientSocket mySocket) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Casino Games!");
        System.out.println("Please login \n");

        System.out.print("Username: ");
        String username = scan.next();

        System.out.print("Password: ");
        String password = scan.next();

        boolean resp = lm.login(username, password, mySocket);
        if (!resp) {
            System.out.println("Username or password incorrect!");
            login(mySocket); // Recurse, try again

            // TODO: Possible login limiter here
        } else {
            System.out.println("Success!");
        }
    }
}
