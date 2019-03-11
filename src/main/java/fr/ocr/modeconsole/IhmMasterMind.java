package fr.ocr.modeconsole;

import fr.ocr.mastermind.ValidationPropale;
import fr.ocr.modeconsole.Libelles.LibellesMenuSecondaire;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.CouleursMastermind;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;


/**
 *
 */
interface ConstLignesMM {
    //pour table de jeu - les diffrents types de lignes de la table de jeu
    int TITRE = 0;
    int LIGNE_STATUS = TITRE + 1;
    int LIGNE_SECRETE = LIGNE_STATUS + 1;
    int LIGNE_ENTETE = LIGNE_SECRETE + 1;
    int LIGNE_PROPOSITION = LIGNE_ENTETE + 1;
    int LIGNE_TOUTES_COULEURS = LIGNE_PROPOSITION + (Integer) getParam(NOMBRE_D_ESSAIS);
    int LIGNE_DE_SAISIE = LIGNE_TOUTES_COULEURS + 1;

    //dimesion
    int NBRE_LIGNESTABLEMM = LIGNE_DE_SAISIE + 1;

}

interface ConstEvalPropale {
    //pour résultat evaluation proposition : Noir == pion bien place,  blanc == pion mal placé
    int NOIR_BIENPLACE = 0;
    int BLANC_MALPLACE = NOIR_BIENPLACE + 1;
}

/**
 *
 */
public class IhmMasterMind implements ConstLignesMM, ConstEvalPropale {


    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
    private Boolean modeDebug = (Boolean) getParam(MODE_DEBUG);
    private Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);

    private LibellesMenuSecondaire modeCourantDuJeu;


    private ArrayList<Integer> compositionChiffresSecrets;
    private CouleursMastermind[] compositionCouleursSecretes;


    // lignes MM affichees par l'ihm. +5 pour les lignes d'infos (titre, ...)

    private LigneJeuMM[] lignesJeuMM = new LigneJeuMM[NBRE_LIGNESTABLEMM];

    private LigneJeuMMProposition[] ligneJeuMMPropositions = new LigneJeuMMProposition[nombreDeEssaisMax];

    private int indexLignesJeuMM = 0, indexLignesProposition = 0;


    /**
     * @param chiffresSecrets
     * @param couleursSecretes
     */
    public IhmMasterMind(LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                         CouleursMastermind[] couleursSecretes, ValidationPropale fctValidePropale) {


        IntStream intStream;
        Stream<Character> stream;
        ArrayList<Character> libelleLigne;

        compositionCouleursSecretes = couleursSecretes;
        compositionChiffresSecrets = chiffresSecrets;
        modeCourantDuJeu = modeDeJeu;

        lignesJeuMM[TITRE] = new LigneJeuMM(true, true, TITRE, TITRE, modeCourantDuJeu.toString());
        lignesJeuMM[LIGNE_STATUS] = new LigneJeuMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format(" --[ Debug=%s   ]---", modeDebug.toString()));
        lignesJeuMM[LIGNE_SECRETE] = new LigneJeuMM(true, true, LIGNE_SECRETE, LIGNE_SECRETE, " -------SECRET--------");

        if (modeDebug) {
            lignesJeuMM[LIGNE_SECRETE].setLibelleLigne(couleursSecretes);
        }

        lignesJeuMM[LIGNE_ENTETE] = new LigneJeuMM(true, true, LIGNE_ENTETE, LIGNE_ENTETE, " ## [ x, x, x, x ] B N");

        lignesJeuMM[LIGNE_TOUTES_COULEURS] = new LigneJeuMM(true, true, LIGNE_TOUTES_COULEURS, LIGNE_TOUTES_COULEURS, "");

        lignesJeuMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values());

        Character c = Libelles.CharactersEscape.K.toString().charAt(0);

        lignesJeuMM[LIGNE_DE_SAISIE] = new LigneJeuMM(true, true, LIGNE_DE_SAISIE, LIGNE_DE_SAISIE, String.format(" Votre Proposition (%c : Retour): ", c));

        for (int k = 0, indexLignesJeuMM = LIGNE_PROPOSITION; k < nombreDeEssaisMax; k++, indexLignesJeuMM++) {

            ligneJeuMMPropositions[k] = new LigneJeuMMProposition(compositionCouleursSecretes,
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
    public Boolean runIhmMM(Scanner scanner) {
        IOConsole.ClearScreen.cls();
        boolean SecretTrouve = false, isEscape = false;
        Integer nbreEssaisConsommes = 0;
        Character escapeChar = Libelles.CharactersEscape.K.toString().charAt(0);
        String patternInitial = ConstruitPatternSaisie(CouleursMastermind.values(), escapeChar);
        ArrayList<Character> propalDuJoueur;
        indexLignesProposition = 0;

        while (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax && !isEscape) {
            lignesJeuMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesJeuMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

            propalDuJoueur = SaisieProposition(scanner, patternInitial, () -> Display(), escapeChar);
            if (propalDuJoueur.contains(escapeChar)) {
                isEscape = true;
            } else {
                ligneJeuMMPropositions[indexLignesProposition++].setPropositionJoueur(propalDuJoueur).setZoneProposition();
                nbreEssaisConsommes++;
            }

        }
        return true;
    }

    /**
     * @return
     */
    private ArrayList<Character> SaisieProposition(Scanner scanner, String pattern, Affichage fctDisplay, Character escChar) {
        ArrayList<Character> propositionJoueur = new ArrayList<Character>(256);
        try {

            Character saisieUneCouleur = escChar;
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
            if (n == LIGNE_DE_SAISIE) {
                System.out.print(lignesJeuMM[n].toString());
            }
            else {
                System.out.println(lignesJeuMM[n].toString());
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
}

/**
 *
 */
abstract class LigneMasterMind implements ConstLignesMM, ConstEvalPropale {
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

    void setLibelleLigne(String infos) {
        libelleLigne = infos;
    }

    void setLibelleLigne(CouleursMastermind[] colMM) {

        StringBuilder listeToutesCol = new StringBuilder(256);
        for (CouleursMastermind v : colMM) {
            listeToutesCol.append(v.getLettreInitiale());
            listeToutesCol.append(' ');
        }

        libelleLigne = listeToutesCol.toString();
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
    private int[] ZoneEvaluation = new int[2];


    private ArrayList<Character> propositionJoueur;

    private ValidationPropale fctValideProposition;
    private ArrayList<Character> combinaisonInitialesSecretes;
    private ArrayList<Integer> combinaisonChiffresSecrets;


    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);


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
        return ZoneEvaluation;
    }

    public void setZoneEvaluation(int[] zoneEvaluation) {
        ZoneEvaluation = zoneEvaluation;
    }

    private String getZoneProposition() {

        return zoneProposition.toString();
    }

    void setZoneProposition() {
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
    }

    Boolean EvalProposition(int i) {
        return fctValideProposition.apply(propositionJoueur, combinaisonInitialesSecretes);
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

        ZoneEvaluation[0] = ZoneEvaluation[1] = 0;

        return this;
    }

    @Override
    public String toString() {
        //  dans le plateau (xx - - - -  n b)
        String valRet = " " +
                String.format("%02d", getRangDansTableJeu()) +
                ' ' +
                getZoneProposition() +
                ' ' +
                getZoneEvaluation()[0] +
                ' ' +
                getZoneEvaluation()[1];
        return valRet;
    }

    void setLibelleLigne() {
        super.setLibelleLigne(this.toString());
    }
}
