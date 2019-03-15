package fr.ocr.modeconsole;

import fr.ocr.mastermind.ValidationPropale;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.CouleursMastermind;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;




/**
 *
 */
public class IhmMasterMind implements Constantes.ConstLignesMM, Constantes.ConstEvalPropale {


    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
    private Boolean modeDebug = (Boolean) getParam(MODE_DEBUG);
    private Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);

    private Constantes.Libelles.LibellesMenuSecondaire modeCourantDuJeu;


    private ArrayList<Integer> compositionChiffresSecrets;



    // lignes MM affichees par l'ihm. +5 pour les lignes d'infos (titre, ...)

    private LigneJeuMM[] lignesJeuMM = new LigneJeuMM[NBRE_LIGNESTABLEMM];

    private LigneJeuMMProposition[] ligneJeuMMPropositions = new LigneJeuMMProposition[nombreDeEssaisMax];

    private int indexLignesJeuMM = 0, indexLignesProposition = 0;


    /**
     * @param chiffresSecrets
     * @param couleursSecretes
     */
    public IhmMasterMind(Constantes.Libelles.LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                         CouleursMastermind[] couleursSecretes, ValidationPropale fctValidePropale) {


        IntStream intStream;
        Stream<Character> stream;
        ArrayList<Character> libelleLigne;


        compositionChiffresSecrets = chiffresSecrets;
        modeCourantDuJeu = modeDeJeu;

        lignesJeuMM[TITRE] = new LigneJeuMM(true, true, TITRE, TITRE, modeCourantDuJeu.toString());

        lignesJeuMM[LIGNE_STATUS] = new LigneJeuMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format("      ", modeDebug.toString()));

        lignesJeuMM[LIGNE_SECRETE] = new LigneJeuMM(true, false, LIGNE_SECRETE, LIGNE_SECRETE, " -------SECRET--------");
        lignesJeuMM[LIGNE_SECRETE].setLibelleLigne(couleursSecretes);

        if (modeDebug) {
            lignesJeuMM[LIGNE_SECRETE].setEstVisible(true);
        }

        String champBlancNoir;
        StringBuilder lesCroixEtVirgules = new StringBuilder(256);
        for (int nbPositions =0; nbPositions<nombreDePositions;nbPositions++) {
            lesCroixEtVirgules.append(' ');
            lesCroixEtVirgules.append('x');
            lesCroixEtVirgules.append(',');
        }

        champBlancNoir = String.format(" ## [%s ] %c %c", lesCroixEtVirgules.substring(0,lesCroixEtVirgules.length()-1),PIONS_BIENPLACES, PIONS_MALPLACES);
        lignesJeuMM[LIGNE_ENTETE] = new LigneJeuMM(true, true, LIGNE_ENTETE, LIGNE_ENTETE, champBlancNoir);

        lignesJeuMM[LIGNE_BLANCH01] = new LigneJeuMM(true, true, LIGNE_BLANCH01, LIGNE_BLANCH01, " ");
        lignesJeuMM[LIGNE_BLANCH02] = new LigneJeuMM(true, true, LIGNE_BLANCH02, LIGNE_BLANCH02, " ");

        lignesJeuMM[LIGNE_TOUTES_COULEURS] = new LigneJeuMM(true, true, LIGNE_TOUTES_COULEURS, LIGNE_TOUTES_COULEURS, " ");

        lignesJeuMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(),nombreDeCouleurs);

        Character c = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);

        lignesJeuMM[LIGNE_DE_SAISIE] = new LigneJeuMM(true, true, LIGNE_DE_SAISIE, LIGNE_DE_SAISIE, String.format(" Votre Proposition (%c : Retour): ", c));

        for (int k = 0, indexLignesJeuMM = LIGNE_PROPOSITION; k < nombreDeEssaisMax; k++, indexLignesJeuMM++) {

            ligneJeuMMPropositions[k] = new LigneJeuMMProposition(couleursSecretes,
                    compositionChiffresSecrets,
                    true,
                    true,
                    k,
                    LIGNE_PROPOSITION,
                    "", fctValidePropale);

            ligneJeuMMPropositions[k].Clear().setLibelleLigne();
            lignesJeuMM[indexLignesJeuMM] = ligneJeuMMPropositions[k];
        }
    }

    /**
     * @param scanner
     * @return
     */
    public void runIhmMM(Scanner scanner) {
        IOConsole.ClearScreen.cls();
        boolean SecretTrouve = false, isEscape = false;
        Integer nbreEssaisConsommes = 0;
        Character escapeChar = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);
        String patternInitial = ConstruitPatternSaisie(CouleursMastermind.values(), escapeChar);
        ArrayList<Character> propalDuJoueur;
        indexLignesProposition = 0;

        while (!isEscape) {
            if (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

                lignesJeuMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesJeuMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

                propalDuJoueur = SaisieProposition(scanner, patternInitial, () -> Display(), escapeChar);

                if (propalDuJoueur.contains(escapeChar)) {
                    isEscape = true;
                } else {
                    SecretTrouve = ligneJeuMMPropositions[indexLignesProposition++].setPropositionJoueur(propalDuJoueur).setZoneProposition().EvalProposition();
                    nbreEssaisConsommes++;
                }
            } else {
                lignesJeuMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesJeuMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
                if (SecretTrouve) {
                    lignesJeuMM[LIGNE_STATUS].setLibelleLigne(" ----   VICTOIRE !!---");
                    lignesJeuMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");
                }
                else {
                    lignesJeuMM[LIGNE_SECRETE].setEstVisible(true);
                    lignesJeuMM[LIGNE_SECRETE].setLibelleLigne(String.format("-- Perdu. Soluce = %s",lignesJeuMM[LIGNE_SECRETE].getLibelleLigne()));
                    lignesJeuMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");
                }
                propalDuJoueur = SaisieProposition(scanner, ConstruitPatternSaisie(escapeChar), () -> Display(), escapeChar);
                if (propalDuJoueur.contains(escapeChar)) {
                    isEscape = true;
                }
            }
        }
    }

    /**
     * @return
     */
    private ArrayList<Character> SaisieProposition(Scanner scanner, String pattern, Affichage fctDisplay, Character escChar) {
        ArrayList<Character> propositionJoueur = new ArrayList<>(256);
        try {

            Character saisieUneCouleur ;
            do {
                saisieUneCouleur = IOConsole.LectureClavier(pattern, scanner, fctDisplay, escChar);
                if (saisieUneCouleur != escChar) {
                    propositionJoueur.add(saisieUneCouleur);
                     String infosSasiie = lignesJeuMM[LIGNE_DE_SAISIE].getLibelleLigne() + saisieUneCouleur.toString() +" ";
                    lignesJeuMM[LIGNE_DE_SAISIE].setLibelleLigne(infosSasiie);
                    if (!doublonAutorise) {
                        int posCol = pattern.indexOf(saisieUneCouleur);
                        int taille = pattern.length();
                        pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
                        taille = pattern.length();
                        String pourLower =String.valueOf(saisieUneCouleur).toLowerCase(Locale.forLanguageTag("fr"));
                        posCol = pattern.indexOf(pourLower.toCharArray()[0]);
                        pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
                    }
                } else {
                    propositionJoueur.clear();
                    propositionJoueur.add(escChar);
                }
            }
            while ((saisieUneCouleur != escChar) && (propositionJoueur.size() < nombreDePositions));

        } catch (AppExceptions appExceptions) {
            appExceptions.printStackTrace();
            propositionJoueur.clear();
            propositionJoueur.add(escChar);
        }
        return propositionJoueur;
    }

    /**
     *
     */
    private void Display() {
        for (int n = TITRE; n <= LIGNE_DE_SAISIE; n++) {
            if (lignesJeuMM[n].isEstVisible()) {
                if (n == LIGNE_DE_SAISIE) {
                    System.out.print(lignesJeuMM[n].toString());
                }
                else {
                    System.out.println(lignesJeuMM[n].toString());
                }
            }
        }
    }

    /**
     * @param colMM
     * @return
     */
    private String ConstruitPatternSaisie(CouleursMastermind[] colMM, Character escCape) {
        StringBuilder listeInitialesColor = new StringBuilder(256);
        String s;
        listeInitialesColor.append('[');
        for (CouleursMastermind v : colMM) {
            listeInitialesColor.append(v.getLettreInitiale());
            listeInitialesColor.append(' ');
            s = String.valueOf(v.getLettreInitiale()).toLowerCase(Locale.forLanguageTag("fr"));
            listeInitialesColor.append(s.toCharArray()[0]);
            listeInitialesColor.append(' ');
        }
        listeInitialesColor.append(escCape);
        listeInitialesColor.append(' ');
        s = String.valueOf(escCape).toLowerCase(Locale.forLanguageTag("fr"));
        listeInitialesColor.append(s.toCharArray()[0]);
        listeInitialesColor.append(']');
        return listeInitialesColor.toString();
    }
    private String ConstruitPatternSaisie(Character escCape) {
        StringBuilder listeInitialesColor = new StringBuilder(256);
        String s;
        listeInitialesColor.append('[');
        listeInitialesColor.append(' ');
        listeInitialesColor.append(escCape);
        listeInitialesColor.append(' ');
        s = String.valueOf(escCape).toLowerCase(Locale.forLanguageTag("fr"));
        listeInitialesColor.append(s.toCharArray()[0]);
        listeInitialesColor.append(']');
        return listeInitialesColor.toString();
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
class LigneJeuMM extends LigneMasterMind {

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

    String getLibelleLigne() {
        return libelleLigne;
    }

    String getLibelleLigneOriginal() {
        return libelleLigneOriginal;
    }

    LigneJeuMM setLibelleLigne(String infos) {

        libelleLigne = infos;
        return this;
    }

    LigneJeuMM setLibelleLigne(CouleursMastermind[] colMM, int nbCouleurs) {

        StringBuilder listeToutesCol = new StringBuilder(256);
        listeToutesCol.append(" Les Couleurs -> ");
        int couleursUtilisees = 0 ;
        for (CouleursMastermind v : colMM) {
            if (couleursUtilisees < nbCouleurs) {
                listeToutesCol.append(v.getLettreInitiale());
                listeToutesCol.append(' ');
            }
            couleursUtilisees++;
        }
        libelleLigne = listeToutesCol.toString();
        return this;
    }

    LigneJeuMM setLibelleLigne(CouleursMastermind[] colMM) {
        setLibelleLigne(colMM,colMM.length);
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
class LigneJeuMMProposition extends LigneJeuMM {

    private StringBuilder zoneProposition = new StringBuilder(256);
    private int[] zoneEvaluation = new int[2];


    private ArrayList<Character> propositionJoueur;

    private ValidationPropale fctValideProposition;
    private ArrayList<Character> combinaisonInitialesSecretes;
    private ArrayList<Integer> combinaisonChiffresSecrets;

    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);


    LigneJeuMMProposition(CouleursMastermind[] secretCouleurs,
                          ArrayList<Integer> secretChiffres,
                          boolean disponible,
                          boolean visible,
                          int rang,
                          int typeligne,
                          String infos,
                          ValidationPropale fct) {

        super(disponible, visible, rang, typeligne, infos);

        combinaisonChiffresSecrets = secretChiffres;

        combinaisonInitialesSecretes = new ArrayList<>(256);

        for (CouleursMastermind couleursMastermind : secretCouleurs) {
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
        for (int i = 0; i <zoneEval.length  ; i++) {
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
