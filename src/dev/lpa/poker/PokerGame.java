package dev.lpa.poker;

import dev.lpa.game.Poker;
import dev.lpa.game.PokerActie;

import java.util.*;

public class PokerGame extends Poker<PokerSpeler> {

    public PokerGame(String gameName) {
        super(gameName);
    }

    public static List<Kaart> dealHoleCards(List<PokerSpeler> spelers) {
        int numberOfCards = 2;
        List<Kaart> deck = Kaart.getDeck();
        Collections.shuffle(deck);
        for (PokerSpeler speler : spelers) {
            List<Kaart> kaartenVoorSpeler = deck.subList(0,numberOfCards);
            speler.setKaarten(new ArrayList<>(kaartenVoorSpeler));
            deck.subList(0, numberOfCards).clear();
        }
        return deck;
    }

    public static List<Kaart> dealRiver(List<PokerSpeler> spelers) {
        List<Kaart> deck = dealHoleCards(spelers);
        int numberOfCards = 3;
        List<Kaart> River = deck.subList(0, numberOfCards);
        for (PokerSpeler speler : spelers) {
            speler.getKaarten().addAll(River);
        }
        deck.subList(0, numberOfCards).clear();
        spelers.forEach(s -> System.out.println("I, " + s.name() + " possess these cards: " + s.getKaarten()));
        return deck;

    }

    public static List<Kaart> dealExtraCard(List<PokerSpeler> spelers){
        List<Kaart> deck = dealRiver(spelers);
        int numberOfCards = 1;
        List<Kaart> river = deck.subList(0, numberOfCards);
        for (PokerSpeler speler: spelers){
            speler.getKaarten().addAll(river);
        }
        deck.subList(0, numberOfCards).clear();
        spelers.forEach(s -> System.out.println("I, " + s.name() + " possess these cards after an Extra Card: " + s.getKaarten()));
        return deck;

    }

    @Override
    public Map<Character, PokerActie> getGameActions(PokerSpeler speler) {

            Map<Character, PokerActie> map = new LinkedHashMap<>(java.util.Map.of(

                    'R', (new PokerActie('R', "Raise", this::raise  )
                    )));
            map.putAll(getStandardActions());
            return map;
        }

    public boolean raise(PokerSpeler speler){
        System.out.println("Player " + speler.name() + " has € " + speler.getRaise() + " left to play " + this.getGameName());
        return true;
    }


    @Override
    public PokerSpeler createNewPlayer(String name) {
        return new PokerSpeler(name);
    }


    public PokerSpeler createNewPlayer(String name, List<Kaart> kaarten, int raise ) {
        return new PokerSpeler(name, kaarten, raise);

    }


}





