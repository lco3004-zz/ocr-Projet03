package fr.ocr;

import fr.ocr.mastermind.ChoixCodeSecret;
import fr.ocr.modeconsole.Libelles.LibellesMenu_Principal;
import fr.ocr.modeconsole.Libelles.LibellesMenu_Secondaire;
import fr.ocr.modeconsole.Menu_Principal;
import fr.ocr.modeconsole.Menu_Secondaire;
import fr.ocr.params.mastermind.CouleursMastermind;
import fr.ocr.params.mastermind.GroupParamsMM;
import fr.ocr.utiles.ApplicationExceptions;
import fr.ocr.utiles.Constantes.VersionPGM;

import java.util.ArrayList;
import java.util.Scanner;

import static fr.ocr.modeconsole.Libelles.LibellesJeux;
import static fr.ocr.modeconsole.Libelles.LibellesMenu_Principal.CHOISIR_MASTERMIND;
import static fr.ocr.modeconsole.Libelles.LibellesMenu_Principal.CHOISIR_PLUS_MOINS;
import static fr.ocr.params.FournisseurParams.getParam;
import static fr.ocr.utiles.Logs.getInstance;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.*;
import static fr.ocr.utiles.Messages.InfosMessages.FIN_NORMALE_APPLICATION;
import static fr.ocr.utiles.Messages.InfosMessages.LANCEMENT_APPLICATION;



/**
 * OCR Projet 03
 *
 */

public class App
{



    public static void main( String[] args ) throws ApplicationExceptions {
        //Creation du Singleton qui gere les logsapplicatifs (log4j2)
        getInstance(App.class.getSimpleName());
        logger.info(String.format("%s Version= %s", LANCEMENT_APPLICATION.getMessageInfos(), VersionPGM.VERSION_APPLICATION.getVersion()));
        Scanner scanner = new Scanner(System.in);

        Menu_Principal menu_principal = new Menu_Principal(scanner);
        boolean bouclePrincipale= true;
        LibellesMenu_Secondaire ch_Sec;
        LibellesMenu_Principal ch_Sup;
        Menu_Secondaire menu_secondaire;

        while (bouclePrincipale) {
            boolean boucleSecondaire;
             ch_Sup = menu_principal.RunMenu();
            switch (ch_Sup) {
                case TITRE:
                    break;
                case CHOISIR_MASTERMIND:
                case CHOISIR_PLUS_MOINS:
                    boucleSecondaire =true;

                    if (ch_Sup == CHOISIR_MASTERMIND) {
                        logger.info(String.format("choix du jeux : %s", LibellesJeux.MASTERMIND.toString()));
                        menu_secondaire = new Menu_Secondaire(LibellesJeux.MASTERMIND.toString(),scanner);
                    }
                    else if ((ch_Sup == CHOISIR_PLUS_MOINS)) {
                        logger.info(String.format("choix du jeux : %s", LibellesJeux.PLUSMOINS.toString()));
                        menu_secondaire = new Menu_Secondaire(LibellesJeux.PLUSMOINS.toString(), scanner);
                    }
                    else {
                        throw new ApplicationExceptions(ERREUR_GENERIC);
                    }

                    while (boucleSecondaire) {
                        ch_Sec= menu_secondaire.RunMenu();

                        switch (ch_Sec) {
                            case TITRE:
                                break;
                            case MODE_DEFENSEUR:
                                logger.info(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                if (ch_Sup == CHOISIR_MASTERMIND) {
                                    menu_secondaire.majLigneEtat(String.format("%s - %s",ch_Sup.toString(),VoirInfoCodeSecret()));
                                }
                                else {
                                    menu_secondaire.majLigneEtat(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                }
                                break;
                            case MODE_CHALLENGER:
                            case MODE_DUEL:
                                logger.info(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                menu_secondaire.majLigneEtat(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                break;
                            case RETOUR:
                                logger.info(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                boucleSecondaire =false;
                                break;
                            case LOGGER_PARAMETRES:
                                logger.info(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                menu_secondaire.majLigneEtat(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                if (ch_Sup == CHOISIR_MASTERMIND) {
                                    logParamtreMM();
                                }
                                break;
                            case QUITTER:
                                logger.info(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                boucleSecondaire =false;
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
    private static void logParamtreMM() throws ApplicationExceptions {
        Object tmpRetour;
        for (GroupParamsMM x : GroupParamsMM.values()) {
            tmpRetour = getParam(x);
            if (tmpRetour instanceof Integer) {
                logger.info(String.format("%s = %d", x.toString(), tmpRetour));
            } else if (tmpRetour instanceof Boolean) {
                logger.info(String.format("%s = %b", x.toString(), tmpRetour));
            } else {
                throw new ApplicationExceptions(TYPE_PARAM_INCORRECT);
            }

        }

    }
    private static String VoirInfoCodeSecret() throws ApplicationExceptions {

        ChoixCodeSecret choixCodeSecret = new ChoixCodeSecret();
        CouleursMastermind[] toutes = CouleursMastermind.values();
        StringBuilder s = new StringBuilder(4096);
        for (CouleursMastermind x: toutes) {
            s.append(String.format("%s%s",x.toString(),", "));
        }
        logger.info("Toutes le couleurs = "+ s.substring(0,s.lastIndexOf(",")));

        ArrayList <Byte> arrayList = choixCodeSecret.getChiffresSecrets();
        CouleursMastermind[] ligneATrouver  = choixCodeSecret.getCouleursSecretes();

         int tailleStringB = s.length();
        s.delete(0,tailleStringB-1);
        for (CouleursMastermind x: ligneATrouver) {
            s.append(String.format("%s%s",x.toString(),", "));
        }
        String valRet =String.format("%s %s","Combinaison secrete = ",s.substring(0,s.lastIndexOf(",")));
        logger.info("Combinaison secrete = "+s.substring(0,s.lastIndexOf(",")));
        return  valRet;
    }
}
