package fr.ocr.utiles;

public enum Messages {
    ;
    public enum DebugMessages {
        X("");
        private String MessageDebug;
        DebugMessages(String s) {
            MessageDebug =s;
        }

        public String getMessageDebug() {
            return MessageDebug;
        }
    }

    public enum ErreurMessages {
        ParamInconnu("Nom de Parametre Inconnu"),
        TypeParamIncorrect("Type de Parametre inconnu"),
        ValeurParamIncorrect("Parametre hors plage tolérance"),
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

    public enum InfosMessages {
        Lancement_Application("Lancement Application "),
        FinNormale_Application("Fin normale Application"),
        Lancement_GestionDesParametres("Lancement FournisseurParams"),
        CreationFichierParametre("Creation du fichier Parametres - "),
        FinNormale_GestionDesParametres("Fin Normale FournisseurParams"),
        RemplacementParValeurDefaut("Remplacement par la valeur par défaut =  ");

        private String MessageInformation;
        InfosMessages(String s) {
            MessageInformation =s;
        }

        public String getMessageInfos() {
            return MessageInformation;
        }
    }
}
