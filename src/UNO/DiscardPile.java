package UNO;


import java.util.ArrayList;


public class DiscardPile {
    protected static ArrayList<Card> discardPile;

    public DiscardPile() {
        this.discardPile = new ArrayList<>();
    }


    public void addCard(Card c) {
        discardPile.add(c);
    }

    public void addCardIn(int index, Card c) { //this is to force the game to place the discarded card at position 0.
        if (c != null) {
            discardPile.add(index, c);
        }else {
            System.out.println("no card added to the discard pile");
        }
    }
   /*
    public Card showLastCard() {
        return discardPile.get(discardPile.size());
    } */

    public Card showLastCard() {// changed this method to avoid so many Exceptions (out of bounds, null pointer)
        if (discardPile.get(0) == null) { // incase a null (Card) is at position 0;
            discardPile.remove(0);
        }
        return discardPile.get(0);
    }
    public int getSizeofDiscardPile(){
        return discardPile.size();
    }


    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }

    //erste Karte wird aufgedeckt


}


