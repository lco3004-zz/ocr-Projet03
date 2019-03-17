package fr.ocr.mastermind;

import fr.ocr.modeconsole.IhmMasterMind;
import fr.ocr.modeconsole.MenuSaisieSecret;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;

import java.util.ArrayList;
import java.util.Scanner;

import static fr.ocr.utiles.Constantes.ConstEvalPropale.BLANC_MALPLACE;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.NOIR_BIENPLACE;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

public class JeuMM  {
    Constantes.CouleursMastermind[] couleursSecretes;
    private Constantes.Libelles.LibellesMenuSecondaire modeDeJeu;
    private FabricationSecret fabricationSecret;
    private ArrayList<Integer> chiffresSecrets;
    private IhmMasterMind ihmMasterMind;
    private Scanner scanner;
    private  ValidationPropale validationPropale ;
    public JeuMM(Constantes.Libelles.LibellesMenuSecondaire modeJeu, Scanner sc) {
        modeDeJeu = modeJeu;
        scanner = sc;
    }

    public void runJeuMM() throws AppExceptions {
        switch (modeDeJeu) {
            case MODE_CHALLENGER:
                fabricationSecret = new FabricationSecret();
                validationPropale = new  EvaluationChallenger ();
                break;
            case MODE_DEFENSEUR:
                MenuSaisieSecret menuSaisieSecret = new MenuSaisieSecret();
                fabricationSecret = new FabricationSecret(menuSaisieSecret.saisirCombinaisonSecrete());
                break;
            case MODE_DUEL:
                break;
            default:
                logger.error(String.format("%s", ERREUR_GENERIC));
                throw new AppExceptions(ERREUR_GENERIC);
        }

        chiffresSecrets = fabricationSecret.getChiffresSecrets();
        Constantes.CouleursMastermind[] couleursSecretes = fabricationSecret.getCouleursSecretes();

        LogLaCombinaisonSecrete(couleursSecretes);

        ihmMasterMind = new IhmMasterMind(modeDeJeu, chiffresSecrets, couleursSecretes,validationPropale );

        ihmMasterMind.runIhmMM(scanner);
    }


    /**
     *
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
class EvaluationChallenger implements ValidationPropale{

    @Override
    public Boolean apply(ArrayList<Character> propaleJoueur,
                         ArrayList<Character> combinaisonSecrete,
                         Integer nombreDePositions,
                         int [] zoneEvaluation) {

        int rangPropale;

        zoneEvaluation[NOIR_BIENPLACE]=0;
        zoneEvaluation[BLANC_MALPLACE]=0;

        for (Character couleurSec : combinaisonSecrete) {
            rangPropale = propaleJoueur.indexOf(couleurSec);
            if (rangPropale >=0) {
                if ((rangPropale == combinaisonSecrete.indexOf(couleurSec))) {
                    zoneEvaluation[NOIR_BIENPLACE]++;
                } else {
                    zoneEvaluation[BLANC_MALPLACE]++;
                }
            }
        }
        return zoneEvaluation[NOIR_BIENPLACE] == nombreDePositions;
    }
}
