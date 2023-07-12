package UNO;

import java.util.Scanner;
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
                    for (Card card : currentPlayer.cardsInHand) {
                        if (getColor() != null) {
                            if (discard.getType().equals(card.getType()) || card.getType().name().charAt(0) == getColor().charAt(0)
                                    || card.getType().equals(Type.PLUS_4) || card.getType().equals(Type.COLORCHANGE)) {
                                cardToPlay = card;
                                cardToPlay.toString();
                                TimeUnit.SECONDS.sleep(1);
                                break;
                            } else if (discard.getType().equals(Type.GREEN) || discard.getType().equals(Type.YELLOW)
                                    || discard.getType().equals(Type.RED) || discard.getType().equals(Type.YELLOW)) {
                                if (discard.getNumber() == card.getNumber()) {
                                    cardToPlay = card;
                                    cardToPlay.toString();
                                    TimeUnit.SECONDS.sleep(1);
                                    break;
                                }
                            }
                        } else {
                            if (discard.getType().equals(card.getType()) || card.getType().equals(Type.PLUS_4)
                                    || card.getType().equals(Type.COLORCHANGE) || card.getType().name().charAt(0) == discard.getType().name().charAt(0)
                                    || isSameColorWithCardInHand() || PassCardCheckCardInHand() || plus2CheckCardInHand()) {
                                cardToPlay = card;
                                cardToPlay.toString();
                                TimeUnit.SECONDS.sleep(1);
                                break;
                            } else if (discard.getType().equals(Type.GREEN) || discard.getType().equals(Type.YELLOW)
                                    || discard.getType().equals(Type.RED) || discard.getType().equals(Type.YELLOW)) {
                                if (discard.getNumber() == card.getNumber()) {
                                    cardToPlay = card;
                                    cardToPlay.toString();
                                    TimeUnit.SECONDS.sleep(1);
                                    break;
                                }
                            } else {
                                System.out.println("No card to play..."); //this will be displayed his hasCardToPlay() failed to do its task
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
        System.out.println("Sorry, something went wrong. Please try again.");
        }
        return cardToPlay;
    }
}