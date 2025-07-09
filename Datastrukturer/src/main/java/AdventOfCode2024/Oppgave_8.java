package AdventOfCode2024;

import java.util.*;

/*
🧩 Oppgave – Resonant kolinearitet

Du får et 2D-kart som viser plasseringen av antenner, der hver antenne har en frekvens representert med ett tegn (a–z, A–Z, 0–9). Antennene sender ut signaler som kan forårsake antinoder.
🔷 Antinode-regel

En antinode oppstår når:

    To antenner med samme frekvens står på rett linje.

    Et punkt langs denne linjen er dobbelt så langt fra den ene antennen som fra den andre.

Hver slik antenne-par lager to antinoder (ett på hver side).

Antinoder kan havne på tomme ruter, eller til og med på antenner. Men bare antinoder innenfor kartets grenser teller.
📥 Input

Et kart i tekstformat, f.eks.:

............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............

Hver linje er én rad. Punktum (.) betyr tom plass.
✅ Oppgave

    Finn og tell hvor mange unike posisjoner på kartet som inneholder en antinode
 */
public class Oppgave_8 {

    private static class Point {
        final int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private Point add(int dx, int dy) {
            return new Point(this.x + dx, this.y + dy);
        }

        private Point subtract(int dx, int dy) {
            return new Point(this.x - dx, this.y - dy);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return this.x == p.x && this.y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x,y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    public static int solve(List<String> grid) {
        // Henter antall rader i rutenettet (gridet)
        int height = grid.size();

        // Henter antall kolonner i rutenettet (basert på første rad)
        int width = grid.get(0).length();

        // Lager en map som grupperer alle antenner etter hvilken frekvens (bokstav) de bruker
        // Nøkkel = frekvens (char), Verdi = liste av punkter der antenner med denne frekvensen står
        Map<Character, List<Point>> antennasByFreq = new HashMap<>();

        // Går gjennom hele gridet rad for rad
        for (int y = 0; y < height; y++) {
            String line = grid.get(y); // Henter én rad
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x); // Henter ett tegn i raden
                if (c != '.') { // Dersom det er en antenne (ikke tom plass)
                    // Legg til punktet (x, y) i lista for denne frekvensen
                    // Sjekk om antennasByFreq allerede har en liste for nøkkelen c (frekvensen).
                    // Hvis JA (listen finnes fra før), så bruk den eksisterende listen.
                    antennasByFreq
                            .computeIfAbsent(c, k -> new ArrayList<>()) // Opprett ny liste hvis frekvensen ikke finnes fra før
                            .add(new Point(x, y)); // Legg til punktet hvor antennen står
                }
            }
        }

        // Lager et sett for å holde unike antinoder (punkter der interferens oppstår)
        Set<Point> antinodes = new HashSet<>();

        // Går gjennom hver frekvens og ser på antennene med samme frekvens
        for (Map.Entry<Character, List<Point>> entry : antennasByFreq.entrySet()) {
            List<Point> points = entry.getValue(); // Liste over antenner med denne frekvensen

            // Går gjennom alle par av antenner med samme frekvens
            for (int i = 0; i < points.size(); i++) {
                for (int j = i + 1; j < points.size(); j++) {
                    Point a = points.get(i);
                    Point b = points.get(j);

                    // Beregner forskjellen i posisjon (vektor) mellom to antenner
                    int dx = b.x - a.x;
                    int dy = b.y - a.y;

                    // Beregner en mulig antinode *forbi* b i samme retning som a → b
                    Point p1 = b.add(dx, dy);
                    if (isInBounds(p1, width, height)) {
                        antinodes.add(p1); // Legg til antinode hvis den er innenfor gridet
                    }

                    // Beregner en mulig antinode *forbi* a i motsatt retning av a → b
                    Point p2 = a.subtract(dx, dy);
                    if (isInBounds(p2, width, height)) {
                        antinodes.add(p2); // Legg til antinode hvis den er innenfor gridet
                    }
                }
            }
        }
        // Returnerer hvor mange unike antinoder vi har funnet i gridet
        return antinodes.size();
    }

    public static boolean isInBounds(Point p, int width, int height) {
        return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
    }

    public static void main(String[] args) {
        List<String> input = Arrays.asList(
                "............",
                "........0...",
                ".....0......",
                ".......0....",
                "....0.......",
                "......A.....",
                "............",
                "............",
                "........A...",
                ".........A..",
                "............",
                "............"
        );
        int result = solve(input);
        System.out.println("Unike antinoder: " + result);  // Forventet: 14
    }
}
