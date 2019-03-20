package fr.ocr.mastermind;

import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JeuMMDefenseur extends JeuMM {

    List<Integer[]> lesScoresPossibles;
    ArrayList<ArrayList<Character>> lesCombinaisonsPossibles;

    public JeuMMDefenseur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
    }

    public void runJeuMM() {
        //MenuSaisieSecret menuSaisieSecret = new MenuSaisieSecret();
        //FabricationSecretMM fabricationSecretMM = new FabricationSecretMM(menuSaisieSecret.saisirCombinaisonSecrete());
        // DEBUT MOCK
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();
        //FIN MOCK
        this.produirePropale = new ProduirePropaleDefenseur();
        this.validerProposition = new EvalPropaleDefenseur();

        super.RunJeuMMDefenseur(fabricationSecretMM);
    }

}
