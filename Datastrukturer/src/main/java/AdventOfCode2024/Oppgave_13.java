package AdventOfCode2024;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Oppgave: Klomaskin-optimalisering

Du befinner deg i spillehallen på et feriested, og målet er å vinne så mange premier som mulig fra klomaskinene. Hver maskin har to knapper, A og B, som beveger kloen langs X- og Y-aksen. Å trykke på A koster 3 poletter, mens B koster 1 polett. Du har en liste over hver maskins knappefunksjoner og premieplassering.

Målet: Finn det minste antall poletter du må bruke for å vinne så mange premier som mulig.

Inndata: En liste over klomaskiner, der hver maskin har følgende informasjon:

    Knapp A: X-bevegelse, Y-bevegelse

    Knapp B: X-bevegelse, Y-bevegelse

    Premie: X-koordinat, Y-koordinat

Eksempel-inndata (fra teksten):
Generated code


Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279



IGNORE_WHEN_COPYING_START
Use code with caution.
IGNORE_WHEN_COPYING_END

Krav:

    For hver maskin, finn ut om det er mulig å vinne premien ved å trykke A og B
    et antall ganger. Du anslår at hver knapp ikke trenger å trykkes ned mer enn
    100 ganger for å vinne en premie.
    Hvis det er mulig å vinne premien, finn den kombinasjonen av A og B som krever
    færrest poletter.
    Beregn totalt antall premier som kan vinnes.

    Beregn minimum antall poletter som kreves for å vinne alle premier som kan vinnes.

Eksempel-output (basert på eksempelet):

    Antall premier vunnet: 2

    Minimum antall poletter brukt: 480

Lever: Skriv kode som leser inndataene (et format definerer du selv), utfører beregningene og skriver ut antall premier vunnet og minimum antall poletter brukt.
 */
public class Oppgave_13 {
    // Maksimalt antall knappetrykk som er tillatt for å prøve å vinne en premie
    private static final int MAX_PRESSES = 100;
    /**
     * Tar inn en liste med maskindata og prøver å optimalisere
     * hvor mange premier man kan vinne, og hvor mange tokens det koster totalt.
     */
    public static Result optimize(List<String> machineData) {
        int totalTokens = 0;  // Total tokens brukt på alle vinnende maskiner
        int prizesWon = 0;    // Antall maskiner man klarer å vinne premie på

        // Gå gjennom alle maskinene
        for (String machineDatum : machineData) {
            // Hent ut konfigurasjon fra tekststrengen
            MachineConfig config = parseMachineData(machineDatum);

            // Løs maskinen: Finn ut om det finnes en kombinasjon av trykk som gir premie
            Solution solution = solveMachine(config);

            if (solution != null) {
                // Legg til tokens brukt for denne maskinen
                totalTokens += solution.tokens();

                // Øk antall premier
                prizesWon++;
            }
        }

        // Returner et Result-objekt med samlet resultat
        return new Result(totalTokens, prizesWon);
    }
    /**
     * Parser maskindata-strengen og trekker ut informasjon om
     * knappene og posisjonen til premien.
     */
    private static MachineConfig parseMachineData(String machineData) {
        // Regex for å finne knapp A og B og deres bevegelser
        Pattern buttonPattern = Pattern.compile("Button (A|B): X\\+([0-9]+), Y\\+([0-9]+)");

        // Regex for å finne premieplasseringen
        Pattern prizePattern = Pattern.compile("Prize: X=([0-9]+), Y=([0-9]+)");

        Matcher buttonMatcher = buttonPattern.matcher(machineData);
        Matcher prizeMatcher = prizePattern.matcher(machineData);

        ButtonConfig buttonA = null;
        ButtonConfig buttonB = null;
        PrizeLocation prize = null;

        int buttonCount = 0;

        // Finn begge knappene i teksten
        while (buttonMatcher.find()) {
            buttonCount++;
            String buttonName = buttonMatcher.group(1);     // "A" eller "B"
            int xMove = Integer.parseInt(buttonMatcher.group(2)); // X+ verdi
            int yMove = Integer.parseInt(buttonMatcher.group(3)); // Y+ verdi

            // Lag ButtonConfig-objekt for hver knapp
            if ("A".equals(buttonName)) {
                buttonA = new ButtonConfig(xMove, yMove, 3);
            } else if ("B".equals(buttonName)) {
                buttonB = new ButtonConfig(xMove, yMove, 1);
            }
        }

        // Vi forventer nøyaktig to knapper – A og B
        if (buttonCount != 2) {
            throw new IllegalArgumentException("Machine Data should have 2 buttons");
        }

        // Finn premieplasseringen
        if (prizeMatcher.find()) {
            int x = Integer.parseInt(prizeMatcher.group(1));
            int y = Integer.parseInt(prizeMatcher.group(2));
            prize = new PrizeLocation(x, y);
        } else {
            throw new IllegalArgumentException("Machine Data should have Prize data.");
        }

        // Returner samlet konfigurasjon for maskinen
        return new MachineConfig(buttonA, buttonB, prize);
    }


    private static Solution solveMachine(MachineConfig config) {
        ButtonConfig buttonA = config.buttonA;
        ButtonConfig buttonB = config.buttonB;
        PrizeLocation prize = config.prize;

        Solution bestSolution = null;
         for (int aPresses = 0; aPresses <= MAX_PRESSES; aPresses++) {
             for (int bPresses = 0; bPresses <= MAX_PRESSES; bPresses++) {
                 int xMove = (aPresses * buttonA.xMove) + (bPresses * buttonB.xMove);
                 int yMove = (aPresses * buttonA.yMove) + (bPresses * buttonB.yMove);

                 if (xMove == prize.x && yMove == prize.y) {
                     int tokens = (aPresses * buttonA.cost) + (bPresses * buttonB.cost);
                     Solution currentSolution = new Solution(tokens, aPresses, bPresses);
                     if (bestSolution == null || currentSolution.tokens < bestSolution.tokens) {
                         bestSolution = currentSolution;
                     }
                 }
             }
         }
        return bestSolution;
    }

    // ➕ RECORDS (immutable datastrukturer)

    // Resultat fra hele kjøringen: hvor mange premier, og hvor mange tokens
    private record Result(int prizesWon, int totalTokens) {}

    // En knapps bevegelse i X og Y per trykk
    private record ButtonConfig(int xMove, int yMove, int cost) {}

    // Koordinatene til premien som skal nås
    private record PrizeLocation(int x, int y) {}

    // Full maskinkonfigurasjon: to knapper og én premie
    private record MachineConfig(ButtonConfig buttonA, ButtonConfig buttonB, PrizeLocation prize) {}

    // Løsning for en enkelt maskin: hvor mange tokens, og hvor mange trykk på hver knapp
    private record Solution(int tokens, int aPresses, int bPresses) {}
    public static void main(String[] args) {
        List<String> machineData = List.of(
                "Button A: X+94, Y+34\nButton B: X+22, Y+67\nPrize: X=8400, Y=5400",
                "Button A: X+26, Y+66\nButton B: X+67, Y+21\nPrize: X=12748, Y=12176",
                "Button A: X+17, Y+86\nButton B: X+84, Y+37\nPrize: X=7870, Y=6450",
                "Button A: X+69, Y+23\nButton B: X+27, Y+71\nPrize: X=18641, Y=10279"
        );

        Result result = optimize(machineData);

        System.out.println("Antall premier vunnet: " + result.prizesWon);
        System.out.println("Minimum antall poletter brukt: " + result.totalTokens);
    }
}
