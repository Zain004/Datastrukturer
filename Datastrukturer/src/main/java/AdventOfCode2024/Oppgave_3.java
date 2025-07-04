package AdventOfCode2024;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*

Oppgave: Reparer julebutikkens datamaskiner!

Julebutikkens datamaskiner er ødelagte og roter til programmet som skal multiplisere tall. Programmet bruker instruksjoner som mul(X,Y) for å multiplisere tallene X og Y. Men minnet er fullt av rot og ugyldige tegn.

Din oppgave:

    Gå gjennom den ødelagte minnestrømmen (puslespillet ditt).

    Finn alle gyldige mul(X,Y) instruksjoner, hvor X og Y er tall mellom 1 og 999 (inkludert).

    Multipliser tallene i hver gyldig instruksjon.

    Summer alle resultatene av disse multiplikasjonene.

    For eksempel, se på følgende del av ødelagt minne:
    xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
    Bare de fire uthevede seksjonene er ekte mulinstruksjoner.
    Å legge sammen resultatet av hver instruksjon gir 161( 2*4 + 5*5 + 11*8 + 8*5).
 */
public class Oppgave_3 {

    public static long beregnMultiplikasjonsSum(String korruptMinne) {
        // Regex for å finne gyldige mul(X,Y) instruksjoner hvor X og Y er tall med 1-3 sifre
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher matcher = pattern.matcher(korruptMinne);

        long totalSum = 0;

        while (matcher.find()) {
            try {
                // matcher.group(0) = hele treffet → "mul(32,64)"
                int x = Integer.parseInt(matcher.group(1)); // matcher.group(1) = første tall inni → "32"
                int y = Integer.parseInt(matcher.group(2)); // matcher.group(2) = andre tall inni → "64"

                // Valider at tallene er innenfor området 1-999 (som spesifisert i oppgavebeskrivelsen, men ikke eksplisitt nevnt først)
                if (x >= 1 && x <= 999 && y >= 1 && y <= 999) {
                    totalSum += (long) x * y; // Cast til long for å unngå potensiell overflow
                } else {
                    System.out.println("Hopper over ugyldig mul-instruksjon: mul(" + x + ", " + y + ") - Tall utenfor rekkevidde.");
                }
            } catch (NumberFormatException e) {
                 System.err.println("Feil ved parsing av tall i mul-instruksjon: " + matcher.group());
            }
        }
        return totalSum;
    }

    public static void main(String[] args) {
        String korruptMinne = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
        System.out.println(beregnMultiplikasjonsSum(korruptMinne));
    }
}
