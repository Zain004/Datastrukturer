package AdventOfCode2024;

/*
En liten alv trenger hjelp til å finne ordet "XMAS" i et ordsøk.
Ordet kan forekomme horisontalt, vertikalt, diagonalt, baklengs,
og kan overlappe andre forekomster. Finn alle forekomster av "XMAS"
i det gitte ordsøket.
 */

public class Oppgave_4 {
    private static final String TARGET = "XMAS";
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();  // Antall tilgjengelige prosessorkjerner for programmet (brukes f.eks. til å lage en trådpool)
    private static char[][] grid; // Vil inneholde selve ordsøkingsrutenettet

    /* {kolonne_1, kolonne_2}
    kolonne_1 er for opp eller ned, opp er -1 og ned er 1
    kolonne_2 er for høyre eller venstre. 1 er til høyre og -1 er til venstre
     */
    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };

    /**
     * Konverterer et array av tekstlinjer (String[]) til et 2D-tegnrutenett (char[][]).
     * Hver linje i tekst-arrayet blir én rad i rutenettet, og hvert tegn blir én celle.
     *
     * Eksempel:
     * lines[1] = "DEF";
     * lines[1].toCharArray(); gir {'D', 'E', 'F'}
     *
     * @param lines  Et array av strenger som representerer rader i rutenettet
     * @return       Et 2D char-array der hver celle inneholder ett tegn fra inputlinjene
     */
    private static char[][] parseGrid(String[] lines) {
        int rows = lines.length;
        int cols = lines[0].length();
        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines[i].toCharArray();
        }
        return grid;
    }

    /**
     * Teller hvor mange ganger et gitt ord forekommer i et 2D-tegnrutenett.
     * Ordet kan finnes i alle 8 retninger: horisontalt, vertikalt og diagonalt.
     *
     * Metoden går gjennom hver celle i rutenettet og sjekker om ordet
     * kan dannes fra den cellen i en av de tillatte retningene.
     *
     * @param grid  2D-rutenett av tegn (char), hvor det letes etter ordet
     * @param word  Ordet som skal søkes etter i rutenettet
     * @return      Antall ganger ordet forekommer i rutenettet
     */
    private static int countWordOccurrences(char[][] grid, String word) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        int wordlen = word.length();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                for (int[] dir : DIRECTIONS) {
                    if (matches(grid, row, col, dir[0], dir[1], word)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Sjekker om et ord finnes i rutenettet fra en gitt startposisjon og retning.
     * Metoden går gjennom hvert tegn i ordet og sammenligner det med bokstavene
     * i rutenettet i angitt retning (dx, dy). Hvis hele ordet stemmer overens
     * og ikke går utenfor rutenettet, returneres true.
     *
     * @param grid  2D-tegnrutenett hvor vi leter etter ordet
     * @param row   Start-rad (y-posisjon) i rutenettet
     * @param col   Start-kolonne (x-posisjon) i rutenettet
     * @param dx    Endring i rad for hver bokstav (f.eks. 1 = ned, -1 = opp)
     * @param dy    Endring i kolonne for hver bokstav (f.eks. 1 = høyre, -1 = venstre)
     * @param word  Ordet som skal sjekkes
     * @return      true hvis hele ordet finnes fra startpunktet i gitt retning, ellers false
     */

    private static boolean matches(char[][] grid, int row, int col, int dx, int dy, String word) {
        for (int i = 0; i < word.length(); i++) { // antall bokstaver den skal gå opp, ned eller sideveis
            int r = row + i * dx;
            int c = col + i * dy;
            // Sjekker at vi ikke går utenfor rutenettet
            if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length) {
                return false;
            }
            // Beskytter mot feil bokstaver. Så snart én bokstav ikke stemmer, avslutter vi søket i den retningen
            if (grid[r][c] != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String[] input = {
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX"
        };
        char[][] grid = parseGrid(input);
        int total = countWordOccurrences(grid, TARGET);

        System.out.println("Totalt antall forekomster av \"" + TARGET + "\": " + total);
    }
}
