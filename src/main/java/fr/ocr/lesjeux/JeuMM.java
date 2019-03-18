package fr.ocr.lesjeux;

import fr.ocr.modeconsole.IhmMasterMind;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.ArrayList;
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

    private LibellesMenuSecondaire modeJeu;

    private Scanner scanner;


    protected ValidationPropale validationPropale = (ArrayList<Character> propaleJoueur,
                                                     ArrayList<Character> combinaisonSecrete,
                                                     Integer nombreDePositions,
                                                     int[] zoneEvaluation) -> false;

    /**

     * @param sc
     */
    public JeuMM(LibellesMenuSecondaire modeJeu, Scanner sc) {
        scanner = sc;
        this.modeJeu = modeJeu;
    }

    /**
     *
     * @param fabricationSecretMM
     */
    public void runJeuMM(FabricationSecretMM fabricationSecretMM) {

        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes());

        new IhmMasterMind(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes(),
                validationPropale).runIhmMM(scanner);
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
