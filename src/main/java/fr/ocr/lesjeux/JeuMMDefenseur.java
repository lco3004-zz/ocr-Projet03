package fr.ocr.lesjeux;

import fr.ocr.modeconsole.MenuSaisieSecret;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;

public class JeuMMDefenseur extends JeuMM {


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
        this.validationPropale = new EvalDefenseurMM((Integer) getParam(NOMBRE_DE_POSITIONS));
        super.runJeuMM(fabricationSecretMM);
    }
}
