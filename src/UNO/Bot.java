package UNO;
import java.util.concurrent.TimeUnit;

import static UNO.GameMethods.*;

public class Bot extends Player {
    public Bot(String name) {
        super(name);
    }

    public static Card botPlaysCard() {
        Player currentPlayer = getCurrentPlayer();
        Card discard = getDiscardPile().showLastCard();
        Card cardToPlay = null;
        try {
            if (!isBlocked()) {
                if (hasValidCardToPlay()) {
                    for (Card card : currentPlayer.cardsInHand) { //the bot's hand will be searched for possible card to play
                        if (discard.getType().equals(card.getType()) || card.getType().equals(Type.PLUS_4)
                                || card.getType().equals(Type.COLORCHANGE) || card.getType().name().charAt(0) == discard.getType().name().charAt(0)
                                || (discard.getType().name().endsWith("PASS") && card.getType().name().endsWith("PASS"))
                                || (discard.getType().name().endsWith("2") && card.getType().name().endsWith("2"))
                                || (discard.getType().name().endsWith("REVERSE") && card.getType().name().endsWith("REVERSE"))) {
                            cardToPlay = card;
                            System.out.println(cardToPlay);
                            TimeUnit.SECONDS.sleep(1); //sets delay, so we can follow what's going on. can be deleted later.
                            break;
                        } else if (discard.getType().equals(Type.GREEN) || discard.getType().equals(Type.YELLOW)
                                || discard.getType().equals(Type.RED) || discard.getType().equals(Type.BLUE)) {
                            if (discard.getNumber() == card.getNumber()) {
                                cardToPlay = card;
                                System.out.println(cardToPlay);
                                TimeUnit.SECONDS.sleep(1);
                                break;
                            }
                        } else if (discard.getType().equals(Type.COLORCHANGE) || discard.getType().equals(Type.PLUS_4)) {
                            if (discard.getType().equals(card.getType()) || card.getType().name().charAt(0) == getColor().charAt(0)
                                    || card.getType().equals(Type.PLUS_4) || card.getType().equals(Type.COLORCHANGE)) {
                                cardToPlay = card;
                                System.out.println(cardToPlay);
                                TimeUnit.SECONDS.sleep(1);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return cardToPlay;
    }
}