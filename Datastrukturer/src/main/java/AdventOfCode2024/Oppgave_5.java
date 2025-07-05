package AdventOfCode2024;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
Du får to sett med data:

    Regler for siderekkefølge: En liste over regler, én per linje, i formatet X|Y.

    Oppdateringer: En liste over oppdateringer, én per linje, der hver oppdatering er en kommaseparert liste over sidetall.

Din oppgave er å:

    Identifisere riktig ordnede oppdateringer: Finn hvilke oppdateringer som følger alle rekkefølgereglene.
    Hvis en oppdatering ikke inneholder alle sidene nevnt i en regel, ignoreres regelen for den oppdateringen.
    Finn midtsidetallet: For hver riktig ordnede oppdatering, finn sidetallet som er i midten når sidene er sortert numerisk.
    Summer midtsidetallene: Legg sammen alle midtsidetallene fra de riktig ordnede oppdateringene.

    Eksempel:

    Regler:

    47|53
    97|13
    97|61
    97|47
    75|29
    61|13
    75|53
    29|13
    97|29
    53|29
    61|53
    97|53
    61|29
    47|13
    75|47
    97|75
    47|61
    75|61
    47|29
    75|13
    53|13

    75,47,61,53,29
    97,61,53,29,13
    75,29,13
    75,97,47,61,53
    61,13,29
    97,13,75,29,47

    I dette eksemplet er de riktig ordnede oppdateringene:

        75,47,61,53,29 (midtsidetall: 61)

        97,61,53,29,13 (midtsidetall: 53)

        75,29,13 (midtsidetall: 29)

    Summen av midtsidetallene er: 61 + 53 + 29 = 143

    Oppgaven din er å finne summen av midtsidetallene for de riktig ordnede oppdateringene basert på ditt fullstendige input.
     */
public class Oppgave_5 {
    private final List<String> rules;
    private final List<String> updates;

    public Oppgave_5(List<String> rules, List<String> updates) {
        this.rules = rules;
        this.updates = updates;
    }

    public int solve() {
        return updates.stream()
                .filter(this::isCorrectlyOrdered)
                .mapToInt(this::getMiddlePage)
                .sum();
    }

    private boolean isCorrectlyOrdered(String update) {
        List<Integer> pages = Arrays.stream(update.split(","))
                .map(String::trim) // trimmer hver streng
                .map(Integer::parseInt) // parser inputen til en integer
                .collect(Collectors.toList());

        for (String rule : rules) {
            String[] parts = rule.split("\\|");

            int before = Integer.parseInt(parts[0].trim());
            int after = Integer.parseInt(parts[1].trim());

            if (pages.contains(before) && pages.contains(after)) {
                if (pages.indexOf(before) > pages.indexOf(after)) {
                    return false; // Hvis before kommer etter after, så er regelen brutt!
                }
            }
        }
        return true;
    }

    public int getMiddlePage(String update) {
        List<Integer> pages = Arrays.stream(update.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        return pages.get(pages.size() / 2); // Heltallsdivisjon, håndterer både partalls- og oddestørrelseslister
    }

    public static void main(String[] args) {
        List<String> rules = List.of(
                "47|53",
                "97|13",
                "97|61",
                "97|47",
                "75|29",
                "61|13",
                "75|53",
                "29|13",
                "97|29",
                "53|29",
                "61|53",
                "97|53",
                "61|29",
                "47|13",
                "75|47",
                "97|75",
                "47|61",
                "75|61",
                "47|29",
                "75|13",
                "53|13"
        );

        List<String> updates = List.of(
                "75,47,61,53,29",
                "97,61,53,29,13",
                "75,29,13",
                "75,97,47,61,53",
                "61,13,29",
                "97,13,75,29,47"
        );

        Oppgave_5 pq = new Oppgave_5(rules, updates);
        int result = pq.solve();
        System.out.println();
        System.out.println("Sum of middle pages: " + result);
    }
}
