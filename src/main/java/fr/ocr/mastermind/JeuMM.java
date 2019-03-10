package fr.ocr.mastermind;

import fr.ocr.modeconsole.IhmMasterMind;
import fr.ocr.modeconsole.Libelles;
import fr.ocr.modeconsole.MenuSaisieSecret;
import fr.ocr.utiles.CouleursMastermind;
import fr.ocr.utiles.AppExceptions;

import java.util.ArrayList;
import java.util.Scanner;

import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

public class JeuMM implements ValidationPropale{
    private Libelles.LibellesMenuSecondaire modeDeJeu;
    private FabricationSecret fabricationSecret;
    private ArrayList<Integer> chiffresSecrets;
    CouleursMastermind[]  couleursSecretes;
    private IhmMasterMind ihmMasterMind;
    private Scanner scanner;

    public JeuMM(Libelles.LibellesMenuSecondaire modeJeu, Scanner sc) {
        modeDeJeu =modeJeu;
        scanner = sc;
    }
    public void runJeuMM () throws AppExceptions {
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
        CouleursMastermind[]  couleursSecretes = fabricationSecret.getCouleursSecretes();
        ihmMasterMind = new IhmMasterMind(modeDeJeu,chiffresSecrets,couleursSecretes, this);

        int boucle =20;
        while ((boucle>0)&&(!ihmMasterMind.runIhmMM(scanner))) {
            //evite de boucler : si utilisateur passe son temps à annuler saisie, stop
            boucle--;
        }
    }

    @Override
    public Boolean apply(ArrayList<Character> x,ArrayList<Character> y) {
        return false;
    }
}
