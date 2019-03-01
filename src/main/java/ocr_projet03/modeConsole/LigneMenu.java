package ocr_projet03.modeConsole;

import java.util.HashMap;

public class LigneMenu <T extends Enum>{
    private String libelleLigne;
    private T referenceLibelle;


    public   LigneMenu (  T  x, String s) {
        libelleLigne =s;
        referenceLibelle = x;
    }

    public String getLibelle_Ligne() {
        return libelleLigne;
    }

    public T getReferenceLibelle() {
        return referenceLibelle;
    }
}
