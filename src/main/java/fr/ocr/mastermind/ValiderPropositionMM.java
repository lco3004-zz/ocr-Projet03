package fr.ocr.mastermind;

import java.util.ArrayList;

import static fr.ocr.utiles.Constantes.ConstEvalPropale.BLANC_MALPLACE;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.NOIR_BIENPLACE;


/**
 * <p>
 * @author Laurent Cordier
 *     méthode apply de validation d'une proposition : valeur par défaut : validation par calcul
 * <p>
 */
public interface ValiderPropositionMM {

    /**
     * méthode par defaut de comparaison d'une proposition avec la combinaison secrete
     * @param proposition liste de character , proposition à évaluer
     * @param secret   liste de charactes, combinaison secrete
     * @param nombreDePositions  , parametre nombre couleur par ligne (nombre de positions)
     * @param zoneEvaluation  tablraude 2 int, nombre de noirs, nombre de blancs - noirs == couleur bien placée
     * @return boolean, true si Proposition et combinaison secrete sont identiques
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
 *  evaluation d'une proposition à soumettre , parmi les propositons possibles , (algo du mode defenseur)
 */
class EvalPropaleParmiPossible implements ValiderPropositionMM {

    /**
     * apply
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

        return ValiderPropositionMM.super.apply(propaleJoueur, combinaisonSecrete, nombreDePositions, zoneEvaluation);
    }
}
/**
 * evaluation de la proposition soumise enmode Challenger
 */
class EvalPropaleChallengeur implements ValiderPropositionMM {

    /**
     * apply
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

        return ValiderPropositionMM.super.apply(propaleJoueur, combinaisonSecrete, nombreDePositions, zoneEvaluation);
    }
}

/**
 * evaluation de la proposition soumise en mode Defenseur
 */
class EvalPropaleDefenseur implements ValiderPropositionMM {

    /**
     * apply
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

/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */