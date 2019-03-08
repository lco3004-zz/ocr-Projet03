package fr.ocr.params.mastermind;

import fr.ocr.params.UnParam;

public enum GroupParamsMM {
    NOMBRE_DE_POSITIONS(4, 3, 6),
    NOMBRE_DE_COULEURS(CouleursMastermind.values().length, 6, CouleursMastermind.values().length),
    NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE(100, 10, 500),
    NOMBRE_D_ESSAIS(12, 8, 12),
    DOUBLON_AUTORISE(false),
    CASE_VIDE_AUTORISE(false);


    private UnParam unParam;

    GroupParamsMM(Integer valdef, Integer valmin, Integer valmax) {
        unParam = new UnParam<>(valdef, Integer.class.getSimpleName(), valmin, valmax);
    }

    GroupParamsMM(Boolean valdef) {
        unParam = new UnParam<>(valdef, Boolean.class.getSimpleName());
    }

    public UnParam getUnParam() {
        return unParam;
    }


}
