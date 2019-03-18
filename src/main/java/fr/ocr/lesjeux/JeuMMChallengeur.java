package fr.ocr.lesjeux;


import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.Scanner;

public class JeuMMChallengeur extends JeuMM {


    /**
     * @param modeJeu
     * @param sc
     */
    public JeuMMChallengeur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);

    }

    public void runJeuMM() {
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();
        this.validationPropale = new EvalChallengeurMM();
        super.runJeuMM(fabricationSecretMM);
    }
}
