package AdventOfCode2024;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
Oppgave: Junglebro-kalibrering

Ingeniørene trenger din hjelp til å kalibrere en hengebro i jungelen.
Elefanter har stjålet operatorene fra kalibreringsligningene, og du må
finne ut hvilke ligninger som kan være sanne ved å sette inn + (pluss) eller * (gange) mellom tallene.

Hvordan det fungerer:

    Hver linje i inputen representerer en ligning. Formatet er testverdi: tall1 tall2 tall3 ....

    Du må bestemme om det er mulig å kombinere tallene på høyre side av kolon ved hjelp av kun + og * operatorer slik at resultatet er lik testverdien på venstre side.

    Operatorer evalueres alltid fra venstre mot høyre, uten hensyn til prioritering.

    Rekkefølgen på tallene kan ikke endres.

Eksempel:
    190: 10 19
    3267: 81 40 27
    83: 17 5
    156: 15 6
    7290: 6 8 6 15
    161011: 16 10 13
    192: 17 8 14
    21037: 9 7 18 13
    292: 11 6 16 20

I dette eksempelet er følgende ligninger gyldige:

    190: 10 19 (fordi 10 * 19 = 190)

    3267: 81 40 27 (fordi 81 + 40 * 27 = 3267 og 81 * 40 + 27 = 3267)

    292: 11 6 16 20 (fordi 11 + 6 * 16 + 20 = 292)

Oppgave:

Gitt en liste med ligninger, finn alle ligningene som kan gjøres sanne.
Beregn summen av testverdiene for disse ligningene. Dette er det totale kalibreringsresultatet
 */
public class Oppgave_7 {

    /**
     * Beregner det totale kalibreringsresultatet basert på en liste med ligninger.
     *
     * @param equations En liste med ligninger i formatet "testverdi: tall1 tall2 ...".
     *                  <testverdi>: <tall1> <tall2> <tall3> ...
     * @return Det totale kalibreringsresultatet (summen av testverdiene for gyldige ligninger).
     */
    public static long calculateTotalCalibrationResult(List<String> equations) {
        return equations.stream()
                .filter(Oppgave_7::isEquationValid)
                .mapToLong(equation -> Long.parseLong(equation.split(":")[0].trim()))
                .sum();
    }

    /**
     * Sjekker om en gitt ligning er gyldig ved å prøve alle mulige kombinasjoner av operatorer.
     *
     * @param equation Ligningen i formatet "testverdi: tall1 tall2 ...".
     * @return `true` hvis ligningen er gyldig, `false` ellers.
     */
    private static boolean isEquationValid(String equation) {
        String[] parts = equation.split(":"); // Del opp input-strengen i to deler, skilt av kolon ':'
        // Sjekk at vi faktisk fikk to deler etter splitting
        // Hvis ikke, er formatet feil og metoden returnerer false
        if (parts.length != 2) {
            System.err.println("Ugyldig ligningsformat: " + equation);
            return false;
        }
        // Den første delen forventes å være målverdien vi skal sammenligne med
        // Fjern eventuelle mellomrom og konverter til long
        long targetValue = Long.parseLong(parts[0].trim());
        // Den andre delen forventes å være en kommaseparert liste av tall
        // Vi splitter på komma, konverterer hver streng til long, og samler dem i en liste
        List<Long> numbers = Arrays.stream(parts[1].split(","))
                .map(Long::parseLong)// Konverter hvert element til long
                .collect(Collectors.toList()); // Samle i en liste
        // Kall en hjelpefunksjon isValid med målverdien og listen av tall
        // isValid returnerer true eller false basert på om ligningen er gyldig
        return isValid(targetValue, numbers);
    }

    public static void main(String[] args) {

    }
}

















