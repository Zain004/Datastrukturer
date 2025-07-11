package AdventOfCode2024;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

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
     * @param diskmap En streng der tallene vekselvis representerer lengden på en fil og ledig plass.
     * @return Sjekksummen av det komprimerte filsystemet.
     * @throws IllegalArgumentException Hvis diskMap inneholder tegn som ikke er tall.
     * Stegvis gjør den dette:
     *     Kaller parseDiskMap() for å gjøre "2333133..." om til [2,3,3,3,1,3,...]
     *     Kaller initialiserDiskBlokker() for å bygge diskens blokker
     *     Kaller komprimerDisk() for å defragmentere
     *     Kaller beregnSjekksum() og returnerer resultatet
     */

    public long komprimerOgBeregnSjekkSum(String diskmap) {
        List<Integer> lengder = parseDiskMap(diskmap);
        List<Blokk> diskBlokker = initialiserDiskBlokker(lengder);
        komprimerDisk(diskBlokker);
        return beregnSjekkSum(diskBlokker);
    }
    /**
     * Parser en streng som representerer et diskkart til en liste av heltall.
     * <p>
     * Strengen skal bestå av kun sifre (0–9), hvor hvert siffer representerer
     * lengden på enten en fil eller en ledig plass på disken, i vekselsvis rekkefølge
     * (fil, ledig, fil, ledig, osv.).
     * </p>
     *
     * @param diskmap Strengen som representerer diskkartet.
     *                Eksempel: "3124" betyr fil med lengde 3, ledig plass 1, fil 2, ledig plass 4.
     * @return En liste med heltall som representerer lengdene av fil- og ledig plassblokker.
     * @throws IllegalArgumentException hvis strengen inneholder andre tegn enn sifre (0–9).
     */
    private List<Integer> parseDiskMap(String diskmap) {
        List<Integer> lengder = new ArrayList<>();
        for (char c : diskmap.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Ugyldig diskkartformat: " + diskmap);
            }
            lengder.add(Character.getNumericValue(c));
        }
        return lengder;
    }

    /**
     * Representerer en blokk på disken (enten en fil eller ledig plass).
     */
    public static class Blokk {
        private final BlokkType type;
        private final int filId;

        public Blokk(BlokkType type, int filId) {
            this.type = type;
            this.filId = filId;
        }

        public BlokkType getType() {
            return type;
        }
        public int getFilId() {
            return filId;
        }
    }

    /**
     * Initialiserer en liste over `Blokk`-objekter som representerer de enkelte blokkene på disken,
     * basert på fil- og ledig plasslengder.
     *
     * @param lengder En liste over heltall som representerer fil- og ledig plasslengder.
     * @return En liste over `Blokk`-objekter som representerer diskblokkene.
     * <p>
         * Tenk på en parkeringsplass hvor tallene i lengder representerer antall parkerte
         * biler og antall ledige plasser etter hverandre. Metoden initialiserDiskBlokker
         * tilsvarer å lage en liste over hver enkelt plass på parkeringsplassen,
         * og for hver plass notere om den er opptatt av en bil eller ledig. Hvis opptatt,
         * lagrer man kanskje bilens registreringsnummer.
     * </p>
     */
    private List<Blokk> initialiserDiskBlokker(List<Integer> lengder) {
        List<Blokk> diskBlokker = new ArrayList<>();
        int filId = 0;
        boolean erFil = true; // Veksler mellom fil og ledig plass

        for (int lengde : lengder) { // itererer over hvert tall og lager annenhver
            // ledig og ikke ledig med variabelen ledig
            for (int i = 0; i < lengde; i++) {
                diskBlokker.add(erFil ? new Blokk(BlokkType.FIL, filId) : new Blokk(BlokkType.LEDIG, -1)); // -1 indikerer ingen filId
            }
            if (erFil) {
                filId++;
            }
            erFil = !erFil; // Bytt mellom fil og ledig plass
        }
        return diskBlokker;
    }
    /**
     * Komprimerer disken ved å flytte filblokker fra slutten til ledig plass lengst til venstre.
     *
     * @param diskBlokker Listen over `Blokk`-objekter som representerer diskblokkene. Denne listen endres direkte.
     */
    private void komprimerDisk(List<Blokk> diskBlokker) {
        int ledigPosisjon = 0;
        int diskEnde = diskBlokker.size() - 1;

        // Finn den første ledige plassen.
        while (ledigPosisjon < diskBlokker.size() && diskBlokker.get(ledigPosisjon).getType() != BlokkType.LEDIG) {
            ledigPosisjon++;
        }

        // Flytt filblokker fra slutten av disken til ledige posisjoner
        while (diskEnde > ledigPosisjon) {
            if (diskBlokker.get(diskEnde).getType() != BlokkType.LEDIG) {
                diskBlokker.set(ledigPosisjon, diskBlokker.get(diskEnde));
                diskBlokker.set(diskEnde, new Blokk(BlokkType.LEDIG, -1));
                ledigPosisjon++;
                    while (ledigPosisjon < diskBlokker.size() && diskBlokker.get(ledigPosisjon).getType() != BlokkType.LEDIG) {
                        ledigPosisjon++;
                    }
                }
                diskEnde--;
            }
        }

    /**
     * Beregner sjekksummen av det komprimerte filsystemet.
     *
     * @param diskBlokker Listen over `Blokk`-objekter som representerer diskblokkene.
     * @return Sjekksummen av filsystemet.
     */
    private long beregnSjekkSum(List<Blokk> diskBlokker) {
        long sjekksum = 0;
        for (int i = 0; i < diskBlokker.size(); i++) {
            Blokk blokk = diskBlokker.get(i);
            if (blokk.getType() == Oppgave_9.BlokkType.FIL) {
                sjekksum += (long) i * blokk.getFilId();
            }
        }
        return sjekksum;
    }

    public static void main(String[] args) {
        Oppgave_9 diskFragmenter = new Oppgave_9();
        String diskMap = "2333133121414131402"; // Eksempel diskkart
        long sjekksum = diskFragmenter.komprimerOgBeregnSjekkSum(diskMap);
        System.out.println("Sjekksummen er: " + sjekksum);
    }
}

