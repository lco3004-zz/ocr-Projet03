package fr.ocr.mastermind;

import fr.ocr.utiles.Constantes;

import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;

@FunctionalInterface
public interface ObtenirPropaleDefenseur {
    ArrayList<Character> getPropaleDefenseur();

}


class ProduirePropaleDefenseur implements ObtenirPropaleDefenseur {
    /**
     * @return
     */
    public ArrayList<Character> getPropaleDefenseur() {

        ArrayList<Character> propositionOrdinateur = new ArrayList<>(256);

        int monCompteur = 1;
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