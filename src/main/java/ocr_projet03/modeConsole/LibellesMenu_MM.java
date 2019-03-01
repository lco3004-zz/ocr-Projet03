package ocr_projet03.modeConsole;

public enum LibellesMenu_MM {

    Titre("MasterMind"),
    Jouer(String.format("%d -> Jouer",Titre.ordinal()+1)),
    Retour(String.format("%d -> Retour Menu Principal",Jouer.ordinal()+1)),
    VoiParametre(String.format("%d -> Voir Paramètres",Retour.ordinal()+1)),
    Quitter(String.format("%c -> Quitter",'Q'));

    private String libelle;

    LibellesMenu_MM(String s1) {
        libelle=s1;
    }

    public String getLibelle() {
        return libelle;
    }
}

