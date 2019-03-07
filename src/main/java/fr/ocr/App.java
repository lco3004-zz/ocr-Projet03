package fr.ocr;

import java.util.ArrayList;
import java.util.Scanner;


import fr.ocr.utiles.ExceptionsApplicatives;
import fr.ocr.utiles.ConstantesApplicatives.VersionPGM;
import fr.ocr.modeConsole.Menu_Principal;
import fr.ocr.modeConsole.Menu_Secondaire;
import fr.ocr.params.paramsMM.CouleursMastermind;
import fr.ocr.params.paramsMM.GroupParamsMM;
import fr.ocr.mastermind.ChoixCodeSecret;


import static fr.ocr.utiles.Messages.ErreurMessages.*;

import static fr.ocr.utiles.LogApplicatifs.*;
import static fr.ocr.utiles.Messages.InfosMessages.*;

import static fr.ocr.params.FournisseurParams.getParam;

import fr.ocr.modeConsole.Libelles.LibellesMenu_Secondaire;
import fr.ocr.modeConsole.Libelles.LibellesMenu_Principal;
import static  fr.ocr.modeConsole.Libelles.LibellesMenu_Principal.*;

import static fr.ocr.modeConsole.Libelles.LibellesJeux;



/**
 * OCR Projet 03
 *
 */

public class App
{



    public static void main( String[] args ) throws ExceptionsApplicatives {
        //Creation du Singleton qui gere les logsapplicatifs (log4j2)
        getInstance(App.class.getSimpleName());
        logger.info(String.format("%s Version= %s",Lancement_Application.getMessageInfos(), VersionPGM.VERSION_PGM.getVersion()));
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
                case Choisir_Mastermind:
                case Choisir_PlusMoins:
                    boucleSecondaire =true;

                    if (ch_Sup == Choisir_Mastermind) {
                        logger.info(String.format("choix du jeux : %s", LibellesJeux.MASTERMIND.toString()));
                        menu_secondaire = new Menu_Secondaire(LibellesJeux.MASTERMIND.toString(),scanner);
                    }
                    else if ((ch_Sup == Choisir_PlusMoins)) {
                        logger.info(String.format("choix du jeux : %s", LibellesJeux.PLUSMOINS.toString()));
                        menu_secondaire = new Menu_Secondaire(LibellesJeux.PLUSMOINS.toString(), scanner);
                    }
                    else {
                        throw new ExceptionsApplicatives(ErreurGeneric);
                    }

                    while (boucleSecondaire) {
                        ch_Sec= menu_secondaire.RunMenu();

                        switch (ch_Sec) {
                            case TITRE:
                                break;
                            case MODE_DEFENSEUR:
                                logger.info(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                if (ch_Sup == Choisir_Mastermind) {
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
                                if (ch_Sup == Choisir_Mastermind) {
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
                                logger.error(ParamInconnu);
                        }
                    }
                    break;

                case QUITTER:
                    logger.info("QUITTER application");
                    bouclePrincipale = false;
                    break;
                    default:
                        logger.error(ParamInconnu);
            }
        }
        scanner.close();
        logger.info(FinNormale_Application.getMessageInfos());
    }
    private static void logParamtreMM() throws ExceptionsApplicatives {
        Object tmpRetour;
        for (GroupParamsMM x : GroupParamsMM.values()) {
            tmpRetour = getParam(x);
            if (tmpRetour instanceof Integer) {
                logger.info(String.format("%s = %d", x.toString(), tmpRetour));
            } else if (tmpRetour instanceof Boolean) {
                logger.info(String.format("%s = %b", x.toString(), tmpRetour));
            } else {
                throw new ExceptionsApplicatives(TypeParamIncorrect);
            }

        }
    }
    private static String VoirInfoCodeSecret() throws ExceptionsApplicatives {

        ChoixCodeSecret choixCodeSecret = new ChoixCodeSecret();
        CouleursMastermind[] toutes = CouleursMastermind.values();
        String s = "";
        for (CouleursMastermind x: toutes) {
            s += x.toString() +", ";
        }
        logger.info("Toutes le couleurs = "+ s.substring(0,s.lastIndexOf(',')));

        ArrayList <Byte> arrayList = choixCodeSecret.getCodeSecret();
        CouleursMastermind[] ligneATrouver  = choixCodeSecret.getLigneSecrete();

        s = "";
        for (CouleursMastermind x: ligneATrouver) {
            s += x.toString() +", ";
        }
        String valRet =String.format("%s %s","Combinaison secrete = ",s.substring(0,s.lastIndexOf(',')));
        logger.info("Combinaison secrete = "+s.substring(0,s.lastIndexOf(',')));
        return  valRet;
    }
}
