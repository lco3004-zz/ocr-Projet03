package fr.ocr.mastermind;

import fr.ocr.utiles.Constantes;

public abstract class LignePlateauMM implements Constantes.ConstLigneSimple, Constantes.ConstEvalPropale {
    private boolean estDisponible;
    private boolean estVisible;
    private int rangDansTableJeu;
    private int typeDeLigne;

    LignePlateauMM(boolean disponible, boolean visible, int rang, int typeLigne) {
        estDisponible = disponible;
        estVisible = visible;
        rangDansTableJeu = rang;
        typeDeLigne = typeLigne;
    }

    public abstract String toString();

    public boolean isEstDisponible() {
        return estDisponible;
    }

    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    public boolean isEstVisible() {
        return estVisible;
    }

    public void setEstVisible(boolean estVisible) {
        this.estVisible = estVisible;
    }

    public int getTypeDeLigne() {
        return typeDeLigne;
    }

    int getRangDansTableJeu() {
        return rangDansTableJeu;
    }

}

