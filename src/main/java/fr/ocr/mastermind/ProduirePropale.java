package fr.ocr.mastermind;

import fr.ocr.modeconsole.EcrireSurEcran;
import fr.ocr.modeconsole.IOConsole;
import fr.ocr.modeconsole.LignePropaleMM;
import fr.ocr.modeconsole.LigneSimpleMM;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.DOUBLON_AUTORISE;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static fr.ocr.utiles.Constantes.ConstLigneSimple.LIGNE_DE_SAISIE;
import static fr.ocr.utiles.Constantes.ConstLigneSimple.TITRE;


/**
 *
 */
public interface ProduirePropale {

    default ArrayList<Character> getPropaleDefenseur() {
        return null;
    }

    default ArrayList<Character> getPropaleChallengeur(Scanner scanner, String pattern, Character escChar) {
        return null;
    }
}

/**
 *
 */
class ProduirePropaleDefenseur implements ProduirePropale {

    public ArrayList<Character> getPropaleDefenseur() {

        ArrayList<Character> propositionOrdinateur = new ArrayList<>(256);

        int monCompteur = 0;
        Integer nbPositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
        for (Constantes.CouleursMastermind v : Constantes.CouleursMastermind.values()) {
            propositionOrdinateur.add(v.getLettreInitiale());
            if (monCompteur == nbPositions - 1)
                break;
            monCompteur++;
        }
        return propositionOrdinateur;
    }
}

/**
 *
 */
class ProduirePropaleChallengeur implements ProduirePropale {

    private LignePropaleMM[] lignesPropaleMM;
    private LigneSimpleMM[] lignesSimpleMM;

    ProduirePropaleChallengeur(LigneSimpleMM[] lignesSimpleMM, LignePropaleMM[] lignesPropaleMM) {
        this.lignesSimpleMM = lignesSimpleMM;
        this.lignesPropaleMM = lignesPropaleMM;
    }

    public ArrayList<Character> getPropaleChallengeur(Scanner scanner, String pattern, Character escChar) {
        ArrayList<Character> propositionJoueur = new ArrayList<>(256);
        Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
        Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);

        try {

            Character saisieUneCouleur;
            do {
                saisieUneCouleur = IOConsole.LectureClavier(pattern, scanner, new EcrireSurEcran() {
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
                }, escChar);

                if (saisieUneCouleur != escChar) {
                    propositionJoueur.add(saisieUneCouleur);
                    String infosSasiie = lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigne() + saisieUneCouleur.toString() + " ";
                    lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(infosSasiie);
                    if (!doublonAutorise) {
                        int posCol = pattern.indexOf(saisieUneCouleur);
                        int taille = pattern.length();
                        pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
                        taille = pattern.length();
                        String pourLower = String.valueOf(saisieUneCouleur).toLowerCase(Locale.forLanguageTag("fr"));
                        posCol = pattern.indexOf(pourLower.toCharArray()[0]);
                        pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
                    }
                } else {
                    propositionJoueur.clear();
                    propositionJoueur.add(escChar);
                }
            }
            while ((saisieUneCouleur != escChar) && (propositionJoueur.size() < nombreDePositions));

        } catch (AppExceptions appExceptions) {
            appExceptions.printStackTrace();
            propositionJoueur.clear();
            propositionJoueur.add(escChar);
        }
        return propositionJoueur;
    }
}