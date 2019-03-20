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
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Constantes.ConstLigneSimple.LIGNE_DE_SAISIE;
import static fr.ocr.utiles.Constantes.ConstLigneSimple.TITRE;
import static java.lang.StrictMath.pow;


/**
 *
 */
public interface ProduirePropale {

    default ArrayList<Character> getPropaleDefenseur() {
        return null;
    }

    default ArrayList<Character> getPropaleChallengeur(Scanner scanner, String pattern, Character escChar) {
        return null;
    }

    void setScorePropale(int[] scorePropale);
}

/**
 *
 */
class ProduirePropaleDefenseur implements ProduirePropale {

    int monCompteur = 0;
    ArrayList<ArrayList<Character>> lesCombinaisonsPossibles;
    int[] scorePropale = new int[2];

    public ProduirePropaleDefenseur() {
        lesCombinaisonsPossibles = produireListeDesPossibles();
    }

    @Override
    public void setScorePropale(int[] sc) {
        System.arraycopy(sc, 0, scorePropale, 0, sc.length);
    }

    public ArrayList<Character> getPropaleDefenseur() {

        return lesCombinaisonsPossibles.get(monCompteur++);
    }

    private ArrayList<ArrayList<Character>> produireListeDesPossibles() {
        int nbCouleurs = (int) getParam(NOMBRE_DE_COULEURS);
        int nbPositions = (int) getParam(NOMBRE_DE_POSITIONS);
        double nbreMax = 0;
        ArrayList<ArrayList<Character>> lesPossibles = new ArrayList<>(4096);
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

            IntStream intStream = String.format(paddingZero, baseConversion(Integer.toString(i), 10, nbCouleurs)).chars().distinct();

            Stream<Character> characterStream = String.format(paddingZero, baseConversion(Integer.toString(i), 10, nbCouleurs)).chars().distinct().mapToObj(c -> (char) c);

            ArrayList<Character> tmpPossible = String.format(paddingZero, baseConversion(Integer.toString(i), 10, nbCouleurs)).chars().distinct().mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayList::new));

            if (tmpPossible.size() == nbPositions) {
                ArrayList<Character> uneComposition = new ArrayList<>(nbPositions);
                int k = 0;
                for (Character v : tmpPossible) {
                    int locTmp = Integer.parseInt(v.toString());
                    uneComposition.add(CouleursMastermind.values()[locTmp].getLettreInitiale());
                }
                lesPossibles.add(uneComposition);
            }

        }
        return lesPossibles;
    }


    /**
     * @param number nombre a convertir (sous forme de chaine)
     * @param sBase  base du nombre à convertir
     * @param dBase  base de destination
     * @return chaine nombre en base dBase
     */
    private Integer baseConversion(String number,
                                   int sBase, int dBase) {
        // Parse the number with source radix
        // and return in specified radix(base)
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
     * @return List<Integer [ ]> les scores possibles qui peuvent être obtenus par une proposition
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


}

/**
 *
 */

class ProduirePropaleChallengeur implements ProduirePropale {

    private LignePropaleMM[] lignesPropaleMM;
    private LigneSimpleMM[] lignesSimpleMM;

    ProduirePropaleChallengeur(LigneSimpleMM[] lignesSimpleMM, LignePropaleMM[] lignesPropaleMM) {
        this.lignesSimpleMM = lignesSimpleMM;
        this.lignesPropaleMM = lignesPropaleMM;
    }

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

    @Override
    public void setScorePropale(int[] scorePropale) {

    }
}