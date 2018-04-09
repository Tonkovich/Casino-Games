package Utils;

import Models.Player;
import Utils.JSONMesssages.HeartBeatMessage;

public class HeartBeat {

    private static HeartBeat instance;
    private HeartBeatSocket hbs = HeartBeatSocket.getInstance();
    private HeartBeatMessage hbm = new HeartBeatMessage();
    private Player p = Player.getInstance();
    private ClientSocket cs = ClientSocket.getInstance();

    public static HeartBeat getInstance() {
        if (instance == null) {
            instance = new HeartBeat();
        }
        return instance;
    }

    private HeartBeat() {
    }

    private boolean running = false;

    public void start() {
        if (!running) {
            running = true;
            t.start();
        }
    }

    public void stop() {
        running = false;
    }

    // Simple thread to just receive and send back to ensure connection
    Thread t = new Thread(() -> {
        hbs.setReceiverHost(cs.getReceiverHost());
        hbs.setReceiverPort(cs.getReceiverPort());
        while (running) {
            hbs.receiveMessage();
            hbs.sendMessage(hbm.heartBeatSend(p.getUserID()));
        }
    });

}
