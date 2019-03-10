package fr.ocr.utiles;

public enum CouleursMastermind {
    AUBERGINE( 0,'A'),
    JAUNE(1,'J'),
    VERT( 2,'V'),
    ROUGE( 3,'R'),
    BLEU( 4,'B'),
    ORANGE( 5,'O'),
    TURQUOISE( 6,'T'),
    MAUVE( 7,'M'),
    INDIGO( 8,'I'),
    CARMIN( 9,'C');

    private int valeurFacialeDeLaCouleur;
    private char lettreInitiale;

    CouleursMastermind(int i, char c) {
        valeurFacialeDeLaCouleur = i;
        lettreInitiale = c;
    }

    public int getValeurFacialeDeLaCouleur() {
        return valeurFacialeDeLaCouleur;
    }
    public char getLettreInitiale () {return lettreInitiale;}
}
