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
public class IhmDefenseurMM implements
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


    public IhmDefenseurMM(LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                          CouleursMastermind[] couleursSecretes, ValiderProposition fctValidePropale,
                          LigneSimpleMM[] lignesSimpleMM, LignePropaleMM[] lignesPropaleMM) {

        this.lignesSimpleMM = lignesSimpleMM;
        this.lignesPropaleMM = lignesPropaleMM;
    }


    public void runIhmMM(Scanner scanner, ProduirePropale getPropaleDef) {


        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        String patternEscape = FabPattSais.ConstruitPatternSaisie(escapeChar);

        ArrayList<Character> propalOrdinateur;

        int indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        while (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

            propalOrdinateur = getPropaleDef.getPropaleDefenseur();

            SecretTrouve = lignesPropaleMM[indexLignesProposition].setPropositionJoueur(propalOrdinateur).setZoneProposition().EvalProposition();

            if (!SecretTrouve) {
                getPropaleDef.setScorePropale(lignesPropaleMM[indexLignesProposition].getZoneEvaluation());
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

}

