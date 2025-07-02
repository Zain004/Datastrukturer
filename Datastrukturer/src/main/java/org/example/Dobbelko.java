package org.example;

import com.sun.jdi.PrimitiveValue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class Dobbelko<T> {

    /**
         * En dobbel kø (Deque) implementasjon som tilbyr effektiv tilgang
         * og modifikasjon fra begge ender. Benytter to interne Deque-er
         * for å optimere ytelsen av leggTilForan/leggTilBak operasjoner.
         *
         * @param <T> Den type elementer som køen skal inneholde.
     */

    private final Deque<T> forreste; // Elementer lagt til 'foran'
    private final Deque<T> bakre; // Elementer lagt til 'bak'

    // Konstanter for å justere balansen mellom 'forreste' og 'bakre'
    private static final int FORAN_THRESHOLD  = 10;
    private static final int BAK_THRESHOLD    = 10;

    /**
     * Konstruktør som initialiserer den doble køen.
    */
    public Dobbelko() {
        forreste = new ArrayDeque<T>();
        bakre = new ArrayDeque<>();
    }

    /**
     * Sjekker om køen er tom.
     *
     * @return `true` hvis køen er tom, `false` ellers.
     */
    public boolean erTom() {
        return forreste.isEmpty() && bakre.isEmpty();
    }

    /**
     * Rebalanserer de interne køene for å unngå at en blir for stor
     * i forhold til den andre. Dette forbedrer den generelle ytelsen
     * ved hyppige leggTilForan/leggTilBak operasjoner.
     */
    private void rebalanser() {
        if(forreste.size() > BAK_THRESHOLD + bakre.size()) {
            overførFraForanTilBak();
        } else if (bakre.size() > FORAN_THRESHOLD + forreste.size()) {
            overførFraBakTilForan();
        }
    }

    /**
     * Hjelpemetode for å overføre elementer fra 'forreste' til 'bakre'
     * slik at fordelingen av elementer er mer balansert.
    */
    private void overførFraForanTilBak() {
        int antallFlyttes = forreste.size() - bakre.size(); // Antallet elementer å flytte. Kan være negativt, vi flytter uansett bare ett.
        antallFlyttes = Math.max(1, antallFlyttes/2); // flytter minst ett element, og omtrent halvparten av differansen
        for (int i = 0; i < antallFlyttes; i++) {
            bakre.addFirst(forreste.removeLast()); // Flytter fra slutten av forreste til starten av bakre
        }
    }

    /**
     * Hjelpemetode for å overføre elementer fra 'bakre' til 'forreste'
     * slik at fordelingen av elementer er mer balansert.
    */
    private void overførFraBakTilForan() {
        int antalllyFlyttes = bakre.size() - forreste.size();
        antalllyFlyttes = Math.max(1, antalllyFlyttes/2);
        for (int i = 0; i < antalllyFlyttes; i++) {
            forreste.addLast(bakre.removeFirst());
        }
    }

    /**
         * Legger til et element foran i køen.
         * Null-verdier tillates ikke og vil kaste en {@link NullPointerException}
         *
         * @param element Elementet som skal legges til.
         * @throws NullPointerException hvis elementet er null.
     */
    public void leggTilForan(T element) {
        Objects.requireNonNull(element, "Elementet kan ikke være null.");
        forreste.addFirst(element);
        rebalanser();
    }

    /**
         * Legger til et element bak i køen.
         * Null-verdier tillates ikke og vil kaste en {@link NullPointerException}
         *
         * @param element Elementet som skal legges til.
         * @throws NullPointerException hvis elementet er null.
     */
    public void leggTilBak(T element) {
        Objects.requireNonNull(element, "Elementet kan ikke være null.");
        bakre.addLast(element);
        rebalanser();
    }

    /**
         * Fjerner og returnerer elementet foran i køen.
         *
         * @return Elementet foran i køen.
         * @throws NoSuchElementException hvis køen er tom.
     */
    public T fjernForan() {
        if (erTom()) {
            throw new NoSuchElementException("Køen er tom.");
        }
        T element = null;

        if (!forreste.isEmpty()) {
            element = forreste.removeFirst();
        } else {
            element = bakre.removeFirst();
        }
        rebalanser();
        return element;
    }

    /**
         * Fjerner og returnerer elementet bak i køen.
         * @return Elementet bak i køen.
         * @throws NoSuchElementException hvis køen er tom.
    */
    public T fjernBak() {
        if (erTom()) {
            throw new NoSuchElementException("Køen er tom.");
        }
        T element = null;
        if (!bakre.isEmpty()) {
            element = bakre.removeFirst();
        } else {
            element = forreste.removeLast();
        }
        rebalanser(); // Vurder rebalansering etter operasjonen
        return element;
    }

    /**
     * Returnerer elementet foran i køen uten å fjerne det.
     * @return Elementet foran i køen.
     * @throws NoSuchElementException hvis køen er tom.
    */
    public T seForan() {
        if (erTom()) {
            throw new NoSuchElementException("Køen er tom.");
        }
        if (!forreste.isEmpty()) {
            return forreste.peekFirst();
        } else {
            return bakre.peekFirst();
        }
    }

    /**
     * Returnerer elementet bak i køen uten å fjerne det.
     *
     * @return Elementet bak i køen.
     * @throws NoSuchElementException hvis køen er tom.
     */
    public T seBak() {
        if (erTom()) {
            throw new NoSuchElementException("Køen er tom.");
        }
        if (!bakre.isEmpty()) {
            return bakre.peekLast();
        } else {
            return forreste.peekLast();
        }
    }

    /**
     * Returnerer antall elementer i køen.
     *
     * @return Antall elementer i køen.
     */
    public int storrelse() {
        return forreste.size() + bakre.size();
    }

    /**
     * Tømmer køen.
     */
    public void tøm() {
        forreste.clear();
        bakre.clear();
    }

    /**
     * Validerer invariantene til denne datastrukturen.  Brukes for testing
     * og feilsøking for å sikre at interne tilstander er konsistente.
     *
     * @return true hvis invariantene holder, ellers false.
     */
    protected boolean validerInvarianter() {
        // Invariant: Summen av størrelsen på forreste og bakre skal være lik storrelse().
        if (forreste.size() + bakre.size() != storrelse()) {
            System.err.println("Invariant brutt: Summen av kø størrelser er feil.");
            return false;
        }
        // Invariant: Ingen av køene skal inneholde null-verdier.  Dette er allerede garantert av leggTilForan og leggTilBak.
        // (Men vi kan legge til en sjekk her hvis vi vil være ekstra forsiktige).
        return true;
    }
    @Override
    public String toString() {
        String forresteString = "Foran: [" + forreste.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
        String bakreString = "Bak: [" + bakre.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";

        return "DobbelKo{" + forresteString + ", " +
                bakreString + "}";
    }
    public static void main(String[] args) {
        Dobbelko<Integer> ko = new Dobbelko<>();

        ko.leggTilForan(1);
        ko.leggTilBak(2);
        ko.leggTilForan(3);
        ko.leggTilBak(4);


        System.out.println("Størrelse: " + ko.storrelse());
        System.out.println("Foran: " + ko.seForan());
        System.out.println("Bak: " + ko.seBak());

        System.out.println(ko.toString());

        // Testing av rebalansering
        for (int i = 0; i < 25; i++) {
            ko.leggTilForan(i);
        }

        System.out.println(ko.toString());

        if (!ko.validerInvarianter()) {
            System.err.println("Invarianter brutt etter rebalansering!");
        } else {
            System.out.println("Invarianter validert etter rebalansering.");
        }
        // Testing av at null-verdier ikke kan legges til
        try {
            ko.leggTilForan(null);
        } catch (NullPointerException e) {
            System.out.println("Fanget forventet NullPointerException: " + e.getMessage());
        }
    }
}
