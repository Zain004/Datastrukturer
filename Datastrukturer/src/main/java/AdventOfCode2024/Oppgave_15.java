package AdventOfCode2024;

/*
Advent of Code - Dag 15: Lagerkaos

En robot har tatt kontroll over et lyktefisklager og skyver rundt esker uten kontroll.
 Din oppgave er å simulere robotens bevegelser og beregne summen av GPS-koordinatene
  til alle eskene etter at roboten har fullført sine bevegelser.

Input:

Et kart over lageret som viser vegger (#), tomme plasser (.), esker (O) og
robotens startposisjon (@). En sekvens av bevegelser for roboten (^ for opp,
 v for ned, < for venstre, > for høyre).

Regler:

    Roboten forsøker å utføre hver bevegelse i sekvensen.
    Hvis roboten prøver å flytte inn i en vegg, skjer ingenting.
    Hvis roboten prøver å flytte inn i en eske, vil den dytte esken i
    samme retning, forutsatt at det ikke er en vegg bak esken. Hvis det
    er en vegg bak esken, skjer ingenting.
    Både roboten og esken kan kun bevege seg én rute om gangen.

GPS-koordinat:
    GPS-koordinaten for en eske er beregnet som 100 * (rad) + (kolonne),
    der rad og kolonne er henholdsvis radnummeret og kolonnenummeret til esken,
    med utgangspunkt i 0 for øverste rad og venstre kolonne.

Oppgave:
Simuler robotens bevegelser i lageret, og beregn summen av GPS-koordinatene
til alle eskene etter at roboten har fullført alle bevegelser.

Eksempler:
    Eksempel 1:

Kart:

Generated code
########
#..O.O.#
##@.O..#
#...O..#
#.#.O..#
#...O..#
#......#
########


Bevegelser: <^^>>>vv<v>>v<<

Sluttresultat:

Generated code
#....OO#
##.@...#
#...O..#
#.#.O..#
#...O..#
#...O..#
#......#
########
IGNORE_WHEN_COPYING_START
content_copy
download
Use code with caution.
IGNORE_WHEN_COPYING_END

Beregning av GPS-koordinater:

Eske 1: rad = 0, kolonne = 4, GPS = 100 * 0 + 4 = 4
Eske 2: rad = 0, kolonne = 5, GPS = 100 * 0 + 5 = 5
Eske 3: rad = 2, kolonne = 4, GPS = 100 * 2 + 4 = 204
Eske 4: rad = 3, kolonne = 2, GPS = 100 * 3 + 2 = 302
Eske 5: rad = 4, kolonne = 2, GPS = 100 * 4 + 2 = 402
Eske 6: rad = 5, kolonne = 2, GPS = 100 * 5 + 2 = 502
Total GPS-sum: 4 + 5 + 204 + 302 + 402 + 502 = 1419
(NB: Dette er ikke korrekt svar ifølge oppgaveteksten, men illustrerer beregningen)

Eksempel 2 (fra oppgaveteksten):
Kart:

Generated code
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########
Bevegelser: (Veldig lang, se original oppgavetekst)

Sluttresultat:

Generated code
##########
#.O.O.OOO#
#........#
#OO......#
#OO@.....#
#O#.....O#
#O.....OO#
#O.....OO#
#OO....OO#
##########

Total GPS-sum: 10092 (Dette er det korrekte svaret ifølge oppgaveteksten)

For å løse oppgaven må du:

Implementere logikken for å flytte roboten og eskene basert på bevegelsene og reglene.

Holde styr på posisjonen til alle eskene.

Beregne GPS-koordinaten for hver eske etter alle bevegelser er fullført.

Summere alle GPS-koordinatene.

Lykke til!
 */

public class Oppgave_15 {
    private char[][] warehouseMap;
    private int robotRow;
    private int robotCol;
    private String movements;

    public Oppgave_15(char[][] warehouseMap, String movements) {
        this.warehouseMap = warehouseMap;
        this.movements = movements;
        findRobotPosition();
    }

