package AdventOfCode2024;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Simuler et boolsk logisk kretsløp basert på en beskrivelse av porter (AND, OR, XOR)
og ledninger. Kretsløpet tar innledende verdier for noen ledninger og definerer
sammenhenger mellom porter og ledninger (f.eks., "x00 AND y00 -> z00"). Målet er
å beregne de endelige verdiene på alle ledningene som starter med "z", kombinere
disse verdiene som et binært tall (z00 er minst signifikant), og konvertere dette
binære tallet til desimal.
 */
public class Oppgave_24 {
    private static final String AND = "AND";
    private static final String OR = "OR";
    private static final String XOR = "XOR";

    private final Map<String, Integer> wireValues = new HashMap<>(); // Lagrer verdien (0 eller 1) for hver ledning.
    private final List<Gate> gates = new ArrayList<>(); // Lagrer alle portene i kretsen.
    private static final Pattern INITIAL_VALUE_PATTERN = Pattern.compile("([a-zA-Z0-9]+):(0|1)");
    private static final Pattern GATE_PATTERN = Pattern.compile("([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)");

    public Oppgave_24(List<String> input) {
        parseInput(input);
    }

    private static class Gate {
        private String inputWire1;
        private String operation;
        private String inputWire2;
        private String outputWire;

        public Gate(String inputWire1, String operation, String inputWire2, String outputWire1) {
            this.inputWire1 = inputWire1;
            this.operation = operation;
            this.inputWire2 = inputWire2;
            this.outputWire = outputWire1;
        }
    }

    /**
     * Parser (tolker) input-linjene for å initialisere ledningsverdier og opprette port-objekter.
     * @param input En liste med strenger som representerer kretsbeskrivelsen.
     */
    private void parseInput(List<String> input) {
        for (String line : input) {
            Matcher initialValueMatcher = INITIAL_VALUE_PATTERN.matcher(line);
            if (initialValueMatcher.matches()) {
                String wire = initialValueMatcher.group(1);
                int value = Integer.parseInt(initialValueMatcher.group(2));
                wireValues.put(wire, value);
            } else {
                Matcher gateMatcher = GATE_PATTERN.matcher(line);
                if (gateMatcher.matches()) {
                    String input1 = gateMatcher.group(1);
                    String operation = gateMatcher.group(2);
                    String input2 = gateMatcher.group(3);
                    String output1 = gateMatcher.group(4);
                    gates.add(new Gate(input1, operation, input2, output1));
                }
            }
        }
    }

    /**
     * Simulerer logikk-kretsen til alle ledninger koblet til 'z' har fått verdier.
     * @return Desimalverdien som representeres av ledningene som starter med 'z'.
     */
    public long simulate() {
        // Fortsett å simulere til alle 'z'-ledningene har verdier. Bruk av en while-løkke
        // for å håndtere tilfeller der gate-evalueringer er avhengige av hverandre.
        boolean changed = true;
        while (changed) {
            changed = false; // Reset på begynnelsen
            for (Gate gate : gates) {
                if (!wireValues.containsKey(gate.outputWire)) {
                    Integer inputValue1 = wireValues.get(gate.inputWire1);
                    Integer inputValue2 = wireValues.get(gate.inputWire2);

                    if (inputValue1 != null && inputValue2 != null) {
                        int outputValue = evaluateGate(gate.operation, inputValue1, inputValue2);
                        wireValues.put(gate.outputWire, outputValue);
                        changed = true; // Settet er endret til sann siden vi oppdaterte en verdi
                    }
                }
            }
        }
        StringBuilder binaryString = new StringBuilder();
        int index = 0;

        while (wireValues.containsKey("z" + index)) {
            binaryString.insert(0, wireValues.get("z" + index));
            index++;
        }

        if (binaryString.length() == 0) {
            return 0;
        }

        return Long.parseLong(binaryString.toString(), 2);
    }
    /**
     * Evaluerer en enkelt gate basert på dens operasjon og inngangsverdier.
     * @param operasjon Gatetypen (OG, ELLER, XELLER).
     * @param input1 Verdien til den første inngangsledningen.
     * @param input2 Verdien til den andre inngangsledningen.
     * @return Utgangsverdien (0 eller 1) til gate.
     */
    private int evaluateGate(String operation, int input1, int input2) {
        switch (operation) {
            case AND:
                return input1 & input2;
            case OR:
                return input1 | input2;
            case XOR:
                return input1 ^ input2;
            default:
                throw new IllegalArgumentException("Invalid gate operation: " + operation);
        }
    }
    public static void main(String[] args) {
        List<String> input = List.of(
                "x00: 1",
                "x01: 0",
                "x02: 1",
                "x03: 1",
                "x04: 0",
                "y00: 1",
                "y01: 1",
                "y02: 1",
                "y03: 1",
                "y04: 1",
                "ntg XOR fgs -> mjb",
                "y02 OR x01 -> tnw",
                "kwq OR kpj -> z05",
                "x00 OR x03 -> fst",
                "tgd XOR rvg -> z01",
                "vdt OR tnw -> bfw",
                "bfw AND frj -> z10",
                "ffh OR nrd -> bqk",
                "y00 AND y03 -> djm",
                "y03 OR y00 -> psh",
                "bqk OR frj -> z08",
                "tnw OR fst -> frj",
                "gnj AND tgd -> z11",
                "bfw XOR mjb -> z00",
                "x03 OR x00 -> vdt",
                "gnj AND wpb -> z02",
                "x04 AND y00 -> kjc",
                "djm OR pbm -> qhw",
                "nrd AND vdt -> hwm",
                "kjc AND fst -> rvg",
                "y04 OR y02 -> fgs",
                "y01 AND x02 -> pbm",
                "ntg OR kjc -> kwq",
                "psh XOR fgs -> tgd",
                "qhw XOR tgd -> z09",
                "pbm OR djm -> kpj",
                "x03 XOR y03 -> ffh",
                "x00 XOR y04 -> ntg",
                "bfw OR bqk -> z06",
                "nrd XOR fgs -> wpb",
                "frj XOR qhw -> z04",
                "bqk OR frj -> z07",
                "y03 OR x01 -> nrd",
                "hwm AND bqk -> z03",
                "tgd XOR rvg -> z12",
                "tnw OR pbm -> gnj"
        );
        Oppgave_24 oppgave = new Oppgave_24(input);
        long result = oppgave.simulate();
        System.out.println(result);
    }
}
