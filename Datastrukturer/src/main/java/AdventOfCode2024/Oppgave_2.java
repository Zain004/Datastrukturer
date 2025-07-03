package AdventOfCode2024;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/*
Analyser en liste med rapporter (tallsekvenser) for å bestemme hvor mange som er "trygge". En rapport er trygg hvis:

    Nivåene er enten alle økende eller alle synkende.

    Tilstøtende nivåer skiller seg med minst 1 og maksimalt 3.

Du får en liste med rapporter, én per linje, der hvert tall i rapporten er atskilt med mellomrom. Tell antall trygge rapporter.
 */
public class Oppgave_2 {

    public static final int MIN_DIFF = 1;
    public static final int MAX_DIFF = 3;

    /**
     * Avgjør om en rapport er trygg basert på de gitte kriteriene.
     * En trygg rapport har nivåer som enten er strengt økende eller strengt synkende,
     * med en forskjell mellom tilstøtende nivåer innenfor det definerte området.
     *
     * @param nivaer Arrayet med nivåverdier.
     * @return True hvis rapporten er trygg; ellers false.
     */

    public static boolean erTryggRapport(int[] nivaer) {
        if (nivaer == null || nivaer.length < 2) {
            return true; // Null eller en-element array anses som trygg
        }

        final boolean okende = nivaer[1] > nivaer[0];
        final boolean synkende = nivaer[0] > nivaer[1];

        if (!okende && !synkende) {
            return false; // Verken økende eller synkende
        }

        IntPredicate erGyldigForskjell = i -> {
            int forskjell = Math.abs
        }

    }

    public static void main(String[] args) {

    }
}
