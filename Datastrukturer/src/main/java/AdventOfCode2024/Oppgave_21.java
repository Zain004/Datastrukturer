package AdventOfCode2024;


import java.util.*;

/*
OK, her er en kort og konkret oppgavetekst for problemet, designet for å være selvforklarende og enkel, med eksempler:

**Oppgave: Robot Tastatur Navigering**

Du styrer en robot som skal trykke inn koder på et numerisk tastatur. Roboten styres via et retningstastatur. Problemet er at *dette* retningstastaturet styres av en *annen* robot, som også styres av et retningstastatur! Målet er å finne den korteste sekvensen av knappetrykk på ditt retningstastatur for å få den første roboten til å trykke inn de riktige kodene.

**Tastaturer:**

*   **Numerisk Tastatur (Dør):**
    ```
    +---+---+---+
    | 7 | 8 | 9 |
    +---+---+---+
    | 4 | 5 | 6 |
    +---+---+---+
    | 1 | 2 | 3 |
    +---+---+---+
    |   | 0 | A |
    +---+---+---+
    ```

*   **Retningstastatur (Robot 1 & 2):**
    ```
        +---+---+
        | ^ | A |
    +---+---+---+
    | < | v | > |
    +---+---+---+
    ```

**Robot-Styring:**

1.  Du trykker på retningstastaturet (opp, ned, venstre, høyre).
2.  Dette styrer Robot 2, som trykker på *sitt* retningstastatur.
3.  Dette styrer Robot 1, som trykker på det numeriske tastaturet.
4.  'A'-knappen på hvert retningstastatur trykker på knappen robotarmen peker på.
5.  Alle roboter starter med armen pekende på 'A' på sitt tastatur.

**Eksempel:**

For å få Robot 1 til å skrive '0' på det numeriske tastaturet, kan du bruke sekvensen `<A` på det *første* retningstastaturet.  `<` beveger robotens arm fra A til 0.  `A` trykker på 0.

**Oppgave:**

Gitt en liste med fem koder (f.eks. "029A", "980A", "179A", "456A", "379A"), finn den korteste sekvensen av knappetrykk på *ditt* retningstastatur for å få Robot 1 til å skrive hver kode.

**Kompleksitetsberegning:**

For hver kode, beregn *kompleksiteten* som `(antall knappetrykk på DITT retningstastatur) * (numerisk verdi av koden, uten ledende nuller)`. For koden "029A" er den numeriske verdien 29.

**Mål:**

Finn summen av kompleksiteten for alle fem kodene.

**Krav:**

*   Roboter må *aldri* sikte på et tomt område på tastaturet (panikk!).
*   Finn den *korteste* sekvensen av knappetrykk.

Denne beskrivelsen er mer fokusert, bruker enklere språk og gir et tydelig eksempel for å illustrere problemet. Det fjerner også alt det ekstra, unødvendige bakgrunnsmaterialet fra den originale oppgaveteksten.

 */
public class Oppgave_21 {


    // interface for keyboard
    interface Keyboard {
        char getKeyAt(Position position); // hent bokstav på en gitt posijsosjon
        Position getStartPosition(); // Returner hvor du står når du starter
        boolean isValidPosition(Position position); // sjekk om posisjonen er gyldig
        Map<Position, Character> getLayout(); // Returnerer en kopy for å unngå modifisering
    }

    /**
     *
     * tilgjengelig fra alle klasser i samme package
     * layout er innkapslet – kun tilgjengelig inne i NumericKeyboard.
     * Dette følger prinsippet om "information hiding": bare NumericKeyboard
     * trenger å vite hvordan tastaturet er bygget opp.
     */
    static class NumericKeyboard implements Keyboard {
        private final Map<Position, Character> layout;

        public NumericKeyboard() {
            layout = new HashMap<>();
            layout.put(new Position(0, 0), '7');
            layout.put(new Position(0, 1), '8');
            layout.put(new Position(0, 2), '9');
            layout.put(new Position(1, 0), '4');
            layout.put(new Position(1, 1), '5');
            layout.put(new Position(1, 2), '6');
            layout.put(new Position(2, 0), '1');
            layout.put(new Position(2, 1), '2');
            layout.put(new Position(2, 2), '3');
            layout.put(new Position(3, 1), '0');
            layout.put(new Position(3, 2), 'A');
        }
        @Override
        public char getKeyAt(Position position) {
            return layout.getOrDefault(position, null); // Return null if postion is invalid
        }

