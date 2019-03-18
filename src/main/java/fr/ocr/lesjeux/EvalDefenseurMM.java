package fr.ocr.lesjeux;

import java.util.ArrayList;
import java.util.List;

public class EvalDefenseurMM implements ValidationPropale {


    private List<Integer[]> lesScoresPossibles;

    /**
     * @param nbPositions le nombre de positions dans une ligne du jeu MM
     */
    public EvalDefenseurMM(Integer nbPositions) {
        lesScoresPossibles = CalculScoresPossibles(nbPositions);
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
     * @param nbPos  Integer , le nombre de positions dans une ligne du jeu MM
     * @return List<Integer [ ]> les scores possibles qui peuvent être obtenus par une proposition
     */
    public static List<Integer[]> CalculScoresPossibles(int nbPos) {
        List<Integer[]> scPossible = new ArrayList<Integer[]>(256);
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
     *
     * @param propaleJoueur  P, la proposition du joueur
     * @param combinaisonSecrete  S, la combinaison secrete à trouver
     * @param nombreDePositions , le nombre positions 'emplacement de pions) ur une ligne du jeu
     * @param zoneEvaluation,  tableau entier de taille 2 qui contient le nombre de Blancs (mal placés) et le nombre de Noirs (bien placés)
     * @return si P est égal à S  , fin de partie donc la méthode répond true (false sinon)
     */
    @Override
    public Boolean apply(ArrayList<Character> propaleJoueur,
                         ArrayList<Character> combinaisonSecrete,
                         Integer nombreDePositions,
                         int[] zoneEvaluation) {


        return false;
    }

    /**
     * @param number nombre a convertir (sous forme de chaine)
     * @param sBase  base du nombre à convertir
     * @param dBase  base de destination
     * @return
     */
    public String baseConversion(String number,
                                 int sBase, int dBase) {
        // Parse the number with source radix
        // and return in specified radix(base)
        return Integer.toString(Integer.parseInt(number, sBase), dBase);
    }
}
