package dev.lpa.game;

import dev.lpa.poker.Kaart;
import dev.lpa.poker.PokerGame;
import dev.lpa.poker.PokerSpeler;
import dev.lpa.poker.ScoredCard;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PokerConsole<T extends Poker<? extends PokerSpeler>> {


    private final T game;
    private static final Scanner scanner = new Scanner(System.in);

    private static List<PokerSpeler> spelers = new ArrayList<>();

    private static Map<ScoredCard, Integer> scoredCardIntegerMap = new EnumMap<>(ScoredCard.class);

    public PokerConsole(T game) {
        this.game = game;
    }

    public List<PokerSpeler> addPlayer(int numberOfPlayers) {
        while (spelers.size() < numberOfPlayers) {
            System.out.print("Enter your playing name: ");
            String name = scanner.nextLine();

            System.out.printf("Welcome to %s, %s!%n".formatted(game.getGameName(), name));
            spelers = game.addPlayer(name);
        }
        return spelers;
    }

    public PokerSpeler Winner(List<PokerSpeler> spelers) {

        PokerSpeler winnaar = null;
        int hoogsteScoreTotNuToe = Integer.MIN_VALUE;
        int hoogsteScoreBijGelijkeStand = 0;
        int hoogstescoreBijEersteDeelnemer = 0;
        // hoogsteScore wordt continu bijgewerkt met elke iteratie doordat de vergelijking is:
        // Speler A via values() -> values() neemt alleen de Integer waarden en niet de ScoredCard waarden van de map mee, Stream en mapToInt en max() wordt de grootste van de scores die in het <Integer> gedeelte(bv 10, 15, 20 -> 20 ) van de map zitten en wordt dan Integer20 -> int 20
        // Speler B via values() -> values() neemt alleen de Integer waarden en niet de ScoredCard waarden van de map mee, Stream en mapToInt en max() wordt de grootste van de scores die in het <Integer> gedeelte(bv 8, 10, 15 -> 15 ) van de map zitten en wordt dan Integer15 -> int 15
        // Vervolgens wordt per loop voortgaande continu de hoogstescoreTotNuToe bijgewerkt met de hoogste score tot op dat moment en dus t/m het einde van de loop
        // De winnaar = speler wordt pas bijgewerkt als voldaan wordt aan de voorwaarde (score > hoogsteScoreTotNuToe): Als deze niet waar is dan wordt winnaar = speler niet bijgewerkt
        for (int i = 0; i < spelers.size(); i++) {
            int score = map(spelers.get(i))
                    .values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0);

            if (score > hoogsteScoreTotNuToe) {
                hoogsteScoreTotNuToe = score;
                hoogstescoreBijEersteDeelnemer = scoredCardIntegerMap.values()
                        .stream()
                        .mapToInt(j -> j)
                        .sum();
                winnaar = spelers.get(i);
            }
            if (i >= 1) {
                if (score == hoogsteScoreTotNuToe) {
                    hoogsteScoreBijGelijkeStand = scoredCardIntegerMap.values()
                            .stream()
                            .mapToInt(k -> k)
                            .sum();
                    if (hoogsteScoreBijGelijkeStand > hoogstescoreBijEersteDeelnemer) {
//                        hoogstescoreBijEersteDeelnemer = hoogsteScoreBijGelijkeStand;
                        winnaar = spelers.get(i);
                        ;
                    } else if (hoogsteScoreBijGelijkeStand == hoogstescoreBijEersteDeelnemer) {
                        System.out.println("There is no winner based on score and sum of all cards between " + spelers.get(i) + " and the previous player. Since this is not the definitive KPI the game will continue. ");

                    }
                }
            }
        }
        System.out.println("The winner is: " + (winnaar != null ? winnaar.name() : null) + " with score " + hoogsteScoreTotNuToe + " and " + hoogsteScoreBijGelijkeStand + " tov " + hoogstescoreBijEersteDeelnemer + " Congratulations!!");
        return winnaar;

    }


        private static Map<ScoredCard, Integer> map (PokerSpeler speler){
            for (ScoredCard s : ScoredCard.values()) {
                System.out.printf(speler.name() + "'s Score for %s is %d %n", s, s.score(speler.getKaarten()));
                scoredCardIntegerMap.put(s, s.score(speler.getKaarten()));

            }
            return scoredCardIntegerMap;
        }

        public static void printfile (PokerSpeler speler){

            Map<ScoredCard, Integer> scoreMap = speler.getScoreCard();
            try (PrintWriter writer =
                         new PrintWriter(speler.name().concat(".txt"))) {
                String header = """
                        Card_Combination,Score""";
                writer.write(header);
                writer.write(System.lineSeparator());
                for (var record : scoreMap.entrySet()) {
                    writer.printf("%s,%d%n", record.getKey(), record.getValue());
                }
            } catch (IOException e) {
                System.out.println();
            }
        }
        public void playGame (List < PokerSpeler > spelers) {

            PokerGame.dealRiver(spelers);
try {
    for (PokerSpeler speler : spelers) {
        boolean done = false;
        while (!done) {
            var gameActions = game.getGameActions(speler);
            System.out.println(speler.name() + " Select from one of the following Actions: ");
            assert gameActions != null;
            for (Character c : gameActions.keySet()) {
                String prompt = gameActions.get(c).prompt();
                System.out.println("\t" + prompt + " (" + c + ")");
            }
            System.out.print("Enter Next Action: ");

            char nextMove = scanner.nextLine().toUpperCase().charAt(0);
            if (nextMove == 'R') {
                System.out.println("What amount is the Raise ?");
                int raise = scanner.nextInt();
                scanner.nextLine();

                System.out.println(speler.name() + " has raised with " + raise);
                speler.setRaise(raise);
            }
            speler.setScoreCard(map(speler));
            printfile(speler);
            PokerActie gameAction = gameActions.get(nextMove);

            if (gameAction != null) {
                System.out.println("-------------------------------------------");
                done = game.executeGameAction(speler, gameAction);

                if (!done) {
                    System.out.println("-------------------------------------------");
                }
            }
        }
    }
}catch (ConcurrentModificationException e){
    System.out.println("The inserted value is not correct");
}
            System.out.println(Winner(spelers));

        }
    }

