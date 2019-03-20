package fr.ocr.mastermind;

import fr.ocr.modeconsole.MenuSaisieSecret;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_COULEURS;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static java.lang.StrictMath.pow;

public class JeuMMDefenseur extends JeuMM {

    private List<Integer[]> lesScoresPossibles;


    public JeuMMDefenseur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
        produireListeDesPossibles();

    }


    public void runJeuMM() {
        MenuSaisieSecret menuSaisieSecret = new MenuSaisieSecret();
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM(menuSaisieSecret.saisirCombinaisonSecrete());
        this.produirePropale = new ProduirePropaleDefenseur();
        this.validerProposition = new EvalPropaleDefenseur();

        super.RunJeuMMDefenseur(fabricationSecretMM);
    }

    private void produireListeDesPossibles() {
        int nbCouleurs = (int) getParam(NOMBRE_DE_COULEURS);
        int nbPositions = (int) getParam(NOMBRE_DE_POSITIONS);
        double nbreMax = 0;
        ArrayList<String> lesPossibles = new ArrayList<>(4096);
        //nombre max, X,   et liste des possibles , Secret []
        // X = Somme[i=0..NbrPos-1] {nbColx 10Puissance(i)}
        //Secret [] = vide
        // Secret [] = de i = 0 à i = X faire Secret[] = Secret[] +  nbPosClist ( Base(nbCol,i) )
        // Secret [] = unique(Secret[]
        long X = 0;
        double puisDix = 0.0;
        for (int i = 0; i < nbPositions; i++) {
            nbreMax += (nbCouleurs - 1) * pow(10, i);
        }
        String paddingZero = String.format("%s%d%s", "%0", nbPositions, "d");
        for (int i = 0; baseConversion(String.valueOf(i), 10, nbCouleurs) <= nbreMax; i++) {

            String.format(paddingZero, baseConversion(Integer.toString(i), 10, nbCouleurs));

            //List <Character> tmpList = Arrays.<Character>asList(tmpTab);

            //ArrayList<Character> tmpList = new Arrays.asList();

            //Stream<Character> characterStream = tmpList.stream().distinct();

            //ArrayList<Character> tmpChaine = characterStream.collect(Collectors.toCollection(ArrayList::new));

            //if (tmpChaine.size() == nbPositions)
            //lesPossibles.add(tmpChaine.toString());
        }
        int k = lesPossibles.size();

        //faire un file writer
        Stream<String> stringStream = lesPossibles.stream().distinct();
        ArrayList<String> t = stringStream.collect(Collectors.toCollection(ArrayList::new));
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
