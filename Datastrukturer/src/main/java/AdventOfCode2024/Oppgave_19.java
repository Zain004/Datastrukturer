package AdventOfCode2024;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
Du er ansatt for å hjelpe en onsen med å organisere håndklærne sine.
Onsen har en liste over tilgjengelige håndklær med forskjellige stripemønstre,
 f.eks. "r" (rød), "wr" (hvit-rød) eller "bwu" (blå-hvit-blå). De har også en
 liste over design de ønsker å lage, f.eks. "brwrr" (blå-rød-hvit-rød-rød).
 Målet ditt er å finne ut hvor mange av disse designene som kan lages ved å
 kombinere de tilgjengelige håndklærne.

Eksempel:

Tilgjengelige håndklær: r, wr, b, g, bwu, rb, gb, br

Ønskede design:

brwrr (kan lages med br, wr, r)

bggr (kan lages med b, g, g, r)

gbbr (kan lages med gb, br)

rrbgbr (kan lages med r, rb, g, br)

ubwu (kan ikke lages)

bwurrg (kan lages med bwu, r, r, g)

brgr (kan lages med br, g, r)

bbrgwb (kan ikke lages)

I dette eksemplet kan 6 av 8 design lages. Oppgaven er å skrive kode som leser inn
tilgjengelige håndklær og ønskede design, og returnerer antall mulige design.
 */
public class Oppgave_19 {

    private static final Set<Character> ALLOWED_CHARACTERS = Set.of('w','u','b','r','g');

    private static final String DELIMITER = ",";

    /**
     * Bestemmer antall ønskede design som kan lages med de tilgjengelige håndklemønstrene.
     *
     * @param availableTowels En kommaseparert streng med tilgjengelige håndklemønstre.
     * @param desiredDesigns En liste over ønskede designstrenger.
     * @return Antall ønskede design som kan lages.
     */
    public int countPossibleDesigns(String availableTowels, List<String> desiredDesigns) {
        // valider input
        if (availableTowels == null || availableTowels.isEmpty() || desiredDesigns == null || desiredDesigns.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        // Bruk et sett for tilgjengelige håndklær for effektivt oppslag. Uforanderlig sett er ideelt.
        Set<String> towelSet = Arrays.stream(availableTowels.split(DELIMITER))
                .map(String::trim)
                .collect(Collectors.toUnmodifiableSet());

        return (int) desiredDesigns.stream()
                .filter(this::isValidDesignString)  // Trinn 1: Bare gyldige designstrenger
                .filter(design -> canCreateDesign(design, towelSet))  // Trinn 2: Bare de som kan lages
                .count();  // Teller hvor mange som gjenstår etter filtrering
    }
    /**
     * Validerer at designstrengen kun inneholder tillatte tegn (w, u, b, r, g).
     * En god praksis for å unngå uventede feil senere i behandlingen.
     *
     * @param design Designstrengen som skal valideres.
     * @return true hvis designstrengen er gyldig, false ellers.
     */
    private boolean isValidDesignString(String design) {
        if (design == null || design.isEmpty()) {
            return false; // eller utfør et unntak, avhengig av krav
        }
        return design.chars()
                .mapToObj(c -> (char) c)
                .allMatch(ALLOWED_CHARACTERS::contains);
    }

    /**
     * Avgjør om et design kan lages ved hjelp av tilgjengelige håndklærmønstre. Bruker dynamisk programmering/memoisering
     * for effektivitet.
     *
     * @param design Den ønskede designstrengen.
     * @param towelSet Mengden av tilgjengelige håndklærmønstre.
     * @return true hvis designet kan lages, false ellers.
     */
    private boolean canCreateDesign(String design, Set<String> towelSet) {
        // Dynamisk programmering: dp[i] er true hvis design[0...i] kan lages
        boolean[] dp = new boolean[design.length() + 1];
        dp[0] = true; // Tom Streng kan alltid lages
        for (int i = 1; i <= design.length(); i++) {
            for (String towel : towelSet) {
                if (i >= towel.length() && design.substring(i - towel.length(), i).equals(towel)) {
                    dp[i] = dp[i - towel.length()];
                }
            }
        }
        return dp[design.length()];
    }
    public static void main(String[] args) {
        Oppgave_19 obj = new Oppgave_19();
        String availableTowels = "r, wr, b, g, bwu, rb, gb, br";
        List<String> desiredDesigns = List.of("brwrr", "bggr", "gbbr", "rrbgbr", "ubwu", "bwurrg", "brgr", "bbrgwb");
        int possibleDesign = obj.countPossibleDesigns(availableTowels, desiredDesigns);
        System.out.println("Number of possible designs: " + possibleDesign);
        //Test with empty input.  Handle edge cases robustly.
        try {
            obj.countPossibleDesigns(null, null);
        } catch(IllegalArgumentException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }
}
