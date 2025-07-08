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
        int height = grid.size(); // antall rader
        int width = grid.get(0).length(); // antall Kolonner per rad

        // Grupperer anntener etter frekvens
        Map<Character, List<Point>> antennasByFreq = new HashMap<>();
        for (int y = 0; y < height; y++) {
            String line = grid.get(y);
            for(int x = 0; x < width; x++) {
                char c = line.charAt(x);
                if (c != '.') {
                    antennasByFreq.computeIfAbsent(c, k -> new ArrayList<>())
                            .add(new Point(x,y));
                }
            }
        }
    }
    public static void main(String[] args) {

    }
}
