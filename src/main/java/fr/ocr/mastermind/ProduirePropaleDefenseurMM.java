package fr.ocr.mastermind;

import fr.ocr.utiles.Constantes;

import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;

public class ProduirePropaleDefenseurMM implements ObtenirPropaleDefenseurMM {
    /**
     * @return
     */
    public ArrayList<Character> getPropaleDefenseur() {
        ArrayList<Character> propositionEnLettre = new ArrayList<>(256);
        ArrayList<Integer> propositionEnChiffre = new ArrayList<>(256);

        int monCompteur = 0;
        for (Constantes.CouleursMastermind v : Constantes.CouleursMastermind.values()) {
            propositionEnChiffre.add(v.getValeurFacialeDeLaCouleur());
            Integer nbPositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
            if (monCompteur == nbPositions - 1)
                break;
            monCompteur++;
        }


        return propositionEnLettre;
    }
}
