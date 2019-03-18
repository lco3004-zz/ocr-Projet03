package fr.ocr.modeconsole;

import fr.ocr.mastermind.ValiderProposition;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;

import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.DOUBLON_AUTORISE;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

/**
 *
 */
public class LigneJeuMM extends LigneMasterMind {

    private String libelleLigne;
    private String libelleLigneOriginal;

    LigneJeuMM(boolean disponible,
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

    public LigneJeuMM setLibelleLigne(String infos) {

        libelleLigne = infos;
        return this;
    }

    LigneJeuMM setLibelleLigne(Constantes.CouleursMastermind[] colMM) {
        setLibelleLigne(colMM, colMM.length);
        return this;
    }

    String getLibelleLigneOriginal() {
        return libelleLigneOriginal;
    }

    LigneJeuMM setLibelleLigne(Constantes.CouleursMastermind[] colMM, int nbCouleurs) {

        StringBuilder listeToutesCol = new StringBuilder(256);
        listeToutesCol.append(" Les Couleurs -> ");
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

    LigneJeuMM Clear() {
        setLibelleLigne(libelleLigneOriginal);
        return this;
    }

    @Override
    public String toString() {
        return getLibelleLigne();
    }
}

/**
 *
 */
abstract class LigneMasterMind implements Constantes.ConstLignesMM, Constantes.ConstEvalPropale {
    private boolean estDisponible;
    private boolean estVisible;
    private int rangDansTableJeu;
    private int typeDeLigne;

    LigneMasterMind(boolean disponible, boolean visible, int rang, int typeLigne) {
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


/**
 *
 */
class LigneJeuMMProposition extends LigneJeuMM {

    private StringBuilder zoneProposition = new StringBuilder(256);
    private int[] zoneEvaluation = new int[2];


    private ArrayList<Character> propositionJoueur;

    private ValiderProposition fctValideProposition;
    private ArrayList<Character> combinaisonInitialesSecretes;
    private ArrayList<Integer> combinaisonChiffresSecrets;

    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);


    LigneJeuMMProposition(Constantes.CouleursMastermind[] secretCouleurs,
                          ArrayList<Integer> secretChiffres,
                          boolean disponible,
                          boolean visible,
                          int rang,
                          int typeligne,
                          String infos,
                          ValiderProposition fct) {

        super(disponible, visible, rang, typeligne, infos);

        combinaisonChiffresSecrets = secretChiffres;

        combinaisonInitialesSecretes = new ArrayList<>(256);

        for (Constantes.CouleursMastermind couleursMastermind : secretCouleurs) {
            combinaisonInitialesSecretes.add(couleursMastermind.getLettreInitiale());
        }
        fctValideProposition = fct;

    }

    LigneJeuMMProposition setPropositionJoueur(ArrayList<Character> propositionJoueur) {
        this.propositionJoueur = propositionJoueur;
        return this;
    }

    private int[] getZoneEvaluation() {
        return zoneEvaluation;
    }

    public void setZoneEvaluation(int[] zoneEval) throws AppExceptions {
        if (zoneEval.length != zoneEvaluation.length) {
            logger.error(ERREUR_GENERIC.getMessageErreur());
            throw new AppExceptions(ERREUR_GENERIC);
        }
        for (int i = 0; i < zoneEval.length; i++) {
            this.zoneEvaluation[i] = zoneEval[i];
        }
    }

    private String getZoneProposition() {

        return zoneProposition.toString();
    }

    LigneJeuMMProposition setZoneProposition() {
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

    Boolean EvalProposition() {
        return fctValideProposition.apply(propositionJoueur,
                combinaisonInitialesSecretes,
                nombreDePositions,
                zoneEvaluation);
    }

    @Override
    LigneJeuMMProposition Clear() {
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

