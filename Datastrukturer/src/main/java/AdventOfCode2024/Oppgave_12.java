package AdventOfCode2024;

import java.util.*;

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
    /**
     * Beregner den totale prisen for å gjerde inn alle sammenhengende planteområder (regioner)
     * på et gitt kart. Hver region består av tilstøtende (horisontalt eller vertikalt) parseller
     * med samme plante-type, og prisen for å gjerde inn en region beregnes som:
     * <br><code>pris = areal * omkrets</code>
     *
     * @param kart Et todimensjonalt kart representert som en array av strenger,
     *             hvor hver tegn representerer en type plante i en parsell.
     *             Kartet forventes å ha rektangulær form (alle strenger like lange).
     * @return Totalpris for å gjerde inn alle identifiserte regioner på kartet.
     *
     * @throws IllegalArgumentException hvis kartet er tomt eller inneholder ujevne rader.
     */

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

    private static Region finnRegion(String[] kart, int rad, int kolonne, char planteType, boolean[][] besøkt) {
        int areal = 0;
        int omkrets = 0;

        Set<Posisjon> regionPosisjoner = new HashSet<>(); // Bruker Set for å unngå duplikater
        List<Posisjon> queue = new ArrayList<>(); //Bruker queue for å besøke alle elementer som henger sammen

        queue.add(new Posisjon(rad, kolonne));
        besøkt[rad][kolonne] = true;
        regionPosisjoner.add(new Posisjon(rad, kolonne));

        while (!queue.isEmpty()) {
            Posisjon aktuellPosisjon = queue.remove(0);
            int aktuellRad = aktuellPosisjon.rad;
            int aktuellKolonne = aktuellPosisjon.kolonne;

            areal++;
            omkrets += beregnOmkrets(kart, aktuellRad, aktuellKolonne, planteType);

            // Sjekk naboer (Opp, ned, venstre, høyre)
            int[][] naboer = {{-1,0}, {1,0}, {0,-1}, {0,1}};
            for (int[] nabo : naboer) {
                int nyRad = aktuellRad + nabo[0];
                int nyKolonne = aktuellKolonne + nabo[1];
                // sjekker om en nabo
                if (erGyldigPosisjon(kart, nyRad, nyKolonne) &&
                kart[nyRad].charAt(nyKolonne) == planteType
                && !besøkt[nyRad][nyKolonne]) {
                    queue.add(new Posisjon(nyRad, nyKolonne));
                    besøkt[nyRad][nyKolonne] = true;
                    regionPosisjoner.add(new Posisjon(nyRad, nyKolonne));
                }
            }
        }

        // Juster omkretsen for interne kanter (mellom regionens celler)
        int internOmkrets = 0;
        for (Posisjon pos : regionPosisjoner) {
            int radP = pos.rad;
            int kolonneP = pos.kolonne;
            int[][] naboer = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] nabo : naboer) {
                int nyRad = pos.rad + nabo[0];
                int nyKolonne = pos.kolonne + nabo[1];
                if (erGyldigPosisjon(kart, nyRad, nyKolonne)
                    && kart[nyRad].charAt(nyKolonne) == planteType
                        && regionPosisjoner.contains(new Posisjon(nyRad,nyKolonne))) {
                    internOmkrets++;
                }
            }
        }
        omkrets -= (internOmkrets / 2);
        return new Region(areal, omkrets);
    }

    private static int beregnOmkrets(String[] kart, int rad, int kolonne, char planteType) {
        int omkrets = 0;
        // Sjekk hver side av parsellen
        int[][] sider = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] side : sider) {
            int nyRad = rad + side[0];
            int nyKolonne = kolonne + side[1];
            if (!erGyldigPosisjon(kart, nyRad, nyKolonne) || kart[nyRad].charAt(nyKolonne) != planteType) {
                omkrets++;
            }
        }
        return omkrets;
    }

    private static boolean erGyldigPosisjon(String[] kart, int rad, int kolonne) {
        return rad >= 0 && rad < kart.length && kolonne >= 0 && kolonne < kart[rad].length();
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

    public static void main(String[] args) {

        String[] kart1 = {"AAAA", "BBCD", "BBCC", "EEEC"};
        System.out.println("Total pris for kart1: " + beregnTotalPris(kart1)); // Forventet: 94
    }
}
