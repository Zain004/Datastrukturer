package AdventOfCode2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
## Oppgave: Apemarkedets Hemmeligheter

Du har blitt sendt til et apemarked for å kjøpe tilbake en stjålet enhet. Apene vil kun akseptere bananer i bytte, og du må tjene bananer ved å selge gode gjemmesteder på markedet. Problemet er at prisene varierer tilfeldig, men faktisk er de **pseudotilfeldige** og basert på en sekvens av "hemmelige tall" generert av hver ape-kjøper. Din oppgave er å simulere disse sekvensene for å forutsi prisene.

**Hvordan hemmelige tall genereres:**

Hver ape-kjøper har et initialt "hemmelig tall". For å generere det neste hemmelige tallet i sekvensen, gjøres følgende operasjoner:

1.  **Multiplikasjon, XOR og Modulo:**
    *   Multipliser det nåværende hemmelige tallet med 64.
    *   "Bland" resultatet inn i det hemmelige tallet (ved hjelp av bitvis XOR).
    *   "Beskjær" det hemmelige tallet (ved å ta modulo 16777216).

2.  **Divisjon, XOR og Modulo:**
    *   Del det nåværende hemmelige tallet med 32 og rund ned til nærmeste heltall.
    *   "Bland" resultatet inn i det hemmelige tallet (ved hjelp av bitvis XOR).
    *   "Beskjær" det hemmelige tallet (ved å ta modulo 16777216).

3.  **Multiplikasjon, XOR og Modulo:**
    *   Multipliser det nåværende hemmelige tallet med 2048.
    *   "Bland" resultatet inn i det hemmelige tallet (ved hjelp av bitvis XOR).
    *   "Beskjær" det hemmelige tallet (ved å ta modulo 16777216).

**Blanding og Beskjæring forklart:**

*   **Blanding (XOR):**  `nytt_hemmelig_tall = gammelt_hemmelig_tall ^ verdi_å_blande_inn`
    *   *Eksempel:* Hvis `gammelt_hemmelig_tall = 42` og `verdi_å_blande_inn = 15`, så blir `nytt_hemmelig_tall = 42 ^ 15 = 37`.
*   **Beskjæring (Modulo):**  `nytt_hemmelig_tall = gammelt_hemmelig_tall % 16777216`
    *   *Eksempel:* Hvis `gammelt_hemmelig_tall = 100000000`, så blir `nytt_hemmelig_tall = 100000000 % 16777216 = 16113920`.

**Oppgaven:**

Du får en liste over initiale hemmelige tall, ett for hver ape-kjøper. Hver kjøper genererer 2000 nye hemmelige tall basert på prosessen beskrevet ovenfor.

Din oppgave er å simulere genereringen av disse hemmelige tallene for hver kjøper, og deretter finne **summen av det 2000. nye hemmelige tallet generert av hver kjøper**.

**Eksempel:**

Anta du har følgende initiale hemmelige tall for fire kjøpere:

*   Kjøper 1: 1
*   Kjøper 2: 10
*   Kjøper 3: 100
*   Kjøper 4: 2024

Etter å ha simulert genereringen av 2000 nye hemmelige tall for hver kjøper, finner du følgende 2000. tall:

*   Kjøper 1: 8685429
*   Kjøper 2: 4700978
*   Kjøper 3: 15273692
*   Kjøper 4: 8667524

Summen av disse tallene er: `8685429 + 4700978 + 15273692 + 8667524 = 37327623`.

**Input:**

En liste over initialiserte hemmelige tall (ditt gåteinnspill).

**Output:**

Summen av det 2000. hemmelige tallet generert av hver kjøper.

**Tips:**

*   Sørg for å bruke 32-bits heltall for å unngå overflow-problemer.
*   Test koden din med mindre antall iterasjoner (f.eks. 10) for å forsikre deg om at algoritmen fungerer korrekt.

Lykke til med å forutse apemarkedet!

 */
public class Oppgave_22 {
    private static final int MODULO = 16777216;
    private static final int ITERATIONS = 2000;

    public static long calculateSumOf2000thSecrets(List<Long> initialSecrets) {
        long totalSum = 0;
        for (long initialSecret : initialSecrets) {
            long finalSecret = generateSecretSequence(initialSecret, ITERATIONS);
            totalSum += finalSecret;
        }
        return totalSum;
    }

    /**
     * Genererer en sekvens av hemmelige tall basert på den gitte algoritmen.
     *
     * @param initialSecret Det initiale hemmelige tallet.
     * @param iterations Antall iterasjoner for å generere nye hemmelige tall.
     * @return Det hemmelige tallet etter å ha kjørt alle iterasjonene.
     */
    public static long generateSecretSequence(long initialSecret, int iterations) {
        long currentSecret = initialSecret;
        for (int i = 0; i < iterations; i++) {
            currentSecret = generateNextSecret(currentSecret);
        }
        return currentSecret;
    }

    /**
     * Genererer det neste hemmelige tallet i sekvensen basert på det nåværende hemmelige tallet.
     *
     * @param currentSecret Det nåværende hemmelige tallet.
     * @return Det neste hemmelige tallet i sekvensen.
     */
    private static long generateNextSecret(long currentSecret) {
        currentSecret = phase1(currentSecret);
        currentSecret = phase2(currentSecret);
        currentSecret = phase3(currentSecret);
        return currentSecret;
    }

    private static long phase1(long secret) {
        long multiplied = secret * 64;
        secret ^= multiplied;
        secret %= MODULO;
        return secret;
    }

    private static long phase2(long secret) {
        long divided = secret / 32;
        secret ^= divided;
        secret %= MODULO;
        return secret;
    }

    private static long phase3(long secret) {
        long multiplied = secret * 2048;
        secret ^= multiplied;
        secret %= MODULO;
        return secret;
    }

    public static void main(String[] args) {
        List<Long> initialSecrets = new ArrayList<>();
        initialSecrets.add(1L);
        initialSecrets.add(10L);
        initialSecrets.add(100L);
        initialSecrets.add(2024L);

        long sumOf2000thSecrets = calculateSumOf2000thSecrets(initialSecrets);
        System.out.println("Summen av det 2000. hemmelige tallet: " + sumOf2000thSecrets);
    }
}
