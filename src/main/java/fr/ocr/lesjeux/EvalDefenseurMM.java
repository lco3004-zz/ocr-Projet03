package fr.ocr.lesjeux;

import java.util.ArrayList;
import java.util.List;

public class EvalDefenseurMM implements ValidationPropale {

    @Override
    public Boolean apply(ArrayList<Character> propaleJoueur,
                         ArrayList<Character> combinaisonSecrete,
                         Integer nombreDePositions,
                         int[] zoneEvaluation) {


        return false;
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
     * @param nbPos
     * @return List<Integer [ ]>
     */
    public static List<Integer[]> LesScoresPossibles(int nbPos) {
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
