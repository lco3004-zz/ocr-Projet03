package fr.ocr.modeconsole;

import fr.ocr.utiles.AppExceptions;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
import static fr.ocr.utiles.Messages.InfosMessages.CTRL_C;


/**
 * <p>
 * classe des io en mode terminal (console)
 *
 * @author laurent cordier
 * </p>
 */
public class IOConsole {

    /**
     * saisie clavier d'un caractere avec pattern
     * @param pattern_Menu  le pattern de la regex du menu
     * @param scanner  le scanner clavier
     * @param ecrireSurEcran  varaible interface
     * @param escapeChar le char escape pour soirtir du menu
     * @return le caractère qui correspond à la saisie utilisateur (filtré par pattern )
     * @throws AppExceptions  si erreur lors de la saisie
     */
    public static Character LectureClavierChar(String pattern_Menu,
                                               Scanner scanner,
                                               EcrireSurEcran ecrireSurEcran,
                                               Character escapeChar) throws AppExceptions {

        Pattern patternChoix = Pattern.compile(pattern_Menu);

        String choix = "";
        Character cRet = '?';

        ClearScreen.cls();

        ecrireSurEcran.Display();

        // scanner en mode hasNext, next - avec pattern de carteres autorisés
        try {
            while (choix.equals("") && scanner.hasNext()) {
                cRet = escapeChar;
                try {
                    try {
                        try {
                            choix = scanner.next(patternChoix);
                            cRet = choix.toUpperCase().charAt(0);

                        } catch (InputMismatchException e1) {
                            //si le caracter saisi n'est pas dans la liste des car. acceptés
                            String tmp = scanner.next();
                            //efface ecran et reaffiche le tout
                            ClearScreen.cls();
                            ecrireSurEcran.Display();
                        } catch (NoSuchElementException e2) {
                            //est-ce ctrl-C ??
                            logger.info(CTRL_C);
                            choix = Character.toString(escapeChar);
                        }
                    } catch (StringIndexOutOfBoundsException e1) {
                        //est-ce ctrl-C ??
                        logger.info(CTRL_C);
                        choix = Character.toString(escapeChar);
                    }
                } catch (Exception e3) {
                    //réponse inconnue
                    logger.error(String.format("%s %s ", ERREUR_GENERIC, e3.getClass().getSimpleName()));
                    throw new AppExceptions(ERREUR_GENERIC);
                }
            }
        } catch (NoSuchElementException e2) {
            //est-ce ctrl-C ??
            logger.info(CTRL_C);

        } catch (Exception e8) {
            //réponse inconnue
            logger.error(String.format("%s %s ", ERREUR_GENERIC, e8.getClass().getSimpleName()));
            throw new AppExceptions(ERREUR_GENERIC);
        }
        //est-ce un ctr-c qui fait sortier de la saisie par la classe Scanner ??
        if (cRet == '?') {
            cRet = escapeChar;
            logger.info(CTRL_C);
        }
        return cRet;
    }
    /*
     * efface l'afficage console . Clear ou Cls sont appellés selon le système
     * sur lequel le programme est exécuté
     */
    public static class ClearScreen {
        public static void cls() {
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
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */