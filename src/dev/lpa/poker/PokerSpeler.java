package dev.lpa.poker;

import dev.lpa.game.Poker;
import dev.lpa.game.Speler;

import java.util.*;

public class PokerSpeler implements Speler {

    private final String name;

    private List<Kaart> kaarten = new ArrayList<>();

    private Map<ScoredCard, Integer> scoreCard = new EnumMap<>(ScoredCard.class);

    private int capital = 100000000;

    public String name() {
        return name;
    }

    public PokerSpeler(String name) {
        this.name = name;
    }

    public int getRaise() {
        return capital;
    }

    public void setRaise(int i) {
        this.capital = capital - i;
    }

    public PokerSpeler(String name, List<Kaart> kaarten, int raise) {
        this.name = name;
        this.kaarten = kaarten;
        this.capital=raise;

    }

    public PokerSpeler(String name, List<Kaart> kaarten, Map<ScoredCard, Integer> scoreCard, int capital) {
        this.name = name;
        this.kaarten = kaarten;
        this.scoreCard = scoreCard;
        this.capital = capital;
    }

    public Map<ScoredCard, Integer> getScoreCard() {
        return scoreCard;
    }

    public void setScoreCard(Map<ScoredCard, Integer> scoreCard) {
        this.scoreCard = scoreCard;
    }

    public List<Kaart> getKaarten() {
        return kaarten;
    }

    public void setKaarten(List<Kaart> kaarten) {
        this.kaarten = kaarten;
    }

    @Override
    public String toString() {
        return "PokerSpeler{" +
                "name='" + name + '\'' +
                ", kaarten=" + kaarten +
                ", scoreCard=" + scoreCard +
                ", capital=" + capital +
                '}';
    }



}
