package dev.lpa.poker;

import jdk.jshell.EvalException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum ScoredCard {

    TWOS (0, 0),
    THREES(0, 1),
    FOURS (0, 2),
    FIVES (0,3),
    SIXES(0,4),
    SEVENS(0,5),
    EIGHTS(0,6),
    NINES(0,7),
    TENS(0,8),
    JACK(0, 9),
    QUEEN(0,10),
    KING(0,11),
    ACE(0,12),
    PAIRS(0),
    THREE_OF_A_KIND(0),
    STRAIGHT(30),
    FLUSH(35),
    FULL_HOUSE(40),
    FOUR_OF_A_KIND(45),
    STRAIGHT_FLUSH(50),
    ROYAL_FLUSH(55);

    private final int defaultScore;
    private int faceValue = 0;
    ScoredCard(int defaultScore, int faceValue) {
        this.defaultScore = defaultScore;
        this.faceValue = faceValue;
    }
    ScoredCard(int defaultScore) {
        this.defaultScore = defaultScore;
    }

    public int getFaceValue() {
        return faceValue;
    }

    private int frequency (List<Kaart> lijst, int value){
        return (int) lijst.stream()
                .mapToInt(Kaart::rank)
                .filter(i -> i == value)
                .count();
    }

    private int frequency (List<Kaart> lijst){
        return frequency(lijst, faceValue);
    }
    private int frequencySingle(List<Kaart> lijst, int value){
        return (int) lijst.stream()
                .mapToInt(Kaart::rank)
                .filter(i -> i==value)
                .findFirst()
                .stream().count();
    }

    private int frequencySingle(List<Kaart> lijst){
        return frequencySingle(lijst, faceValue);
    }


    private Map<Kaart.Suit, Integer> Flush(List<Kaart> kaarten){
        Map<Kaart.Suit, Integer> suit = new HashMap<>();
        for (Kaart i : kaarten){
            suit.merge(i.suit(),1,Math::addExact);
        }
        return suit;
    }

    private Map<Integer, Integer> TexasHoldEm(List<Kaart> kaarten){
        Map<Integer, Integer> Map = new HashMap<>();
        for (Kaart i : kaarten) {
            Map.merge(i.rank(),1,Math::addExact);
        }
        return Map;
    }

    private int checkStraight(List<Kaart> kaarten){
        kaarten.sort(Comparator.comparingInt(Kaart::rank));
        int contigousCount = 1;
        for (int i = 0; i < kaarten.size()-1; i++){
            if (kaarten.get(i + 1).rank() == kaarten.get(i).rank() + 1) {
                contigousCount++;
                }
            }
        return contigousCount;
    }
    private boolean checkFullHouse(List<Kaart> kaarten){
        Map<Integer, Integer> vergelijk = TexasHoldEm(kaarten);
        if (vergelijk.size() == 2){
            for (int value: vergelijk.values()) {
                // Stel je voor dat je 2 entrys hebt in de map vergelijk: dan is het mogelijk dat je bijvoorbeeld 4 koningen hebt en 1 queen: dan is size ook 2 maar dan heb je geen FullHouse.
                // In die map is dan : queen.rank() 13 = 1 en koning.rank() = 4. Dan wordt dus voldaan aan voorwaarde value == 1 en dat kan dus niet een fullhouse zijn
                if (value == 1) return false;
            }
            return true;
        }
        return false;
    }

    private boolean checkFlush(List<Kaart> kaarten){
        Map<Kaart.Suit, Integer> flushpuppy = Flush(kaarten);
        return flushpuppy.size() == 1;
    }

    private boolean checkStraightFlush(List<Kaart> kaarten){
        return (checkFlush(kaarten) && (checkStraight(kaarten) == kaarten.size()));

    }

    private boolean checkRoyalFlush(List<Kaart> kaarten) {
        kaarten.sort(Comparator.comparingInt(Kaart::rank));
        if (kaarten.get(kaarten.size()-1).rank() == ACE.getFaceValue() ){
            return checkStraightFlush(kaarten);
        }
        return false;
    }

    public int score (List<Kaart> kaarten){
        int score = switch (this){
            case TWOS, THREES, FOURS, FIVES, SIXES, SEVENS, EIGHTS, NINES, TENS, JACK, QUEEN, KING, ACE -> frequencySingle(kaarten) * getFaceValue();

            case PAIRS -> {
                Map<Integer, Integer> map = TexasHoldEm(kaarten);
                yield map.entrySet().stream()
                        .filter(entry -> entry.getValue() >= 2)
                        .mapToInt(entry -> entry.getKey() * 2)
                        .findFirst()
                        .orElse(0);
            }
            case THREE_OF_A_KIND -> {
                Map<Integer, Integer> map = TexasHoldEm(kaarten);
                yield map.entrySet().stream()
                        .filter(entry -> entry.getValue() == 3)
                        .mapToInt(entry -> entry.getKey() * 3)
                        .findFirst()
                        .orElse(0);
            }
            case STRAIGHT -> {if ((checkStraight(kaarten)) == kaarten.size()){
                yield STRAIGHT.defaultScore;
            }
            else {yield 0;}
            }
            case FLUSH -> checkFlush(kaarten) ? FLUSH.defaultScore : 0;

            case FULL_HOUSE -> checkFullHouse(kaarten) ? FULL_HOUSE.defaultScore : 0;

            case FOUR_OF_A_KIND -> {
                Map<Integer, Integer> map = TexasHoldEm(kaarten);
                yield map.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() == 4)
                        .mapToInt(entry -> entry.getKey() * 4)
                        .findFirst()
                        .orElse(0);
            }
            case STRAIGHT_FLUSH -> checkStraightFlush(kaarten) ? STRAIGHT_FLUSH.defaultScore : 0;

            case ROYAL_FLUSH -> checkRoyalFlush(kaarten) ? ROYAL_FLUSH.defaultScore : 0;



            default -> faceValue;

        };
        return score;
    }

}