        @Override
        public Position getStartPosition() {
            return new Position(0, 0); // 'A' key
        }

        @Override
        public boolean isValidPosition(Position position) {
            return layout.containsKey(position);
        }

        @Override
        public Map<Position, Character> getLayout() {
            return new HashMap<>(layout);
        }

    }

    private static class Position {
        private final int row, col;
        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
        public int getRow() {
            return row;
        }
        public int getCol() {
            return col;
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
            return "(" + row + "," + col + ")";
        }
    }

    // Represents the Directional Keyboard
    static class DirectionalKeyboard implements Keyboard {
        private final Map<Position, Character> layout;

        public DirectionalKeyboard() {
            layout = new HashMap<>();
            layout.put(new Position(0, 1), '^');
            layout.put(new Position(1, 0), '<');
            layout.put(new Position(1, 1), 'v');
            layout.put(new Position(1, 2), '>');
        }

        @Override
        public char getKeyAt(Position position) {
            return layout.getOrDefault(position, null);
        }

        @Override
        public Position getStartPosition() {
            return new Position(0, 1); // '^' key
        }

        @Override
        public boolean isValidPosition(Position position) {
            return layout.containsKey(position);
        }

        @Override
        public Map<Position, Character> getLayout() {
            return new HashMap<>(layout);
        }
    }
    /**
     * Klasse som løser tastaturgåten basert på to forskjellige tastaturer.
     * <p>
     * Den bruker et numerisk tastatur og et retningsbasert tastatur for å oversette koder
     * gjennom flere steg og kalkulerer total "kompleksitet" av løsningene.
     */
    static class KeyboardPuzzleSolver {
        /** Tastatur for å oversette tallkoder */
        private final Keyboard numericKeyboard;
        /** Tastatur for å oversette retningssekvenser */
        private final Keyboard directionalKeyboard;
        private static final char[] DIRECTIONS = {'^', 'v', '<', '>'};
        /**
         * Oppretter en ny løser for tastaturgåten.
         *
         * @param numericKeyboard tastaturet som brukes til å tolke numeriske koder
         * @param directionalKeyboard tastaturet som brukes til å tolke retningssekvenser
         */
        public KeyboardPuzzleSolver(Keyboard numericKeyboard, Keyboard directionalKeyboard) {
            this.numericKeyboard = numericKeyboard;
            this.directionalKeyboard = directionalKeyboard;
        }

        /**
         * Løser gåten for en liste med koder.
         *
         * For hver kode utføres tre oversettelser via tastaturene:
         * <ol>
         *   <li>Oversett kode til retningssekvens med numerisk tastatur</li>
         *   <li>Oversett retningssekvens til ny retningssekvens med retningsbasert tastatur (første runde)</li>
         *   <li>Oversett retningssekvens til ny retningssekvens med retningsbasert tastatur (andre runde)</li>
         * </ol>
         * Hvis en oversettelse mislykkes, avbrytes prosessen.
         * Kompleksiteten av resultatet akkumuleres.
         *
         * @param codes listen av koder som skal løses
         * @return total kompleksitet av løsningene, eller 0 hvis mislykket
         */
        public long solve(List<String> codes) {
            long totalComplexity = 0;

            // Behandler hver kode i listen
            for (String code : codes) {
                // Steg 1: Oversett koden til en retningssekvens basert på numerisk tastatur
                String directionalSequence1 = translateCodeToDirectionalSequence(code, numericKeyboard);

                // Steg 2: Oversett retningssekvensen med det retningsbaserte tastaturet (første gang)
                String directionalSequence2 = translateDirectionalSequenceToDirectionalSequence(directionalSequence1, directionalKeyboard);

                // Steg 3: Oversett retningssekvensen igjen med retningsbasert tastatur (andre gang)
                String directionalSequence3 = translateDirectionalSequenceToDirectionalSequence(directionalSequence2, directionalKeyboard);

                // Hvis oversettelsen mislykkes, stopp og returner 0
                if (directionalSequence3 == null) {
                    System.err.println("Failed to solve code: " + code);
                    return 0;
                }

                // Beregn kompleksitet for denne koden basert på den endelige retningssekvensen
                long complexity = calculateComplexity(code, directionalSequence3);

                // Akkumuler kompleksiteten
                totalComplexity += complexity;

                // Skriv ut resultatet for denne koden
                System.out.println(code + ": " + directionalSequence3);
            }

            // Returner total kompleksitet etter å ha behandlet alle koder
            return totalComplexity;
        }

