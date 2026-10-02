package dev.lpa.game;

import dev.lpa.poker.BigBlind;
import dev.lpa.poker.PokerGame;
import dev.lpa.poker.PokerSpeler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Poker<T extends PokerSpeler> {

    private final String gameName;
    private static Map<Character, PokerActie> standardActions = null;

    List<PokerSpeler> pokerspelers = new ArrayList<>();

    public Poker(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public Map<Character, PokerActie> getStandardActions() {

        if (standardActions == null) {
            standardActions = new LinkedHashMap<>(java.util.Map.of(
                    'I',
                    new PokerActie('I', "Print Player Info",
                            this::getPlayerInfo),
                    'Q',
                    new PokerActie('Q', "Quit Game",
                            this::quitGame),
                    'B', new PokerActie('B', "Blind",
                            this::smallBlind)
            ));
        }
        return standardActions;
    }

    private boolean getPlayerInfo(PokerSpeler pokerSpeler) {
        System.out.println(pokerSpeler);
        return true;
    }

    public boolean executeGameAction(PokerSpeler player, PokerActie action) {
        return action.actie().test(player);
    }
    public abstract Map<Character, PokerActie> getGameActions(PokerSpeler speler) ;


    public boolean quitGame(PokerSpeler speler) {
        System.out.println("Sorry to see you go, " + speler.name());
        pokerspelers.remove(speler);
        return true;
    }

    public abstract T createNewPlayer(String name);

    public List<PokerSpeler> addPlayer(String name) {

        T player = createNewPlayer(name);
        if (player != null) {
            pokerspelers.add(player);

        }
        return pokerspelers;
    }

    public boolean smallBlind(PokerSpeler player){

        System.out.println("The big blind for this " + (player.name()) + " is set to " + BigBlind.HUNDRED);
        return true;
    }

}
