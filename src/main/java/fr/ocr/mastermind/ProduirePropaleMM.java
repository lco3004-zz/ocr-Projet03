package fr.ocr.mastermind;

import fr.ocr.modeconsole.EcrireSurEcran;
import fr.ocr.modeconsole.IOConsole;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes.CouleursMastermind;

import java.util.ArrayList;
import java.util.List;
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
 * ***************************************************************************************************************
 *
 * @author Laurent Cordier
 * <p>
 * <p>
 * ***************************************************************************************************************
 */

/**
 * ***************************************************************************************************************
 *
 *
 * ***************************************************************************************************************
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
 * ***************************************************************************************************************
 *
 *
 * ***************************************************************************************************************
 */
class ProduirePropaleMMDefenseur implements ProduirePropaleMM {
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);


    private int monCompteur = 0;

    private ArrayList<UnePropale> lesPropalesEvaluees;

    private ArrayList<ArrayList<Character>> lesCombinaisonsPossibles;

    /**
     *
     */
    ProduirePropaleMMDefenseur() {
        lesCombinaisonsPossibles = produireListeDesPossibles();
        lesPropalesEvaluees = new ArrayList<>(4096);
    }

    /**
     * @param laPropaleScoree  proposition avec son score obtenue après présentation au validateur
     * @param sc  scanner de saisie clavier
     */
    @Override
    public void setScorePropale(ArrayList<Character> laPropaleScoree, int[] sc) {

        Integer laPos = lesCombinaisonsPossibles.indexOf(laPropaleScoree);

        lesPropalesEvaluees.add(new UnePropale(laPropaleScoree, sc, laPos));
    }

    /**
     *
     * @param laPropaleScoree dernière proposition présentée, avec son score obtenue après présentation au validateur
     * @return une nouvelle proposition à présenter au validateur
     */
    private UnePropale chercheNouvellePropale(UnePropale laPropaleScoree) {

        //premiere propale - donc celle de rang 0 des possibles
        if (laPropaleScoree == null) {
            int[] scinitial = {0, 0};
            return new UnePropale(lesCombinaisonsPossibles.get(0), scinitial, 0);
        }

        // à partir d'icice n'est plus la premiere propostion

        EvalPropaleChallengeur verifieScore = new EvalPropaleChallengeur();

        UnePropale laNouvellePropale = null;

        for (ArrayList<Character> nouvelleCombinaison : lesCombinaisonsPossibles) {

            if (lesCombinaisonsPossibles.indexOf(nouvelleCombinaison) != laPropaleScoree.sonRang) {

                for (UnePropale unePrecentePropale : lesPropalesEvaluees) {

                    int[] scoreTmp = new int[2];

                    laNouvellePropale = new UnePropale(nouvelleCombinaison, scoreTmp, lesCombinaisonsPossibles.indexOf(nouvelleCombinaison));

                    verifieScore.apply(nouvelleCombinaison, unePrecentePropale.saValeur, nombreDePositions, scoreTmp);

                    if ((scoreTmp[0] != unePrecentePropale.sonScore[0]) ||
                            (scoreTmp[1] != unePrecentePropale.sonScore[1])) {
                        laNouvellePropale = null;
                        break;
                    }
                }
            }
            if (laNouvellePropale != null)
                break;
        }
        return laNouvellePropale;
    }


    /**
     *
     * @return proposition sous forme de liste de caractères, qui sont les initiales des couleurs de la proposition
     */
    @Override
    public ArrayList<Character> getPropaleDefenseur() {
        UnePropale unePropale;

        if (monCompteur == 0) {
            unePropale = chercheNouvellePropale(null);
            monCompteur = 1;
        } else {
            unePropale = chercheNouvellePropale(lesPropalesEvaluees.get(lesPropalesEvaluees.size() - 1));
        }

        return unePropale.saValeur;
    }

    /**
     *
     * @return liste des combinaisons possibles
     */
    private ArrayList<ArrayList<Character>> produireListeDesPossibles() {
        int nbCouleurs = (int) getParam(NOMBRE_DE_COULEURS);
        int nbPositions = (int) getParam(NOMBRE_DE_POSITIONS);
        Boolean isDoublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
        double nbreMax = 0;
        ArrayList<ArrayList<Character>> lesPossibles = new ArrayList<>(TAIILE_INITIALE);
        //nombre max, Classe_X,   et liste des possibles , Secret []
        // Classe_X = Somme[i=0..NbrPos-1] {nbColx 10Puissance(i)}
        //Secret [] = vide
        // Secret [] = de i = 0 à i = Classe_X faire Secret[] = Secret[] +  nbPosClist ( Base(nbCol,i) )
        // Secret [] = unique(Secret[]
        long X = 0;
        double puisDix = 0.0;
        for (int i = 0; i < nbPositions; i++) {
            nbreMax += (nbCouleurs - 1) * pow(10, i);
        }
        String paddingZero = String.format("%s%d%s", "%0", nbPositions, "d");

        for (int i = 0; baseConversion(String.valueOf(i), 10, nbCouleurs) <= nbreMax; i++) {

            String tmpString = String.format(paddingZero, baseConversion(Integer.toString(i), 10, nbCouleurs));


            ArrayList<Character> tmpPossible;

            if (isDoublonAutorise) {
                tmpPossible = String.format(paddingZero, baseConversion(Integer.toString(i), 10, nbCouleurs)).chars().mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayList::new));
            } else {
                tmpPossible = String.format(paddingZero, baseConversion(Integer.toString(i), 10, nbCouleurs)).chars().distinct().mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayList::new));
            }
            ArrayList<Character> uneComposition = new ArrayList<>(nbPositions + 1);

            if (tmpPossible.size() == nbPositions) {
                for (Character v : tmpPossible) {
                    int locTmp = Integer.parseInt(v.toString());
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

            for (int nbCombiParLigne = tailleTab; nbCombiParLigne < tailleTab + 10; nbCombiParLigne++) {
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
    private Integer baseConversion(String number, int sBase, int dBase) {
        return Integer.valueOf(Integer.toString(Integer.parseInt(number, sBase), dBase));
    }

    /**
     * Sc(i,j)
     * I   J
     * 0 (0..nbPos)
     * 1 (0..nbPos-1)
     * 2 (0..nbPos-2)
     * 3 (0)
     * 4 (0..nbPos-4) 0..0
     *
     * @param nbPos Integer , le nombre de positions dans une ligne du jeu MM
     * @return List of arrays of Integer , les scores possibles qui peuvent être obtenus par une proposition
     */
    public List<Integer[]> CalculScoresPossibles(int nbPos) {
        List<Integer[]> scPossible = new ArrayList<>(256);
        for (int noirs = 0; noirs < nbPos - 1; noirs++) {
            for (int blancs = 0; blancs <= nbPos - noirs; blancs++) {

                scPossible.add(new Integer[]{noirs, blancs});
            }
        }
        scPossible.add(new Integer[]{nbPos - 1, 0});
        scPossible.add(new Integer[]{nbPos, 0});
        return scPossible;
    }

    /**
     * ***************************************************************************************************************
     *
     *
     * ***************************************************************************************************************
     */
    class UnePropale {

        int[] sonScore = new int[2];
        ArrayList<Character> saValeur;
        Integer sonRang;

        UnePropale(ArrayList<Character> propale, int[] sc, Integer rangdeLaPropale) {
            System.arraycopy(sc, 0, sonScore, 0, sc.length);
            saValeur = propale;
            sonRang = rangdeLaPropale;
        }
    }

}

/**
 * ***************************************************************************************************************
 *
 *
 * ***************************************************************************************************************
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
                saisieUneCouleur = IOConsole.LectureClavier(pattern, scanner, new EcrireSurEcran() {
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
                    String infosSasiie = lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigne() + saisieUneCouleur.toString() + " ";
                    lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(infosSasiie);
                    if (!doublonAutorise) {
                        int posCol = pattern.indexOf(saisieUneCouleur);
                        int taille = pattern.length();
                        pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
                        taille = pattern.length();
                        String pourLower = String.valueOf(saisieUneCouleur).toLowerCase(Locale.forLanguageTag("fr"));
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
}