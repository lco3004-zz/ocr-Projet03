package fr.ocr.mastermind;

import fr.ocr.modeconsole.EcrireSurEcran;
import fr.ocr.modeconsole.IOConsole;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.FabPattSais;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.PIONS_BIENPLACES;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.PIONS_MALPLACES;
import static fr.ocr.utiles.Constantes.ConstLigneSimple.*;
import static fr.ocr.utiles.Constantes.CouleursMastermind;
import static fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

public interface JeuMasterMind {

    static JeuMMChallengeur CHALLENGEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        return new JeuMMChallengeur(modeJeu, sc);
    }

    static JeuMMDefenseur DEFENSEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        return new JeuMMDefenseur(modeJeu, sc);
    }

    void runJeuMM();
}

/**
 *
 */

class JeuMMDefenseur extends JeuMM {

    List<Integer[]> lesScoresPossibles;
    ArrayList<ArrayList<Character>> lesCombinaisonsPossibles;

    JeuMMDefenseur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
    }

    @Override
    public void runJeuMM() {

        //MenuSaisieSecret menuSaisieSecret = new MenuSaisieSecret();
        //FabricationSecretMM fabricationSecretMM = new FabricationSecretMM(menuSaisieSecret.saisirCombinaisonSecrete());

        // DEBUT MOCK
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();
        //FIN MOCK
        this.produirePropaleMM = new ProduirePropaleMMDefenseur();
        this.validerPropositionMM = new EvalPropaleDefenseur();

        super.RunJeuMMDefenseur(fabricationSecretMM);
    }
}

/**
 *
 */
class JeuMMChallengeur extends JeuMM {

    JeuMMChallengeur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);

    }

    @Override
    public void runJeuMM() {
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();
        this.validerPropositionMM = new EvalPropaleChallengeur();

        this.produirePropaleMM = new ProduirePropaleMMChallengeur(lignesSimpleMM, lignesPropaleMM);
        super.RunJeuMMChallengeur(fabricationSecretMM);
    }
}

/**
 * "Modele" du jeuMastermind
 * Note :
 * * la combinaison secrete est soit calculée par ordinateur en mode challeger
 * * soit saisie par le joueur
 * *la fabrication de la composition secrete 'S' dépend de :
 * * * NOMBRE_DE_COULEURS : le nombre de couleurs disponibles  'N'
 * * * * limité à 18 max par construction, valeur min 6 couleurs qui est une valeur std)
 * * * NOMBRE_DE_POSITIONS : le nombre de couleurs 'P'  constituant la composition secrete S,
 * * * * 8 max par construction, min 4 qui est une valeur Std
 * * * DOUBLON_AUTORISE
 */
abstract class JeuMM implements JeuMasterMind {

    LignePropaleMM[] lignesPropaleMM = new LignePropaleMM[(Integer) getParam(NOMBRE_D_ESSAIS)];
    LigneSimpleMM[] lignesSimpleMM = new LigneSimpleMM[NBRE_LIGNESTABLEMM];
    ValiderPropositionMM validerPropositionMM = new ValiderPropositionMM() {
        @Override
        public Boolean apply(ArrayList<Character> proposition, ArrayList<Character> secret, Integer nombreDePositions, int[] zoneEvaluation) {
            return null;
        }
    };
    ProduirePropaleMM produirePropaleMM;
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
    private Boolean modeDebug = (Boolean) getParam(MODE_DEBUG);

    private Constantes.Libelles.LibellesMenuSecondaire modeJeu;
    private Scanner scanner;


    JeuMM(Constantes.Libelles.LibellesMenuSecondaire modeJeu, Scanner sc) {
        scanner = sc;
        this.modeJeu = modeJeu;

    }

    private void PreparationMenu(LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                                 CouleursMastermind[] couleursSecretes) {


        lignesSimpleMM[TITRE] = new LigneSimpleMM(true, true, TITRE, TITRE, modeDeJeu.toString());

        lignesSimpleMM[LIGNE_STATUS] = new LigneSimpleMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format(" Mode debug = %s", getParam(MODE_DEBUG).toString()));

        lignesSimpleMM[LIGNE_SECRETE] = new LigneSimpleMM(true, false, LIGNE_SECRETE, LIGNE_SECRETE, " -------SECRET--------");

        String champBlancNoir;
        StringBuilder lesCroixEtVirgules = new StringBuilder(256);
        for (int nbPositions = 0; nbPositions < nombreDePositions; nbPositions++) {
            lesCroixEtVirgules.append(' ');
            lesCroixEtVirgules.append('x');
            lesCroixEtVirgules.append(',');
        }
        lignesSimpleMM[LIGNE_TOUTES_COULEURS] = new LigneSimpleMM(true, true, LIGNE_TOUTES_COULEURS, LIGNE_TOUTES_COULEURS, " ");

        champBlancNoir = String.format(" ## [%s ] %c %c", lesCroixEtVirgules.substring(0, lesCroixEtVirgules.length() - 1), PIONS_BIENPLACES, PIONS_MALPLACES);
        lignesSimpleMM[LIGNE_ENTETE] = new LigneSimpleMM(true, true, LIGNE_ENTETE, LIGNE_ENTETE, champBlancNoir);

        lignesSimpleMM[LIGNE_BLANCH01] = new LigneSimpleMM(true, true, LIGNE_BLANCH01, LIGNE_BLANCH01, " ");
        lignesSimpleMM[LIGNE_BLANCH02] = new LigneSimpleMM(true, true, LIGNE_BLANCH02, LIGNE_BLANCH02, " ");

