package ocr_projet03.modeConsole;

import java.util.HashMap;

public class LigneMenu <T extends Enum>{
    private String libelleLigne;
    private T referenceLibelle;
    private Character selecteur;



    public   LigneMenu (T  x, String s) {
        libelleLigne =s;
        referenceLibelle = x;
        selecteur='0';
    }
    public   LigneMenu (  T  x, String s,Character c) {
        libelleLigne =s;
        referenceLibelle = x;
        selecteur=c;

    }
    public String getLibelle_Ligne() {
        return libelleLigne;
    }

    public T getReferenceLibelle() {
        return referenceLibelle;
    }
    public Character getSelecteur() { return selecteur; }
}
