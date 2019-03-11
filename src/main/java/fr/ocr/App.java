package fr.ocr;

import fr.ocr.mastermind.FabricationSecret;
import fr.ocr.mastermind.JeuMM;

import fr.ocr.modeconsole.MenuPrincipal;
import fr.ocr.modeconsole.MenuSecondaire;
import fr.ocr.params.Parametres;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.VersionPGM;


import java.util.ArrayList;
import java.util.Scanner;


import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.utiles.Constantes.Libelles.LibellesMenuPrincipal.CHOISIR_MASTERMIND;
import static fr.ocr.utiles.Logs.getInstance;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.PARAM_INCONNU;
import static fr.ocr.utiles.Messages.ErreurMessages.TYPE_PARAM_INCORRECT;
import static fr.ocr.utiles.Messages.InfosMessages.FIN_NORMALE_APPLICATION;
import static fr.ocr.utiles.Messages.InfosMessages.LANCEMENT_APPLICATION;


/**
 * OCR Projet 03
 */

public class App {


    public static void main(String[] args) throws AppExceptions {
        //Creation du Singleton qui gere les logsapplicatifs (log4j2)
        getInstance(App.class.getSimpleName());
        logger.info(String.format("%s Version= %s", LANCEMENT_APPLICATION.getMessageInfos(), VersionPGM.VERSION_APPLICATION.getVersion()));
        Scanner scanner = new Scanner(System.in);

        MenuPrincipal menu_principal = new MenuPrincipal(scanner);
        boolean bouclePrincipale = true;
        Constantes.Libelles.LibellesMenuSecondaire ch_Sec;
        Constantes.Libelles.LibellesMenuPrincipal ch_Sup;
        MenuSecondaire menu_secondaire;

        while (bouclePrincipale) {
            boolean boucleSecondaire;
            ch_Sup = menu_principal.RunMenu();
            switch (ch_Sup) {
                case TITRE:
                    break;
                case CHOISIR_MASTERMIND:
                case CHOISIR_PLUS_MOINS:
                    boucleSecondaire = true;

                    if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                        logger.info(String.format("choix du jeux : %s", Constantes.Libelles.LibellesJeux.MASTERMIND.toString()));
                        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);
                    } else {
                        logger.info(String.format("choix du jeux : %s", Constantes.Libelles.LibellesJeux.PLUSMOINS.toString()));
                        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.PLUSMOINS.toString(), scanner);
                    }

                    while (boucleSecondaire) {
                        ch_Sec = menu_secondaire.RunMenu();

                        switch (ch_Sec) {
                            case TITRE:
                                break;
                            case  MODE_CHALLENGER :
                                logger.info(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                    //menu_secondaire.majLigneEtat(String.format("%s - %s", ch_Sup.toString(), VoirInfoCodeSecret()));
                                    JeuMM jeuMM = new JeuMM(ch_Sec, scanner);
                                    jeuMM.runJeuMM();
                                } else {
                                    menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                }
                                break;
                            case MODE_DEFENSEUR:
                            case MODE_DUEL:
                                logger.info(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                break;
                            case RETOUR:
                                logger.info(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                boucleSecondaire = false;
                                break;
                            case LOGGER_PARAMETRES:
                                logger.info(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                    menu_secondaire.logParamtreMM();
                                }
                                break;
                            case QUITTER:
                                logger.info(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                boucleSecondaire = false;
                                bouclePrincipale = false;
                                break;
                            case LIGNE_ETAT:
                                break;
                            default:
                                logger.error(PARAM_INCONNU);
                        }
                    }
                    break;

                case QUITTER:
                    logger.info("QUITTER application");
                    bouclePrincipale = false;
                    break;
                default:
                    logger.error(PARAM_INCONNU);
            }
        }
        scanner.close();
        logger.info(FIN_NORMALE_APPLICATION.getMessageInfos());
    }


}
