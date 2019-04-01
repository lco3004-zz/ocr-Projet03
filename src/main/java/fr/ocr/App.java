package fr.ocr;


import fr.ocr.mastermind.JeuMasterMind;
import fr.ocr.modeconsole.MenuPrincipal;
import fr.ocr.modeconsole.MenuSecondaire;
import fr.ocr.plusmoins.MonThread;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuPrincipal;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;
import fr.ocr.utiles.Constantes.VersionPGM;
import fr.ocr.utiles.MesOptions;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.Scanner;

import static fr.ocr.params.LireParametres.OverrideParamModeDeveloppeur;
import static fr.ocr.params.LireParametres.getAllParams;
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

        MesOptions options = new MesOptions();

        CmdLineParser parser = new CmdLineParser(options);

        try {
            parser.parseArgument(args);
            if (options.isModeDebug()) {

                OverrideParamModeDeveloppeur();
            }

        } catch (CmdLineException e) {

            //parser.setUsageWidth(80);
            parser.printUsage(System.out);
        }


        //creation objet logger (singleton)
        getInstance(App.class.getSimpleName());
        logger.info(String.format("%s Version= %s", LANCEMENT_APPLICATION.getMessageInfos(), VersionPGM.VERSION_APPLICATION.getVersion()));

        //scanner sur console
        Scanner scanner = new Scanner(System.in);

        //récupération des paramètres applicatifs , avec contrôle de cohérence
        getAllParams();

        MenuPrincipal menu_principal = new MenuPrincipal(scanner);

        boolean bouclePrincipale = true;

        LibellesMenuSecondaire ch_Sec;

        LibellesMenuPrincipal ch_Sup;

        MenuSecondaire menu_secondaire;

        //boucle principale du programme
        while (bouclePrincipale) {

            boolean boucleSecondaire;

            //appel le menu principal du programme
            ch_Sup = menu_principal.RunMenu();

            switch (ch_Sup) {
                case TITRE:
                    break; // necessaire car ch_Sup est une instance d'enum - evite warning

                //choix du jeu
                case CHOISIR_MASTERMIND:
                case CHOISIR_PLUS_MOINS:
                    boucleSecondaire = true;

                    if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                        logger.info(String.format("choix du jeux : %s", Constantes.Libelles.LibellesJeux.MASTERMIND.toString()));

                        //creation  du menu du mastermind
                        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);
                    } else {
                        logger.info(String.format("choix du jeux : %s", Constantes.Libelles.LibellesJeux.PLUSMOINS.toString()));

                        //creation  du menu du jeu plusmoins
                        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.PLUSMOINS.toString(), scanner);
                    }

                    while (boucleSecondaire) {
                        //appel du menu secondaire (plusmoins ou mastermind)
                        ch_Sec = menu_secondaire.RunMenu();
                        logger.info(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));

                        switch (ch_Sec) {
                            case TITRE:
                                break; // idem ci-dessus, ch_Sec est une instance d'Enum ; permet d'éviter les warning

                            case MODE_CHALLENGER:
                                if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                    JeuMasterMind.CHALLENGEUR(ch_Sec, scanner);
                                } else {
                                    menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));

                                    MonThread monThread= new MonThread("jeu PlusMoins");
                                    try {
                                        monThread.run();
                                        monThread.join();
                                    } catch (InterruptedException e) {
                                        menu_secondaire.majLigneEtat(String.format("fin de %s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                    }
                                }
                                break;

                            case MODE_DEFENSEUR:
                                if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                    JeuMasterMind.DEFENSEUR(ch_Sec, scanner);
                                } else {
                                    menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                }
                                break;

                            case MODE_DUEL:
                                if (ch_Sup.equals(CHOISIR_MASTERMIND)) {
                                    JeuMasterMind.DUEL(ch_Sec, scanner);
                                } else {
                                    menu_secondaire.majLigneEtat(String.format("%s du jeu %s", ch_Sec.toString(), ch_Sup.toString()));
                                }
                                break;

                            case RETOUR:
                                boucleSecondaire = false;
                                break;

                            case LOGGER_PARAMETRES:
                                //log les parametres du jeu dans le fichier géré par log4J
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
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */
