package fr.ocr.mastermind;


import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.Scanner;

public class JeuMMChallengeur extends JeuMM {

    public JeuMMChallengeur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);

    }

    public void runJeuMM() {
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();
        this.validerProposition = new EvalPropaleChallengeur();

        this.produirePropale = new ProduirePropaleChallengeur(lignesSimpleMM, lignesPropaleMM);
        super.RunJeuMMChallengeur(fabricationSecretMM);
    }
}
