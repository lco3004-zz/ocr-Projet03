package fr.ocr.modeconsole;

import fr.ocr.utiles.AppExceptions;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
import static fr.ocr.utiles.Messages.InfosMessages.CTRL_C;

class IOConsole {

    /*
     * saisie clavier avec pattern
     *
     * retourne le caractère qui correspond à la saisie utilisateur (filtré par pattern )
     */
    static Character LectureClavier(String pattern_Menu,
                                    Scanner scanner,
                                    Affichage affichage,
                                    Character escapeChar) throws AppExceptions {

        Pattern patternChoix = Pattern.compile(pattern_Menu);

        String choix = "";
        Character cRet = escapeChar;

        ClearScreen.cls();

        affichage.Display();

        while (choix.equals("") && scanner.hasNext()) {
            cRet = escapeChar;
            try {
                try {
                    try {
                        choix = scanner.next(patternChoix);
                        cRet = choix.toUpperCase().charAt(0);
                    } catch (InputMismatchException e1) {
                        String tmp = scanner.next();
                        ClearScreen.cls();
                        affichage.Display();
                    }
                } catch (StringIndexOutOfBoundsException e1) {
                    logger.info(CTRL_C);
                    choix = Character.toString(escapeChar);
                }
            } catch (Exception e3) {
                logger.error(String.format("%s %s ", ERREUR_GENERIC, e3.getClass().getSimpleName()));
                throw new AppExceptions(ERREUR_GENERIC);
            }
        }
        return cRet;
    }

    /*
     * efface l'afficage console . Clear ou Cls sont appellés selon le système
     * sur lequel le programme est exécuté
     */
    static class ClearScreen {
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
}
