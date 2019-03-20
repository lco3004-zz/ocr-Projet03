package fr.ocr.modeconsole;


import fr.ocr.mastermind.LignePropaleMM;
import fr.ocr.mastermind.LigneSimpleMM;
import fr.ocr.mastermind.ProduirePropale;
import fr.ocr.mastermind.ValiderProposition;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.ConstEvalPropale;
import fr.ocr.utiles.Constantes.ConstLigneSimple;
import fr.ocr.utiles.Constantes.CouleursMastermind;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;
import fr.ocr.utiles.FabPattSais;

import java.util.ArrayList;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;


/**
 *
 */
public class IhmChallengeurMM implements
        ConstLigneSimple,
        ConstEvalPropale,
        EcrireSurEcran {

    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
    private Boolean modeDebug = (Boolean) getParam(MODE_DEBUG);
    private Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);


    private ArrayList<Integer> compositionChiffresSecrets;

    private LignePropaleMM[] lignesPropaleMM;
    private LigneSimpleMM[] lignesSimpleMM;

    private Character escapeChar = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);


    public IhmChallengeurMM(LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                            CouleursMastermind[] couleursSecretes, ValiderProposition fctValidePropale,
                            LigneSimpleMM[] lignesSimpleMM, LignePropaleMM[] lignesPropaleMM) {

        this.lignesSimpleMM = lignesSimpleMM;
        this.lignesPropaleMM = lignesPropaleMM;
    }


    public void runIhmMM(Scanner scanner, ProduirePropale getPropaleDef) {

        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        String patternInitial = FabPattSais.ConstruitPatternSaisie(CouleursMastermind.values(), escapeChar);

        ArrayList<Character> propaleChallengeur;

        int indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        while (!isEscape) {
            if (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

                lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

                propaleChallengeur = getPropaleDef.getPropaleChallengeur(scanner, patternInitial, escapeChar);

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
                propaleChallengeur = getPropaleDef.getPropaleChallengeur(scanner, FabPattSais.ConstruitPatternSaisie(escapeChar), escapeChar);
                if (propaleChallengeur.contains(escapeChar)) {
                    isEscape = true;
                }
            }
        }
    }
}

