package UNO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import static UNO.GameMethods.setCurrentPlayer;


public class PlayerList {


    protected static ArrayList<Player> playerlist = new ArrayList<>();

    public ArrayList<Player> getPlayerlist() {
        return playerlist;
    }


    public Player getPlayerByID(int id) {
        Player result = null;
        for (Player spieler : playerlist) {
            if (id == spieler.id) {
                result = spieler;
            }
        }
        return result;
    }


    public void setPlayerlist(ArrayList<Player> playerlist) {
        this.playerlist = playerlist;
    }

    public PlayerList() {
        this.playerlist = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "PlayerList: " + playerlist;
    }

    public void add(Player player) {
        playerlist.add(player);
    }


    public void addPlayer() {
        for (int i = 0; i < 4; i++) {
            Player player = new Player(setPlayerNames());
            playerlist.add(player);
        }
    }

    public String setPlayerNames() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type in your name: ");
        String name = scanner.next();
        return name;
    }
// random zahl zw 0 und 4

    public void showId (){
        for(Player player : playerlist){
            System.out.println(player.name + player.id);
        }
    }
}
