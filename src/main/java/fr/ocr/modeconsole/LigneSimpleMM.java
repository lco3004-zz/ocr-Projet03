package fr.ocr.modeconsole;

import fr.ocr.utiles.Constantes;

public class LigneSimpleMM extends LignePlateauMM {


    private String libelleLigne;
    private String libelleLigneOriginal;

    public LigneSimpleMM(boolean disponible,
                         boolean visible,
                         int rang,
                         int typeligne, String info) {

        super(disponible, visible, rang, typeligne);
        libelleLigne = info;
        libelleLigneOriginal = info;
    }

    public String getLibelleLigne() {
        return libelleLigne;
    }

    public LigneSimpleMM setLibelleLigne(String infos) {

        libelleLigne = infos;
        return this;
    }

    public LigneSimpleMM setLibelleLigne(Constantes.CouleursMastermind[] colMM) {
        setLibelleLigne(colMM, colMM.length);
        return this;
    }

    public String getLibelleLigneOriginal() {
        return libelleLigneOriginal;
    }

    public LigneSimpleMM setLibelleLigne(Constantes.CouleursMastermind[] colMM, int nbCouleurs) {

        StringBuilder listeToutesCol = new StringBuilder(256);
        listeToutesCol.append("Les Couleurs -> ");
        int couleursUtilisees = 0;
        for (Constantes.CouleursMastermind v : colMM) {
            if (couleursUtilisees < nbCouleurs) {
                listeToutesCol.append(v.getLettreInitiale());
                listeToutesCol.append(' ');
            }
            couleursUtilisees++;
        }
        libelleLigne = listeToutesCol.toString();
        return this;
    }

    public LigneSimpleMM Clear() {
        setLibelleLigne(libelleLigneOriginal);
        return this;
    }

    @Override
    public String toString() {
        return getLibelleLigne();
    }
}

