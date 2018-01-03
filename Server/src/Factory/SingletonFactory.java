package Factory;

public class SingletonFactory {
    private Poker poker;
    private Slots slots;

    public Poker createPokerGame(){
        return new Poker();
    }

    public Slots createSlotsGame(){
        return new Slots();
    }
}
