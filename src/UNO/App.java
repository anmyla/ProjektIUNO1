package UNO;

import java.io.PrintStream;
import java.util.Scanner;

import static UNO.GameMethods.*;
import static UNO.GameMethods.resetColorToDefault;

public class App {
    private final Scanner input;
    private final PrintStream output;
    private boolean exit = false;


    GameMethods gameMethods = new GameMethods();

    public App(Scanner input, PrintStream output){
        this.input = input;
        this.output = output;
    }

    //die Gameloop
    public void Run() {
        initialize();
        printState();

        while(!exit) {
            readUserInput();
            updateState();
            printState();
        }
    }

    private void initialize() {
        gameMethods.prepareGame();
    }

    private void readUserInput() {
        gameMethods.playerPlaysCard();
    }

    private void updateState() {
        if(getCurrentPlayer().getPlayedCard() != null) { // if the current player made a "move"
            gameMethods.chosenCardValidityCheck(); // her "move" will be checked
            acceptPlayersInput(); // if her "move" is valid, it will be taken out of her hand and placed on to the discard pile.
        }
        else {
            setBlocked(false); //in case the currentPlayer could not make a move, block is set to default so next player can play.
        }

        resetColorToDefault(); // this method will reset the chosen color to NULL when the COLORCHANGE/PLUS4 card is no longer on top of the discard deck.
        gameMethods.nextTurn(); // decide which player is going to play next
    }


    private void printState() {
        gameMethods.printTopCardOfDiscardPile();

    }

}