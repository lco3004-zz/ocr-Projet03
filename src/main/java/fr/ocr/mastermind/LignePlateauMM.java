package fr.ocr.mastermind;

import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.ConstEvalPropale;
import fr.ocr.utiles.Constantes.ConstTypeDeLigne;
import fr.ocr.utiles.Constantes.CouleursMastermind;

import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static fr.ocr.utiles.Constantes.ConstTailleStringBuilder.TAIILE_INITIALE;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
/**
 * @author Laurent Cordier
 * <p>
 *     modele des lignes de la table de jeu Mastermind
 * <p>
 */


/**
 * <p>
 *     Attribut et comportement généraux d'une ligne
 *     estDisponible : toujours vrai - reservé pour mise en place d'un rollback
 *     estVisible : est utilisé pour affichage oui/non de la ligne qui affiche (ou pas) la combinaison secrete
 *     rangDans le tableau : explicite
 *     typeDeligne : valeur prise parmi  Constantes.ConstTypeDeLigne
 * <p>
 */
abstract class LignePlateauMM implements ConstTypeDeLigne, ConstEvalPropale {

    private boolean estDisponible;

    private boolean estVisible;

    private int rangDansTableJeu;

    private int typeDeLigne;

    /*
     * Construteur
     * @param disponible
     * @param visible
     * @param rang
     * @param typeLigne
     */
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

/**
 *<p>
 *     ligne simple d'affichage
 *</p>
 */
class LigneMM extends LignePlateauMM {

    private String libelleLigne;

    private String libelleLigneOriginal;

    /*
     * Constructeur
     *
     * @param disponible   boolean  : à true ligne est disponible pour recevoir une proposition (true par defaut)
     * @param visible      boolean  : à true ligne s'affiche.
     * @param rang         int : rang de cette ligne dans le tableau des lignes
     * @param typeligne    int  type de ligne (simple )
     * @param info          String valeur par defaut qui sera affichée pour cette ligne
     */
    LigneMM(boolean disponible, boolean visible, int rang, int typeligne, String info) {

        super(true, visible, rang, typeligne);

        libelleLigne = info;

        libelleLigneOriginal = info;
    }

    /**
     * retourne l'attibut libelleLigne
     * @return String,  l'attribut libelleLigne
     */
    public String getLibelleLigne() {
        return libelleLigne;
    }

    /**
     * renseigne l'attribut libelleLigne
     * @param infos  : String qui sera affichée, pour cette ligne
     * @return   :  LigneMM,  l'objet ligne (this)  ! utilie pour chainage de méthode
     */
    public LigneMM setLibelleLigne(String infos) {

        libelleLigne = infos;

        return this;
    }

    /**
     * renseigne l'attribut libelleLigne
     * @param colMM   CouleursMastermind[] , liste de couleur qui sera affichée, pour cette ligne
     * @return LigneMM,  l'objet ligne (this)  ! utilie pour chainage de méthode
     */
    LigneMM setLibelleLigne(CouleursMastermind[] colMM) {
        return setLibelleLigne(colMM, colMM.length);
    }

    /**
     * renseigne l'attribut libelleLigne
     * @param colMM CouleursMastermind[] , liste de couleur qui sera affichée, pour cette ligne
     * @param nbCouleurs int, nombre de couleurs utilisées pour jouer (parametrage du jeu)
     *                   c'est donc un sous-ensemble de l'ensemble des couleurs déclarées dans le code source (Enum)
     * @return LigneMM,  l'objet ligne (this)  ! utilie pour chainage de méthode
     */
    LigneMM setLibelleLigne(CouleursMastermind[] colMM, int nbCouleurs) {
        return setLibelleLigne(colMM, nbCouleurs, "Les Couleurs -> ");
    }

    /**
     *renseigne l'attribut  libelleLigne
     *
     * @param colMM     CouleursMastermind[] , liste de couleur qui sera affichée, pour cette ligne
     * @param nbCouleurs  int, nombre de couleurs utilisées pour jouer (parametrage du jeu)
     *                        c'est donc un sous-ensemble de l'ensemble des couleurs déclarées dans le code source (Enum)
     * @param enTete    String , libelle à placer avant le contenu de colMM
     * @return LigneMM,  l'objet ligne (this)  ! utilie pour chainage de méthode
     */
    LigneMM setLibelleLigne(CouleursMastermind[] colMM, int nbCouleurs, String enTete) {

        StringBuilder listeToutesCol = new StringBuilder(TAIILE_INITIALE);

        listeToutesCol.append(enTete);

        int couleursUtilisees = 0;

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

    /**
     * retourne l'attibut libelleLigneOriginal
     * @return String,  l'attribut libelleLigneOriginal
     */
    public String getLibelleLigneOriginal() {
        return libelleLigneOriginal;
    }

    /**
     *  efface la ligne courante en remettant son libelle par defaut
     *   @return LigneMM,  l'objet ligne (this)  ! utilie pour chainage de méthode
     */
    public LigneMM Clear() {
        setLibelleLigne(libelleLigneOriginal);
        return this;
    }

    /**
     * renvoie l'attribut libelleLigne de la ligne courante
     */
    @Override
    public String toString() {
        return getLibelleLigne();
    }
}

/**
 *<p>
 *     ligne d'affichaget et de validation (score) d'une proposition
 *</p>
 */
class LignePropaleMM extends LigneMM {

