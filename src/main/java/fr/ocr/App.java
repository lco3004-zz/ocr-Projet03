package fr.ocr;

import fr.ocr.mastermind.JeuMMChallengeur;
import fr.ocr.mastermind.JeuMMDefenseur;
import fr.ocr.modeconsole.MenuPrincipal;
import fr.ocr.modeconsole.MenuSecondaire;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.VersionPGM;

import java.util.Scanner;

import static fr.ocr.utiles.Constantes.Libelles.LibellesMenuPrincipal.CHOISIR_MASTERMIND;
import static fr.ocr.utiles.Logs.getInstance;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.PARAM_INCONNU;
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
                        logger.info(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));

                        switch (ch_Sec) {
                            case TITRE:
                                break;
                            case MODE_CHALLENGER:
                                if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                    JeuMMChallengeur jeuMMChallengeur = new JeuMMChallengeur(ch_Sec, scanner);
                                    jeuMMChallengeur.runJeuMM();
                                } else {
                                    menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                }
                                break;
                            case MODE_DEFENSEUR:
                                if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                    JeuMMDefenseur jeuMMDefenseur = new JeuMMDefenseur(ch_Sec, scanner);
                                    jeuMMDefenseur.runJeuMM();
                                } else {
                                    menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                }
                                break;
                            case MODE_DUEL:
                                menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                break;
                            case RETOUR:
                                boucleSecondaire = false;
                                break;
                            case LOGGER_PARAMETRES:
                                menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                    menu_secondaire.logParamtreMM();
                                }
                                break;
                            case QUITTER:
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
