package AdventOfCode2024;

import java.awt.*;

/*
Oppgave: Toalett-redoubt Sikkerhetsfaktor

Du befinner deg i EBHQ, og må hjelpe en historiker å trygt komme seg til toalettet.
Området er fylt med roboter som beveger seg i rette linjer. Din oppgave er å forutsi
 robotenes posisjoner etter 100 sekunder og beregne en sikkerhetsfaktor basert på
 deres plasseringer.

Detaljer:
    Input: En liste over roboter med startposisjon (p=x,y) og hastighet (v=x,y).
    Område: Robotene befinner seg i et rom som er 101 fliser bredt og 103 fliser høyt.
    Bevegelse: Robotene beveger seg i rette linjer med konstant hastighet.
    Når en robot treffer en kant av rommet, teleporterer den til den motsatte siden (wraparound).
    Tid: Simuler robotenes bevegelse i 100 sekunder.
    Kvadrantdefinisjon: Rommet deles inn i fire kvadranter basert på midtpunktet
    (50.5, 51.5). Roboter som er nøyaktig på midtpunktet (horisontalt eller vertikalt) regnes ikke som en del av noen kvadrant.
    Sikkerhetsfaktor: Tell antall roboter i hver kvadrant. Multipliser disse tallene sammen for å få den totale sikkerhetsfaktoren.

Eksempel:
La oss si at vi har følgende roboter i et rom som er 11 fliser bredt og 7 fliser høyt (dette er et mindre eksempel enn den faktiske oppgaven):
    Robot 1: p=0,4 v=3,-3
    Robot 2: p=6,3 v=-1,-3

Etter 100 sekunder, antar vi (for eksempelets skyld) at:
    Robot 1 befinner seg i posisjon (2.3, 1.7)
    Robot 2 befinner seg i posisjon (5.8, 3.2)

Midtpunktet i dette rommet er (5.5, 3.5).
    Kvadrant 1 (x < 5.5, y < 3.5): Robot 1
    Kvadrant 2 (x > 5.5, y < 3.5): Robot 2
    Kvadrant 3 (x < 5.5, y > 3.5): Ingen roboter
    Kvadrant 4 (x > 5.5, y > 3.5): Ingen roboter

Antall roboter i hver kvadrant er:
    Kvadrant 1: 1
    Kvadrant 2: 1
    Kvadrant 3: 0
    Kvadrant 4: 0

Sikkerhetsfaktoren er 1 * 1 * 0 * 0 = 0.

Mål:
Beregn sikkerhetsfaktoren etter 100 sekunder basert på robotenes posisjoner i det 101x103 store rommet fra din faktiske inputdata.

Hint:
    Bruk wraparound-logikk for å håndtere roboters bevegelse når de treffer kantene.
    Husk å ekskludere roboter som er nøyaktig i midten når du teller roboter i hver kvadrant.
 */
public class Oppgave_14 {
    /**
     * Oppgave_14 simulerer bevegelsen til roboter i et rom med wrapping,
     * som beskrevet i Advent of Code, Dag 14.
     * <p>
     * Hver robot har en startposisjon og en hastighet. Etter et gitt antall sekunder
     * oppdateres posisjonen, og roboter som går utenfor kanten av rommet
     * vil teleporteres (wrappe) til motsatt side.
     */
    // Rommets bredde og høyde (antall fliser)
    private final int roomWidth;
    private final int roomHeight;
    // Hvor mange sekunder simuleringen skal kjøre
    private final int simulationTime;
    // Liste over alle roboter i simuleringen
    private final List<Robot> robots;

    /**
     * Oppretter en ny simulering av robotbevegelse.
     *
     * @param roomWidth      Bredden på rommet i fliser
     * @param roomHeight     Høyden på rommet i fliser
     * @param simulationTime Antall sekunder simuleringen skal kjøre
     * @param robotData      Liste med rådata som beskriver robotene
     */
    public Oppgave_14(int roomWidth, int roomHeight, int simulationTime, List<Robot> robotData) {
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.simulationTime = simulationTime;
        // Denne skal konvertere input data til faktiske Robot-objekter
        this.robots = parseRobots(robotData);
    }

    // Inner klasse som representerer en robot
     static class Robot {
        private double x;
        private double y;
        private double vx;
        private double vy;

        public Robot(double x, double y, double vx, double vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }
        /**
         * @return Nåværende X-posisjon etter bevegelse
         */
        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }
        public void move(int roomWidth, int roomHeight) {
            this.x = (this.x + this.vx) % roomWidth;
            if (this.x < 0) {
                this.x += roomWidth;
            }
            this.y = (this.y + this.vy) % roomHeight;
            if (this.y < 0) {
                this.y += roomHeight;
            }
        }
    }
    public static void main(String[] args) {

    }
}
