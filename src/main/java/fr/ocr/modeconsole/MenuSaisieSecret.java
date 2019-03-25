package fr.ocr.modeconsole;


import fr.ocr.utiles.Constantes;

import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;

/**
 * <p>
 * @author laurent cordier
 * MOOK saisie clavier combinaisonSecrete secret MM
 * </p>
 */
public class MenuSaisieSecret {

    public ArrayList<Integer> saisirCombinaisonSecrete() {
        ArrayList<Integer> combinaisonSecrete = new ArrayList<>(256);
        int monCompteur = 0;
        for (Constantes.CouleursMastermind v : Constantes.CouleursMastermind.values()) {
            combinaisonSecrete.add(v.getValeurFacialeDeLaCouleur());
            Integer nbPositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
            if (monCompteur == nbPositions - 1)
                break;
            monCompteur++;
        }
        return combinaisonSecrete;
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */