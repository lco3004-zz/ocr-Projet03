package fr.ocr.mastermind;

import java.util.LinkedList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;

public class jeuMM implements TypeDesLignes {
    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);

}
