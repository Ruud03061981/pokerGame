import dev.lpa.game.PokerConsole;
import dev.lpa.poker.PokerGame;
import dev.lpa.poker.PokerSpeler;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        var game = new PokerConsole<>(new PokerGame("TexasHoldEm") );
        game.playGame(game.addPlayer(4));

    }
}