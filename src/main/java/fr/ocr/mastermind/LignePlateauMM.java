package fr.ocr.mastermind;

import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;

import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
/**
 * ***************************************************************************************************************
 *
 * @author Laurent Cordier
 * <p>
 * <p>
 * ***************************************************************************************************************
 */


/**
 * ***************************************************************************************************************
 * <p>
 * <p>
 * ***************************************************************************************************************
 */
abstract class LignePlateauMM implements Constantes.ConstLigneSimple, Constantes.ConstEvalPropale {

    private boolean estDisponible;

    private boolean estVisible;

    private int rangDansTableJeu;

    private int typeDeLigne;

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    LignePlateauMM(boolean disponible, boolean visible, int rang, int typeLigne) {
        estDisponible = disponible;
        estVisible = visible;
        rangDansTableJeu = rang;
        typeDeLigne = typeLigne;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public abstract String toString();

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public boolean isEstDisponible() {
        return estDisponible;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public boolean isEstVisible() {
        return estVisible;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public void setEstVisible(boolean estVisible) {
        this.estVisible = estVisible;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public int getTypeDeLigne() {
        return typeDeLigne;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    int getRangDansTableJeu() {
        return rangDansTableJeu;
    }

}

/**
 * ***************************************************************************************************************
 *
 *
 * ***************************************************************************************************************
 */
class LigneSimpleMM extends LignePlateauMM {

    private String libelleLigne;

    private String libelleLigneOriginal;

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    LigneSimpleMM(boolean disponible,
                  boolean visible,
                  int rang,
                  int typeligne, String info) {

        super(disponible, visible, rang, typeligne);
        libelleLigne = info;
        libelleLigneOriginal = info;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public String getLibelleLigne() {
        return libelleLigne;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public LigneSimpleMM setLibelleLigne(String infos) {

        libelleLigne = infos;
        return this;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    LigneSimpleMM setLibelleLigne(Constantes.CouleursMastermind[] colMM) {
        return setLibelleLigne(colMM, colMM.length);
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public String getLibelleLigneOriginal() {
        return libelleLigneOriginal;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    LigneSimpleMM setLibelleLigne(Constantes.CouleursMastermind[] colMM, int nbCouleurs) {
        return setLibelleLigne(colMM, nbCouleurs, "Les Couleurs -> ");
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    LigneSimpleMM setLibelleLigne(Constantes.CouleursMastermind[] colMM, int nbCouleurs, String enTete) {

        StringBuilder listeToutesCol = new StringBuilder(256);
        listeToutesCol.append(enTete);
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

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public LigneSimpleMM Clear() {
        setLibelleLigne(libelleLigneOriginal);
        return this;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    @Override
    public String toString() {
        return getLibelleLigne();
    }
}

/**
 * ***************************************************************************************************************
 *
 *
 * ***************************************************************************************************************
 */
class LignePropaleMM extends LigneSimpleMM {

    private StringBuilder zoneProposition = new StringBuilder(256);

    private int[] zoneEvaluation = new int[2];

    private ArrayList<Character> propositionJoueur;

    private ValiderPropositionMM fctValideProposition;

    private ArrayList<Character> combinaisonInitialesSecretes;

    private ArrayList<Integer> combinaisonChiffresSecrets;

    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);


    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    LignePropaleMM(Constantes.CouleursMastermind[] secretCouleurs,
                   ArrayList<Integer> secretChiffres,
                   boolean disponible,
                   boolean visible,
                   int rang,
                   int typeligne,
                   String infos,
                   ValiderPropositionMM fct) {

        super(disponible, visible, rang, typeligne, infos);

        combinaisonChiffresSecrets = secretChiffres;

        combinaisonInitialesSecretes = new ArrayList<>(256);

        for (Constantes.CouleursMastermind couleursMastermind : secretCouleurs) {
            combinaisonInitialesSecretes.add(couleursMastermind.getLettreInitiale());
        }
        fctValideProposition = fct;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public LignePropaleMM setPropositionJoueur(ArrayList<Character> propositionJoueur) {
        this.propositionJoueur = propositionJoueur;
        return this;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public int[] getZoneEvaluation() {
        return zoneEvaluation;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public void setZoneEvaluation(int[] zoneEval) throws AppExceptions {
        if (zoneEval.length != zoneEvaluation.length) {
            logger.error(ERREUR_GENERIC.getMessageErreur());
            throw new AppExceptions(ERREUR_GENERIC);
        }
        System.arraycopy(zoneEval, 0, zoneEvaluation, 0, zoneEval.length);

    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    private String getZoneProposition() {
        return zoneProposition.toString();
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public LignePropaleMM setZoneProposition() {
        zoneProposition.delete(0, zoneProposition.length());
        zoneProposition.append('[');
        zoneProposition.append(' ');
        for (Character character : propositionJoueur) {
            zoneProposition.append(character);
            zoneProposition.append(',');
            zoneProposition.append(' ');
        }
        zoneProposition.deleteCharAt(zoneProposition.lastIndexOf(Character.toString(',')));
        zoneProposition.append(']');
        return this;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    public Boolean EvalProposition() {
        return fctValideProposition.apply(propositionJoueur,
                combinaisonInitialesSecretes,
                nombreDePositions,
                zoneEvaluation);
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    @Override
    public LignePropaleMM Clear() {
        zoneProposition.delete(0, zoneProposition.length());
        zoneProposition.append('[');
        zoneProposition.append(' ');
        for (int i = 0; i < nombreDePositions; i++) {
            zoneProposition.append('-');
            zoneProposition.append(',');
            zoneProposition.append(' ');
        }

        zoneProposition.deleteCharAt(zoneProposition.lastIndexOf(Character.toString(',')));
        zoneProposition.append(']');

        zoneEvaluation[0] = zoneEvaluation[1] = 0;

        return this;
    }

    /*
     * ***************************************************************************************************************
     *
     * ***************************************************************************************************************
     */
    @Override
    public String toString() {
        //  dans le plateau (xx - - - -  n b)
        return " " +
                String.format("%02d", getRangDansTableJeu()) +
                ' ' +
                getZoneProposition() +
                ' ' +
                getZoneEvaluation()[NOIR_BIENPLACE] +
                ' ' +
                getZoneEvaluation()[BLANC_MALPLACE];
    }

    void setLibelleLigne() {
        super.setLibelleLigne(this.toString());
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */

