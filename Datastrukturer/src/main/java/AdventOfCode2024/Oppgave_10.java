package AdventOfCode2024;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
Et reinsdyr har gitt deg et topografisk kart over Lavaøya,
der høydene er representert med tall fra 0 (lavest) til 9 (høyest).
 Kartet består av et rutenett med tall, og du skal finne alle turstier
 fra toppunkt 0 til 9.

En gyldig tursti:
    Starter på et tall 0
    Ender på et tall 9

🧮 Mål: Finn summen av poengsummene til alle stistartpunktene i kartet.
🧪 Eksempel

Gitt dette kartet:

10..9..
2...8..
3...7..
4567654
...8..3
...9..2
.....01
oppgaven sier at du skal finne hvor mange 9 ere du kan nå fra en 0 til den andre,
hvor mange 9'ere som kommer imellom 0'ene

Her er det to startpunkter (0):

    Startpunktet øverst har poengsum 1

    Startpunktet nederst har poengsum 2

🔢 Totalt: 1 + 2 = 3

Du får input som en liste med strenger (én per rad). Du skal returnere ett tall:
summen av poengsummene til alle stistartpunktene i kartet.
 */
public class Oppgave_10 {
    private int[][] map;
    private int rows;
    private int cols;

    public Oppgave_10(int[][] map) {
        this.map = map;
        this.rows = map.length;
        this.cols = map[0].length;
    }

    /**
     * Representerer et startpunkt (trailhead) for en tursti på kartet.
     * En trailhead er definert ved sin rad- og kolonneposisjon.
     */
    public static class Trailhead {
        /** Radposisjonen til trailhead i kartet */
        int row;

        /** Kolonneposisjonen til trailhead i kartet */
        int col;

        /**
         * Oppretter et nytt trailhead-objekt med gitt rad og kolonne.
         *
         * @param row radindeks for trailhead
         * @param col kolonneindeks for trailhead
         */
        public Trailhead(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * Returnerer en strengrepresentasjon av trailhead-posisjonen i formatet "(rad, kolonne)".
         *
         * @return posisjonsstreng for trailhead
         */
        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }


    /**
     * Beregner total poengsum for alle startpunkter (trailheads) i kartet.
     * Dette gjøres ved å finne alle trailheads og summere poengsummen for hver.
     *
     * @return summen av poengsummene til alle trailheads på kartet
     */

    public int solve() {
        List<Trailhead> trailheads = findTrailHeads();
        int totalScore = 0;

        for (Trailhead trailhead : trailheads) {
            totalScore += calculateTrailHeadScore(trailhead);
        }
        return totalScore;
    }

    /**
     * Finner alle trailheads (startpunkter) i kartet.
     * Et trailhead er en posisjon hvor høyden er 0.
     *
     * @return liste over alle trailheads i kartet
     */
    private List<Trailhead> findTrailHeads() {
        List<Trailhead> trailheads = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == 0) {
                    trailheads.add(new Trailhead(i, j));
                }
            }
        }
        return trailheads;
    }
    /**
     * Beregner poengsummen for et gitt trailhead (startpunkt med høyde 0) i kartet.
     *
     * En gyldig sti kan bare gå til naboposisjoner (opp, ned, venstre, høyre) der høyden
     * øker med nøyaktig 1 i hvert steg. Stier som følger denne regelen kan nå én eller flere
     * posisjoner med høyde 9. Poengsummen er lik antall slike 9-ere som kan nås fra trailhead.
     *
     * Algoritmen bruker Breadth-First Search (BFS) og holder styr på besøkte posisjoner
     * for å unngå gjentatt besøk.
     *
     * @param trailhead startpunkt med høyde 0
     * @return antall forskjellige posisjoner med høyde 9 som kan nås fra trailhead via en gyldig sti
     */

    private int calculateTrailHeadScore(Trailhead trailhead) {
        int score = 0;
        boolean[][] visited = new boolean[rows][cols]; //Track visited cells for each trailhead

        Queue<Coordinate> queue = new LinkedList<>();

        queue.offer(new Coordinate(trailhead.row, trailhead.col));
        visited[trailhead.row][trailhead.col] = true;

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            int currentRow = current.row;
            int currentCol = current.col;

            if (map[currentRow][currentCol] == 9) {
                score++;
            }
            // Utforsk tilstøtende celler
            int[] dr = {0,0,1,-1}; // Right, Left, Down, Up
            int[] dc = {1,-1,0,0};

            for (int i = 0; i < 4; i++) {
                int newRow = currentRow + dr[i];
                int newCol = currentCol + dc[i];

                if (isValid(newRow, newCol) && !visited[newRow][newCol] && map[newRow][newCol] == map[currentRow][currentCol] + 1) {
                    queue.offer(new Coordinate(newRow, newCol));
                    visited[newRow][newCol] = true;
                }
            }
        }
        return score;
    }

    private static class Coordinate {
        private int row, col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
    public static void main(String[] args) {
        int[][] map = {
                {1, 0, 9, 2, 9, 2},
                {2, 2, 8, 2, 8, 2},
                {3, 2, 7, 2, 7, 2},
                {4, 5, 6, 7, 6, 5, 4},
                {2, 2, 8, 2, 2, 3},
                {2, 2, 9, 2, 2, 2},
                {2, 2, 2, 2, 0, 1}
        };
        Oppgave_10 oppgave = new Oppgave_10(map);
        System.out.println("Total Score: " + oppgave.solve());

    }
}
