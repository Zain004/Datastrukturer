package org.example;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BinaryOperator;

public class TurneringsTree<T> {

    private static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;
        Node<T> parent;

        Node(T value) {
            this.value = value;
        }
    }

    private final Comparator<T> comparator; // Comparator for å sammenligne elementer.
    private final T dummyParticipant; // Dummy-deltaker for å fylle treet.
    private final BinaryOperator<T> winnerFunction; // Funksjon for å bestemme vinneren.
    private int size; // Antall elementer i turneringen.
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); // Lås for å kontrollere trådsikkerhet.
    private final Lock readLock = lock.readLock(); // Lås for lesetilgang.
    private final Lock writeLock = lock.writeLock(); // Lås for skrivetilgang.
    private Node<T> root; // Rotnoden i turneringstreet.
    private Node<T>[] leaves; // Array av bladnodene i treet.
    private volatile boolean constructionComplete = false; // Flag for å indikere at trekonstruksjonen er fullført.

/**
 * Konstruerer et Turneringstre fra en array av elementer med en gitt Comparator.
 * @param elements   Arrayet av elementer som skal delta i turneringen. Kan ikke være null.
 * @param comparator Comparator for å sammenligne elementene. Hvis null, brukes naturlig
 *                   rekkefølge.
 * @param dummyParticipant En "dummy"-deltaker som fyller treet hvis antall deltakere ikke er
 * en potens av 2. Bør "tape" mot alle andre elementer. Kan være null
 * hvis antall deltakere alltid vil være en potens av 2 og hvis`elements` ikke inneholder null-elementer.
 * @throws NullPointerException hvis elementer er null og ingen comparator er gitt.
 */

    public TurneringsTree(T[] elements, Comparator<T> comparator, T dummyParticipant) {
        Objects.requireNonNull(elements, "Elementer kan ikke være null");
        this.size = elements.length;
        this.comparator =
                (comparator != null) ? comparator : ((o1, o2) -> ((Comparable<T>) o1).compareTo(o2));

        if (Arrays.stream(elements).anyMatch(Objects::isNull) && dummyParticipant == null) {
            throw new IllegalArgumentException(
                    "Elementer inneholder null-elementer, men ingen dummyParticipant er definert.");
        }

        this.dummyParticipant = dummyParticipant;

        this.winnerFunction =
                (a, b) -> (a == null) ? b : ((b == null) ? a : (this.comparator.compare(a, b) >= 0 ? a : b));


        buildTree(elements);
        constructionComplete = true; // Sett til true etter at treet er bygget
    }

    public TurneringsTree(T[] elements) {
        this(elements, null, null);
    }

    private void buildTree(T[] elements) {
        writeLock.lock();
        try {
            // regner først om det trengs noen dummy-deltakere som skal tape
            int numberOfLeaves = calculateNextPowerOfTwo(size);

            //  Lager en array som skal holde på alle bladnodene i treet (nederste nivå).
            leaves = new Node[numberOfLeaves];
            for (int i = 0; i < size; i++) {
                leaves[i] = new Node<>(elements[i]);
            }
            // Fyll ut resten av bladnodene med dummy-deltakere
            for (int i = size; i < numberOfLeaves; i++) {
                leaves[i] = new Node<>(dummyParticipant);
            }

            // Bygg treet fra bunnen opp
            Node<T>[] currentLevel = leaves; // starter først med nederste nivået

            // Fortsetter så lenge vi ikke er kommet opp til roten
            while (currentLevel.length > 1) {
                // neste nivå skal inneholde halvparten så mange noder
                Node<T>[] nextLevel = new Node[(currentLevel.length + 1) / 2];
                // går gjennom nodene to og to
                for (int i = 0; i < currentLevel.length; i += 2) {
                    Node<T> left = currentLevel[i]; //  hent venstre noden på indeksen vi jobber over
                    Node<T> right = (i + 1 < currentLevel.length) ? currentLevel[i + 1] : null; // hent høyre noden til indeksen vi  jobber over

                    Node<T> parentNode = new Node<>(null); // Verdi beregnes nedenfor

                    parentNode.left = left;
                    left.parent = parentNode; //Sett parent
                    if (right != null) {
                        parentNode.right = right;
                        right.parent = parentNode; //Sett parent
                    }

                    parentNode.value = winnerFunction.apply(left.value, (right != null ? right.value : null)); //Bruk winnerFunction istedenfor å sammenligne nodene direkte
                    nextLevel[i / 2] = parentNode; // Legger foreldre-noden inn i neste nivå.
                }
                currentLevel = nextLevel;
            }

            root = currentLevel[0]; // Den siste noden er roten
        } finally {
            writeLock.unlock();
        }
    }

    /**
     Denne sjekker om treet har 2 barn hver, fordi turneringstreet er enklest å
     implementere og balansere når antall blader er en potens av 2.
     Hvis antall elementer ikke er en potens av 2, fylles treet med dummy-deltakere for å oppnå dette.
     * @param n
     * @return powerOfTwo
     */
    private int calculateNextPowerOfTwo(int n) {
        int powerOfTwo = 1;
        while (powerOfTwo < n) {
            powerOfTwo *= 2;
        }
        return powerOfTwo;
    }

    /**
     * Henter vinneren av turneringen. Ytelsesoptimalisert ved å unngå readLock etter konstruksjon.
     *
     * @return Vinneren av turneringen (rotnoden), eller null hvis treet er tomt.
     */
    public T getWinner() {
        /*
         Hvis konstruksjonen av treet ikke er ferdig, så må vi være forsiktige
         før vi leser data. Det er nødvendig å bruke readLock for å sikre
         trådsikkerhet – altså at ingen leser treet mens en annen tråd fortsatt bygger det.
         Det sikrer at vi ikke leser vinneren mens treet
         fortsatt bygges, som kunne gitt feil eller ustabilt resultat.
         */
        if (!constructionComplete) {
            // sikre korrektlesing før konstruksjon er ferdig
            readLock.lock();
            try {
                return (root != null) ? root.value : null;
            } finally {
                readLock.unlock();
            }
        }
        // Når konstruksjonen er ferdig, trenger vi ikke lenger bruke lås –
        // det er trygt å lese rett fra roten uten å blokkere.
        return (root != null) ? root.value : null;
    }

    /**
     * Endrer verdien til en deltaker (bladnode) og oppdaterer verdien langs treet oopover,
     * ved hjelp av en egen funksjon.
     * @param index   Indeksen til deltakeren (i den opprinnelige element-arrayen).
     * @param newValue Den nye verdien til deltakeren.
     * @throws IndexOutOfBoundsException hvis indeksen er ugyldig.
     */
    public void updateParticipant(int index, T newValue) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Ugyldig indeks: " + index);
        }
        writeLock.lock();
        try {
            Node<T> leafNode = leaves[index];
            leafNode.value = newValue; // oppdaterer verdien i bladet
            updateUpwards(leafNode);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Oppdaterer verdiene i foreldrenodene oppover i treet,
     * fra den gitte noden og helt opp til roten.
     * Metoden bruker winnerFunction til å beregne vinneren mellom venstre og høyre barn.
     * Merk: Den jobber **fra bladet og opp mot roten**, ikke fra roten og nedover.
     */
    private void updateUpwards(Node<T> node) {
        Node<T> current = node;
        while (current.parent != null) {
            Node<T> parent = current.parent;
            T leftValue = (parent.left != null) ? parent.left.value : null;
            T rightValue = (parent.right != null) ? parent.right.value : null;
            // beregner den nye wineneren etter endring av bladnone
            parent.value = winnerFunction.apply(leftValue, rightValue); // bruker funksjonen jeg lagde
            current = parent;
        }
    }

    /**
     * Setter inn en ny verdi i turneringstreet og konkurrerer den oppover. Denne metoden
     * legger til den nye verdien på slutten av eksisterende deltakere.
     * @param newValue Den nye verdien som skal settes inn.
     */
    public void insertParticipant(T newValue) {
        Objects.requireNonNull(newValue, "Ikke tillat med null verdier.");
        writeLock.lock();
        try {
            // 1. Oppdater størrelsen, en ekstra deltaker i turneringen
            size++;
            // 2. Oppretter en ny bladnode som representerer deltakeren
            Node<T> newLeaf = new Node<>(newValue);
            // 3. Utvid blad-array (om nødvendig)
            int numberOfLEaves = calculateNextPowerOfTwo(size);
            if (numberOfLEaves > leaves.length) {
                // Må utvide arrayet
                leaves = Arrays.copyOf(leaves, numberOfLEaves);
                // Fyll ut ekstra blad med dummyParticipants.
                for (int i = size; i < numberOfLEaves; i++) {
                    if (i >= size) {
                        leaves[i] = new Node<>(dummyParticipant);
                    }
                }
            }
            // 4. Sett inn ny bladnode
            leaves[size - 1] = newLeaf;
            // 5. Koble til treet
            if (root == null) {
                // Spesialtilfelle: Treet er tomt
                root = newLeaf;
                return;
            }
            // Finn riktig plassering for den nye noden
            Node<T> current = root;
            int level = 0;
        /*  Vi traverserer nedover i treet for å finne en plass til den nye noden.
              Eksempel: Når "Anna" blir med i turneringen, må vi finne ut hvem hun
              skal møte først, og hvor i turneringstreet hennes kamp skal plasseres.  */
            while (current.left != null || current.right != null) {
                /*Vi bruker modulus-sjekken size % (2 << level) == 0 som en pekepinn
                på at vi har fylt venstre greiner opp til et visst nivå, og at det
                nå er klart for å legge til høyre gren. */
                if (size % (2 << level) == 0) { // 2 << level er det samme som 2^(level + 1)
                    // Ny node skal være høyre barn
                    if (current.right == null) {
                        break; //Exit hvis vi finner en node som ikke har to barn ennå
                    }
                    current = current.right;
                } else {
                    // Ny node skal være venstre barn
                    if(current.left == null){
                        break; //Exit hvis vi finner en node som ikke har to barn ennå
                    }
                    current = current.left;
                }
                level++;
            }
            // current har blitt navigert til en node som ikke har 2 barn enda
            // opprett ny foreldrenode, fordi når nodene skal konkurrere skrives vinnerverdien til newParent
            Node<T> newParent = new Node<>(null);
            // Koble den nye bladnoden og den eksisterende noden til den nye foreldrenoden
            // Partall: Den nye noden skal til høyre.
            // Oddetall: Den nye noden skal til venstre.
            if (size % 2 == 0) {
                // Ny node er høyre barn
                newParent.left = current;
                newParent.right = newLeaf;
            } else {
                // Ny node er venstre barn
                newParent.left = newLeaf;
                newParent.right = current;
            }

            /*
            Her konkurerer de og vinner verdien skrives til newparent.
             */
            newParent.value = winnerFunction.apply(newParent.left.value, newParent.right.value);
            // Koble den nye foreldrenoden til treet
            if (current == root) {
                // Den eksisterende noden var roten
                root = newParent;
            } else {
                // Koble den nye foreldrenoden til besteforeldren
                if (current.parent.left == current) {
                    current.parent.left = newParent;
                } else {
                    current.parent.right = newParent;
                }
            }
            newParent.parent = current.parent; // Sett forelder
            // alle noder må vite hvem som er forelderen deres, spesielt for å kunne gå oppover senere
            current.parent = newParent; // Sett parent
            newLeaf.parent = newParent;
            // 6. Konkurer oppover
            updateUpwards(newLeaf);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Sletter en deltaker fra turneringstreet basert på indeks.
     * @param index Indeksen til deltakeren som skal slettes.
     * @throws IndexOutOfBoundsException hvis indeksen er ugyldig.
     */
    public void deleteParticipant(int index) {
        writeLock.lock();
        try {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Ugyldig indeks: " + index);
            }
            // 1. Finn bladnode som skal slettes
            Node<T> nodeToDelete = leaves[index];
            // 2. Håndter spesialtilfeller (rotnode, tomt tre)
            if (nodeToDelete == root) {
                root = null;
                size = 0;
                leaves = new Node[leaves.length]; // lag et nytt tomt leaves array
                return;
            }
            // 3. Finn foreldrenode
            Node<T> parent = nodeToDelete.parent;
            // 4. Finn søsken
            Node<T> sibling = (parent.left == nodeToDelete) ? parent.right : parent.left;
            // 5. Koble besteforelder til søsken
            if (parent == root) {
                // Hvis foreldren er roten, blir søsken den nye roten
                root = sibling;
                sibling.parent = null; // Fjern forelder
            } else {
                // Koble besteforelder til søsken
                Node<T> grandParent = parent.parent;
                if (grandParent.left == parent) {
                    grandParent.left = sibling;
                } else {
                    grandParent.right = sibling;
                }
                sibling.parent = grandParent;
            }
            // 6. Oppdater treet oppover
            updateUpwards(sibling);
            // 7. Oppdater størrelse
            size--;
            // 8. Oppdater leaves array (valgfritt: kan fylle hullet med null eller flytte elementer)
            leaves[index] = null; // Logisk sletting: Sett elementet til null
            // Fysisk sletting:  Flytt de resterende elementene.
            for (int i = index; i < size; i++) {
                leaves[i] = leaves[i + 1];
            }
            leaves[size] = null; // Sett den siste til null
        } finally {
            writeLock.unlock();
        }
    }


    /**
     * Henter treets størrelse (antall faktiske deltakere).
     *
     * @return Antall deltakere.
     */
    public int getSize() {
        readLock.lock();
        try {
            return size;
        } finally {
            readLock.unlock();
        }
    }

    // printer treet inorder
    public void printTree() {
        readLock.lock();
        try {
            printInorder(root);
        } finally {
            readLock.unlock();
        }
    }

    private void printInorder(Node<T> node) {
        if (node != null) {
            printInorder(node.left);
            System.out.println(node.value + " ");
            printInorder(node.right);
        }
    }
    public static void main(String[] args) {
        Integer[] participants1 = {5, 2, 8, 9, 4, 7, 3};
        TurneringsTree<Integer> tree1 = new TurneringsTree<>(participants1);
        tree1.printTree();
        System.out.println("Vinner (Tree1): " + tree1.getWinner());
        tree1.updateParticipant(3, 3);
        System.out.println("Vinner (Tree1): " + tree1.getWinner());
        tree1.insertParticipant(45);
        System.out.println("Vinner (Tree1): " + tree1.getWinner());
    }
}
