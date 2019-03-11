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

public class JeuMM implements ValidationPropale {
    Constantes.CouleursMastermind[] couleursSecretes;
    private Constantes.Libelles.LibellesMenuSecondaire modeDeJeu;
    private FabricationSecret fabricationSecret;
    private ArrayList<Integer> chiffresSecrets;
    private IhmMasterMind ihmMasterMind;
    private Scanner scanner;

    public JeuMM(Constantes.Libelles.LibellesMenuSecondaire modeJeu, Scanner sc) {
        modeDeJeu = modeJeu;
        scanner = sc;
    }

    public void runJeuMM() throws AppExceptions {
        switch (modeDeJeu) {
            case MODE_CHALLENGER:
                fabricationSecret = new FabricationSecret();
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

        ihmMasterMind = new IhmMasterMind(modeDeJeu, chiffresSecrets, couleursSecretes, this);

        ihmMasterMind.runIhmMM(scanner);
    }

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
