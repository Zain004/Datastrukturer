package AdventOfCode2024;

import java.util.Scanner;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/*
Analyser en liste med rapporter (tallsekvenser) for å bestemme hvor mange som er "trygge". En rapport er trygg hvis:

    Nivåene er enten alle økende eller alle synkende.

    Tilstøtende nivåer skiller seg med minst 1 og maksimalt 3.

Du får en liste med rapporter, én per linje, der hvert tall i rapporten er atskilt med mellomrom. Tell antall trygge rapporter.
 */
public class Oppgave_2 {

    public static final int MIN_DIFF = 1; // Minimum tillatt forskjell mellom nivåer
    public static final int MAX_DIFF = 3; // Maksimum tillatt forskjell mellom nivåer


    /**
     * Avgjør om en rapport er trygg basert på de gitte kriteriene.
     * En trygg rapport har nivåer som enten er strengt økende eller strengt synkende,
     * med en forskjell mellom tilstøtende nivåer innenfor det definerte området.
     *
     * @param nivaer Arrayet med nivåverdier.
     * @return True hvis rapporten er trygg; ellers false.
     *
    7 6 4 2 1Trygt . fordi nivåene alle synker med 1 eller 2
    1 2 7 8 9 fordi Usikkert ​ 2 7er en økning på 5.
    9 7 6 2 1 fordi Usikkert ​ 6 2er en nedgang på 4.
    1 3 2 4 5 fordi Usikkert ​ 1 3øker, men 3 2avtar.
    8 6 4 4 1 fordi Usikkert ​ 4 4er verken en økning eller en nedgang.
    1 3 6 7 9Trygt . fordi nivåene øker med 1, 2 eller 3

     */

    public static boolean erTryggRapport(int[] nivaer) {
        if (nivaer == null || nivaer.length < 2) {
            return true; // Null eller en-element array anses som trygg
        }

        // Lager 2 tester som er tilstrekkelige nok for å finne retingnen arrayet går
        // enten økende eller synkende
        final boolean okende = nivaer[1] > nivaer[0];
        final boolean synkende = nivaer[0] > nivaer[1];

        // Hvis nivåene verken stiger eller synker, er det ikke en gyldig sekvens
        if (!okende && !synkende) {
            return false; // Verken økende eller synkende
        }

        // Bruker en funksjonell tilnærming for å teste om hoppet mellom tallene er gyldig
        IntPredicate erGyldigForskjell = i -> {
            int forskjell = Math.abs(nivaer[i] - nivaer[i -1]);
            return forskjell >= MIN_DIFF && forskjell <= MAX_DIFF;
        };

        for (int i = 1; i < nivaer.length; i++) {
            if (okende && nivaer[i] <= nivaer[i-1]) {
                return false; // Hvis det skulle være økende, men en verdi er lavere eller lik forrige
            } else if (synkende && nivaer[i] >= nivaer[i-1]) {
                return false;  // Hvis det skulle være synkende, men en verdi er høyere eller lik forrige
            }

            if (!erGyldigForskjell.test(i)) {
                return false; // Hvis forskjellen mellom nivåene ikke er gyldig
            }
        }
        // Hvis alle betingelser er oppfylt, er rapporten trygg
        return true;
    }

    public static void main(String[] args) {
        int[] nivaer_1 = new int[]{7, 6, 4, 2, 1};
        System.out.println("Nivåer_1 er trygg: " + erTryggRapport(nivaer_1));


        int[] nivaer_2 = new int[]{8, 6, 4, 4, 1};
        System.out.println("Nivåer_2 er trygg: " + erTryggRapport(nivaer_2));
    }
}
