package AdventOfCode2024;
/*
## Oppgave: Lås og Nøkkel Match

Du har fått skjemaer for låser og nøkler. Hver lås og nøkkel består av et rutenett med `#` og `.`. Låser har fylt ut øverste rad (`#`) og tom nederste rad (`.`), mens nøkler har motsatt (tom øverste rad og fylt nederste rad). Oppgaven din er å finne hvor mange unike lås/nøkkel-par som "passer" sammen, definert som at det ikke er overlapp mellom låsens "pinner" og nøkkelens "form" i noen kolonne.

**Slik fungerer det:**

1.  **Konverter skjema til høyder:**
    *   For hver lås, finn høyden på pinnene i hver kolonne. Høyden er antall `#` fra toppen av kolonnen.
    *   For hver nøkkel, finn høyden på formen i hver kolonne. Høyden er antall `#` fra bunnen av kolonnen.

2.  **Sjekk overlapp:**
    *   For hvert lås/nøkkel-par, iterer gjennom kolonnene.
    *   For hver kolonne, beregn summen av låsens pinnehøyde og nøkkelens formhøyde.
    *   Hvis summen er større enn 6 (antall rader), betyr det at låsen og nøkkelen overlapper i den kolonnen og paret *ikke* passer.

3.  **Teller passende par:**
    *   Teller antallet lås/nøkkel-par som *ikke* overlapper i *noen* kolonne.

**Eksempel (hentet fra oppgaveteksten):**

**Låser:**

```
#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....
```

**Nøkler:**

```
.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####
```

**Konverterte høyder:**

*   Låser: `[0, 5, 3, 4, 3]`, `[1, 2, 0, 5, 3]`
*   Nøkler: `[5, 0, 2, 1, 3]`, `[4, 3, 4, 0, 2]`, `[3, 0, 2, 0, 1]`

**Analyse:**

*   Lås 1 (`0, 5, 3, 4, 3`) og Nøkkel 1 (`5, 0, 2, 1, 3`): Overlapp i siste kolonne (3 + 3 = 6, men vi trenger < 6).
*   Lås 1 (`0, 5, 3, 4, 3`) og Nøkkel 2 (`4, 3, 4, 0, 2`): Overlapp i andre kolonne (5 + 3 = 8).
*   Lås 1 (`0, 5, 3, 4, 3`) og Nøkkel 3 (`3, 0, 2, 0, 1`): Passer.
*   Lås 2 (`1, 2, 0, 5, 3`) og Nøkkel 1 (`5, 0, 2, 1, 3`): Overlapp i første kolonne (1 + 5 = 6, men vi trenger < 6).
*   Lås 2 (`1, 2, 0, 5, 3`) og Nøkkel 2 (`4, 3, 4, 0, 2`): Passer.
*   Lås 2 (`1, 2, 0, 5, 3`) og Nøkkel 3 (`3, 0, 2, 0, 1`): Passer.

**Resultat:**

I dette eksemplet er det 3 lås/nøkkel-par som passer.

**Oppgave:**

Du vil bli gitt en fil som inneholder dine egne lås- og nøkkelskjemaer.  Analyser skjemaene og finn hvor mange unike lås/nøkkel-par som passer sammen. Legg vekt på at alle kolonnene må passe for at hele lås/nøkkelparet skal anses som et treff.
 */

import java.util.ArrayList;
import java.util.List;

public class Oppgave_25 {
    private static final int GRID_HEIGHT = 6; // Høyden på rutenettet (lås og nøkkel)

    /**
     * Analyserer lås- og nøkkelskjemaene for å finne antall unike passende par.
     *
     * @param locks   En liste med låseskjemaer (String-array).
     * @param keys    En liste med nøkkelskjemaer (String-array).
     * @return Antallet unike lås/nøkkelpar som passer.
     */
    public int countMatchingPairs(List<String[]> locks, List<String[]> keys) {
        if (locks == null || keys == null || locks.isEmpty() || keys.isEmpty()) {
            return 0; // Ingen par kan matches hvis en av listene er tom.
        }

        int matchingPairCount = 0;
        for (String[] lock : locks) {
            for (String[] key : keys) {
                if (isValidPair(lock, key)) {
                    matchingPairCount++;
                }
            }
        }
        return matchingPairCount;
    }

