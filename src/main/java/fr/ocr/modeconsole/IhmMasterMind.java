package fr.ocr.modeconsole;


import fr.ocr.mastermind.ProduirePropale;
import fr.ocr.mastermind.ValiderProposition;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.ConstEvalPropale;
import fr.ocr.utiles.Constantes.ConstLignesMM;
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
        ConstLignesMM,
        ConstEvalPropale,
        EcrireSurEcran {


    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
    private Boolean modeDebug = (Boolean) getParam(MODE_DEBUG);
    private Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);


    private ArrayList<Integer> compositionChiffresSecrets;


    // lignes MM affichees par l'ihm. +5 pour les lignes d'infos (titre, ...)

    private LigneJeuMM[] lignesJeuMM;

    private LigneJeuMMProposition[] ligneJeuMMPropositions = new LigneJeuMMProposition[nombreDeEssaisMax];

    private int indexLignesJeuMM = 0, indexLignesProposition = 0;


    private Character escapeChar = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);

    /**
     *
     * @param modeDeJeu
     * @param chiffresSecrets
     * @param couleursSecretes
     * @param fctValidePropale
     */
    public IhmMasterMind(LibellesMenuSecondaire modeDeJeu,
                         ArrayList<Integer> chiffresSecrets,
                         CouleursMastermind[] couleursSecretes,
                         ValiderProposition fctValidePropale,
                         LigneJeuMM[] lignesJeuMM, boolean isSecretVisible) {

        this.lignesJeuMM = lignesJeuMM;
        PreparationMenu(modeDeJeu, chiffresSecrets, couleursSecretes, isSecretVisible);

        PrepareLignesPropositions(couleursSecretes, fctValidePropale);
    }


    /**
     *
     * @param couleursSecretes
     * @param validerProposition
     */
    void PrepareLignesPropositions(CouleursMastermind[] couleursSecretes, ValiderProposition validerProposition) {
        for (int k = 0, indexLignesJeuMM = LIGNE_PROPOSITION; k < nombreDeEssaisMax; k++, indexLignesJeuMM++) {

            ligneJeuMMPropositions[k] = new LigneJeuMMProposition(couleursSecretes,
                    compositionChiffresSecrets,
                    true,
                    true,
                    k,
                    LIGNE_PROPOSITION,
                    "", validerProposition);

            ligneJeuMMPropositions[k].Clear().setLibelleLigne();
            lignesJeuMM[indexLignesJeuMM] = ligneJeuMMPropositions[k];
        }
    }

    /**
     * @param modeDeJeu
     * @param chiffresSecrets
     * @param couleursSecretes
     */
    void PreparationMenu(LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                         CouleursMastermind[] couleursSecretes, boolean isSecretVisible) {

        compositionChiffresSecrets = chiffresSecrets;


        lignesJeuMM[TITRE] = new LigneJeuMM(true, true, TITRE, TITRE, modeDeJeu.toString());

        lignesJeuMM[LIGNE_STATUS] = new LigneJeuMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format("      ", modeDebug.toString()));

        lignesJeuMM[LIGNE_SECRETE] = new LigneJeuMM(true, false, LIGNE_SECRETE, LIGNE_SECRETE, " -------SECRET--------");
        lignesJeuMM[LIGNE_SECRETE].setLibelleLigne(couleursSecretes);

        if (modeDebug || isSecretVisible) {
            lignesJeuMM[LIGNE_SECRETE].setEstVisible(true);
        }

        String champBlancNoir;
        StringBuilder lesCroixEtVirgules = new StringBuilder(256);
        for (int nbPositions = 0; nbPositions < nombreDePositions; nbPositions++) {
            lesCroixEtVirgules.append(' ');
            lesCroixEtVirgules.append('x');
            lesCroixEtVirgules.append(',');
        }

        champBlancNoir = String.format(" ## [%s ] %c %c", lesCroixEtVirgules.substring(0, lesCroixEtVirgules.length() - 1), PIONS_BIENPLACES, PIONS_MALPLACES);
        lignesJeuMM[LIGNE_ENTETE] = new LigneJeuMM(true, true, LIGNE_ENTETE, LIGNE_ENTETE, champBlancNoir);

        lignesJeuMM[LIGNE_BLANCH01] = new LigneJeuMM(true, true, LIGNE_BLANCH01, LIGNE_BLANCH01, " ");
        lignesJeuMM[LIGNE_BLANCH02] = new LigneJeuMM(true, true, LIGNE_BLANCH02, LIGNE_BLANCH02, " ");

        Character c = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);

        lignesJeuMM[LIGNE_DE_SAISIE] = new LigneJeuMM(true, true, LIGNE_DE_SAISIE, LIGNE_DE_SAISIE, String.format(" Votre choix (%c : Retour): ", c));

    }

    /**
     * @param scanner
     */
    public void runIhmMMDefenseur(Scanner scanner, ProduirePropale getPropaleDef) {


        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        String patternEscape = ConstruitPatternSaisie(escapeChar);

        ArrayList<Character> propalOrdinateur;

        indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        lignesJeuMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("mettre coueleur saisie defenseur ici");

        while (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

            propalOrdinateur = getPropaleDef.getPropaleDefenseur();

            SecretTrouve = ligneJeuMMPropositions[indexLignesProposition++].setPropositionJoueur(propalOrdinateur).setZoneProposition().EvalProposition();

            nbreEssaisConsommes++;
        }

        if (SecretTrouve) {
            lignesJeuMM[LIGNE_STATUS].setLibelleLigne("!! Ordinateur Gagne !!");
        } else {
            lignesJeuMM[LIGNE_SECRETE].setLibelleLigne("!! Ordinateur Perd !!");
        }

        lignesJeuMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesJeuMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
        //
        // pour confirmation sortie du jeu , par le defenseur (sinon - pas d'affichage et retour direct au menu superieur
        // seule saise possible 'escapeChar'
        //
        IOConsole.LectureClavier(patternEscape, scanner, new EcrireSurEcran() {
            @Override
            public void Display() {
                for (int n = TITRE; n <= LIGNE_DE_SAISIE; n++) {
                    if (lignesJeuMM[n].isEstVisible()) {
                        if (n == LIGNE_DE_SAISIE) {
                            System.out.print(lignesJeuMM[n].toString());
                        } else {
                            System.out.println(lignesJeuMM[n].toString());
                        }
                    }
                }
            }
        }, escapeChar);
    }

    /**
     * @param scanner
     * @return
     */
    public void runIhmMMChallengeur(Scanner scanner, ProduirePropale getPropaleDef) {


        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        String patternInitial = ConstruitPatternSaisie(CouleursMastermind.values(), escapeChar);

        ArrayList<Character> propaleChallengeur;

        indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        lignesJeuMM[LIGNE_TOUTES_COULEURS] = new LigneJeuMM(true, true, LIGNE_TOUTES_COULEURS, LIGNE_TOUTES_COULEURS, " ");

        lignesJeuMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);


        while (!isEscape) {
            if (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

                lignesJeuMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesJeuMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

                propaleChallengeur = getPropaleDef.getPropaleChallengeur(scanner, patternInitial, escapeChar);

                if (propaleChallengeur.contains(escapeChar)) {
                    isEscape = true;
                } else {
                    SecretTrouve = ligneJeuMMPropositions[indexLignesProposition++].setPropositionJoueur(propaleChallengeur).setZoneProposition().EvalProposition();
                    nbreEssaisConsommes++;
                }
            } else {
                lignesJeuMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesJeuMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
                if (SecretTrouve) {
                    lignesJeuMM[LIGNE_STATUS].setLibelleLigne(" ----   VICTOIRE !!---");
                    lignesJeuMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");
                } else {
                    lignesJeuMM[LIGNE_SECRETE].setEstVisible(true);
                    lignesJeuMM[LIGNE_SECRETE].setLibelleLigne(String.format("-- Perdu. Soluce = %s", lignesJeuMM[LIGNE_SECRETE].getLibelleLigne()));
                    lignesJeuMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");
                }
                propaleChallengeur = getPropaleDef.getPropaleChallengeur(scanner, ConstruitPatternSaisie(escapeChar), escapeChar);
                if (propaleChallengeur.contains(escapeChar)) {
                    isEscape = true;
                }
            }
        }
    }

    /**
     *
     * @param colMM
     * @param escCape
     * @return
     */
    private String ConstruitPatternSaisie(Constantes.CouleursMastermind[] colMM, Character escCape) {
        StringBuilder listeInitialesColor = new StringBuilder(256);
        String s;
        listeInitialesColor.append('[');
        for (Constantes.CouleursMastermind v : colMM) {
            listeInitialesColor.append(v.getLettreInitiale());
            listeInitialesColor.append(' ');
            s = String.valueOf(v.getLettreInitiale()).toLowerCase(Locale.forLanguageTag("fr"));
            listeInitialesColor.append(s.toCharArray()[0]);
            listeInitialesColor.append(' ');
        }
        listeInitialesColor.append(escCape);
        listeInitialesColor.append(' ');
        s = String.valueOf(escCape).toLowerCase(Locale.forLanguageTag("fr"));
        listeInitialesColor.append(s.toCharArray()[0]);
        listeInitialesColor.append(']');
        return listeInitialesColor.toString();
    }

    /**
     * @param escCape
     * @return
     */
    private String ConstruitPatternSaisie(Character escCape) {
        StringBuilder listeInitialesColor = new StringBuilder(256);
        String s;
        listeInitialesColor.append('[');
        listeInitialesColor.append(' ');
        listeInitialesColor.append(escCape);
        listeInitialesColor.append(' ');
        s = String.valueOf(escCape).toLowerCase(Locale.forLanguageTag("fr"));
        listeInitialesColor.append(s.toCharArray()[0]);
        listeInitialesColor.append(']');
        return listeInitialesColor.toString();
    }


}

