package ocr_projet03.modeConsole;

public class LigneMenu <T extends Enum>{
    private String libelleLigne;
    private T referenceLibelle;
    private Character selecteur;



    public   LigneMenu (T  x, String s) {
        libelleLigne =s;
        referenceLibelle = x;
        selecteur=null;
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

    public void setLibelleLigne(String libelleLigne) {
        this.libelleLigne = libelleLigne;
    }
}
