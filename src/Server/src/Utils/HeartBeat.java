package Utils;

import Models.Games.Player;
import Utils.JSONMessages.HeartbeatMessages;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class HeartBeat {
    // TODO: Make thread that iterates through all players logged in
    /**
     * Should use thread, timer, and etc to check constantly for success messages.
     * If player is not connected they should be logged out automatically, saved, and thats it
     * <p>
     * Should implement methods where if in a game, removed from game, sent client updates,
     * and pot should remain the same and game should not be interrupted.
     * <p>
     * Game should however be closed if less than two players, all money returned and "Sorry bud" message.
     */

    private static HeartBeat instance;
    private boolean running = true;
    private Players playersDB = Players.getInstance();
    private HeartbeatMessages hbm = new HeartbeatMessages();

    private static List<Integer> waitingReply;

    public static HeartBeat getInstance() {
        if (instance == null) {
            instance = new HeartBeat();
            waitingReply = new ArrayList<>();
        }
        return instance;
    }

    private HeartBeat() {
    }

    // Is called when server finds a heartBeat message
    public void receive(JsonObject obj) {
        int userID = obj.getInt("userID");
        waitingReply.remove(userID);
    }

    private void send(Player p) {
        p.sendMessage(hbm.heartBeatSend());
        waitingReply.add(p.getUserID());
    }

    private void DisconnectUser(int userID) {
        playersDB.logoutPlayer(userID);
        // TODO: Just like what it says duh
        // Also read long comment above of how to implement
    }

    public void start() {
        if (!running) {
            t.start();
            running = true;
        }
    }

    public void stop() {
        running = false;
    }

    // Might not be the best implementation. Using casting to avoid possible conversion performance hits
    Thread t = new Thread(() -> {
        while (true) {
            for (Object o : playersDB.getPlayers()) {
                Player p = (Player) o;
                send(p);
            }

            // TODO: sleep thread after all message sent, then after time, check waitingReply for still waiting
            // TODO: disconnect them after said time has past and still exist
            for (Integer i : waitingReply) {
                // Look for waiting to reply players
            }
        }
    });

}
