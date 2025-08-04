package AdventOfCode2023;

/*
Oppgave:

Kalibreringsdokumentet består av tekstlinjer. For hver linje, finn det første og
siste sifferet som forekommer i linjen. Kombiner disse to sifrene (i den rekkefølgen
 de forekommer) for å danne et tosifret tall. Dette tallet er linjens kalibreringsverdi.

Eksempel:

Gitt følgende linjer fra kalibreringsdokumentet:

    1abc2

    pqr3stu8vwx

    a1b2c3d4e5f

    treb7uchet

Kalibreringsverdiene for disse linjene er:

    12

    38

    15

    77

Summen av kalibreringsverdiene er 12 + 38 + 15 + 77 = 142.

Utfordring:

Du får nå et større kalibreringsdokument. Beregn summen av alle kalibreringsverdiene i dette dokumentet:
Generated code


hgfndfg1h
trht9823d
tg4h5gj2jk

(Hint: Svaret skal være 107)

Leveranse:

Skriv et program eller en funksjon som tar et kalibreringsdokument (som en liste av strenger) som input og returnerer summen av alle kalibreringsverdiene.
 */


import AdventOfCode2024.Oppgave_25;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Oppgave_1 {
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");

    /**
     * Behandler et kalibreringsdokument for å beregne summen av kalibreringsverdier.
     *
     * @param calibrationDocument En liste med strenger som representerer linjene i dokumentet.
     * @return Summen av alle kalibreringsverdier i dokumentet. Returnerer 0 hvis dokumentet er null eller tomt.
     * @throws IllegalArgumentException hvis en linje i dokumentet ikke inneholder minst ett siffer. Dette indikerer
     *                                   et problem med inndataene og tvinger klienten til å håndtere det.
     */
    public int calculateCalibrationSum(List<String> calibrationDocument) {
        if (calibrationDocument == null  || calibrationDocument.isEmpty()) {
            return 0; // Håndterer tom eller null input på en elegant måte. Retur av 0 er en rimelig standardverdi.
        }
        return calibrationDocument.stream()
                .mapToInt(this::extractCalibrationValue)
                .sum();
    }

    /**
     * Henter ut kalibreringsverdien fra en enkelt linje i kalibreringsdokumentet.
     *
     * @param line Linjen som skal behandles. Kan ikke være null eller tom.
     * @return Kalibreringsverdien for linjen.
     * @throws IllegalArgumentException hvis linjen er null, tom eller ikke inneholder sifre.
     */
    private int extractCalibrationValue(String line) {
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Calibration line cannot null or empty");
        }

        Matcher matcher = DIGIT_PATTERN.matcher(line);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid calibration line: " + line);
        }

        String firstDigit = matcher.group(); // Finner det første sifferet
        String lastDigit = firstDigit; // Default to the first digit if only one is present.

        while(matcher.find()) {
            lastDigit = matcher.group();
        }

        try {
            return Integer.parseInt(firstDigit + lastDigit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid calibration line: " + line);
        }
    }
    public static void main(String[] args) {
        Oppgave_1 processor = new Oppgave_1();
        List<String> document = List.of("hgfndfg1h", "trht9823d", "tg4h5gj2jk");
        int sum = processor.calculateCalibrationSum(document);
        System.out.println("Calibration sum: " + sum);

        // Example of handling null and empty documents.
        System.out.println("Null Document Sum: " + processor.calculateCalibrationSum(null));
        System.out.println("Empty Document Sum: " + processor.calculateCalibrationSum(List.of()));
    }
}
