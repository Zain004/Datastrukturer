package AdventOfCode2024;

import java.util.*;

/*
## Oppgave: LAN-party analyse

Du har infiltrert et LAN-party planlagt i påskeharens hovedkvarter. Du har lastet ned et kart over det lokale nettverket, som viser forbindelsene mellom datamaskinene. Din oppgave er å analysere dette nettverkskartet og finne ut hvor mange grupper av datamaskiner som er tilkoblet.

**Data:**

Nettverkskartet er gitt som en liste over forbindelser. Hver linje representerer en forbindelse mellom to datamaskiner, for eksempel:

```
kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn
```

**Krav:**

1.  **Finn triangler:** Identifiser alle settene med tre datamaskiner (triangler) der hver datamaskin er koblet til de to andre i settet. For eksempel, hvis datamaskin A er koblet til B, B er koblet til C, og C er koblet til A, utgjør de et gyldig sett.
2.  **Filtrer etter navn:**  Bare tell triangler som inneholder minst én datamaskin hvis navn begynner med bokstaven "t".

**Eksempel:**

Med det gitte nettverkskartet er disse noen av de gyldige settene:

```
co,de,ta
co,ka,ta
de,ka,ta
qp,td,wh
tb,vc,wq
tc,td,wh
td,wh,yn
```

**Spørsmål:**

Hvor mange setninger av tre datamaskiner (triangler) inneholder minst én datamaskin med et navn som begynner med "t"?

**Hint:**

*   Forbindelser er ikke retningsbestemte ( `kh-tc` er det samme som `tc-kh`).
*   Du må implementere en algoritme for å finne triangler i grafen som representeres av nettverkskartet.
*   Pass på å håndtere duplikater.  `A,B,C` er det samme triangelet som `C,B,A`.

**Innsending:**

Send inn et tall som representerer antall triangler som tilfredsstiller kravene.

 */
public class Oppgave_23 {

    /**
     * Teller antall trekanter i nettverksgrafen som inneholder minst ett datamaskinnavn som begynner med 't'.
     *
     * @param connections En liste med strenger som representerer nettverkstilkoblinger, f.eks. "computerA-computerB".
     * @return Antall trekanter som inneholder en maskin med 't'.
     */
    public static int countTrianglesWithT(List<String> connections) {
        /*Nøkkelen (String) er navnet på en datamaskin (f.eks. "computerA").
        Verdien (Set<String>) er en samling (mengde) av maskiner som den er direkte koblet til.*/
        // 1. Bygg en representasjon av grafen med tilstøtende liste.
        Map<String, Set<String>> adjacencyList = buildAdjacencyList(connections);
        // 2. Finn alle trekanter og filtrer de som inneholder en 't'-maskin.
        Set<Set<String>> triangles = findTriangles(adjacencyList);
        // 3. Tell trekanter med en 't'-maskin.
        int count = 0;
        for (Set<String> triangle : triangles) {
            if (triangle.stream().anyMatch(name -> name.startsWith("t"))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Bygger en tilstøtende listerepresentasjon av nettverksgrafen fra en liste over tilkoblinger.
     *
     * @param connections En liste over strenger som representerer nettverkstilkoblingene.
     * @return En oversikt der nøklene er datamaskinnavn og verdiene er sett med tilkoblede datamaskinnavn.
     */
    private static Map<String, Set<String>> buildAdjacencyList(List<String> connections) {
        Map<String, Set<String>> adjacencyList = new HashMap<>();

        for (String connection : connections) {
            String[] computers = connection.split("-");
            String computerA = computers[0];
            String computerB = computers[1];

            adjacencyList.computeIfAbsent(computerA, k -> new HashSet<>()).add(computerB);
            adjacencyList.computeIfAbsent(computerB, k -> new HashSet<>()).add(computerA);
        }
        return adjacencyList;
    }

    /**
     * Finner alle trekanter i nettverksgrafen representert av adjacencylisten.
     *
     * @param adjacencyList Et kart som representerer adjacencylisten til grafen.
     * @return Et sett med sett, der hvert indre sett representerer en trekant (et sett med tre tilkoblede datamaskinnavn).
     */
    private static Set<Set<String>> findTriangles(Map<String, Set<String>> adjacencyList) {
        Set<Set<String>> triangles = new HashSet<>();

        for (String computerA : adjacencyList.keySet()) {
            for (String computerB : adjacencyList.get(computerA)) {
                if (computerA.compareTo(computerB) >= 0) continue; // Unngå dupliserte kanter (A-B er det samme som B-A)

                for (String computerC : adjacencyList.get(computerB)) {
                    if (computerB.compareTo(computerC) >= 0) continue; // Unngå dupliserte kanter

                    // Sjekk om A er koblet til C for å danne en trekant.
                    if (adjacencyList.get(computerA).contains(computerC)) {
                        // Sorter trekanten for å unngå dupliserte trekanter med ulik rekkefølge. Dette er nøkkelen til korrekthet.
                        List<String> sortedTriangle = Arrays.asList(computerA, computerB, computerC);
                        Collections.sort(sortedTriangle);
                        triangles.add(new HashSet<>(sortedTriangle)); // Bruk av Set for å automatisk fjerne potensielle duplikater
                    }
                }
            }
        }
        return triangles;
    }

    public static void main(String[] args) {
        // Test data (replace with your actual input)
        List<String> connections = Arrays.asList(
                "kh-tc", "qp-kh", "de-cg", "ka-co", "yn-aq", "qp-ub", "cg-tb", "vc-aq", "tb-ka", "wh-tc",
                "yn-cg", "kh-ub", "ta-co", "de-co", "tc-td", "tb-wq", "wh-td", "ta-ka", "td-qp", "aq-cg",
                "wq-ub", "ub-vc", "de-ta", "wq-aq", "wq-vc", "wh-yn", "ka-de", "kh-ta", "co-tc", "wh-qp",
                "tb-vc", "td-yn"
        );

        int count = countTrianglesWithT(connections);
        System.out.println("Number of triangles containing a machine name starting with 't': " + count);
    }
}
