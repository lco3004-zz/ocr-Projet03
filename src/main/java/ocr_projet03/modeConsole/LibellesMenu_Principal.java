package ocr_projet03.modeConsole;


public enum LibellesMenu_Principal {
    Titre("OCR-Projet03 - Menu Principal"),
    JeuMastermind(String.format("%d -> pour jouer au MasterMind",Titre.ordinal()+1)),
    JeuPlusMoins(String.format("%d -> pour jouer au PlusMoins",JeuMastermind.ordinal()+1)),
    Quitter(String.format("%c -> Quitter",'Q'));
    private String libelle;
    LibellesMenu_Principal(String s) {
        libelle=s;
    }

    public String getLibelle() {
        return libelle;
    }
}

