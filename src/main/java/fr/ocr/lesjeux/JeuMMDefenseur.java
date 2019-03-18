package fr.ocr.lesjeux;

import fr.ocr.modeconsole.MenuSaisieSecret;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.Scanner;

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
        this.validationPropale = new EvalChallengeurMM();
        super.runJeuMM(fabricationSecretMM);
    }
}
