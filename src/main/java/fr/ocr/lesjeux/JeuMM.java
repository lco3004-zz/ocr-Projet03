package fr.ocr.lesjeux;

import fr.ocr.modeconsole.IhmMasterMind;
import fr.ocr.utiles.Constantes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static fr.ocr.utiles.Logs.logger;

/**
 * "Modele" du jeuMastermind
 * Note :
 * * la combinaison secrete est soit calculée par ordinateur en mode challeger
 * * soit saisie par le joueur
 * *la fabrication de la composition secrete 'S' dépend de :
 * * * NOMBRE_DE_COULEURS : le nombre de couleurs disponibles  'N'
 * * * * limité à 18 max par construction, valeur min 6 couleurs qui est une valeur std)
 * * * NOMBRE_DE_POSITIONS : le nombre de couleurs 'P'  constituant la composition secrete S,
 * * * * 8 max par construction, min 4 qui est une valeur Std
 * * * DOUBLON_AUTORISE
 */
public abstract class JeuMM {

    private Constantes.Libelles.LibellesMenuSecondaire modeDeJeu;

    private Scanner scanner;

    protected ValidationPropale validationPropale = (ArrayList<Character> propaleJoueur,
                                                     ArrayList<Character> combinaisonSecrete,
                                                     Integer nombreDePositions,
                                                     int[] zoneEvaluation) -> false;

    /**
     * @param modeJeu
     * @param sc
     */
    public JeuMM(Constantes.Libelles.LibellesMenuSecondaire modeJeu, Scanner sc) {
        modeDeJeu = modeJeu;
        scanner = sc;
    }

    /**
     *
     * @param fabricationSecretMM
     */
    public void runJeuMM(FabricationSecretMM fabricationSecretMM) {

        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes());

        new IhmMasterMind(modeDeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes(),
                validationPropale).runIhmMM(scanner);
    }

    /**
     *
     * @param number nombre a convertir (sous forme de chaine)
     * @param sBase  base du nombre à convertir
     * @param dBase  base de destination
     * @return
     */
    public  String baseConversion(String number,
                                  int sBase, int dBase)
    {
        // Parse the number with source radix
        // and return in specified radix(base)
        return Integer.toString(Integer.parseInt(number, sBase), dBase);
    }
    /**
     *       Sc(i,j)
     *       I   J
     *       0 (0..nbPos)
     *       1 (0..nbPos-1)
     *       2 (0..nbPos-2)
     *       3 (0)
     *       4 (0..nbPos-4) 0..0
     * @param nbPos
     * @return List< Integer [] >
     */
    public static List< Integer [] > LesScoresPossibles (int nbPos) {
        List<Integer [] > scPossible = new ArrayList<Integer [] >(256);
        for (int noirs =0; noirs < nbPos -1 ;noirs++) {
            for (int blancs = 0; blancs <= nbPos - noirs; blancs++) {

                scPossible.add(new Integer[]{noirs, blancs});
            }
        }
        scPossible.add(new Integer[] {nbPos-1, 0});
        scPossible.add(new Integer[] {nbPos, 0});
        return scPossible;
    }
    /**
     * @return le code secret (String)
     */
    private String LogLaCombinaisonSecrete(Constantes.CouleursMastermind[] couleursSecretes) {

        Constantes.CouleursMastermind[] toutes = Constantes.CouleursMastermind.values();
        StringBuilder s = new StringBuilder(4096);

        for (Constantes.CouleursMastermind x : toutes) {
            s.append(String.format("%s%s", x.toString(), ", "));
        }
        logger.info("Toutes les couleurs = " + s.substring(0, s.lastIndexOf(", ")));

        int tailleStringB = s.length();
        s.delete(0, tailleStringB - 1);
        for (Constantes.CouleursMastermind x : couleursSecretes) {
            s.append(String.format("%s%s", x.toString(), ", "));
        }
        String valRet = String.format("%s %s", "Combinaison secrete = ", s.substring(0, s.lastIndexOf(", ")));
        logger.info("Combinaison secrete = " + s.substring(0, s.lastIndexOf(",")));
        return valRet;
    }

}