        /**
         * Oversetter en kode (som f.eks. "123A") til en retningssekvens (f.eks. "RRDLD")
         * basert på det oppgitte tastaturet.
         * <p>
         * Starter på tastaturets definert startposisjon, og for hvert tegn i koden,
         * forsøker den å finne korteste vei til tasten på tastaturet.
         * Hvis én enkelt tast ikke kan nås, returneres {@code null}.
         *
         * @param code     strengen som representerer koden som skal tolkes
         * @param keyboard tastaturet som brukes for å tolke posisjonene
         * @return en streng med retningsbevegelser (f.eks. "UUDDLR"), eller {@code null} hvis ikke mulig
         */
        private String translateCodeToDirectionalSequence(String code, Keyboard keyboard) {
            // Start fra den definerte startposisjonen på tastaturet
            Position startPosition = keyboard.getStartPosition();
            // Strengen som samler hele retningssekvensen
            StringBuilder sequence = new StringBuilder();
            // Gå gjennom hver tast i koden
            for (char key : code.toCharArray()) {
                // Finn korteste vei fra nåværende posisjon til tasten
                String path = findShortestPath(startPosition, key, keyboard);
                if (path == null) {
                    // Hvis ingen vei finnes, er koden ugyldig
                    return null;
                }
                // Legg til retningene i den samlede sekvensen
                sequence.append(path);
                // Oppdater startposisjon for neste tast
                startPosition = findLastPosition(path, keyboard, startPosition);
            }

            // Returner hele retningssekvensen som streng
            return sequence.toString();
        }

        private String translateDirectionalSequenceToDirectionalSequence(String directionalSequence, Keyboard keyboard) {
            return translateCodeToDirectionalSequence(directionalSequence, keyboard); // bruker samme logikk
        }

        private String findShortestPath(Position startPosition, char targetKey, Keyboard keyboard) {
            Queue<Pair<Position, String>> queue = new LinkedList<>();
            Set<Position> visited = new HashSet<>();

            queue.offer(new Pair<>(startPosition, null));
            visited.add(startPosition);

            while (!queue.isEmpty()) {
                Pair<Position, String> pair = queue.poll();
                Position position = pair.getKey();

                String path = pair.getValue();
                char keyAtPosition = keyboard.getKeyAt(position);

                if (keyAtPosition == targetKey) {
                    return path + "A";
                }

                for (char direction : DIRECTIONS) {
                    Position nextPosition = calculateNextPosition(position, direction, keyboard);
                    if (nextPosition != null && !visited.contains(nextPosition) && keyboard.isValidPosition(nextPosition)) {
                        queue.offer(new Pair<>(nextPosition, path + direction));
                        visited.add(nextPosition);
                    }
                }
            }
            return null; // ingen path funnet
        }

        private Position calculateNextPosition(Position position, char direction, Keyboard keyboard) {
            int row = position.getRow();
            int col = position.getCol();

            switch (direction) {
                case '^':
                    return new Position(row - 1, col);
                case 'v':
                    return new Position(row + 1, col);
                case '<':
                    return new Position(row, col - 1);
                case '>':
                    return new Position(row, col + 1);
                default:
                    return null;
            }
        }

        private Position findLastPosition(String path, Keyboard keyboard, Position startPosition) {
            Position currentPosition = startPosition;
            for (char move : path.toCharArray()) {
                currentPosition = calculateNextPosition(currentPosition, move, keyboard);
            }
            return currentPosition;
        }
        private long calculateComplexity(String code, String sequence) {
            try {
                int numericValue = Integer.parseInt(code.replaceAll("[^0-9]", ""));
                return (long) sequence.length() * numericValue;
            } catch (NumberFormatException e) {
                System.err.println("Could not parse numeric value from code: " + code);
                return 0;
            }
        }
        // Hjelpeklasse for Pair
        static class Pair<K, V> {
            private final K key;
            private final V value;
            public Pair(K key, V value) {
                this.key = key;
                this.value = value;
            }
            public K getKey() {
                return key;
            }
            public V getValue() {
                return value;
            }
        }

    }
    public static void main(String[] args) {
        List<String> codes = Arrays.asList("029A", "980A", "179A", "456A", "379A");
        KeyboardPuzzleSolver solver = new KeyboardPuzzleSolver(
                new NumericKeyboard(),
                new DirectionalKeyboard()
        );
        long totalComplexity = solver.solve(codes);
        System.out.println("Total complexity: " + totalComplexity);
    }
}
