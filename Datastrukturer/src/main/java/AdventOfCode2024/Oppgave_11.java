package AdventOfCode2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Du står foran en linje med steiner, hver gravert med et tall. Hver gang du blunker, endrer steinene seg samtidig i henhold til følgende regler:
    Hvis steinen er 0: Den erstattes av en stein med tallet 1.
    Hvis steinen har et partall antall sifre: Den deles i to steiner.
    Den venstre steinen får de venstre sifrene, og den høyre steinen får de høyre sifrene. (F.eks. 1000 blir 10 og 0).
    Ellers: Steinen erstattes av en stein med tallet lik det gamle tallet multiplisert med 2024.
    Gitt en initial linje med steiner og et antall blunk, finn hvor mange steiner det er etter det antallet blunk.
 */
public class Oppgave_11 {
    private static final int Multiplier = 2024;

    public static List<Long> simulate(List<Long> initialStones, int blinks) {
        List<Long> stones = new ArrayList<>(initialStones);
        for (int i = 0; i < blinks; i++) {
            stones = blink(stones);
        }
        return stones;
    }

    private static List<Long> blink(List<Long> stones) {
        List<Long> nextStones = new ArrayList<>();
        for (Long stone : stones) {
            if (stone == 0) {
                nextStones.add(1L);
            } else if (String.valueOf(stone).length() % 2 == 0) {
                /*String.valueOf(stone) → Gjør tallet om til tekst. F.eks. 99L → "99"*/
                String stoneStr = String.valueOf(stone);
                int halfLength = stoneStr.length() / 2;
                // Splitter opp tallet ved å bruke substring, tar første 2 delene
                nextStones.add(Long.parseLong(stoneStr.substring(0, halfLength)));
                // så de neste 2 delene
                nextStones.add(Long.parseLong(stoneStr.substring(halfLength)));
            } else {
                nextStones.add(stone * Multiplier);
            }
        }
        return nextStones;
    }
    public static void main(String[] args) {
        // Example usage
        List<Long> initialArrangement = Arrays.asList(0L, 1L, 10L, 99L, 999L);
        int numBlinks = 1;
        List<Long> finalArrangement = simulate(initialArrangement, numBlinks);
        System.out.println("Initial arrangement: " + initialArrangement);
        System.out.println("After " + numBlinks + " blink(s): " + finalArrangement);
    }
}
