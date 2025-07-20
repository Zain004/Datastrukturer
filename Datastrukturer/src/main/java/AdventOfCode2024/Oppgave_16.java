package AdventOfCode2024;

import com.sun.jdi.PrimitiveValue;

import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/*
Oppgave: Reinsdyrlabyrint – Korteste vei

Du er strandet i en reinsdyrlabyrint! Målet ditt er å finne den laveste poengsummen et reinsdyr kan oppnå for å komme fra start (S) til slutt (E).

Regler:

Reinsdyret starter ved 'S' og vender mot øst.

Bevege seg fremover én rute (ikke gjennom vegger '#') koster 1 poeng.

Roterer 90 grader med eller mot klokken koster 1000 poeng.

Eksempel:

Kart:

Generated code
###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############


Forventet utfall:

Den laveste poengsummen for denne labyrinten er 7036.

Din oppgave:

Du får et kart over reinsdyrlabyrinten som en tekststreng. Skriv kode for å finne den laveste poengsummen et reinsdyr kan oppnå for å komme fra start til slutt.

Inndata (Eksempel):

Generated code
"###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.
#.###.#.#.#.#.#
#S..#.....#...#
###############"


Utdata (Eksempel):

Generated code
7036

Tips:
    Bruk en algoritme for søk etter korteste vei, som f.eks. Dijkstra eller A*.
    Husk å ta hensyn til kostnaden for både bevegelse og rotasjon.
    Representer retningen reinsdyret peker i.
    Effektiv kode er viktig for å unngå at programmet kjører for lenge.
 */

public class Oppgave_16 {
    private static final int MOVE_COST = 1;
    private static final int ROTATE_COST = 1000;
    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';

    // Enum for å representere endringer
    private enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    // Klasse for å representere en tilstand i labyrinten
    private static class State implements Comparable<State> {
        int row;
        int col;
        Direction direction;
        int cost;

        public State(int row, int col, Direction direction, int cost) {
            this.row = row;
            this.col = col;
            this.direction = direction;
            this.cost = cost;
        }

