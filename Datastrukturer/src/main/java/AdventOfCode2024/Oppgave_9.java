package AdventOfCode2024;

import java.util.List;

/*
Du får et diskkart representert som en streng av tall, f.eks. "12345". Tallene angir vekselvis lengden på en fil og lengden på ledig plass. Flytt filblokker fra slutten av disken til ledig plass lengst til venstre. Etter flyttingen, beregn en sjekksum ved å summere (posisjon * filID) for hver filblokk (start fra posisjon 0, filID starter på 0). Ignorer ledig plass.

Eksempel:

Input: "12345"

Disk layout initialt: 0..111....22222

Etter flytting: 022111222......

Sjekksum: (0 * 0) + (1 * 0) + (2 * 2) + (3 * 2) + (4 * 1) + (5 * 1) + (6 * 1) + (7 * 2) + (8 * 2) + (9 * 2) = 0 + 0 + 4 + 6 + 4 + 5 + 6 + 14 + 16 + 18 = 73

Beregn sjekksummen for det komprimerte diskkartet.

 */
public class Oppgave_9 {

    /**
     * Representerer en blokk på disken: enten en fil eller ledig plass.
     */
    public enum BlokkType {
        FIL,
        LEDIG
    }

    /**
     * Komprimerer en harddisk representert av en sekvens av fil- og ledig plasslengder,
     * og beregner sjekksummen av det komprimerte filsystemet.
     *
     * @param diskMap En streng der tallene vekselvis representerer lengden på en fil og ledig plass.
     * @return Sjekksummen av det komprimerte filsystemet.
     * @throws IllegalArgumentException Hvis diskMap inneholder tegn som ikke er tall.
     */

    public long komprimerOgBeregnSjekkSum(String diskmap) {
        List<Integer> lengder = parseDiskMap(diskmap);
        List<Blokk> diskBlokker = initialiserDiskBlokker(lengder);
        komprimerDisk(diskBlokker);
        return beregnSjekkSum(diskBlokker);
    }

    private List<Integer> parseDiskMap(String diskmap) {
        
    }
}
