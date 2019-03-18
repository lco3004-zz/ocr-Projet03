package fr.ocr.mastermind;

import java.util.ArrayList;

import static fr.ocr.utiles.Constantes.ConstEvalPropale.BLANC_MALPLACE;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.NOIR_BIENPLACE;


class EvalChallengeurMM implements ValiderPropositionMM {

    @Override
    public Boolean apply(ArrayList<Character> propaleJoueur,
                         ArrayList<Character> combinaisonSecrete,
                         Integer nombreDePositions,
                         int[] zoneEvaluation) {

        int rangPropale;

        zoneEvaluation[NOIR_BIENPLACE] = 0;
        zoneEvaluation[BLANC_MALPLACE] = 0;

        for (Character couleurSec : combinaisonSecrete) {
            rangPropale = propaleJoueur.indexOf(couleurSec);
            if (rangPropale >= 0) {
                if ((rangPropale == combinaisonSecrete.indexOf(couleurSec))) {
                    zoneEvaluation[NOIR_BIENPLACE]++;
                } else {
                    zoneEvaluation[BLANC_MALPLACE]++;
                }
            }
        }
        return zoneEvaluation[NOIR_BIENPLACE] == nombreDePositions;
    }
}


/**
 *
 */
class EvalDefenseurMM implements ValiderPropositionMM {

    /**
     * @param propaleJoueur      P, la proposition du joueur
     * @param combinaisonSecrete S, la combinaison secrete à trouver
     * @param nombreDePositions  , le nombre positions 'emplacement de pions) ur une ligne du jeu
     * @param zoneEvaluation,    tableau entier de taille 2 qui contient le nombre de Blancs (mal placés) et le nombre de Noirs (bien placés)
     * @return si P est égal à S  , fin de partie donc la méthode répond true (false sinon)
     */
    @Override
    public Boolean apply(ArrayList<Character> propaleJoueur,
                         ArrayList<Character> combinaisonSecrete,
                         Integer nombreDePositions,
                         int[] zoneEvaluation) {


        return false;
    }
}

