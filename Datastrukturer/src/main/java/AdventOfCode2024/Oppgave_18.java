package AdventOfCode2024;
/*
Oppgave: Naviger gjennom korrupt minne

Du befinner deg i et 2D rutenett som representerer et minneområde.
Målet ditt er å komme deg fra øverste venstre hjørne (0,0) til
nederste høyre hjørne (70, 70) (eller 6,6 i eksempelet).
Du kan bevege deg opp, ned, venstre eller høyre.

En strøm av "bytes" faller ned i minnet og ødelegger cellene de
lander i. Du får en liste over koordinater (X, Y) som representerer
hvor disse bytene vil lande. Ødelagte celler kan ikke traverseres.

Simuler de første 1024 bytene som faller (eller alle bytene hvis
listen er kortere enn 1024). Finn deretter den korteste veien fra
(0,0) til (70,70) som unngår de ødelagte cellene.

Eksempel:

Rutenettstørrelse: 7x7 (koordinater fra 0 til 6)
Byteposisjoner (første 12):

Generated code
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1


Etter at disse bytene har falt, ser rutenettet slik ut ('.' = trygg, '#' = korrupt):

Generated code
...#...
..#..#.
....#..
...#..#
..#..#.
.#..#..
#.#....

Korteste vei fra (0,0) til (6,6) er 22 trinn. En mulig sti er:

Generated code
OO.#OOO
.O#OO#O
.OOO#OO
...#OO#
..#OO#.
.#.O#..
#.#OOOO

Oppgave:

Gitt den fullstendige listen over byteposisjoner (fra oppgaveteksten) og en
 rutenettstørrelse på 71x71 (koordinater fra 0 til 70), simuler de første
 1024 byte-nedfallene. Hva er minimum antall trinn som kreves for
 å nå (70,70) fra (0,0)?
 */
public class Oppgave_18 {

    public static void main(String[] args) {
        
    }
}
