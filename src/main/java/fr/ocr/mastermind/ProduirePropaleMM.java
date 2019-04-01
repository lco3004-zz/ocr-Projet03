package fr.ocr.mastermind;

import fr.ocr.modeconsole.EcrireSurEcran;
import fr.ocr.modeconsole.IOConsole;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes.CouleursMastermind;
import fr.ocr.utiles.Messages;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Constantes.ConstTailleStringBuilder.TAIILE_INITIALE;
import static fr.ocr.utiles.Constantes.ConstTypeDeLigne.LIGNE_DE_SAISIE;
import static fr.ocr.utiles.Constantes.ConstTypeDeLigne.TITRE;
import static fr.ocr.utiles.Logs.logger;
import static java.lang.StrictMath.pow;

/**
 * @author Laurent Cordier
 * <p>
 *     construit une proposition soit par saisie, soit par algo
 *</p>
 *<p>
 *     interface d'accès au deux modes principaux d'obtention d'une proposition (defenseur, challengeur)
 *     la méthode setScorePropale est utilisé en mode défenseur.
 *</p>
 */
public interface ProduirePropaleMM {

    default ArrayList<Character> getPropaleDefenseur() {
        return null;
    }

    default ArrayList<Character> getPropaleChallengeur(Scanner scanner, String pattern, Character escChar) {
        return null;
    }

    default void setScorePropale(ArrayList<Character> laPropaleScoree, int[] scorePropale) {
    }
}

/**
 *  Proposition en mode Defenseur : algo
 *
 *      * la propositon Pprime est identique au secret S à chercher .
 *      *
 *      * les propales Pi  déja scorées ont donné  un score : Score(Pi,S) = SCi .
 *      *
 *      * Donc si Pprime est égal à S , alors pour toutes les propales déja scorées :
 *      *        Score (Pi,Pprime)  doit être égal au score  Score(Pi,S)
 *      *
 *      * Sinon, Pprime n'est pas égale à S , donc ne pas retenir cette propostion Pprime.
 *      *
 *      * on passe à la suivante dans liste des possibles.
 *      *
 *      * sachant que la liste des possibles est complète, cette conditon est suffisante pour garantir
 *      * qu'il existe une Pprime tel que Score(Pprime, S) == SC(S,S)
 *
 */
class ProduirePropaleMMDefenseur implements ProduirePropaleMM {

