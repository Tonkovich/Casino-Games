package Utils.Packet;

import Models.Games.Player;
import Utils.Database.Players;
import Utils.JSONMessages.HeartbeatMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

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
    private boolean running = false;
    private HeartbeatMessages hbm = new HeartbeatMessages();
    private static Players playersDB;
    private static final Logger log = LogManager.getLogger(HeartBeat.class);

    private static Map<Integer, Integer> waitingReply;

    public static HeartBeat getInstance() {
        if (instance == null) {
            instance = new HeartBeat();
            waitingReply = new HashMap<>();
            playersDB = Players.getInstance();
        }
        return instance;
    }

    private HeartBeat() {
    }

    // Is called when server finds a heartBeat message
    public void receive(JsonObject obj) {
        int userID = obj.getInt("userID");
        log.info(userID + " replied");
        waitingReply.remove(userID);
    }

    private void send(Player p) {
        p.sendHeartBeat(hbm.heartBeatSend());
        waitingReply.putIfAbsent(p.getUserID(), 3);
        log.info("Sending heartbeat to " + p.getUserID() + ".." + waitingReply.get(p.getUserID()));
    }

    private void disconnectUser(int userID) {
        playersDB.logoutPlayer(userID);
        // TODO: Just like what it says duh
        // Also read long comment above of how to implement
    }

    public void start() {
        if (!running) {
            running = true;
            t.start();
        }
    }

    public void stop() {
        running = false;
    }

    private void sleep() {
        try {
            Thread.sleep(5000); // 5 second standard
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
        }
    }

    // Might not be the best implementation. Using casting to avoid possible conversion performance hits
    Thread t = new Thread(() -> {
        log.info("Heartbeat started");
        while (running) {
            for (Object o : playersDB.getPlayers()) {
                Player p = (Player) o;
                send(p);
            }
            sleep(); // Pause to allow clients to reply

            // Clients have three missed chances
            for (Integer i : waitingReply.keySet()) {
                if (waitingReply.get(i) == 0) {
                    disconnectUser(i);
                } else {
                    int j = waitingReply.get(i) - 1;
                    waitingReply.put(i, j);
                }
            }

        }
        log.info("Heartbeat stopped: No users online");
    });

}
