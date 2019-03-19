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

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;


/**
 *
 */
public class IhmMasterMind implements
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


    public IhmMasterMind(LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                         CouleursMastermind[] couleursSecretes, ValiderProposition fctValidePropale,
                         LigneSimpleMM[] lignesSimpleMM, LignePropaleMM[] lignesPropaleMM,
                         boolean isSecretVisible) {

        this.lignesSimpleMM = lignesSimpleMM;
        this.lignesPropaleMM = lignesPropaleMM;
    }


    public void runIhmMMDefenseur(Scanner scanner, ProduirePropale getPropaleDef) {


        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        String patternEscape = ConstruitPatternSaisie(escapeChar);

        ArrayList<Character> propalOrdinateur;

        int indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        while (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

            propalOrdinateur = getPropaleDef.getPropaleDefenseur();

            SecretTrouve = lignesPropaleMM[indexLignesProposition++].setPropositionJoueur(propalOrdinateur).setZoneProposition().EvalProposition();
            nbreEssaisConsommes++;
        }

        if (SecretTrouve) {
            lignesSimpleMM[LIGNE_STATUS].setLibelleLigne("!! Ordinateur Gagne !!");
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

    public void runIhmMMChallengeur(Scanner scanner, ProduirePropale getPropaleDef) {


        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        String patternInitial = ConstruitPatternSaisie(CouleursMastermind.values(), escapeChar);

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
                propaleChallengeur = getPropaleDef.getPropaleChallengeur(scanner, ConstruitPatternSaisie(escapeChar), escapeChar);
                if (propaleChallengeur.contains(escapeChar)) {
                    isEscape = true;
                }
            }
        }
    }


    private String ConstruitPatternSaisie(Constantes.CouleursMastermind[] colMM,
                                          Character escapeChar) {
        StringBuilder patternSaisie = new StringBuilder(256);
        String s;
        patternSaisie.append('[');
        for (Constantes.CouleursMastermind v : colMM) {
            patternSaisie.append(v.getLettreInitiale());
            patternSaisie.append(' ');
            s = String.valueOf(v.getLettreInitiale()).toLowerCase(Locale.forLanguageTag("fr"));
            patternSaisie.append(s.toCharArray()[0]);
            patternSaisie.append(' ');
        }
        patternSaisie.append(escapeChar);
        patternSaisie.append(' ');
        s = String.valueOf(escapeChar).toLowerCase(Locale.forLanguageTag("fr"));
        patternSaisie.append(s.toCharArray()[0]);
        patternSaisie.append(']');
        return patternSaisie.toString();
    }

    /**
     * @param escapeChar le pattern de saisie ne contient que le caractère d'échappement
     * @return String listeInitialesColor (pattern de saisie
     */
    private String ConstruitPatternSaisie(Character escapeChar) {
        StringBuilder patternSaisie = new StringBuilder(256);
        String s;
        patternSaisie.append('[');
        patternSaisie.append(' ');
        patternSaisie.append(escapeChar);
        patternSaisie.append(' ');
        s = String.valueOf(escapeChar).toLowerCase(Locale.forLanguageTag("fr"));
        patternSaisie.append(s.toCharArray()[0]);
        patternSaisie.append(']');
        return patternSaisie.toString();
    }

}

