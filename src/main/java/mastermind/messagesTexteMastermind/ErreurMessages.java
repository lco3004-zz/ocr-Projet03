package mastermind.messagesTexteMastermind;

public enum ErreurMessages {
    ParamInconnu("Nom de Parametre Inconnu\""),
    TypeParamIncorrect("Type de Parametre inconnu"),
    ChoixRandomNonComplet(" Random ne rend pas assez de COuleru au hazard - Choix du secretpar defaut - Allo houston on a un pb"),
    ModeIOInconnu ("mode IO inconnu ou incompatible avec constructeur"),
    ErreurGeneric("Ne doit pas etre utulisee");

    /**
     *
     */
    private String messageDuCodeErreur;
    ErreurMessages(String s) {
        messageDuCodeErreur=s;
    }

    /**
     *
     */
    public String getMessageDuCodeErreur() {
        return messageDuCodeErreur;
    }
}