        @Override
        public int compareTo(State o) {
            return Integer.compare(this.cost, o.cost);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return row == state.row && col == state.col && direction == state.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, direction);
        }
    }

    public static int solveMaze(String mazeString) {
        // 1. Parser labyrinten fra String til et char[][] array
        char[][] maze = parseMaze(mazeString);
        int rows = maze.length;
        int cols = maze[0].length;

        // 2. Finn start- og sluttposisjon
        int startRow = -1;
        int startCol = -1;
        int endRow = -1;
        int endCol = -1;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == START) {
                    startRow = i;
                    startCol = j;
                } else if (maze[i][j] == END) {
                    endRow = i;
                    endCol = j;
                }
            }
        }

        // Valider at start og sluttposisjoner er funnet.  Kast exception om ikke.
        if (startRow == -1 || startCol == -1) {
            throw new IllegalArgumentException("Start position not found in the maze.");
        }
        if (endRow == -1 || endCol == -1) {
            throw new IllegalArgumentException("End position not found in the maze.");
        }


        // 3. Initialiser Dijkstra
        PriorityQueue<State> queue = new PriorityQueue<>();
        queue.offer(new State(startRow, startCol, Direction.EAST, 0)); // Reinsdyret starter mot øst

        //Bruker et Set for å optimalisere ytelse da contains/add er O(1) i et HashSet.
        Set<State> visited = new HashSet<>();
        visited.add(new State(startRow, startCol, Direction.EAST,0));

        //Array som inneholder minste kostnad for hver state. Brukes istedenfor map for å spare minne og øke ytelse
        int[][][][] minCost = new int[rows][cols][4][1]; // [row][col][direction][0=cost]
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < 4; k++) { // 4 directions
                    minCost[i][j][k][0] = Integer.MAX_VALUE;
                }
            }
        }
        minCost[startRow][startCol][Direction.EAST.ordinal()][0] = 0;


        // 4. Dijkstra algoritme
        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            int row = currentState.row;
            int col = currentState.col;
            Direction direction = currentState.direction;
            int cost = currentState.cost;

            // Sjekker om vi er på mål
            if (row == endRow && col == endCol) {
                return cost;
            }

            // Hopper over allerede besøkte stater. (Kan skje pga priority queue)
            if(visited.contains(currentState)){
                continue;
            }
            visited.add(currentState);

            // Muligheter: Fremover, rotere høyre, rotere venstre

            // A. Fremover
            int nextRow = row;
            int nextCol = col;

            switch (direction) {
                case NORTH:
                    nextRow--;
                    break;
                case EAST:
                    nextCol++;
                    break;
                case SOUTH:
                    nextRow++;
                    break;
                case WEST:
                    nextCol--;
                    break;
            }

            if (isValidMove(maze, nextRow, nextCol) && maze[nextRow][nextCol] != WALL) {
                int newCost = cost + MOVE_COST;
                Direction nextDirection = direction;
                if (newCost < minCost[nextRow][nextCol][nextDirection.ordinal()][0]) {

                    State nextState = new State(nextRow, nextCol, nextDirection, newCost);
                    if(!visited.contains(nextState)) { //Unngå å legge til noder som allerede er prosessert.
                        queue.offer(nextState);
                    }
                }
            }

            // B. Rotere Høyre
            Direction rotateRight = rotateRight(direction);
            int newCostRotateRight = cost + ROTATE_COST;

            if (newCostRotateRight < minCost[row][col][rotateRight.ordinal()][0]) {
                State nextStateRotateRight = new State(row, col, rotateRight, newCostRotateRight);
                if(!visited.contains(nextStateRotateRight)){ //Unngå å legge til noder som allerede er prosessert.
                    queue.offer(nextStateRotateRight);
                }
            }


            // C. Rotere Venstre
            Direction rotateLeft = rotateLeft(direction);
            int newCostRotateLeft = cost + ROTATE_COST;
            if (newCostRotateLeft < minCost[row][col][rotateLeft.ordinal()][0]) {
                State nextStateRotateLeft = new State(row, col, rotateLeft, newCostRotateLeft);
                if(!visited.contains(nextStateRotateLeft)){ //Unngå å legge til noder som allerede er prosessert.
                    queue.offer(nextStateRotateLeft);
                }
            }
        }

        // Hvis ingen vei finnes
        return -1;
    }

    private static char[][] parseMaze(String mazeString) {
        String[] rows = mazeString.split("\n");
        int numRows = rows.length;
        int numCols = rows[0].length();
        char[][] maze = new char[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            maze[i] = rows[i].toCharArray();
        }

        return maze;
    }

    public static boolean isValidMove(char[][] maze, int row, int col) {
        return row >= 0 && row < maze.length && col >= 0 && col < maze[0].length;
    }

    private static Direction rotateRight(Direction direction) {
        switch (direction) {
            case NORTH: return Direction.EAST;
            case EAST: return Direction.SOUTH;
            case SOUTH: return Direction.WEST;
            case WEST: return Direction.NORTH;
            default: return null; // Skal ikke skje
        }
    }
    private static Direction rotateLeft(Direction direction) {
        switch (direction) {
            case NORTH: return Direction.WEST;
            case EAST: return Direction.NORTH;
            case SOUTH: return Direction.EAST;
            case WEST: return Direction.SOUTH;
            default: return null; // Skal ikke skje
        }
    }



    public static void main(String[] args) {
        String maze1 = "###############\n#.......#....E#\n#.#.###.#.###.#\n#.....#.#...#.#\n#.###.#####.#.#\n#.#.#.......#.#\n#.#.#####.###.#\n#...........#.#\n###.#.#####.#.#\n#...#.....#.#.#\n#.#.#.###.#.#.#\n#.....#...#.#.#\n#.###.#.#.#.#.#\n#S..#.....#...#\n###############";
        String maze2 = "#################\n#...#...#...#..E#\n#.#.#.#.#.#.#.#.#\n#.#.#.#...#...#.#\n#.#.#.#.###.#.#.#\n#...#.#.#.....#.#\n#.#.#.#.#.#####.#\n#.#...#.#.#.....#\n#.#.#####.#.###.#\n#.#.#.......#...#\n#.#.###.#####.###\n#.#.#...#.....#.#\n#.#.#.#####.###.#\n#.#.#.........#.#\n#.#.#.#########.#\n#S#.............#\n#################";

        System.out.println("Maze 1 lowest cost: " + solveMaze(maze1));
        System.out.println("Maze 2 lowest cost: " + solveMaze(maze2));
    }
}