    //nombre de couleur par ligne de jeu (nombre de position)
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);

    private int monCompteur = 0;

    //liste des propositions qui ont déja été soumises à validation
    private ArrayList<UnePropale> lesPropalesEvaluees;

    //liste de toutes les propostions possible dans ce jeu (dépend du paramétrage)
    private ArrayList<ArrayList<Character>> lesCombinaisonsPossibles;

    /**
     *   constructeur
     */
    ProduirePropaleMMDefenseur() {
        lesCombinaisonsPossibles = produireListeDesPossibles();
        lesPropalesEvaluees = new ArrayList<>(4096);
    }

    /**
     * memorise la propostion en cours et son score (creation objet laPropaleScoree)
     * @param laPropaleScoree  proposition avec son score obtenue après présentation au validateur
     * @param sc  scanner de saisie clavier
     */
    @Override
    public void setScorePropale(ArrayList<Character> laPropaleScoree, int[] sc) {

        Integer laPos = lesCombinaisonsPossibles.indexOf(laPropaleScoree);

        lesPropalesEvaluees.add(new UnePropale(laPropaleScoree, sc, laPos));
    }

    /**
     * détermine quelle proposition parmi les possibles, il faut soumettre à validation
     *      * la propositon Pprime est identique au secret S à chercher .
     *      *
     *      * les propales P<i>  déja scorées ont donné  un score : Score(P<i>,S) = SC<i> .
     *      *
     *      * Donc si Pprime est égal à S , alors pour toutes les propales déja scorées :
     *      *        Score (P<i>,Pprime)  doit être égal au score  Score(P<i>,S)
     *      *
     *      * Sinon, Pprime n'est pas égale à S , donc ne pas retenir cette propostion Pprime.
     *      *
     *      * on passe à la suivante dans liste des possibles.
     *      *
     *      * sachant que la liste des possibles est complète, cette conditon est suffisante pour garantir
     *      * qu'il existe une Pprime tel que Score(Pprime, S> == SC(S,S)
     * @param laPropaleScoree dernière proposition présentée, avec son score obtenue après présentation au validateur
     * @return une nouvelle proposition à présenter au validateur
     */
    private UnePropale chercheNouvellePropale(UnePropale laPropaleScoree) {

        Boolean isDoublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);

        //premiere propale - donc celle de rang 0 des possibles
        if (laPropaleScoree == null) {
            int[] scinitial = {0, 0};
            return new UnePropale(lesCombinaisonsPossibles.get((lesCombinaisonsPossibles.size() / 3) * 2), scinitial, 0);
        }

        // à partir d'ici n'est plus la premiere propostion
        UnePropale laNouvellePropale = null;

        //pour toutes les combinaisons du jeu (selon paramétrage)
        for (ArrayList<Character> nouvelleCombinaison : lesCombinaisonsPossibles) {

            //ne pas repasser sur la propale qui vient d'être présentée à validateur
            if (lesCombinaisonsPossibles.indexOf(nouvelleCombinaison) != laPropaleScoree.sonRang) {

                //réxéamine toutes le propales déja scorées .
                for (UnePropale unePrecedentePropale : lesPropalesEvaluees) {

                    int[] scoreTmp = new int[2];

                    laNouvellePropale = new UnePropale(nouvelleCombinaison, scoreTmp, lesCombinaisonsPossibles.indexOf(nouvelleCombinaison));

                    // appel de la méthode de validation par calcul - c'est la méthode qui est aussi utilisé par le mode Challenger
                    (new ScorerProposition()).apply(nouvelleCombinaison, unePrecedentePropale.saValeur, nombreDePositions, scoreTmp, isDoublonAutorise);

                    //si le score ne correspond pas à celui obtenu lors de la validation de la propale "unePrecedentePropale"
                    //stop, on passe à la combinaison possible suivante
                    if ((scoreTmp[0] != unePrecedentePropale.sonScore[0]) ||
                            (scoreTmp[1] != unePrecedentePropale.sonScore[1])) {
                        laNouvellePropale = null;
                        break;
                    }
                }
            }
            //on a trouvé une proposition candidate
            if (laNouvellePropale != null)
                break;
        }
        return laNouvellePropale;
    }

    /**
     * renvoie une propostion candidate
     * @return proposition sous forme de liste de caractères, qui sont les initiales des couleurs de la proposition
     */
    @Override
    public ArrayList<Character> getPropaleDefenseur() {
        UnePropale unePropale;

        //cas particulier si c'est le premier appel à la méthode, on renvoie la première proposition de la liste
        //des propositons possibles

        if (monCompteur == 0) {
            unePropale = chercheNouvellePropale(null);
            monCompteur = 1;
        } else {
            unePropale = chercheNouvellePropale(lesPropalesEvaluees.get(lesPropalesEvaluees.size() - 1));
        }

        return (unePropale != null) ? unePropale.saValeur : null;
    }

    /**
     * Constuit la lsite des propositions possible pour ce jeu (paramétrage)
     * Methode : calculer en base B telque B = nombreDeCouleurs du jeu (paramétrage)
     * les propositons vont de la Proposition0 "[0,0,..,0]" où taille de Proposition0 == nombreDePositions
     * à la PropsiitonN "[nombreDeCouleurs,nombreDeCouleurs,...,nombreDeCouleurs]" où taille de Proposition0 == nombreDePositions
     * ie pour 4 positions et 6 couleurs
     * les possibles vont de [0,0,0,0] à [5,5,5,5]
     * @return liste des combinaisons possibles sous forme de liste de liste de character
     */
    private ArrayList<ArrayList<Character>> produireListeDesPossibles() {

        int nbCouleurs = (int) getParam(NOMBRE_DE_COULEURS);

        int nbPositions = (int) getParam(NOMBRE_DE_POSITIONS);

        Boolean isDoublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);

        double nbreMax = 0;

        ArrayList<ArrayList<Character>> lesPossibles = new ArrayList<>(TAIILE_INITIALE);

        for (int i = 0; i < nbPositions; i++) {
            nbreMax += (nbCouleurs - 1) * pow(nbCouleurs, i);
        }

        String StringFormat = String.format("%s%d%s", "%", nombreDePositions, "s");

        ArrayList<Character> tmpPossible;
        ArrayList<Character> uneComposition;

        for (int i = 0; i <= nbreMax; i++) {

            if (isDoublonAutorise) {
                tmpPossible = String.format(StringFormat, baseConversion(String.valueOf(i), 10, nbCouleurs)).replace(' ', '0').chars().mapToObj(e -> (char) e).collect(Collectors.toCollection(ArrayList::new));

            } else {
                tmpPossible = String.format(StringFormat, baseConversion(String.valueOf(i), 10, nbCouleurs)).replace(' ', '0').chars().distinct().mapToObj(e -> (char) e).collect(Collectors.toCollection(ArrayList::new));
            }
            uneComposition = new ArrayList<>(nbPositions + 1);

            if (tmpPossible.size() == nbPositions) {
                for (Character v : tmpPossible) {
                    int locTmp = Integer.valueOf(baseConversion(String.valueOf(v), nbCouleurs, 10));
                    uneComposition.add(CouleursMastermind.values()[locTmp].getLettreInitiale());
                }
                lesPossibles.add(uneComposition);
            }

        }
        //
        logger.debug(String.format("Tableau des Possibles , Nombre = %02d , DoublonAutorise ? = %b", lesPossibles.size(), isDoublonAutorise));

        StringBuilder tmpLigneLog = new StringBuilder(TAIILE_INITIALE);

        int lignedansfichierlog = 0;

        for (int tailleTab = 0; tailleTab < lesPossibles.size(); tailleTab += 10) {
            tmpLigneLog.delete(0, tmpLigneLog.length());

            tmpLigneLog.append(String.format("-> %d : |", ++lignedansfichierlog));

            for (int nbCombiParLigne = tailleTab; nbCombiParLigne < lesPossibles.size() && nbCombiParLigne < tailleTab + 10; nbCombiParLigne++) {

                tmpLigneLog.append(" ");

                tmpLigneLog.append(lesPossibles.get(nbCombiParLigne));
            }

            tmpLigneLog.append(" |");

            logger.debug(tmpLigneLog.toString());
        }

        return lesPossibles;
    }

    /**
     * @param number nombre a convertir (sous forme de chaine)
     * @param sBase  base du nombre à convertir
     * @param dBase  base de destination
     * @return chaine nombre en base dBase
     */
    private String baseConversion(String number, int sBase, int dBase) {
        String tmpVal;
        try {
            tmpVal = Integer.toString(Integer.parseInt(number, sBase), dBase);
        } catch (NumberFormatException e) {
            //incoherence dans le changement de base, stop programme. ne devraitpas se produire
            logger.error(Messages.ErreurMessages.ERREUR_GENERIC);
            throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
        }
        return tmpVal;
    }

    /*
     *  innerclase pour conserver la propositon X avec son score
     */
    class UnePropale {

        int[] sonScore = new int[2];
        ArrayList<Character> saValeur;
        Integer sonRang;

        /**
         * @param propale         propostion
         * @param sc              score de la propositon
         * @param rangdeLaPropale rang de la propositon dans la lsite des possibles
         */
        UnePropale(ArrayList<Character> propale, int[] sc, Integer rangdeLaPropale) {
            System.arraycopy(sc, 0, sonScore, 0, sc.length);
            saValeur = propale;
            sonRang = rangdeLaPropale;
        }
    }

}

/**
 *  Proposition en mode Challengeur : saisie clavier
 */
class ProduirePropaleMMChallengeur implements ProduirePropaleMM {

    private LignePropaleMM[] lignesPropaleMM;
    private LigneMM[] lignesSimpleMM;

    ProduirePropaleMMChallengeur(LigneMM[] lignesSimpleMM, LignePropaleMM[] lignesPropaleMM) {
        this.lignesSimpleMM = lignesSimpleMM;
        this.lignesPropaleMM = lignesPropaleMM;
    }

    @Override
    public ArrayList<Character> getPropaleChallengeur(Scanner scanner, String pattern, Character escChar) {
        ArrayList<Character> propositionJoueur = new ArrayList<>(256);
        Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
        Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);

        try {

            Character saisieUneCouleur;
            do {
                saisieUneCouleur = IOConsole.LectureClavierChar(pattern, scanner, new EcrireSurEcran() {
                    @Override
                    public void Display() {
                        for (int n = TITRE; n <= LIGNE_DE_SAISIE; n++) {
                            if (lignesSimpleMM[n].isEstVisible()) {
                                if (n == LIGNE_DE_SAISIE) {
                                    System.out.print(lignesSimpleMM[n].toString());
                                } else {
                                    System.out.println(lignesSimpleMM[n].toString());
                                }
                            }
                        }
                    }
                }, escChar);

                if (saisieUneCouleur != escChar) {
                    propositionJoueur.add(saisieUneCouleur);
                    pattern = ReduirePattern(pattern, doublonAutorise, saisieUneCouleur);
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
     * retrait de la couleur qui vient d'être saisie (saisieUneCouleur) , du pattern qui controle la saisie
     * cette fonction n'a de sens que pour le mode "doulon non autorisé"
     *
     * @param pattern          String , le pattern à re
     * @param doublonAutorise  Boolean , doublon oui/no - paramétrage
     * @param saisieUneCouleur Character , l'initiale de la couleur a retiré du pattern de saisie
     * @return String pattern  initial moins le caractere qui vient d'etre saisi donc n'est  plus dispo en mode sans doublon
     */
    private String ReduirePattern(String pattern, Boolean doublonAutorise, Character saisieUneCouleur) {
        String infosSaisie = lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigne() + saisieUneCouleur.toString() + " ";
        lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(infosSaisie);
        //si le mode est sans doublon
        if (!doublonAutorise) {
            //retrait  du  caractere saisi de la liste des caracteres disponibles
            int posCol = pattern.indexOf(saisieUneCouleur);
            int taille = pattern.length();
            pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
            taille = pattern.length();
            String pourLower = String.valueOf(saisieUneCouleur).toLowerCase(Locale.forLanguageTag("fr"));
            posCol = pattern.indexOf(pourLower.toCharArray()[0]);
            pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
        }
        return pattern;
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */