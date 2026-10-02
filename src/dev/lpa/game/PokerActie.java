package dev.lpa.game;

import dev.lpa.poker.PokerSpeler;

import java.util.function.Predicate;

public record PokerActie(char key, String prompt, Predicate<PokerSpeler> actie) {



}
