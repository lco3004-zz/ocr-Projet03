package fr.ocr.modeconsole;

import fr.ocr.utiles.ValiderProposition;

import java.util.ArrayList;

@FunctionalInterface
public interface ValiderPropositionDefenseur extends ValiderProposition {

    Boolean apply(ArrayList<Character> proposition,
                  ArrayList<Character> secret,
                  Integer nombreDePositions,
                  int[] zoneEvaluation);
}


/**
 *
 */
class EvalDefenseurChallengeurMM implements ValiderPropositionDefenseur {

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