    //la proposition qui sera fournie par odinateur ou joueur selon le mode du jeu
    private ArrayList<Character> propositionJoueur;

    // pour affichage de la proposition
    private StringBuilder zoneProposition;

    // tableau de score" Blanc" et "Noir"
    private int[] zoneEvaluation = new int[2];

    //méthode de validation de la proposition (methode fournit en parametre lors de la construction de la ligne
    private ValiderPropositionMM fctValideProposition;

    //le secret auquel il faut comparer la proposition, sous forme char (initiale de la couleur . ie V , initiale de Vert)
    private ArrayList<Character> combinaisonInitialesSecretes;

    //le secret sous forme de chiffre
    private ArrayList<Integer> combinaisonChiffresSecrets;

    //nombre de positions dans la ligne : nombre de couleur par ligne de proposition
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);

    /**
     * Constructeur
     * @param secretCouleurs   CouleursMastermind[] , combinaison secrete , dans sa forme Couleur (i.e VERT,JAUNE,...)
     * @param secretChiffres   ArrayList d'Integer combinaison secrete dans sa forme en chiffre (i.e 1,8, 4..)
     * @param disponible       boolean true (reserve)
     * @param visible          boolean : true la ligne est affichable donc visible à l'écran
     * @param rang             int , rang de la ligne dans le tableau des lignes (ligneMM)  du jeu
     * @param typeligne         int type de ligne (proposition)
     * @param infos             String, libelle par defaut de la ligne (sa valeur originale
     * @param fct               méthode de validaiton de la proposition
     */
    LignePropaleMM(CouleursMastermind[] secretCouleurs, ArrayList<Integer> secretChiffres, boolean disponible,
                   boolean visible, int rang, int typeligne, String infos, ValiderPropositionMM fct) {

        super(disponible, visible, rang, typeligne, infos);

        combinaisonChiffresSecrets = secretChiffres;

        combinaisonInitialesSecretes = new ArrayList<>(TAIILE_INITIALE);

        zoneProposition = new StringBuilder(TAIILE_INITIALE);

        for (Constantes.CouleursMastermind couleursMastermind : secretCouleurs) {
            combinaisonInitialesSecretes.add(couleursMastermind.getLettreInitiale());
        }
        fctValideProposition = fct;
    }

    /**
     * renseigne l'attribut propositionJoueur
     * @param propositionJoueur  ArrayList Character, proposition sous forme initiale de couleur (i.e J pour Jaune)
     * @return LignePropaleMM,  l'objet ligne (this)  ! utile pour chainage de méthode
     */
    public LignePropaleMM setPropositionJoueur(ArrayList<Character> propositionJoueur) {
        this.propositionJoueur = propositionJoueur;
        return this;
    }

    /**
     * renvoie la valeur de l'attribut zoneEvaluation, qui est le score de la proposition sous forme [NOIRS,BLANCS]
     * @return int [], score de la proposition , résultat de son evaluation par la méthode fctValideProposition
     */
    public int[] getZoneEvaluation() {
        return zoneEvaluation;
    }

    /**
     * copie le paramètre zoneEval dans l'attribut zoneEvaluation
     * @param zoneEval   : score obtneu par la proposition
     * @throws AppExceptions , leve exception si zoneEval et zoneEvaluation sont de taille différentes
     */
    public void setZoneEvaluation(int[] zoneEval) throws AppExceptions {
        if (zoneEval.length != zoneEvaluation.length) {
            logger.error(ERREUR_GENERIC.getMessageErreur());
            throw new AppExceptions(ERREUR_GENERIC);
        }
        System.arraycopy(zoneEval, 0, zoneEvaluation, 0, zoneEval.length);

    }

    /**
     * renvoie la valeur de zoneProposition sous forme de String
     * @return String valeur de l'attribut zoneProposition sous forme de String
     */
    private String getZoneProposition() {
        return zoneProposition.toString();
    }

    /**
     * à partir de l'attribut propositionJoueur,
     * construit la chaine de caractère qui sera affiché (attribut  zoneProposition)
     * @return LignePropaleMM,  l'objet ligne (this)  ! utile pour chainage de méthode
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

    /**
     *  appel de la fonction d'évaluation associée à cette ligne (attribut  fctValideProposition)
     *  note : fctValideProposition peut varier d'une lignePropale à une autre , c'est le cas du mode duel
     *  où alternativement la méthode de validdation est soit automatique (mode challengeur), soit saisie par le defenseur
     *  (mode defenseur)
     * @return Boolean , true si proposition est égal à secret
     */
    public Boolean EvalProposition() {
        return fctValideProposition.apply(propositionJoueur,
                combinaisonInitialesSecretes,
                nombreDePositions,
                zoneEvaluation);
    }

    /**
     * l'attribut zoneProposition est remis à sa valeur par defaut
     * @return LignePropaleMM,  l'objet ligne (this)  ! utile pour chainage de méthode
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

    /**
     *conversion du score en String
     * @return String, le score sous forme de String avec séparateur et mise en forme
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

    /**
     * ***************************************************************************************************************
     * renseigne le champ  libelleLigne
     * ***************************************************************************************************************
     */
    void setLibelleLigne() {
        super.setLibelleLigne(this.toString());
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */

