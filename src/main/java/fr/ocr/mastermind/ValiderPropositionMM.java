package fr.ocr.mastermind;

import java.util.ArrayList;

import static fr.ocr.utiles.Constantes.ConstEvalPropale.BLANC_MALPLACE;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.NOIR_BIENPLACE;


/**
 * @author Laurent Cordier
 * <p>
 * <p>
 */

/**
 * <p>
 *     méthode apply de validation d'une proposition : valeur par défaut : validation par calcul
 * <p>
 */
public interface ValiderPropositionMM {

    /**
     *
     * @param proposition
     * @param secret
     * @param nombreDePositions
     * @param zoneEvaluation
     * @return
     */
    default Boolean apply(ArrayList<Character> proposition,

                          ArrayList<Character> secret,

                          Integer nombreDePositions,

                          int[] zoneEvaluation) {
        int rangPropale;

        zoneEvaluation[NOIR_BIENPLACE] = 0;
        zoneEvaluation[BLANC_MALPLACE] = 0;

        for (Character couleurSec : secret) {
            rangPropale = proposition.indexOf(couleurSec);
            if (rangPropale >= 0) {
                if ((rangPropale == secret.indexOf(couleurSec))) {
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
class EvalPropaleParmiPossible implements ValiderPropositionMM {

    /**
     *
     * @param propaleJoueur
     * @param combinaisonSecrete
     * @param nombreDePositions
     * @param zoneEvaluation
     * @return
     */
    @Override
    public Boolean apply(ArrayList<Character> propaleJoueur,
                         ArrayList<Character> combinaisonSecrete,
                         Integer nombreDePositions,
                         int[] zoneEvaluation) {

        return ValiderPropositionMM.super.apply(propaleJoueur, combinaisonSecrete, nombreDePositions, zoneEvaluation);
    }
}
/**
 */
class EvalPropaleChallengeur implements ValiderPropositionMM {

    /**
     *
     * @param propaleJoueur
     * @param combinaisonSecrete
     * @param nombreDePositions
     * @param zoneEvaluation
     * @return
     */
    @Override
    public Boolean apply(ArrayList<Character> propaleJoueur,
                         ArrayList<Character> combinaisonSecrete,
                         Integer nombreDePositions,
                         int[] zoneEvaluation) {

        return ValiderPropositionMM.super.apply(propaleJoueur, combinaisonSecrete, nombreDePositions, zoneEvaluation);
    }
}

/**
 */
class EvalPropaleDefenseur implements ValiderPropositionMM {

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

        return (new EvalPropaleChallengeur()).apply(propaleJoueur, combinaisonSecrete, nombreDePositions, zoneEvaluation);
    }
}

