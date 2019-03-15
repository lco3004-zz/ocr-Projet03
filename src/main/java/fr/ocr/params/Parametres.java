package fr.ocr.params;


import fr.ocr.utiles.Constantes;

public enum Parametres {
    NOMBRE_DE_POSITIONS(4, 4, 6),
    NOMBRE_DE_COULEURS(8, 8, Constantes.CouleursMastermind.values().length),
    NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE(100, 10, 500),
    NOMBRE_D_ESSAIS(8, 4, 12),
    DOUBLON_AUTORISE(false),
    CASE_VIDE_AUTORISE(false),
    MODE_DEBUG(false);


    private UnParam unParam;

    Parametres(Integer valdef, Integer valmin, Integer valmax) {
        unParam = new UnParam<>(valdef, Integer.class.getSimpleName(), valmin, valmax);
    }

    Parametres(Boolean valdef) {
        unParam = new UnParam<>(valdef, Boolean.class.getSimpleName());
    }

    public String getTypeParam() {

        return unParam.getTypeParam();
    }

    public Number getValeurMax() {

        return unParam.getValeurMax();
    }

    public Number getValeurMin() {

        return unParam.getValeurMin();
    }

    public <T> Object getValeurDefaut() {
        return unParam.getValeurDefaut();
    }
}

class UnParam<T> {

    private T valeurDefaut;
    private String typeParam;
    private Number valeurMin;
    private Number valeurMax;

    UnParam(T valdef, String type) {

        typeParam = type;
        valeurDefaut = valdef;
    }

    UnParam(T valdef, String type, Integer valMin, Integer valMax) {

        typeParam = type;
        valeurDefaut = valdef;
        valeurMin = valMin;
        valeurMax = valMax;
    }

    T getValeurDefaut() {
        return valeurDefaut;
    }

    String getTypeParam() {
        return typeParam;
    }

    Number getValeurMax() {
        return valeurMax;
    }

    Number getValeurMin() {
        return valeurMin;
    }
}
