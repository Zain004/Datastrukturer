package AdventOfCode2024;

import java.util.*;
import java.util.stream.IntStream;

/*
Du har fått to lister med tall. Sorter hver liste i stigende rekkefølge.
 Beregn den absolutte differansen mellom tallene på samme posisjon i de to sorterte listene.
  Summer alle disse differansene for å finne den totale avstanden.
  Hva er den totale avstanden mellom dine to lister?
 */
public class Oppgave_1 {
    public static int totalDistance(List<Integer> leftList, List<Integer> rightList) {
        Objects.requireNonNull(leftList, "Left list kan ikke være null. ");
        Objects.requireNonNull(rightList, "Right list kan ikke være null. ");

        if (leftList.size() != rightList.size()) {
            throw new IllegalArgumentException("Listene må ha samme lengde.");
        }

        // Oppretter immutable kopier
        List<Integer> sortedLeft = new ArrayList<>(leftList);
        List<Integer> sortedRight = new ArrayList<>(rightList);

        // Sorterer listene
        Collections.sort(sortedLeft);
        Collections.sort(sortedRight);
        // Lager en strøm av indekser, transformerer hver indeks til den
        // absolutte differansen mellom elementene på den indeksen i listene, og summerer differansene.
        return IntStream.range(0, sortedLeft.size())
                .map(i -> Math.abs(sortedLeft.get(i) - sortedRight.get(i)))
                .sum();
    }
    public static void main(String[] args) {
        // Eksempeldata (fra oppgaven)
        List<Integer> leftList = List.of(3, 4, 2, 1, 3, 3); // Bruk List.of for immutable lister
        List<Integer> rightList = List.of(4, 3, 5, 3, 9, 3);

        try {

        } catch (IllegalArgumentException e) {

        }
    }
}
