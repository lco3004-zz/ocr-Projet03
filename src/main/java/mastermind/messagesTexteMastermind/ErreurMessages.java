package mastermind.messagesTexteMastermind;

public enum ErreurMessages {
    ParamInconnu("Nom de Parametre Inconnu\""),
    TypeParamIncorrect("Type de Parametre inconnu"),
    ChoixRandomNonComplet(" Random ne rend pas assez de COuleru au hazard - Choix du secretpar defaut - Allo houston on a un pb"),
    ModeIOInconnu ("mode IO inconnu ou incompatible avec constructeur"),
    ParametrageIllisible("Impossible de lire le fichier parametre (parametres par defaut) - "),
    EcritureParametresImpossible("Ecriture dans fichier Parametre impossible - "),
    ErreurGeneric("Ne doit pas etre utilisée!");

    private String MessageErreur;
    ErreurMessages(String s) {
        MessageErreur=s;
    }

    public String getMessageErreur() {
        return MessageErreur;
    }
}
