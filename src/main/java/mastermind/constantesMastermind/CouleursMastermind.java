package mastermind.constantesMastermind;

/**
 *
 */
public enum CouleursMastermind {
    AUBERGINE ((byte)0) ,
    JAUNE((byte)1),
    VERT((byte)2),
    ROUGE((byte)3),
    BLEU((byte)4),
    ORANGE((byte)5),
    VIOLET((byte)6),
    ROSE((byte)7),
    INDIGO((byte)8),
    ABRICOT((byte)9);

    /**
     *
     */
    private Byte valeurFacialeDeLaCouleur;

    /**
     *
     * @param i
     */
    CouleursMastermind(Byte i) {
        valeurFacialeDeLaCouleur =i;
    }

    public Byte getValeurFacialeDeLaCouleur() {
        return valeurFacialeDeLaCouleur;
    }
}
