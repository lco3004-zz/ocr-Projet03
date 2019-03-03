package ocr_projet03;


import ocr_projet03.constantesOcr_Projet03.VersionPGM;
import ocr_projet03.exceptionOcr_Projet03.ExceptionMastermind;
import ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages;
import ocr_projet03.modeConsole.LibellesMenu_Principal;
import ocr_projet03.modeConsole.LibellesMenu_Secondaire;
import ocr_projet03.modeConsole.Menu_Principal;
import ocr_projet03.modeConsole.Menu_Secondaire;
import ocr_projet03.paramsOcr_Projet03.paramsMM.CouleursMastermind;
import ocr_projet03.paramsOcr_Projet03.paramsMM.GroupParamsMM;
import ocr_projet03.partieMastermind.ChoixCodeSecret;

import java.util.ArrayList;
import java.util.Scanner;

import static ocr_projet03.logsOcr_Projet03.logApplicatif.getInstance;
import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ErreurGeneric;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ParamInconnu;
import static ocr_projet03.messagesTexteOcr_Projet03.InfosMessages.FinNormale_Application;
import static ocr_projet03.messagesTexteOcr_Projet03.InfosMessages.Lancement_Application;
import static ocr_projet03.modeConsole.LibellesJeux.MASTERMIND;
import static ocr_projet03.modeConsole.LibellesJeux.PLUSMOINS;
import static ocr_projet03.modeConsole.LibellesMenu_Principal.Choisir_Mastermind;
import static ocr_projet03.modeConsole.LibellesMenu_Principal.Choisir_PlusMoins;
import static ocr_projet03.paramsOcr_Projet03.FournisseurParams.getParam;


/**
 * OCR Projet 03
 *
 */

public class oc_Projer03
{



    public static void main( String[] args ) throws ExceptionMastermind {
        //Creation du Singleton qui gere les logs (log4j2)
        getInstance(oc_Projer03.class.getSimpleName());
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
                        logger.info(String.format("choix du jeux : %s",MASTERMIND.toString()));
                        menu_secondaire = new Menu_Secondaire(MASTERMIND.toString(),scanner);
                    }
                    else if ((ch_Sup == Choisir_PlusMoins)) {
                        logger.info(String.format("choix du jeux : %s",PLUSMOINS.toString()));
                        menu_secondaire = new Menu_Secondaire(PLUSMOINS.toString(), scanner);
                    }
                    else {
                        throw new ExceptionMastermind(ErreurGeneric);
                    }

                    while (boucleSecondaire) {
                        ch_Sec= menu_secondaire.RunMenu();

                        switch (ch_Sec) {
                            case TITRE:
                                break;
                            case MODE_CHALLENGER:
                            case MODE_DEFENSEUR:
                            case MODE_DUEL:
                                if (ch_Sup == Choisir_Mastermind) {
                                    VoirInfoCodeSecret();
                                }
                                logger.info(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                menu_secondaire.majLigneEtat(String.format("%s du jeu %s",ch_Sec.toString(), ch_Sup.toString()));
                                break;
                            case RETOUR:
                                logger.info("vous revenez au menu principal");
                                boucleSecondaire =false;
                                break;
                            case VoirParametres:
                                if (ch_Sup == Choisir_Mastermind) {
                                    logger.info(String.format("Vous voyez les paramètres du jeu : %s",MASTERMIND.toString()));
                                    voirParamtreMM();
                                }
                                else if ((ch_Sup == Choisir_PlusMoins)){
                                    logger.info(String.format("Vous voyez les paramètres du jeu : %s",PLUSMOINS.toString()));
                                }
                                else
                                    logger.error(ParamInconnu);
                                break;
                            case QUITTER:
                                logger.info("vous quittez le menu secondaire");
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
    private static void voirParamtreMM() throws ExceptionMastermind {
        Object tmpRetour;
        for (GroupParamsMM x : GroupParamsMM.values()) {
            tmpRetour = getParam(x);
            if (tmpRetour instanceof Integer) {
                logger.info(String.format("%s = %d", x.toString(), (Integer) tmpRetour));
                ;
            } else if (tmpRetour instanceof Boolean) {
                logger.info(String.format("%s = %b", x.toString(), (Boolean) tmpRetour));
                ;
            } else {
                throw new ExceptionMastermind(ErreurMessages.TypeParamIncorrect);
            }

        }
    }
    private static void VoirInfoCodeSecret() throws ExceptionMastermind {

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
        logger.info("Combinaison secrete = "+s.substring(0,s.lastIndexOf(',')));

    }
}
