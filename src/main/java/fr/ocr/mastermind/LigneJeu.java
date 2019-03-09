package fr.ocr.mastermind;

import fr.ocr.params.CouleursMastermind;

import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;


public class LigneJeu implements TypeDesLignes{

    private ArrayList<Character> zoneProposition = new ArrayList(256);
    private ArrayList<Integer> ZoneEvaluation = new ArrayList(256);

    private boolean disponible = true;
    private boolean visible = true;
    private String  rangDansTableJeu;

    private int typeDeLigne;


    private CouleursMastermind[] combinaisonCouleursSecretes;
    private ArrayList<Integer> combinaisonChiffresSecrets;

    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);


    public LigneJeu(CouleursMastermind[] secretCouleurs, ArrayList<Integer> secretChiffres, String rang, int typeligne) {
        combinaisonChiffresSecrets = secretChiffres;
        combinaisonCouleursSecretes = secretCouleurs;
        rangDansTableJeu=rang;
        typeDeLigne = typeligne;


    }

    ArrayList<Character> getZoneProposition() {
        return zoneProposition;
    }

    void setZoneProposition(ArrayList<Character> valPropale) {
        zoneProposition.clear();
        zoneProposition.add('[');
        zoneProposition.add(' ');
        for(int i = 0 ; i <valPropale.size()-1;i++) {
            zoneProposition.add(valPropale.get(i));
            zoneProposition.add(',');
            zoneProposition.add(' ');
        }
        zoneProposition.add(' ');
        zoneProposition.add(']');
    }

    ArrayList<Integer> getZoneEvaluation() {
        return ZoneEvaluation;
    }

    void setZoneEvaluation(ArrayList<Integer> zoneEvaluation) {
        ZoneEvaluation = zoneEvaluation;
    }



    int getTypeDeLigne() {
        return typeDeLigne;
    }

    void setTypeDeLigne(int typeDeLigne) {
        this.typeDeLigne = typeDeLigne;
    }

    Boolean isDisponible() {
        return disponible;
    }

    void isDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    Boolean isVisible() {
        return visible;
    }

    void isVisible(Boolean visible) {
        this.visible = visible;
    }

    private void EvalProposition(int i) {
    }

    public String toString() {
        return  String.format("| %d | %s",rangDansTableJeu,zoneProposition.toArray().toString());
    }
    public void Clear() {
        zoneProposition.clear();
        ZoneEvaluation.clear();

        zoneProposition.add('[');
        zoneProposition.add(' ');
        for(int i = 0 ; i <nombreDePositions;i++) {
            zoneProposition.add('-');
            zoneProposition.add(',');
            zoneProposition.add(' ');
        }
        zoneProposition.add(' ');
        zoneProposition.add(']');
    }
    public void Reset() {
        zoneProposition.clear();
        ZoneEvaluation.clear();
    }
}
