package fr.ocr.mastermind;

import java.util.ArrayList;

import static fr.ocr.utiles.Constantes.ConstEvalPropale.BLANC_MALPLACE;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.NOIR_BIENPLACE;


/**
 *@author Laurent Cordier
 *
 * méthode apply de validation d'une proposition : valeur par défaut : validation par calcul
 *
 */
public interface ControleProposition {

    /**
     * méthode par defaut de comparaison d'une proposition avec la combinaison secrete
     * @param p_proposition liste de character , proposition à évaluer
     * @param p_secret   liste de charactes, combinaison secrete
     * @param nombreDePositions  , parametre nombre couleur par ligne (nombre de positions)
     * @param zoneEvaluation  tablraude 2 int, nombre de noirs, nombre de blancs - noirs == couleur bien placée
     * @param isDoublon  boolean , vrai si les doublons sont autorises
     * @return boolean, true si Proposition et combinaison secrete sont identiques
     */
    default Boolean apply(ArrayList<Character> p_proposition,

                          ArrayList<Character> p_secret,

                          Integer nombreDePositions,

                          int[] zoneEvaluation,
                          boolean isDoublon) {
        int rangPropale;

        zoneEvaluation[NOIR_BIENPLACE] = 0;
        zoneEvaluation[BLANC_MALPLACE] = 0;

        if (isDoublon) {
            @SuppressWarnings("unchecked")
            ArrayList<Character> localProposition = (ArrayList<Character>) p_proposition.clone();

            @SuppressWarnings("unchecked")
            ArrayList<Character> localSecret = (ArrayList<Character>) p_secret.clone();

            for (int i = 0; i < localSecret.size(); i++) {
                for (int n = 0; n < localProposition.size(); n++) {
                    if (localSecret.get(i) == localProposition.get(n)) {
                        if (i == n) {
                            zoneEvaluation[NOIR_BIENPLACE]++;
                            localSecret.set(i, '.');
                            localProposition.set(n, ';');
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < localSecret.size(); i++) {
                for (int n = 0; n < localProposition.size(); n++) {
                    if (localSecret.get(i) == localProposition.get(n)) {
                        zoneEvaluation[BLANC_MALPLACE]++;
                        localSecret.set(i, ':');
                        localProposition.set(n, ',');
                        break;
                    }
                }
            }
        } else {
            for (Character couleurSec : p_secret) {
                rangPropale = p_proposition.indexOf(couleurSec);
                if (rangPropale >= 0) {
                    if ((rangPropale == p_secret.indexOf(couleurSec))) {
                        zoneEvaluation[NOIR_BIENPLACE]++;
                    } else {
                        zoneEvaluation[BLANC_MALPLACE]++;
                    }
                }
            }
        }
        return zoneEvaluation[NOIR_BIENPLACE] == nombreDePositions;
    }
}

/**
 *  evaluation d'une proposition à soumettre , parmi les propositons possibles , (algo du mode defenseur)
 */
class ScorerProposition implements ControleProposition {

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
                         int[] zoneEvaluation,
                         boolean isDoublon) {

        return ControleProposition.super.apply(propaleJoueur, combinaisonSecrete, nombreDePositions, zoneEvaluation, isDoublon);
    }
}

/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */