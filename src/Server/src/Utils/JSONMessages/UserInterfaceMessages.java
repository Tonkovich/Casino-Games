package Utils.JSONMessages;

import Models.Games.Player;
import Models.Parts.CardGame.Card;
import Models.Parts.CardGame.Hand;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Collection;

public class UserInterfaceMessages {

    public String updateClients(double pot, Hand pHand, Hand hHand, boolean initialBettingRound
            , int numOfPlayers, int smallBlind, int bigBlind, Collection<Double> playerBets, Collection<Player> players) {
        Card card1 = pHand.getCards().get(0);
        Card card2 = pHand.getCards().get(1);

        // House hand can be variable
        JsonObjectBuilder houseHand = Json.createObjectBuilder();

        int i = 1;
        for (Card c : hHand.getCards()) {
            houseHand.add("card" + i, Json.createObjectBuilder()
                    .add("suit", c.getSuit().getName())
                    .add("value", c.getCardValue().getVal())
                    .build()
            );
            i++;
        }

        JsonObjectBuilder allPlayerBets = Json.createObjectBuilder();
        int j = 1;
        for (Double d : playerBets) {
            allPlayerBets.add("player" + j, d);
            j++;
        }

        JsonObjectBuilder allUsernames = Json.createObjectBuilder();

        int k = 1;
        for (Player p : players) {
            allUsernames.add("name" + k, p.getUsername());
            k++;
        }

        JsonObjectBuilder allWallets = Json.createObjectBuilder();

        int w = 1;
        for (Player p : players) {
            allWallets.add("wallet" + w, p.getPlayerWallet());
            w++;
        }

        JsonObject json = Json.createObjectBuilder()
                .add("updateGUI", "updating gui..")
                .add("pot", pot)
                .add("playerHand", Json.createObjectBuilder()
                        .add("card1", Json.createObjectBuilder()
                                .add("suit", card1.getSuit().getName())
                                .add("value", card1.getCardValue().getVal()).build()
                        ).add("card2", Json.createObjectBuilder()
                                .add("suit", card2.getSuit().getName())
                                .add("value", card2.getCardValue().getVal()).build()
                        ).build()
                )
                .add("houseHand", houseHand.build())
                .add("houseHandSize", i)
                .add("initialBettingRound", initialBettingRound)
                .add("numberOfPlayers", numOfPlayers)
                .add("bigBlind", bigBlind)
                .add("smallBlind", smallBlind)
                .add("playerBets", allPlayerBets.build())
                .add("playerBetsSize", playerBets.size())
                .add("allUsernames", allUsernames.build())
                .add("allWallets", allWallets.build())
                .build();

        return json.toString();
    }
}
