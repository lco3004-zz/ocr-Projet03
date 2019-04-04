package fr.ocr;


import fr.ocr.mastermind.JeuMasterMind;
import fr.ocr.modeconsole.MenuPrincipal;
import fr.ocr.modeconsole.MenuSecondaire;
import fr.ocr.params.LireParametres;
import fr.ocr.plusmoins.JeuPlusMoins;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuPrincipal;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;
import fr.ocr.utiles.Constantes.VersionPGM;
import fr.ocr.utiles.MesOptions;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.Scanner;

import static fr.ocr.utiles.Constantes.Libelles.LibellesMenuPrincipal.CHOISIR_MASTERMIND;
import static fr.ocr.utiles.Logs.getInstance;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.PARAM_INCONNU;
import static fr.ocr.utiles.Messages.InfosMessages.FIN_NORMALE_APPLICATION;
import static fr.ocr.utiles.Messages.InfosMessages.LANCEMENT_APPLICATION;

/**
 * <p>
 * @author laurent cordier
 * OCR Projet 03
 * </p>
 */
public class App {

    public static void main(String[] args) throws AppExceptions {

        //creation objet logger (singleton)
        getInstance(App.class.getSimpleName());
        logger.info(String.format("%s Version= %s", LANCEMENT_APPLICATION.getMessageInfos(), VersionPGM.VERSION_APPLICATION.getVersion()));
        //récupération des paramètres applicatifs , avec contrôle de cohérence
        LireParametres.getAllParams();

        MesOptions options = new MesOptions();

        CmdLineParser parser = new CmdLineParser(options);

        try {
            parser.parseArgument(args);
            if (options.isModeDebug()) {

                LireParametres.OverrideParamModeDeveloppeur();
            }

        } catch (CmdLineException e) {

            //parser.setUsageWidth(80);
            parser.printUsage(System.out);
        }



        boolean bouclePrincipale = true;

        LibellesMenuSecondaire ch_Sec;

        LibellesMenuPrincipal ch_Sup;

        MenuSecondaire menu_secondaire;

        try (Scanner scannerPrimaire = new Scanner(System.in)) {

            //boucle principale du programme
            while (bouclePrincipale) {

                boolean boucleSecondaire;

                //scanner sur console


                ch_Sup = (new MenuPrincipal(scannerPrimaire)).RunMenu();


                switch (ch_Sup) {

                    case CHOISIR_MASTERMIND:
                    case CHOISIR_PLUS_MOINS:
                        boucleSecondaire = true;

                        while (boucleSecondaire) {

                            if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                //creation  du menu du mastermind
                                menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scannerPrimaire);
                            } else {
                                //creation  du menu du jeu plusmoins
                                menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.PLUSMOINS.toString(), scannerPrimaire);
                            }
                            ch_Sec = menu_secondaire.RunMenu();


                            switch (ch_Sec) {
                                case MODE_CHALLENGER:
                                    if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                        JeuMasterMind.CHALLENGEUR(ch_Sec, scannerPrimaire);
                                    } else {
                                        JeuPlusMoins.CHALLENGEUR(ch_Sec, scannerPrimaire);
                                    }
                                    break;

                                case MODE_DEFENSEUR:
                                    if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                        JeuMasterMind.DEFENSEUR(ch_Sec, scannerPrimaire);

                                    } else {
                                        JeuPlusMoins.DEFENSEUR(ch_Sec, scannerPrimaire);
                                    }
                                    break;

                                case MODE_DUEL:
                                    if ((ch_Sup.equals(CHOISIR_MASTERMIND))) {
                                        JeuMasterMind.DUEL(ch_Sec, scannerPrimaire);
                                    } else {
                                        JeuPlusMoins.DUEL(ch_Sec, scannerPrimaire);
                                    }
                                    break;

                                case RETOUR:
                                    boucleSecondaire = false;
                                    break;

                                case LOGGER_PARAMETRES:
                                    if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                        menu_secondaire.logParamtreMM();
                                    }
                                    break;
                                case QUITTER:
                                    boucleSecondaire = false;
                                    bouclePrincipale = false;
                                    break;

                                case TITRE:
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
                    case TITRE:
                        break; // necessaire car ch_Sup est une instance d'enum - evite warning
                    default:
                        logger.error(PARAM_INCONNU);
                }
            }
            logger.info(FIN_NORMALE_APPLICATION.getMessageInfos());


        }
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */
