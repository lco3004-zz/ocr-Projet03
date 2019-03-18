package fr.ocr.lesjeux;

import java.util.ArrayList;

import static fr.ocr.utiles.Constantes.ConstEvalPropale.BLANC_MALPLACE;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.NOIR_BIENPLACE;

public class EvalChallengeurMM implements ValidationPropale {

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