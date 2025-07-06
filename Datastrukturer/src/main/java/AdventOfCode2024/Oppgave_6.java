package AdventOfCode2024;

/*
Oppgave:

En vakt følger en bestemt patruljerute i et kartlagt område. Ruten defineres av følgende regler:

    Hvis det er en hindring rett foran vakten, sving 90 grader til høyre.

    Ellers, ta et skritt fremover.

Gitt et kart som viser vaktens startposisjon og retning, samt plasseringen av hindringer, simuler vaktens rute til vakten forlater kartet.

Mål:

Finn antall unike posisjoner vakten besøker (inkludert startposisjonen) før den forlater kartet.
 */


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Oppgave_6 {

    private enum Direction {
        UP(-1, 0), RIGHT(0,1), DOWN(1,0), LEFT(0,-1);

        int dRow, dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }
        // Returnerer retningen man får ved å snu 90 grader med klokka (høyre) fra nåværende retning
        Direction turnRight() {
            return values()[(this.ordinal() + 1) % 4];
        }
    }

    private static class Point {
        int row, col;  // Rad- og kolonneposisjon i kartet

        // Konstruktør som setter rad og kolonne for punktet
        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        // Overstyrer equals for å sammenligne to Point-objekter basert på rad og kolonne
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return row == p.row && col == p.col;
        }

        // Overstyrer hashCode for å sikre at like Point-objekter gir samme hash-verdi
        // Dette er viktig for at Point skal fungere korrekt i HashSet og andre hash-baserte samlinger
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }


    public static int simulateGuard(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        // Finn startposisjon og startretning til vakten
        // Initialiseres med ugyldige verdier inntil vi finner vakten på kartet
        int startRow = -1, startCol = -1;
        Direction dir = null;
        // Ytre løkke merket med 'outer' for å enkelt bryte ut av begge løkkene samtidig
        outer:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = map[r][c];
                // Finn retningen til vakten basert på symbolet i kartet
                switch (ch) {
                    case '^' : dir = Direction.UP; break;
                    case 'v' : dir = Direction.DOWN; break;
                    case '>' : dir = Direction.RIGHT; break;
                    case '<' : dir = Direction.LEFT; break;
                }
                // Når vi har funnet vakten, lagrer vi posisjonen og bryter ut av løkkene
                if (dir != null) {
                    startRow = r;
                    startCol = c;
                    break outer;
                }
            }
        }
        // Sett for å holde styr på hvilke ruter vakten har besøkt
        Set<Point> visited = new HashSet<>();
        int row = startRow, col = startCol;
        visited.add(new Point(row, col)); // Legg til startposisjonen
        // Simuler vakten som går i retningen den peker, til den går utenfor kartet
        while (true) {
            int nextRow = row + dir.dRow;
            int nextCol = col + dir.dCol;
            // Sjekk om neste posisjon er utenfor kartet
            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                break;  // Vakten forlater kartet, stopp simuleringen
            }
            // Hvis neste posisjon er en vegg ('#'), snu vakten 90 grader til høyre
            if (map[nextRow][nextCol] == '#') {
                dir = dir.turnRight();
            } else {
                // Flytt vakten til neste posisjon og merk den som besøkt
                row = nextRow;
                col = nextCol;
                visited.add(new Point(row, col));
            }
        }
        // Returner antall unike posisjoner vakten har besøkt
        return visited.size();
    }

    public static void main(String[] args) {
        String[] lines = {
                "....#.....",
                ".........#",
                "..........",
                "..#.......",
                ".......#..",
                "..........",
                ".#..^.....",
                "........#.",
                "#.........",
                "......#..."
        };
        char[][] map = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            map[i] = lines[i].toCharArray();
        }
        int result = simulateGuard(map);
        System.out.println("Unike besøkte posisjoner: " + result);
    }
}
