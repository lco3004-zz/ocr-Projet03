package fr.ocr.mastermind;

import fr.ocr.modeconsole.IhmMasterMind;
import fr.ocr.modeconsole.Libelles;
import fr.ocr.modeconsole.MenuSaisieSecret;
import fr.ocr.params.CouleursMastermind;
import fr.ocr.utiles.AppExceptions;

import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

public class JeuMM {
    private Libelles.LibellesMenuSecondaire modeDeJeu;
    public JeuMM(Libelles.LibellesMenuSecondaire modeJeu) {
        modeDeJeu =modeJeu;
    }
    public void runJeuMM () throws AppExceptions {
        switch (modeDeJeu) {
            case MODE_CHALLENGER:
                MenuSaisieSecret menuSaisieSecret = new MenuSaisieSecret();
                FabricationSecret fabricationSecret = new FabricationSecret(menuSaisieSecret.saisirCombinaisonSecrete());
                ArrayList<Integer> chiffresSecrets = fabricationSecret.getChiffresSecrets();
                CouleursMastermind[]  couleursSecretes = fabricationSecret.getCouleursSecretes();
                IhmMasterMind ihmMasterMind = new IhmMasterMind(chiffresSecrets,couleursSecretes);
                ihmMasterMind.runIhmMM();
                break;
            case MODE_DEFENSEUR:
                break;
            case MODE_DUEL:
                break;
            default:
                logger.error(String.format("%s", ERREUR_GENERIC));
                throw new AppExceptions(ERREUR_GENERIC);
        }
    }
}
