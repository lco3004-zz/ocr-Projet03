package fr.ocr.mastermind;

import fr.ocr.modeconsole.IhmChallengeurMM;
import fr.ocr.modeconsole.IhmDefenseurMM;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.CouleursMastermind;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.ArrayList;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.PIONS_BIENPLACES;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.PIONS_MALPLACES;
import static fr.ocr.utiles.Constantes.ConstLigneSimple.*;
import static fr.ocr.utiles.Logs.logger;

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
abstract class JeuMM {

    LignePropaleMM[] lignesPropaleMM = new LignePropaleMM[(Integer) getParam(NOMBRE_D_ESSAIS)];
    LigneSimpleMM[] lignesSimpleMM = new LigneSimpleMM[NBRE_LIGNESTABLEMM];
    ValiderProposition validerProposition = new ValiderProposition() {
        @Override
        public Boolean apply(ArrayList<Character> proposition, ArrayList<Character> secret, Integer nombreDePositions, int[] zoneEvaluation) {
            return null;
        }
    };
    ProduirePropale produirePropale;
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
    private Boolean modeDebug = (Boolean) getParam(MODE_DEBUG);

    private LibellesMenuSecondaire modeJeu;
    private Scanner scanner;


    JeuMM(LibellesMenuSecondaire modeJeu, Scanner sc) {
        scanner = sc;
        this.modeJeu = modeJeu;

    }

    private void PreparationMenu(LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                                 CouleursMastermind[] couleursSecretes) {


        lignesSimpleMM[TITRE] = new LigneSimpleMM(true, true, TITRE, TITRE, modeDeJeu.toString());

        lignesSimpleMM[LIGNE_STATUS] = new LigneSimpleMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format(" %s", getParam(MODE_DEBUG).toString()));

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
                    "", validerProposition);
            lignesPropaleMM[k].Clear().setLibelleLigne();

            lignesSimpleMM[indexLignesJeuMM] = lignesPropaleMM[k];
        }
    }

    private void PrepareRunJeuMM(FabricationSecretMM fabricationSecretMM) {

        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes());

        PreparationMenu(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes());
    }

    void RunJeuMMChallengeur(FabricationSecretMM fabricationSecretMM) {

        PrepareRunJeuMM(fabricationSecretMM);
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(fabricationSecretMM.getCouleursSecretes());

        if (modeDebug) {
            lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);
        }
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);

        new IhmChallengeurMM(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes(),
                validerProposition,
                lignesSimpleMM,
                lignesPropaleMM)
                .runIhmMM(scanner, produirePropale);
    }


    void RunJeuMMDefenseur(FabricationSecretMM fabricationSecretMM) {

        PrepareRunJeuMM(fabricationSecretMM);
        int nbColSec = fabricationSecretMM.getCouleursSecretes().length;
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(fabricationSecretMM.getCouleursSecretes(), nbColSec, " Combinaison secrete : ");


        new IhmDefenseurMM(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes(),
                validerProposition,
                lignesSimpleMM,
                lignesPropaleMM)
                .runIhmMM(scanner, produirePropale);
    }

    /**
     * @return le code secret (String)
     */
    private String LogLaCombinaisonSecrete(CouleursMastermind[] couleursSecretes) {

        CouleursMastermind[] toutes = CouleursMastermind.values();
        StringBuilder s = new StringBuilder(4096);

        for (CouleursMastermind x : toutes) {
            s.append(String.format("%s%s", x.toString(), ", "));
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
