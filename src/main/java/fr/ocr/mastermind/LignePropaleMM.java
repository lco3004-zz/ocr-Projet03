package fr.ocr.mastermind;

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
public class LignePropaleMM extends LigneSimpleMM {

    private StringBuilder zoneProposition = new StringBuilder(256);
    private int[] zoneEvaluation = new int[2];


    private ArrayList<Character> propositionJoueur;

    private ValiderProposition fctValideProposition;
    private ArrayList<Character> combinaisonInitialesSecretes;
    private ArrayList<Integer> combinaisonChiffresSecrets;

    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);


    LignePropaleMM(Constantes.CouleursMastermind[] secretCouleurs,
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

    public LignePropaleMM setPropositionJoueur(ArrayList<Character> propositionJoueur) {
        this.propositionJoueur = propositionJoueur;
        return this;
    }

    public int[] getZoneEvaluation() {
        return zoneEvaluation;
    }

    public void setZoneEvaluation(int[] zoneEval) throws AppExceptions {
        if (zoneEval.length != zoneEvaluation.length) {
            logger.error(ERREUR_GENERIC.getMessageErreur());
            throw new AppExceptions(ERREUR_GENERIC);
        }
        System.arraycopy(zoneEval, 0, zoneEvaluation, 0, zoneEval.length);

        /*
        for (int i = 0; i < zoneEval.length; i++) {
            zoneEvaluation[i] = zoneEval[i];
        }
         */
    }

    private String getZoneProposition() {

        return zoneProposition.toString();
    }

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

    public Boolean EvalProposition() {
        return fctValideProposition.apply(propositionJoueur,
                combinaisonInitialesSecretes,
                nombreDePositions,
                zoneEvaluation);
    }

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

