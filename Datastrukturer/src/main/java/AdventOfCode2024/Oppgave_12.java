package AdventOfCode2024;

import java.util.Objects;

/*
## Oppgave: Hagegjerder

Alvene trenger hjelp til å beregne den totale kostnaden for
å gjerde inn hageparseller. Sammenhengende parseller med samme
plantetype danner en region. Kostnaden for en region er
`areal  * omkrets`, hvor areal er antall parseller og omkrets er antall
sider som ikke berører samme type parsell.

**Eksempel:**

For kartet:
    AAAA
    BBCD
    BBCC
    EEEC

Utregning:
*   **Region A:** Areal = 4, Omkrets = 10, Pris = 4 * 10 = 40
*   **Region B:** Areal = 4, Omkrets = 8, Pris = 4 * 8 = 32
*   **Region C:** Areal = 4, Omkrets = 10, Pris = 4 * 10 = 40
*   **Region D:** Areal = 1, Omkrets = 4, Pris = 1 * 4 = 4
*   **Region E:** Areal = 3, Omkrets = 8, Pris = 3 * 8 = 24

Totalpris: 40 + 32 + 40 + 4 + 24 = **140**

**Oppgave:**

Gitt et kart over hageparseller, beregn den totale kostnaden
 for å gjerde inn alle regionene.

 */
public class Oppgave_12 {
    public static int beregnTotalPris(String[] kart) {
        int totalPris = 0;
        boolean[][] besøkt = new boolean[kart.length][kart[0].length()]; // Holder styr på besøkte parseller.
        // Går gjennom nedover
        for (int rad = 0; rad < kart.length; rad++) {
            for (int kolonne = 0; kolonne < kart[rad].length(); kolonne++) {
                    if (!besøkt[rad][kolonne]) {
                        char planteType = kart[rad].charAt(kolonne);
                        Region region = finnRegion(kart, rad, kolonne, planteType, besøkt);
                        totalPris += region.areal * region.omkrets;
                    }
            }
        }
        return totalPris;
    }

    private static class Region {
        private int areal;
        private int omkrets;
        public Region(int areal, int omkrets) {
            this.areal = areal;
            this.omkrets = omkrets;
        }
    }
    private static class Posisjon {
        private int rad;
        private int kolonne;
        public Posisjon(int rad, int kolonne) {
            this.rad = rad;
            this.kolonne = kolonne;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Posisjon posisjon = (Posisjon) o;

            if(rad != posisjon.rad) return false;
            return kolonne == posisjon.kolonne;
        }

        @Override
        public int hashCode() {
            int result = rad;
            result = 31 *  result + kolonne;
            return result;
        }
    }
    private
    public static void main(String[] args) {

    }
}