    /**
     * Sjekker om et gitt lås/nøkkelpar er gyldig (ingen overlapp i noen kolonne).
     *
     * @param lock  Låseskjemaet (String-array).
     * @param key   Nøkkelskjemaet (String-array).
     * @return `true` hvis paret er gyldig, ellers `false`.
     */
    private boolean isValidPair(String[] lock, String[] key) {
        if (lock == null || key == null || lock.length != GRID_HEIGHT || key.length != GRID_HEIGHT) {
            return false; // Ugyldig input
        }

        int numCols = lock[0].length();
        if (numCols != key[0].length()) {
            return false; // Lås og nokkel må ha samme antall kolonner
        }

        for (int col = 0; col < numCols; col++) {
            int lockHeight = getLockHeight(lock, col);
            int keyHeight = getKeyHeight(key, col);

            if (lockHeight + keyHeight >= GRID_HEIGHT) {
                return false;
            }
        }
        return true;
    }

    /**
     * Beregner høyden på låsepinnene i en gitt kolonne.
     *
     * @param lock  Låseskjemaet (String-array).
     * @param col   Kolonneindeksen.
     * @return Høyden på låsepinnene i den gitte kolonnen.
     */
    private int getLockHeight(String[] lock, int col) {
        int height = 0;
        for (int row = 0; row < GRID_HEIGHT; row++) {
            if (lock[row].charAt(col) == '#') {
                height++;
            }
        }
        return height;
    }

    /**
     * Beregner høyden på nøkkelformen i en gitt kolonne.
     *
     * @param key   Nøkkelskjemaet (String-array).
     * @param col   Kolonneindeksen.
     * @return Høyden på nøkkelformen i den gitte kolonnen.
     */
    private int getKeyHeight(String[] key, int col) {
        int height = 0;
        for (int row = GRID_HEIGHT -1; row >= 0; row--) {
            if (key[row].charAt(col) == '#') {
                height++;
            } else {
                break; // Vi kan stoppe når vi finner dne første '.'
            }
        }
        return height;
    }

    /**
     * Hjelpemetode for å parse skjemaene fra en streng. Denne er viktig fordi den håndterer formatering
     * og potensielle feil i input-data.
     * @param schemaData String-representasjon av skjemaene, separert med linjeskift og mulige tomme linjer.
     * @return List<String[]> Liste over skjemaer, der hvert skjema er representert som et String[].
     */
    public static List<String[]> parseSchemas(String schemaData) {
        List<String[]> schemas = new ArrayList<>();
        String[] schemaStrings = schemaData.split("\n\n"); // // Separer skjemaer basert på tomme linjer
        for (String schemaString : schemaStrings) {
            String[] lines = schemaString.split("\n"); // Del opp i rader


            // Fjern tomme linjer fra starten og slutten, og valider formatet
            List<String> validLines = new ArrayList<>();
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty()) {
                    validLines.add(line);
                }
            }

            if (validLines.size() == GRID_HEIGHT) {
                schemas.add(validLines.toArray(new String[0]));
            } else {
                System.err.println("Advarsel: Ugyldig skjemaformat ignorert: " + schemaString);
            }
        }
        return schemas;
    }

    public static void main(String[] args) {
        String locksData = "#####\n.####\n.####\n.####\n.#.#.\n.#....\n.....\n" +
                           "#####\n##.##\n.#.##\n...##\n...#.\n...#.\n.....";

        String keysData = ".....\n#....\n#....\n#...#\n#.#.#\n#.###\n#####\n" +
                          ".....\n.....\n#.#..\n###..\n###.#\n###.#\n#####\n" +
                          ".....\n.....\n.....\n#....\n#.#..\n#.#.#\n#####";
        List<String[]> locks = parseSchemas(locksData);
        List<String[]> keys = parseSchemas(keysData);

        Oppgave_25 oppgave = new Oppgave_25();
        int matchingPairCount = oppgave.countMatchingPairs(locks, keys);
        System.out.println("Antall matchende lås/nøkkel par:" + matchingPairCount);
    }
}
