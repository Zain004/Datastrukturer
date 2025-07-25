package AdventOfCode2024;

import javax.swing.text.Position;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
Oppgave: Juksekoder på Racerbanen

Du er en programmerer som deltar i en løpsfestival i CPU-en.
Målet er å fullføre en kronglete racerbane på kortest mulig tid.
Du har muligheten til å jukse én gang ved å deaktivere kollisjon
 i opptil 2 pikosekunder, slik at du kan gå gjennom vegger. Men
 du må være tilbake på vanlig bane etter jukseperioden, ellers
 blir du diskvalifisert.

Du har fått et kart over racerbanen, som består av spor (.), vegger
 (#), startposisjonen (S) og sluttposisjonen (E). Hver bevegelse opp,
  ned, til venstre eller høyre tar 1 pikosekund.

Målet:

Finn ut hvor mange juksekoder som vil spare deg for minst 100 pikosekunder.
En juksekode identifiseres unikt ved sin start- og sluttposisjon, der
startposisjonen er der juksekoden aktiveres og sluttposisjonen er der du
er tilbake på vanlig bane. Tiden spart av en juksekode er differansen
mellom den raskeste veien fra start til slutt uten juks, og den raskeste
veien fra start til slutt med juks (der juksen varer i maksimalt 2 pikosekunder).

Eksempelkart (som vist i oppgaveteksten):

Generated code
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############


Input:

Du vil få et kart over racerbanen i et lignende format som eksempelet ovenfor. Kartet vil definere plasseringen av start, slutt, vegger og spor.

Eksempelberegning (ikke nødvendigvis for å løse oppgaven, men for å illustrere konsepter):

Anta at den korteste veien fra start til slutt uten juks er 150 pikosekunder.

Hvis en juksekode tillater deg å fullføre banen på 48 pikosekunder (ved å gå gjennom vegger i maksimalt 2 pikosekunder), vil den spare deg for 102 pikosekunder (150 - 48 = 102). Denne juksekoden teller med i resultatet.

Hvis en juksekode tillater deg å fullføre banen på 60 pikosekunder, vil den spare deg for 90 pikosekunder (150 - 60 = 90). Denne juksekoden teller ikke med i resultatet.

Krav:

Du må finne den korteste veien mellom to punkter på kartet, både med og uten juks.

Jukseperioden kan maksimalt vare i 2 pikosekunder (dvs. du kan bevege deg gjennom maksimalt to vegger etter å ha aktivert juksekoden).

Etter jukseperioden må programmet befinne seg på vanlig bane igjen (et spor).

Tips:

Bruk algoritmer for å finne korteste vei, som f.eks. Dijkstra eller A*.

Tenk på hvordan du kan representere kartet og posisjoner i koden din.

Del opp problemet i mindre delproblemer.

Lykke til med løpet!
 */
public class Oppgave_20 {

    private static final char Track = '.';
    private static final char Wall = '#';
    private static final char START = 'S';
    private static final char END = 'E';
    private static final int[] DR = {-1,1,0,0}; // Endring i rad(opp, ned, venstre, høyre)
    private static final int[] DC = {0,0,-1,1}; // Endirng i kolonne (Opp, ned, Venstre, Høyre)
    private static final int NUMBER_OF_DIRECTIONS = 4;
    private static final int MAX_CHEAT_STEPS = 2;
    private static final int MIN_SAVING = 100;

    private char[][] map;
    private int width;
    private int height;
    private Position start;
    private Position end;

    private static class Position {
        private int row, col;
        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    public Oppgave_20(String mapFilePath) throws IOException {
        loadMap(mapFilePath);
    }

    /**
     * Leser inn et kart fra en tekstfil, lagrer det som et 2D char-array,
     * og finner start- og sluttposisjonen i kartet.
     * <p>
     * Filen leses linje for linje, og hver linje konverteres til en rad i kartet.
     * Metoden forventer at kartet inneholder én startposisjon og én sluttposisjon,
     * definert av konstantene {@code START} og {@code END}.
     * </p>
     *
     * @param mapFilePath filstien til kartfilen som skal lastes
     * @throws IOException hvis filen ikke kan leses
     * @throws IllegalArgumentException hvis start- eller sluttposisjon ikke finnes i kartet
     */
    private void loadMap(String mapFilePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        height = lines.size();
        width = lines.get(0).length();
        map = new char[height][width];

        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            map[i] = line.toCharArray();
            for (int j = 0; j < width; j++) {
                if (map[i][j] == START) {
                    start = new Position(i, j);
                } else if (map[i][j] == END) {
                    end = new Position(i, j);
                }
            }
        }

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start or end position are null");
        }
    }

    /**
     * Løser problemet ved å finne antall "jukse"-strategier som gir en
     * betydelig kortere rute fra start til slutt. En juks innebærer at man
     * går gjennom maks to vegger (definert som MAX_CHEAT_STEPS).
     *
     * @return Antall gyldige jukseruter som gir besparelse over terskelverdien.
     */
    public int solve() {
        // Finn den korteste vanlige veien uten juks
        int shortestPathWithoutCheat = findShortestPath(start, end, false);

        // Teller antall gyldige jukseruter som gir ønsket besparelse
        int savingCheats = 0;

        // Iterer gjennom alle mulige startposisjoner i kartet
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                // Hopper over vegger – man kan ikke starte et juks fra en vegg
                if (map[row][col] == Wall) continue;

                Position cheatStart = new Position(row, col);

                // Undersøker alle posisjoner inntil 3 ruter unna (i alle retninger, inkludert diagonalt)
                // Math.max / min sikrer at vi ikke går utenfor rutenettet
                for (int endRow = Math.max(0, row - 3); endRow < Math.min(height, row + 4); endRow++) {
                    for (int endCol = Math.max(0, col - 3); endCol < Math.min(width, col + 4); endCol++) {

                        // Kan ikke avslutte jukset i en vegg
                        if (map[endRow][endCol] == Wall) continue;

                        Position cheatEnd = new Position(endRow, endCol);

                        // Finn korteste vei fra cheatStart til cheatEnd,
                        // tillatt å gå gjennom maks MAX_CHEAT_STEPS vegger
                        int cheatPathLength = findShortestPathWithMaxWallSteps(cheatStart, cheatEnd, MAX_CHEAT_STEPS);

                        // Hvis det finnes en gyldig juksesti mellom start og sluttpunkt
                        if (cheatPathLength != Integer.MAX_VALUE) {

                            // Finn vanlig korteste vei fra slutten av jukseruten til mål
                            int pathFromCheatEndToGoal = findShortestPath(new Position(endRow, endCol), end, false);

                            // Full lengde for jukseruten: juksesti + normalsti etter juks
                            int pathWithCheat = cheatPathLength + pathFromCheatEndToGoal;

                            // Beregn hvor mange skritt man sparer ved å jukse
                            int saving = shortestPathWithoutCheat - pathWithCheat;

                            // Hvis man sparer nok (over terskelverdi), regnes dette som en gyldig juks
                            if (saving >= MIN_SAVING) {
                                savingCheats++;
                            }
                        }
                    }
                }
            }
        }

        // Returnerer totalt antall nyttige jukseruter
        return savingCheats;
    }


    /**
     * Finner den korteste veien mellom to posisjoner på kartet ved hjelp av bredde-først-søk (BFS).
     *
     * @param start       startposisjonen
     * @param end         sluttposisjonen
     * @param allowCheating om det er tillatt å gå gjennom vegger
     * @return lengden på den korteste veien, eller Integer.MAX_VALUE hvis ingen vei finnes
     */
    private int findShortestPath(Position start, Position end, boolean allowCheating) {
        Queue<State>
    }

    private static class State {
        private Oppgave_20.Position position;
        private int distance;
        public State(Oppgave_20.Position position, int distance) {
            this.position = position;
            this.distance = distance;
        }
    }

    private static class CheatState {
        private Oppgave_20.Position position;
        private int distance;
        private int wallSteps;
        public CheatState(Oppgave_20.Position position, int distance, int wallSteps) {
            this.position = position;
            this.distance = distance;
            this.wallSteps = wallSteps;
        }
    }


    public static void main(String[] args) {

    }
}
