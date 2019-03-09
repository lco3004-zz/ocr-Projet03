package fr.ocr.modeconsole;

import com.sun.org.apache.xpath.internal.functions.Function;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class IOConsole {
    /*
     * efface l'afficage console . Clear ou Cls sont appellés selon le système
     * sur lequel le programme est exécuté
     */
    static final class ClearScreen {
        static void cls() {
            try {

                final String os = System.getProperty("os.name");

                // la commande a exécuté par le shell windows "cmd qui appelle une autre cmd" est la seule
                //qui fonctione (Windows 10 pro - insiders build 17134.523 27/08/2018
                if (os.contains("Windows"))
                    new ProcessBuilder("cmd", "/c", "cmd.exe /c cls").inheritIO().start().waitFor();
                else
                    Runtime.getRuntime().exec("clear");
            } catch (InterruptedException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /*
     * saisie clavier avec pattern
     *
     * retourne le caractère qui correspond à la saisie utilisateur (filtré par pattern )
     */
    static public Character LectureClavier(String pattern_Menu, Scanner scanner, Affichage affichage ) {
        Pattern patternChoix = Pattern.compile(pattern_Menu);
        String choix = "";
        IOConsole.ClearScreen.cls();

        affichage.Display();

        while (choix.equals("") && scanner.hasNext()) {

            try {
                choix = scanner.next(patternChoix).toUpperCase();

            } catch (InputMismatchException | StringIndexOutOfBoundsException e1) {
                String tmp = scanner.next();
                IOConsole.ClearScreen.cls();
                affichage.Display();
            }
        }
        return choix.toUpperCase().charAt(0);
    }


}
