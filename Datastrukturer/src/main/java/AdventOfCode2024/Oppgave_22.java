package AdventOfCode2024;
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

    public static void main(String[] args) {

    }
}
