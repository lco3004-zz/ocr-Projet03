package fr.ocr.mastermind;

import fr.ocr.modeconsole.IhmMasterMind;
import fr.ocr.modeconsole.Libelles;
import fr.ocr.modeconsole.MenuSaisieSecret;
import fr.ocr.params.CouleursMastermind;
import fr.ocr.utiles.AppExceptions;

import java.util.ArrayList;
import java.util.Objects;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

public class JeuMM implements ValidationPropale{
    private Libelles.LibellesMenuSecondaire modeDeJeu;
    FabricationSecret fabricationSecret;
    ArrayList<Integer> chiffresSecrets;
    CouleursMastermind[]  couleursSecretes;
    IhmMasterMind ihmMasterMind;

    public JeuMM(Libelles.LibellesMenuSecondaire modeJeu) {
        modeDeJeu =modeJeu;
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
        ihmMasterMind = new IhmMasterMind(chiffresSecrets,couleursSecretes,this::apply);
        ValidationPropale validationPropale;
        ihmMasterMind.runIhmMM();
    }

    @Override
    public Boolean apply(ArrayList<Character> x,ArrayList<Character> y) {
        return false;
    }
}
