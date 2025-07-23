package AdventOfCode2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import java.io.IOException;

/*
Oppgave: Naviger gjennom korrupt minne

Du befinner deg i et 2D rutenett som representerer et minneområde.
Målet ditt er å komme deg fra øverste venstre hjørne (0,0) til
nederste høyre hjørne (70, 70) (eller 6,6 i eksempelet).
Du kan bevege deg opp, ned, venstre eller høyre.

En strøm av "bytes" faller ned i minnet og ødelegger cellene de
lander i. Du får en liste over koordinater (X, Y) som representerer
hvor disse bytene vil lande. Ødelagte celler kan ikke traverseres.

Simuler de første 1024 bytene som faller (eller alle bytene hvis
listen er kortere enn 1024). Finn deretter den korteste veien fra
(0,0) til (70,70) som unngår de ødelagte cellene.

Eksempel:

Rutenettstørrelse: 7x7 (koordinater fra 0 til 6)
Byteposisjoner (første 12):

Generated code
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1


Etter at disse bytene har falt, ser rutenettet slik ut ('.' = trygg, '#' = korrupt):

Generated code
...#...
..#..#.
....#..
...#..#
..#..#.
.#..#..
#.#....

Korteste vei fra (0,0) til (6,6) er 22 trinn. En mulig sti er:

Generated code
OO.#OOO
.O#OO#O
.OOO#OO
...#OO#
..#OO#.
.#.O#..
#.#OOOO

Oppgave:

Gitt den fullstendige listen over byteposisjoner (fra oppgaveteksten) og en
 rutenettstørrelse på 71x71 (koordinater fra 0 til 70), simuler de første
 1024 byte-nedfallene. Hva er minimum antall trinn som kreves for
 å nå (70,70) fra (0,0)?
 */
public class Oppgave_18 {
    private static final int GRID_SIZE = 71; // 70 + 1 (0-indexed)
    private static final int TARGET_X = 70;
    private static final int TARGET_Y = 70;
    private static final int MAX_BYTES = 1024;

    private static final int[] DX = {0,0,1,-1}; // Right, left, down, up
    private static final int[] DY = {1,-1,0,0};



    /**
     * Leser koordinatposisjoner fra en tekstfil.
     * Hver linje i filen forventes å inneholde to heltall separert med komma,
     * som representerer x- og y-koordinater.
     *
     * Metoden stopper å lese etter å ha lest MAX_BYTES linjer eller ved filslutt.
     *
     * @param filename filnavnet som skal leses
     * @return en liste av Point-objekter med koordinatene, eller null hvis det oppstår feil
     */
    private static List<Point> readBytePositions(String filename) {
        List<Point> positions = new ArrayList<>();

        // Åpner filen for lesing med BufferedReader, som buffrer input for effektivitet
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int count = 0;  // Teller antall linjer lest for å begrense til MAX_BYTES

            // Les linje for linje, stopp ved filslutt eller når count når MAX_BYTES
            while ((line = br.readLine()) != null && count < MAX_BYTES) {
                // Splitter linjen på komma for å hente ut x og y koordinater
                String[] parts = line.split(",");

                if (parts.length == 2) {
                    try {
                        // Konverterer tekst til heltall, fjerner eventuelle mellomrom
                        int x = Integer.parseInt(parts[0].trim());
                        int y = Integer.parseInt(parts[1].trim());

                        // Legger til koordinatene som et Point-objekt i listen
                        positions.add(new Point(x, y));
                        count++;
                    } catch (NumberFormatException e) {
                        // Feil ved konvertering fra tekst til tall, logges og metoden returnerer null
                        System.err.println("Invalid coordinate format: " + line);
                        return null;
                    }
                } else {
                    // Hvis linjen ikke har nøyaktig to deler, logges feil og metoden returnerer null
                    System.err.println("Invalid number of coordinates: " + line);
                    return null;
                }
            }
        } catch (IOException e) {
            // Håndterer eventuelle I/O-feil ved lesing av filen, logger feilen og returnerer null
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }

        // Returnerer listen med alle lesne koordinater
        return positions;
    }

    /**
     * Finner den korteste veien fra startpunktet (0,0) til et forhåndsdefinert målpunkt (TARGET_X, TARGET_Y)
     * i et todimensjonalt rutenett. Rutenettet inneholder ødelagte byte-posisjoner som ikke kan krysses.
     *
     * Algoritmen bruker Breadth-First Search (BFS) for å garantere at den finner den korteste stien i antall steg,
     * siden BFS utforsker alle noder på ett nivå før den går videre til neste.
     *
     * @param bytePositions Liste over korrupt(e) byte-posisjoner som ikke kan krysses (f.eks. 1024 "infiserte" punkter)
     * @return Antall steg i korteste vei fra (0,0) til (TARGET_X, TARGET_Y), eller -1 hvis ingen vei finnes
     */
    private static int findShortestPAth(List<Point> bytePositions) {
        // Oppretter et GRID_SIZE x GRID_SIZE rutenett, der true = korrupt, false = trygt
        boolean[][] grid = new boolean[GRID_SIZE][GRID_SIZE];

        // Marker de første 1024 bytene som korrupte
        for (Point p : bytePositions) {
            grid[p.x][p.y] = true;  // Kan ikke gå gjennom denne cellen
        }

        // Oppretter BFS-kø og legger til startpunktet (0,0) med 0 steg
        Queue<State> queue = new LinkedList<>();
        queue.offer(new State(0, 0, 0));

        // Holder styr på hvilke celler vi allerede har besøkt for å unngå uendelige løkker
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
        visited[0][0] = true;

        // BFS-løkke: henter neste posisjon fra køen og utforsker naboer
        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            int x = currentState.x;
            int y = currentState.y;
            int steps = currentState.steps;

            // Hvis vi har nådd målet, returner antall steg brukt
            if (x == TARGET_X && y == TARGET_Y) {
                return steps;
            }

            // Utforsk alle fire retninger: opp, ned, høyre, venstre
            for (int i = 0; i < 4; i++) {
                int nextX = x + DX[i];
                int nextY = y + DY[i];

                // Sjekk om neste posisjon er gyldig, ikke korrupt og ikke besøkt før
                if (isValid(nextX, nextY) && !grid[nextX][nextY] && !visited[nextX][nextY]) {
                    queue.offer(new State(nextX, nextY, steps + 1)); // Legg til ny posisjon i køen
                    visited[nextX][nextY] = true; // Merk som besøkt
                }
            }
        }
        // Hvis vi ikke finner noen vei til målet, returner -1
        return -1;
    }

    private static boolean isValid(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }

    private static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class State {
        int x, y, steps;
        public State(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }
    }

    public static void main(String[] args) {
        String filename = "bytes.txt";

        // Leser posisjoner fra fil
        List<Point> positions = readBytePositions(filename);

        if (positions == null) {
            System.err.println("Kunne ikke lese filen eller fant ugyldige data.");
            return;
        }

        // Kjører BFS og finner korteste vei
        int shortestPath = findShortestPAth(positions);

        if (shortestPath == -1) {
            System.out.println("Ingen vei funnet fra (0, 0) til målpunktet.");
        } else {
            System.out.println("Korteste vei: " + shortestPath + " steg.");
        }
    }
}