    private char[][] deepCopy(char[][] original) {
        if (original == null) {
            return null;
        }
        char[][] copy = new char[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i],    // ← kilderaden (rad nr. i i original)
                    0,              // ← start fra indeks 0 i den raden
                    copy[i],        // ← mål-raden (rad nr. i i copy)
                    0,              // ← start på indeks 0 i mål-raden
                    original[i].length); // ← antall elementer å kopiere

        }
        return copy;
    }

    private void findRobotPosition() {
        for (int i = 0; i < warehouseMap.length; i++) {
            for (int j = 0; j < warehouseMap[i].length; j++) {
                if (warehouseMap[i][j] == '@') {
                    robotRow = i;
                    robotCol = j;
                    return;
                }
            }
        }
        throw new IllegalArgumentException("No robot found in the initial map");
    }

    public long calculateTotalGpsCoordinates() {
        simulateMovements();
        return calculateGpsSum();
    }

    private void simulateMovements() {
        for (char move : movements.toCharArray()) {
            moveRobot(move);
        }
    }
    private void moveRobot(char move) {
        int newRow = robotRow;
        int newCol = robotCol;

        switch (move) {
            case '^':
                newRow--;
                break;
            case 'v':
                newRow++;
                break;
            case '<':
                newCol--;
                break;
            case '>':
                newCol++;
                break;
            default:
                throw new IllegalArgumentException("Invalid move: " + move);
        }

        if (isWall(newRow, newCol)) {
            return;
        }

        if (isBox(newRow, newCol)) {
            if (canMoveBox(newRow, newCol, move)) {
                moveBox(newRow, newCol, move);
                moveRobotTo(newRow, newCol);
            }
            // Hvis boksen ikke kan flyttes, forblir roboten også på plass
        } else {
            moveRobotTo(newRow, newCol);
        }
    }

    private void moveRobotTo(int newRow, int newCol) {
        warehouseMap[robotRow][robotCol] = '.'; // Fjern roboten fra den gamle posisjonen
        robotRow = newRow;
        robotCol = newCol;
        warehouseMap[robotRow][robotCol] = '@'; // Sett roboten på den nye posisjonen
    }

    private boolean canMoveBox(int boxRow, int boxCol, char move) {
        int newBoxRow = boxRow;
        int newBoxCol = boxCol;

        switch (move) {
            case '^':
                newBoxRow--;
                break;
            case 'v':
                newBoxRow++;
                break;
            case '<':
                newBoxCol--;
                break;
            case '>':
                newBoxCol++;
                break;
        }
        return !isWall(newBoxRow, newBoxCol) && !isBox(newBoxRow, newBoxCol);
    }

    private void moveBox(int boxRow, int boxCol, char move) {
        int newBoxRow = boxRow;
        int newBoxCol = boxCol;

        switch (move) {
            case '^':
                newBoxRow--;
                break;
            case 'v':
                newBoxRow++;
                break;
            case '<':
                newBoxCol--;
                break;
            case '>':
                newBoxCol++;
                break;
        }

        warehouseMap[boxRow][boxCol] = '.';
        warehouseMap[newBoxRow][newBoxCol] = 'O';
    }

    private boolean isWall(int row, int col) {
        return row < 0 || row >= warehouseMap.length || col < 0 || col >= warehouseMap[0].length
        || warehouseMap[row][col] == '#';
    }

    private boolean isBox(int row, int col) {
        return row >= 0 && row < warehouseMap.length && col >= 0 && col < warehouseMap[0].length && warehouseMap[row][col] == 'O';
    }
    private long calculateGpsSum() {
        long totalGps = 0;
        for (int i = 0; i < warehouseMap.length; i++) {
            for (int j = 0; j < warehouseMap[i].length; j++) {
                if (warehouseMap[i][j] == 'O') {
                    totalGps += calculateGpsCoordinate(i, j);
                }
            }
        }
        return totalGps;
    }
    private long calculateGpsCoordinate(int row, int col) {
        return 100L * row + col;
    }

    private char[][] getWarehouseMap() {
        return deepCopy(warehouseMap);
    }

    public void printMap() {
        for(int i = 0; i < warehouseMap.length; i++) {
            for (int j = 0; j < warehouseMap[i].length; j++) {
                System.out.print(warehouseMap[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        char[][] initialMap = {
                {'#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', '.', '.', 'O', '.', 'O', '.', '#'},
                {'#', '#', '@', '.', 'O', '.', '.', '#'},
                {'#', '.', '.', '.', 'O', '.', '.', '#'},
                {'#', '#', '.', '#', '.', 'O', '.', '#'},
                {'#', '.', '.', '.', 'O', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#'}
        };

        String movements = "<^^>>>vv<v>>v<<";

        Oppgave_15 game = new Oppgave_15(initialMap, movements);
        long totalGps = game.calculateTotalGpsCoordinates();

        System.out.println("Total GPS coordinate sum: " + totalGps);

        // Print final warehouse state for verification
        System.out.println("Final Warehouse State:");
        game.printMap();
    }
}
