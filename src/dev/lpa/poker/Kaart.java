package dev.lpa.poker;

import java.util.ArrayList;
import java.util.List;

public record Kaart(Suit suit, String face, int rank) {

    public enum Suit {
        HARTEN, SCHOPPEN, RUITEN, KLAVEREN;

        public char soortSuit() {
            return (new char[]{9827, 9830, 9829, 9824})[this.ordinal()];
        }

    }

    @Override
    public String toString() {
        int index = face.contains("10") ? 2 : 1;
        String faceString = face.substring(0, index);
        return "%s%c(%d)".formatted(faceString, suit.soortSuit(), rank );
    }

    public static Kaart getNumericCard(int rank, Suit suit){
        if (rank > 1 && rank <=10 && suit == Suit.HARTEN || suit == Suit.KLAVEREN || suit == Suit.RUITEN || suit == Suit.SCHOPPEN){
            return new Kaart(suit, String.valueOf(rank), rank-2);
        }
        else {
            System.out.println(" The current rank " + rank + " suit " + suit );
        }
        return null;
    }

    public static Kaart getFaceCard(char abbrev, Suit suit){
        int i = "JQKA".indexOf(abbrev);
        if (i > -1){
            return new Kaart(suit, String.valueOf(abbrev), i + 9);
        }
        return null;
    }

    public static List<Kaart> getDeck(){
        List<Kaart> nieuweKaart = new ArrayList<>(55);
        for (Suit c : Suit.values()){
            for (int i = 2; i <= 10; i ++){
                nieuweKaart.add(new Kaart(c, String.valueOf(i), i-2));
            }
            for (char d : new char[]{'J', 'Q', 'K', 'A'}){
                int i = "JQKA".indexOf(d);
                nieuweKaart.add(new Kaart(c, String.valueOf(d), i + 9));
            }
        }
        return nieuweKaart;
    }
}
