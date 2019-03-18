package fr.ocr.mastermind;


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
        this.validerPropositionMM = new EvalChallengeurMM();
        super.runJeuMMChallengeur(fabricationSecretMM);
    }
}
