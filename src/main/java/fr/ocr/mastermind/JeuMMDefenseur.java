package fr.ocr.mastermind;

import fr.ocr.modeconsole.MenuSaisieSecret;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JeuMMDefenseur extends JeuMM {

    private List<Integer[]> lesScoresPossibles;

    /**
     * @param modeJeu
     * @param sc
     */
    public JeuMMDefenseur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);

    }

    public void runJeuMM() {
        MenuSaisieSecret menuSaisieSecret = new MenuSaisieSecret();
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM(menuSaisieSecret.saisirCombinaisonSecrete());
        this.validerPropositionMM = new EvalDefenseurMM();
        this.obtenirPropaleDefenseurMM = new ProduirePropaleDefenseurMM();
        super.runJeuMMDefenseur(fabricationSecretMM);
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
