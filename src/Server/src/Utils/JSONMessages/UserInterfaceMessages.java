package Utils.JSONMessages;

import Models.Parts.CardGame.Card;
import Models.Parts.CardGame.Hand;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class UserInterfaceMessages {

    public String updateClients(double pot, Hand pHand, Hand hHand) {
        Card card1 = pHand.getCards().get(0);
        Card card2 = pHand.getCards().get(1);

        // House hand can be variable
        JsonObjectBuilder houseHand = Json.createObjectBuilder();

        int i = 1;
        for (Card c : hHand.getCards()) {
            houseHand.add("card" + i, Json.createObjectBuilder()
                    .add("suit", c.getSuit().getName())
                    .add("value", c.getCardValue().getVal())).build();
            i++;
        }

        JsonObject json = Json.createObjectBuilder()
                .add("updateGUI", "updating gui..")
                .add("pot", pot)
                .add("playerHand", Json.createObjectBuilder()
                        .add("card1", Json.createObjectBuilder()
                                .add("suit", card1.getSuit().getName())
                                .add("value", card1.getCardValue().getVal())
                                .build()
                        )
                        .add("card2", Json.createObjectBuilder()
                                .add("suit", card2.getSuit().getName())
                                .add("value", card2.getCardValue().getVal())
                                .build()
                        )
                        .build()
                )
                .add("houseHand", houseHand.build())
                .build();
        return json.toString();
    }
}