        Character c = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);

        lignesSimpleMM[LIGNE_DE_SAISIE] = new LigneSimpleMM(true, true, LIGNE_DE_SAISIE, LIGNE_DE_SAISIE, String.format(" Votre choix (%c : Retour): ", c));
        for (int k = 0, indexLignesJeuMM = LIGNE_PROPOSITION; k < (Integer) getParam(NOMBRE_D_ESSAIS); k++, indexLignesJeuMM++) {
            lignesPropaleMM[k] = new LignePropaleMM(couleursSecretes,
                    chiffresSecrets,
                    true,
                    true,
                    k,
                    LIGNE_PROPOSITION,
                    "", validerPropositionMM);
            lignesPropaleMM[k].Clear().setLibelleLigne();

            lignesSimpleMM[indexLignesJeuMM] = lignesPropaleMM[k];
        }
    }

    private void PrepareRunJeuMM(FabricationSecretMM fabricationSecretMM) {

        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes(), nombreDeCouleurs);

        PreparationMenu(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes());
    }

    void RunJeuMMChallengeur(FabricationSecretMM fabricationSecretMM) {
        Character escapeChar = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);
        Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);

        PrepareRunJeuMM(fabricationSecretMM);
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(fabricationSecretMM.getCouleursSecretes());

        if (modeDebug) {
            lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);
        }
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);

        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        String patternInitial = FabPattSais.ConstruitPatternSaisie(CouleursMastermind.values(), escapeChar);

        ArrayList<Character> propaleChallengeur;

        int indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        while (!isEscape) {
            if (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

                lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

                propaleChallengeur = produirePropaleMM.getPropaleChallengeur(scanner, patternInitial, escapeChar);

                if (propaleChallengeur.contains(escapeChar)) {
                    isEscape = true;
                } else {
                    SecretTrouve = lignesPropaleMM[indexLignesProposition++].setPropositionJoueur(propaleChallengeur).setZoneProposition().EvalProposition();
                    nbreEssaisConsommes++;
                }
            } else {
                lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
                if (SecretTrouve) {
                    lignesSimpleMM[LIGNE_STATUS].setLibelleLigne(" ----   VICTOIRE !!---");
                    lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");
                } else {
                    lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);
                    lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(String.format("-- Perdu. Soluce = %s", lignesSimpleMM[LIGNE_SECRETE].getLibelleLigne()));
                    lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");
                }
                propaleChallengeur = produirePropaleMM.getPropaleChallengeur(scanner, FabPattSais.ConstruitPatternSaisie(escapeChar), escapeChar);
                if (propaleChallengeur.contains(escapeChar)) {
                    isEscape = true;
                }
            }
        }

    }

    void RunJeuMMDefenseur(FabricationSecretMM fabricationSecretMM) {
        Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);
        Character escapeChar = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);

        PrepareRunJeuMM(fabricationSecretMM);
        int nbColSec = fabricationSecretMM.getCouleursSecretes().length;
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(fabricationSecretMM.getCouleursSecretes(), nbColSec, " Combinaison secrete : ");
        lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);

        /*new IhmDefenseurMM(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes(),
                validerPropositionMM,
                lignesSimpleMM,
                lignesPropaleMM)
                .runIhmMM(scanner, produirePropaleMM);
        */


        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        String patternEscape = FabPattSais.ConstruitPatternSaisie(escapeChar);

        ArrayList<Character> propalOrdinateur;

        int indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        while (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

            propalOrdinateur = produirePropaleMM.getPropaleDefenseur();

            SecretTrouve = lignesPropaleMM[indexLignesProposition].setPropositionJoueur(propalOrdinateur).setZoneProposition().EvalProposition();

            if (!SecretTrouve) {
                produirePropaleMM.setScorePropale(propalOrdinateur, lignesPropaleMM[indexLignesProposition].getZoneEvaluation());
            }

            indexLignesProposition++;
            nbreEssaisConsommes++;
        }

        if (SecretTrouve) {
            lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne("!! Ordinateur Gagne !!");

        } else {
            lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne("!! Ordinateur Perd !!");
        }

        lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
        //
        // pour confirmation sortie du jeu , par le defenseur (sinon - pas d'affichage et retour direct au menu superieur
        // seule saise possible 'escapeChar'
        //
        IOConsole.LectureClavier(patternEscape, scanner, new EcrireSurEcran() {
            @Override
            public void Display() {
                for (int n = TITRE; n <= LIGNE_DE_SAISIE; n++) {
                    if (lignesSimpleMM[n].isEstVisible()) {
                        if (n == LIGNE_DE_SAISIE) {
                            System.out.print(lignesSimpleMM[n].toString());
                        } else {
                            System.out.println(lignesSimpleMM[n].toString());
                        }
                    }
                }
            }
        }, escapeChar);
    }


    /**
     * @return le code secret (String)
     */
    private String LogLaCombinaisonSecrete(CouleursMastermind[] couleursSecretes, int nombreDeCouleurs) {

        CouleursMastermind[] toutes = CouleursMastermind.values();
        StringBuilder s = new StringBuilder(4096);
        int nbCoulRetenue = 0;
        for (CouleursMastermind x : toutes) {
            s.append(String.format("%s%s", x.toString(), ", "));
            nbCoulRetenue++;
            if (nbCoulRetenue >= nombreDeCouleurs)
                break;
        }
        logger.info("Toutes les couleurs = " + s.substring(0, s.lastIndexOf(", ")));

        int tailleStringB = s.length();
        s.delete(0, tailleStringB - 1);
        for (CouleursMastermind x : couleursSecretes) {
            s.append(String.format("%s%s", x.toString(), ", "));
        }
        String valRet = String.format("%s %s", "Combinaison secrete = ", s.substring(0, s.lastIndexOf(", ")));
        logger.info("Combinaison secrete = " + s.substring(0, s.lastIndexOf(",")));
        return valRet;
    }

}

