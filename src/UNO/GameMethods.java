package UNO;

import java.util.Random;
import java.util.Scanner;


public class GameMethods {

    private static CardDeck cardDeck = new CardDeck();
    private static DiscardPile discardPile = new DiscardPile();
    private static PlayerList playerList = new PlayerList();
    private static boolean penaltyGiven; //NEW
    public static String color;
    private static Player currentPlayer;

    protected static Player previousPlayer;
    boolean isClockwise = true;
    static int currentPlayerIndex;
    static boolean blocked;

    protected static boolean chosenCardValid;

    public static boolean isChosenCardValid() {
        return chosenCardValid;
    }

    public static void setPreviousPlayer(Player previousPlayer) {
        GameMethods.previousPlayer = previousPlayer;
    }

    public static void setChosenCardValid(boolean chosenCardValid) {
        GameMethods.chosenCardValid = chosenCardValid;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }


    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(Player currentPlayer) {
        GameMethods.currentPlayer = currentPlayer;
    }

    public static DiscardPile getDiscardPile() {
        return discardPile;
    }

    public static String getColor() {
        return color;
    }

    public static void setColor(String color) {
        GameMethods.color = color;
    }

    public static boolean isPenaltyGiven() {
        return penaltyGiven;
    }

    public static void setPenaltyGiven(boolean penaltyGiven) {
        GameMethods.penaltyGiven = penaltyGiven;
    }

    public static boolean isBlocked() {
        return blocked;
    }

    public static void setBlocked(boolean blocked) {
        GameMethods.blocked = blocked;
    }


    public void prepareGame() {
        cardDeck.createCards(Type.YELLOW);
        cardDeck.createCards(Type.RED);
        cardDeck.createCards(Type.BLUE);
        cardDeck.createCards(Type.GREEN);
        cardDeck.createActionCards();
        cardDeck.shuffleCards();
        playerList.addPlayer();
        System.out.println();
        cardDeck.distributeCards(playerList, cardDeck);
        setBlocked(false);
        System.out.println();
        firstPlayer();
        System.out.println();
        putFirstCardOnTable();

    }

    // wird nur am Anfang des Spiels festgelegt
    public void firstPlayer() {
        System.out.println("Setting each player's turn...:");
        Random rand = new Random();
        int initialPlayerIndex = rand.nextInt(3);
        setCurrentPlayerIndex(initialPlayerIndex);
        setCurrentPlayer(getPlayerByIndex(initialPlayerIndex));
        //   setPreviousPayer(plisReverseCard());
        System.out.println(getPlayerByIndex(currentPlayerIndex).getName() + ",you start the game. ");
    }

    public void putFirstCardOnTable() {
        // todo: Regeln implementieren, falls erste Karte eine plus4, colorchange, reverse, oder pass card ist
        //Wenn eine Farbenwahl Karte zu Beginn des Spiels gezogen wird, entscheidet der Spieler zur Linken
        //des Gebers, welche Farbe als nächstes gelegt werden soll
        Card firstCard = cardDeck.dealCard();
        discardPile.addCardIn(0, firstCard);
    }

    public static void colorChangeCard() { //method for when a player used a COLORCHANGE card
        Player currentPlayer = getCurrentPlayer();
        Card playedCard = currentPlayer.getPlayedCard();
        try {
            if (playedCard.getType().equals(Type.PLUS_4) || playedCard.getType().equals(Type.COLORCHANGE)) {
                System.out.println(currentPlayer.getName() + " please choose a color: ");
                Scanner input = new Scanner(System.in);
                String color = input.nextLine();
                setColor(color);
            }
        } catch (
                NullPointerException e) { //NPE can happen if current player has a valid card to play but inputs a card that she does not have in hand.
            System.out.printf(currentPlayer.getName() + ", you just made an invalid move!");
            currentPlayer.cardsInHand.add(cardDeck.dealCard());
            System.out.println("You have to take a card as a penalty and you are now blocked from making further moves.");

        }
    }

    public void checkIfCurrentPlayerMustBePenalized() {
        Player currentPlayer = getCurrentPlayer();
        Card card = discardPile.showLastCard();

        if (card.getType().equals(Type.PLUS_4) && !isPenaltyGiven()) {
            System.out.println(getCurrentPlayer().getName() + ", you get 4 penalty cards and you are block from playing this turn.");
            plus4Card();
            currentPlayer.printCardsInHand();
            setPenaltyGiven(true); //this is to tell the program that the penalty has been "claimed"
            setBlocked(true); //this is to block the player from making a turn.
            setPreviousPlayer(getPlayerByIndex(currentPlayerIndex));
        } else if ((card.getType().equals(Type.RED_PLUS2) || card.getType().equals(Type.YELLOW_PLUS2)
                || card.getType().equals(Type.GREEN_PLUS2) || card.getType().equals(Type.BLUE_PLUS2)) && !isPenaltyGiven()) {
            System.out.println(getCurrentPlayer().getName() + ", you get 2 penalty cards and you are block form playing this turn.");
            plus2Card();
            currentPlayer.printCardsInHand();
            setPenaltyGiven(true);
            setBlocked(true);
            setPreviousPlayer(getPlayerByIndex(currentPlayerIndex));
        }
    }

    public void plus4Card() { //simple but important method to be called in the checkIfCurrentPlayerMustBePenalized() method.
        Player currentPlayer = getCurrentPlayer();
        Card topDiscardCard = discardPile.showLastCard();
        if (topDiscardCard.getType().equals(Type.PLUS_4)) {
            currentPlayer.takeCard(cardDeck.dealCard());
            currentPlayer.takeCard(cardDeck.dealCard());
            currentPlayer.takeCard(cardDeck.dealCard());
            currentPlayer.takeCard(cardDeck.dealCard());
        }
    }

    public void plus2Card() { //simple but important method to be called in the checkIfCurrentPlayerMustBePenalized() method.
        Player currentPlayer = getCurrentPlayer();
        Card topDiscardCard = discardPile.showLastCard();

        if (topDiscardCard.getType().equals(Type.RED_PLUS2) || topDiscardCard.getType().equals(Type.YELLOW_PLUS2)
                || topDiscardCard.getType().equals(Type.GREEN_PLUS2) || topDiscardCard.getType().equals(Type.BLUE_PLUS2)) {
            currentPlayer.takeCard(cardDeck.dealCard());
            currentPlayer.takeCard(cardDeck.dealCard());
        }
    }


    //Methode, um zu überprüfen, ob die gespielte Karte überhaupt gespielt werden darf:
    public boolean chosenCardValidityCheck() { //card validation
        Player currentPlayer = getCurrentPlayer();
        boolean chosenCardValid = false;
        Card card = currentPlayer.getPlayedCard();
        Card discard = discardPile.showLastCard();

        if(card.getType().equals(Type.COLORCHANGE) || card.getType().equals(Type.PLUS_4)) {
            chosenCardValid = true;
        } else if (discard.getType().equals(Type.COLORCHANGE) || discard.getType().equals(Type.PLUS_4)) {
            if (card.getType().name().charAt(0) == getColor().charAt(0)) {
                chosenCardValid = true;
            } else {
                chosenCardValid = false;
            }
        } else if (discard.getType().equals(Type.GREEN) || discard.getType().equals(Type.YELLOW)
                || discard.getType().equals(Type.RED) || discard.getType().equals(Type.YELLOW)) {
            if (discard.getNumber() == card.getNumber() || discard.getType().name().charAt(0) == card.getType().name().charAt(0)) {
                chosenCardValid = true;
            } else {
                chosenCardValid = false;
            }
        } else if (discard.getType().equals(card.getType()) || card.getType().name().charAt(0) == discard.getType().name().charAt(0)
                || card.getType().equals(Type.PLUS_4) || card.getType().equals(Type.COLORCHANGE)
                || isSameColor() || passCardCheck() || plus2Check()) {
            chosenCardValid = true;
        } else { //if player chose a card that is not valid based on what is on the top of discard deck
            chosenCardValid = false;
            System.out.println("Sorry, this is not a valid move. Now you have to draw a penalty card!");
            currentPlayer.cardsInHand.add(cardDeck.dealCard());
        }
        setChosenCardValid(chosenCardValid);
        return chosenCardValid;
    }


    //Methode, die überprüft, ob eine Reversekarte oben auf dem DiscardPile liegt
    public int isReverseCard() { //This method is to decide who has the next turn when the card "<->" is played
        int indexOfTheCurrentPlayer = getCurrentPlayerIndex(); //index of the current player

        if (indexOfTheCurrentPlayer == 0) {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 3;
                isClockwise = false;
            } else {
                indexOfTheCurrentPlayer = 1;
                isClockwise = true;
            }
        } else if (indexOfTheCurrentPlayer == 3) {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 2;
                isClockwise = false;
            } else {
                indexOfTheCurrentPlayer = 0;
                isClockwise = true;
            }
        } else {
            if (isClockwise) {
                indexOfTheCurrentPlayer--;
                isClockwise = false;
            } else {
                indexOfTheCurrentPlayer++;
                isClockwise = true;
            }
        }
        return indexOfTheCurrentPlayer;
    }


    public int isPassCard() { //This method is to decide who has the next turn when the passcard  is played
        //boolean nicht besser?
        int indexOfTheCurrentPlayer = getCurrentPlayerIndex(); //index of the current player

        if (indexOfTheCurrentPlayer == 1) {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 3;
            } else {
                indexOfTheCurrentPlayer = 3;
            }
        } else if (indexOfTheCurrentPlayer == 3) {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 1;
            } else {
                indexOfTheCurrentPlayer = 1;
            }
        } else if (indexOfTheCurrentPlayer == 2) {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 0;
            } else {
                indexOfTheCurrentPlayer = 0;
            }
        } else {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 2;
            } else {
                indexOfTheCurrentPlayer = 2;

            }
        }
        return indexOfTheCurrentPlayer;
    }

    public int isRegularCard() { //This method is to decide who has the next turn when a normal card is played
        int indexOfTheCurrentPlayer = getCurrentPlayerIndex(); //index of the current player

        if (indexOfTheCurrentPlayer == 1) {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 2;
            } else {
                indexOfTheCurrentPlayer = 0;
            }
        } else if (indexOfTheCurrentPlayer == 0) {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 1;
            } else {
                indexOfTheCurrentPlayer = 3;
            }
        } else if (indexOfTheCurrentPlayer == 3) {
            if (isClockwise) {
                indexOfTheCurrentPlayer = 0;
            } else {
                indexOfTheCurrentPlayer = 2;
            }
        } else {
            if (isClockwise) {
                indexOfTheCurrentPlayer++;
            } else {
                indexOfTheCurrentPlayer--;

            }
        }
        return indexOfTheCurrentPlayer;
    }


    public void playerPlaysCard() {
        Player currentPlayer = getCurrentPlayer();
        Scanner input = new Scanner(System.in);

        if (discardPile.getDiscardPile().size() == 1) {
            initialPlayerPlaysCard();
        } else {
            checkIfCurrentPlayerMustBePenalized(); //before a player makes a move, it will be check if the player must receive a penalty.
            if (!isBlocked()) {
                if (hasValidCardToPlay()) {
                    System.out.println(currentPlayer);
                    currentPlayer.printCardsInHand();
                    System.out.println(currentPlayer.getName() + " , your move! Type in the ID of the card you would like to play: ");
                    int intCardID = input.nextInt();
                    Card cardToPlay = currentPlayer.getCardByID(intCardID);
                    currentPlayer.setPlayedCard(cardToPlay);
                    colorChangeCard();
                } else {
                    System.out.println("Sorry, " + currentPlayer.getName() + ", you don't have a valid card to play. Please draw a card.");
                    //current player nimmt eine Karte vom Deck und fügt sie seinen Karten hinzu
                    currentPlayer.cardsInHand.add(cardDeck.dealCard());
                    currentPlayer.printCardsInHand();
                    if (hasValidCardToPlay()) {
                        // remove card from hand, add to card to discard pile = play this card
                        System.out.println(currentPlayer.getName() + " , your move! Type in the ID of the card you would like to play");
                        int intCardID = input.nextInt();
                        Card cardToPlay = currentPlayer.getCardByID(intCardID);
                        currentPlayer.setPlayedCard(cardToPlay);
                        colorChangeCard();
                    } else {
                        System.out.println("Sorry, " + currentPlayer.getName() + " you STILL don't have a card to play out this turn.");
                    }
                }
            }
        }
    }

    public static Player getPlayerByIndex(int playerIndex) {
        Player result;
        result = playerList.getPlayerlist().get(playerIndex);
        return result;
    }

    public void nextTurn() {
        Card topCard = discardPile.showLastCard();

        try {
            if (topCard.getType().equals(Type.YELLOW_REVERSE) || topCard.getType().equals(Type.BLUE_REVERSE)
                    || topCard.getType().equals(Type.RED_REVERSE) || topCard.getType().equals(Type.GREEN_REVERSE)) {
                currentPlayerIndex = isReverseCard();
            } else if (topCard.getType().equals(Type.YELLOW_PASS) || topCard.getType().equals(Type.BLUE_PASS)
                    || topCard.getType().equals(Type.RED_PASS) || topCard.getType().equals(Type.GREEN_PASS)) {
                currentPlayerIndex = isPassCard();
            } else {
                currentPlayerIndex = isRegularCard();
            }
        } catch (NullPointerException e) {
            System.out.println("The previous player skipped a turn because he/she is penalized or blocked."); //or has no card to play
            currentPlayerIndex = isRegularCard();
        }
        setCurrentPlayer(getPlayerByIndex(currentPlayerIndex));
    }


    public void initialPlayerPlaysCard() { //change the method name ....this will be run just once every round
        Player currentPlayer = getCurrentPlayer();

        int currentPlayerIndex = getCurrentPlayerIndex();
        int totalPlayers = playerList.getPlayerlist().size();
        // Calculate the previous player index using modulo to wrap around to the last index when currentPlayerIndex is 0.
        int previousPlayerIndex = (currentPlayerIndex - 1 + totalPlayers) % totalPlayers;
        // Set the previous player
        setPreviousPlayer(playerList.getPlayerByID(previousPlayerIndex));

        Scanner input = new Scanner(System.in);
        Card cardToPlay = null;
        Card firstCard = discardPile.showLastCard();

        while (firstCard.getType().equals((Type.PLUS_4))) {
            cardDeck.add(firstCard);
            cardDeck.shuffleCards();
            putFirstCardOnTable();
        }

        if (firstCard.getType().equals(Type.RED_PASS) || firstCard.getType().equals(Type.GREEN_PASS) ||
                firstCard.getType().equals(Type.BLUE_PASS) || firstCard.getType().equals(Type.YELLOW_PASS)) {
            System.out.println(currentPlayer.getName() + ", you have skip  this turn.");
            setPreviousPlayer(getPlayerByIndex(currentPlayerIndex));
            setBlocked(true);
        } else if (firstCard.equals(Type.RED_PLUS2) || firstCard.equals(Type.YELLOW_PLUS2)
                || firstCard.equals(Type.GREEN_PLUS2) || firstCard.equals(Type.BLUE_PLUS2)) {
            System.out.println(currentPlayer.getName() + ", you have to draw 2 penalty cards and have to skip this turn.");
            plus2Card();
            setPreviousPlayer(getPlayerByIndex(currentPlayerIndex));
            setBlocked(true);
        } else if (firstCard.getType().equals(Type.COLORCHANGE)) { // current player will set the color but the player on the left (nextPlayer) will resume the game
            System.out.println(currentPlayer.getName() + ", choose a color:");
            String color = input.nextLine().toUpperCase();
            setColor(color);
            currentPlayer.setPlayedCard(firstCard); // player didn't play any card. just set/added the newColor for the COLORCHANGE card
            setPreviousPlayer(getPlayerByIndex(currentPlayerIndex));
            setBlocked(true);
        } else {
            if (hasValidCardToPlay() && !isBlocked()) {
                currentPlayer.printCardsInHand();
                System.out.println(currentPlayer.getName() + " ,your move! Type in the ID of the card you would like to play");
                int intCardID = input.nextInt();
                cardToPlay = currentPlayer.getCardByID(intCardID);
                currentPlayer.setPlayedCard(cardToPlay);
                colorChangeCard(); //to handle COLORCHANGE cards in case player used it
            } else {
                System.out.println("Sorry, " + currentPlayer.getName() + ", you don't have a valid card to play. Please draw a card.");
                //current player nimmt eine Karte vom Deck und fügt sie seinen Karten hinzu
                currentPlayer.cardsInHand.add(cardDeck.dealCard());
                System.out.println(currentPlayer.cardsInHand);
                if (hasValidCardToPlay()) {
                    // remove card from hand, add to card to discard pile = play this card
                    System.out.println(currentPlayer.getName() + " ,your move! Type in the ID of the card you would like to play");
                    int intCardID = input.nextInt();
                    cardToPlay = currentPlayer.getCardByID(intCardID);
                    currentPlayer.setPlayedCard(cardToPlay);
                    colorChangeCard();
                } else {
                    System.out.println("Sorry, " + currentPlayer.getName() + " you don't have a card to play out this turn.");
                }
            }
        }
    }

    public void printTopCardOfDiscardPile() { // just used another color so it is easier to find it on the console
        Card card = discardPile.showLastCard();
        String specialFontColor = "\u001B[35m"; // ANSI escape sequence for pink color
        String resetDefaultFontColor = "\u001B[0m"; // Reset the color back to default

        System.out.println();

        System.out.print(specialFontColor + "DISCARD PILE: ");
        if ((card.getType().equals(Type.COLORCHANGE) || card.getType().equals(Type.PLUS_4)) && !getColor().isEmpty()) { //if COLORCHANGE, the newColor must be printed too.
            System.out.print(card + " New Color: " + getColor());
        } else if (!card.getType().equals("Color")) {
            System.out.print(card);
        } else {
            System.out.print(card + " New Color: " + getColor() + ",  " + card.getNumber());
        }
        System.out.println(resetDefaultFontColor);
    }

    public boolean isSameColor() {
        Player currentPlayer = getCurrentPlayer();
        Card c1 = currentPlayer.getPlayedCard();
        Card c2 = discardPile.showLastCard();
        boolean samecolor = false;
        if (c2.getType().equals(Type.PLUS_4) || c2.getType().equals(Type.COLORCHANGE)) {
            if (c1.getType().name().charAt(0) == getColor().charAt(0)) {
                samecolor = true;
            }
        } else {
            char firstLetterCard1 = c1.getType().name().charAt(0);
            char firstLetterCard2 = c2.getType().name().charAt(0);
            if (firstLetterCard1 == firstLetterCard2) {
                samecolor = true;
            }
        }
        return samecolor;
    }

    public boolean isSameColorWithCardInHand() {
        Player currentPlayer = getCurrentPlayer();
        Card topcard = discardPile.showLastCard();
        boolean topCardIsSameColorWithCardInHand = false;

        for (Card card : currentPlayer.getCardsInHand()) {
            if (topcard.getType().equals(Type.PLUS_4) || topcard.getType().equals(Type.COLORCHANGE)) {
                if (card.getType().name().charAt(0) == getColor().charAt(0)) {
                    topCardIsSameColorWithCardInHand = true;
                    break;
                }
            } else {
                char firstLetterCard1 = card.getType().name().charAt(0);
                char firstLetterCard2 = topcard.getType().name().charAt(0);
                if (firstLetterCard1 == firstLetterCard2) {
                    topCardIsSameColorWithCardInHand = true;
                    break;
                } else {
                    topCardIsSameColorWithCardInHand = false;
                }
            }
        }
        return topCardIsSameColorWithCardInHand;
    }

    public boolean passCardCheck() {
        Player currentPlayer = getCurrentPlayer();
        Card c1 = currentPlayer.getPlayedCard();
        Card c2 = discardPile.showLastCard();
        boolean botharepassCards = false;
        boolean lastLetterCard1 = c1.getType().name().endsWith("PASS");
        boolean lastLetterCard2 = c2.getType().name().endsWith("PASS");
        if (lastLetterCard1 == true && lastLetterCard2 == true) {
            botharepassCards = true;
        }
        return botharepassCards;
    }

    public boolean PassCardCheckCardInHand() {
        Player currentPlayer = getCurrentPlayer();
        Card topcard = discardPile.showLastCard();
        boolean botharepassCards = false;

        for (Card card : currentPlayer.getCardsInHand()) {
            boolean lastLetterCard1 = card.getType().name().endsWith("PASS");
            boolean lastLetterCard2 = topcard.getType().name().endsWith("PASS");
            if (lastLetterCard1 == true && lastLetterCard2 == true) {
                botharepassCards = true;
                break;
            } else {
                botharepassCards = false;
            }
        }
        return botharepassCards;
    }

    public boolean plus2Check() {
        Player currentPlayer = getCurrentPlayer();
        Card c1 = currentPlayer.getPlayedCard();
        Card c2 = discardPile.showLastCard();
        boolean botharepassCards = false;
        boolean lastLetterCard1 = c1.getType().name().endsWith("2");
        boolean lastLetterCard2 = c2.getType().name().endsWith("2");
        if (lastLetterCard1 == true && lastLetterCard2 == true) {
            botharepassCards = true;
        }
        return botharepassCards;
    }


    public boolean plus2CheckCardInHand() {
        Player currentPlayer = getCurrentPlayer();
        Card topcard = discardPile.showLastCard();
        boolean bothArePlus2Cards = false;

        for (Card card : currentPlayer.getCardsInHand()) {
            boolean lastLetterCard1 = card.getType().name().endsWith("2");
            boolean lastLetterCard2 = topcard.getType().name().endsWith("2");
            if (lastLetterCard1 == true && lastLetterCard2 == true) {
                bothArePlus2Cards = true;
                break;
            } else {
                bothArePlus2Cards = false;
            }
        }
        return bothArePlus2Cards;
    }


    public boolean hasValidCardToPlay() {
        boolean isValid = false;
        Player currentPlayer = getCurrentPlayer();
        Card discard = getDiscardPile().showLastCard();

        for (Card card : currentPlayer.cardsInHand) {
            if (getColor() != null) {
                if (discard.getType().equals(card.getType()) || card.getType().name().charAt(0) == getColor().charAt(0)
                        || card.getType().equals(Type.PLUS_4) || card.getType().equals(Type.COLORCHANGE)) {
                    isValid = true;
                    break;
                } else if (discard.getType().equals(Type.GREEN) || discard.getType().equals(Type.YELLOW)
                        || discard.getType().equals(Type.RED) || discard.getType().equals(Type.YELLOW)) {
                    if (discard.getNumber() == card.getNumber()) {
                        isValid = true;
                        break;
                    }
                } else {
                    isValid = false;
                }
            } else {
                if (discard.getType().equals(card.getType())  || card.getType().equals(Type.PLUS_4)
                        || card.getType().equals(Type.COLORCHANGE) || card.getType().name().charAt(0) == discard.getType().name().charAt(0)
                        || isSameColorWithCardInHand() || PassCardCheckCardInHand() || plus2CheckCardInHand()) {
                    isValid = true;
                } else if (discard.getType().equals(Type.GREEN) || discard.getType().equals(Type.YELLOW)
                        || discard.getType().equals(Type.RED) || discard.getType().equals(Type.YELLOW)) {
                    if (discard.getNumber() == card.getNumber()) {
                        isValid = true;
                        break;
                    }
                } else {
                    isValid = false;
                }
            }
        }
        return isValid;
    }


    public static void acceptPlayersInput() { //this method will take the playedCard from the player's hand and add it to the DISCARD DECK.
        Player currentPlayer = getCurrentPlayer();
        Card playedCard = currentPlayer.getPlayedCard();
        if (isChosenCardValid()) {
            if (playedCard.getType().equals(Type.PLUS_4) || playedCard.getType().equals(Type.BLUE_PLUS2) ||
                    playedCard.getType().equals(Type.GREEN_PLUS2) || playedCard.getType().equals(Type.RED_PLUS2)
                    || playedCard.getType().equals(Type.YELLOW_PLUS2)) {
                setBlocked(false);
                setPenaltyGiven(false);
                currentPlayer.setPlayedCard(null);
            } else {
                setBlocked(false); //IMPORTANT! Resets this value after every player's turn
                setPenaltyGiven(true);
                currentPlayer.setPlayedCard(null);
            }
            discardPile.addCardIn(0, playedCard);
            currentPlayer.cardsInHand.remove(playedCard);
        } else {
            System.out.println("Chosen card is invalid!!"); //just put this one here so i know if i got in this part of the code
        }
    }

    public Player getPreviousPlayer() {
        int currentPlayerIndex = getCurrentPlayerIndex();
        if (currentPlayerIndex == 0) {
            if (isClockwise) {
                currentPlayerIndex = 3;
            } else {
                currentPlayerIndex++;
            }
        } else if (currentPlayerIndex == 3) {
            if (isClockwise) {
                currentPlayerIndex = 2;
            } else {
                currentPlayerIndex = 0;
            }
        } else {
            if (isClockwise) {
                currentPlayerIndex--;
            } else {
                currentPlayerIndex++;
            }
        }
        Player previousPlayer = playerList.getPlayerByID(currentPlayerIndex);
        return previousPlayer;
    }

    public static void resetColorToDefault() {
        if (discardPile.getSizeofDiscardPile() > 1) {
            if (discardPile.getDiscardPile().get(1).getType().equals(Type.COLORCHANGE)
                    || discardPile.getDiscardPile().get(1).getType().equals(Type.PLUS_4)) {
                setColor(null);
            }
        }
    }

}
