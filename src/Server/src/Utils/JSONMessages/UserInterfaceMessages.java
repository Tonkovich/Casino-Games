package Utils.JSONMessages;

import Models.Games.Player;
import Models.Parts.CardGame.Card;
import Models.Parts.CardGame.Hand;

import javax.json.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UserInterfaceMessages {

    public String updateClients(double pot, Hand pHand, Hand hHand, boolean initialBettingRound
            , int smallBlind, int bigBlind, Collection<Double> playerBets, Collection<Player> players
            , double prevBet, Set<Integer> playerIDs, boolean gameDone, Map<Integer, Hand> playerHands) {

        Card card1 = pHand.getCards().get(0);
        Card card2 = pHand.getCards().get(1);

        // House hand can be variable
        JsonObjectBuilder houseHand = Json.createObjectBuilder();

        int i = 1;
        synchronized (hHand.getCards()) {
            for (Card c : hHand.getCards()) {
                houseHand.add("card" + i, Json.createObjectBuilder()
                        .add("suit", c.getSuit().getName())
                        .add("value", c.getCardValue().getVal())
                        .build()
                );
                i++;
            }
        }

        JsonArrayBuilder playerID = Json.createArrayBuilder();
        for (Integer w : playerIDs) {
            playerID.add(w);
        }

        JsonArray playerIDArray = playerID.build();

        JsonObjectBuilder allPlayerBets = Json.createObjectBuilder();
        int j = 0;
        for (Double d : playerBets) {
            allPlayerBets.add("player" + playerIDArray.getInt(j), d);
            j++;
        }

        JsonObjectBuilder allUsernames = Json.createObjectBuilder();

        for (Player p : players) {
            allUsernames.add("name" + p.getUserID(), p.getUsername());
        }

        JsonObjectBuilder allWallets = Json.createObjectBuilder();

        for (Player p : players) {
            allWallets.add("wallet" + p.getUserID(), p.getPlayerWallet());
        }

        JsonObjectBuilder finalCards = Json.createObjectBuilder();
        for (Integer w : playerHands.keySet()) {
            finalCards.add("player" + w, Json.createObjectBuilder()
                    .add("card1", Json.createObjectBuilder()
                            .add("suit", playerHands.get(w).getCards().get(0).getSuit().getName())
                            .add("value", playerHands.get(w).getCards().get(0).getCardValue().getVal())
                    )
                    .add("card2", Json.createObjectBuilder()
                            .add("suit", playerHands.get(w).getCards().get(1).getSuit().getName())
                            .add("value", playerHands.get(w).getCards().get(1).getCardValue().getVal())
                    )
            );
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
                .add("bigBlind", bigBlind)
                .add("smallBlind", smallBlind)
                .add("playerBets", allPlayerBets.build())
                .add("allUsernames", allUsernames.build())
                .add("allWallets", allWallets.build())
                .add("prevBet", prevBet)
                .add("playerIDs", playerIDArray)
                .add("gameDone", gameDone)
                .add("otherPlayersHands", finalCards.build())
                .build();
        return json.toString();
    }
}
