package UNO;


public class Card {
    private Type type;
    private int number;

    private int value;
    private static int counter = 0;

    private int CardID;

    public int getCardID() {
        return CardID;
    }

    public Card(Type type, int number, int value) {
        this.type = type;
        this.number = number;
        this.value = value;
        this.CardID = counter++;
    }

    public Card(Type type, int value) {
        this.type = type;
        this.value = value;
        this.CardID = counter++;
    }

    @Override
    public String toString() {

        if (getType().equals(Type.BLUE) || getType().equals(Type.GREEN) || getType().equals(Type.YELLOW) || getType().equals(Type.RED)) {

            return "ID: " + CardID +  "\t" +
                    "Type: " + type  +"\t"+
                    "Number: " + number;

        } else {

            return "ID: " + CardID + "\t" +
                    "Type: " + type;

        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}