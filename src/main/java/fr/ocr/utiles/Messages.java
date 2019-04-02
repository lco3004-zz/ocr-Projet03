package fr.ocr.utiles;

/**
 * <p>
 *
 * @author laurent cordier
 * </p>
 */
public enum Messages {
    ;

    public enum DebugMessages {
        X("");
        private String messageDebug;

        DebugMessages(String s) {
            messageDebug = s;
        }

        public String getMessageDebug() {
            return messageDebug;
        }
    }

    public enum ErreurMessages {
        PARAM_INCONNU("Nom de Parametre Inconnu"),
        TYPE_PARAM_INCORRECT("Type de Parametre inconnu"),
        VALEUR_PARAM_INCORRECT("Parametre hors plage tolérance"),
        CHOIX_RANDOM_NON_COMPLET(" Random ne rend pas assez de COuleru au hazard - Choix du secretpar defaut - Allo houston on a un pb"),
        MODE_IO_INCONNU("mode IO inconnu ou incompatible avec constructeur"),
        PARAMETRAGE_ILLISIBLE("Impossible de lire le fichier parametre (parametres par defaut) - "),
        ECRITURE_PARAMETRES_IMPOSSIBLE("Ecriture dans fichier Parametre impossible - "),
        SORTIE_PGM_SUR_ERREURNONGEREE("Exception inattendue - fin pgm"),
        ERREUR_GENERIC("Ne doit pas etre utilisée!");

        private String messageErreur;

        ErreurMessages(String s) {
            messageErreur = s;
        }

        public String getMessageErreur() {
            return messageErreur;
        }
    }

    public enum InfosMessages {
        LANCEMENT_APPLICATION("Lancement Application "),
        FIN_NORMALE_APPLICATION("Fin normale Application"),
        LANCEMENT_GESTION_DES_PARAMETRES("Lancement LireParametres"),
        CREATION_FICHIER_PARAMETRE("Creation du fichier Parametres - "),
        FIN_NORMALE_GESTION_DES_PARAMETRES("Fin Normale LireParametres"),
        CTRL_C("interuption programme par CTRL-C"),
        SORTIE_SUR_ESCAPECHAR("Sortie par escape char"),
        REMPLACEMENT_PAR_VALEUR_DEFAUT("Remplacement par la valeur par défaut =  "),
        RANDOM_ECHOUE_COLPARDEF("Le Random a échoué, les premières couleurs de la liste sont retenues"),
        TENTAIVE_DE_TRICHE_DEFENSEUR("Incoherence score entre calcul et saisie  - Suspicion de fraude"),
        SORTIE_ESCAPE_SAISIE_SCORE("Sortie sur escape saisie Score mode Defenseur, copie du score calculé"),
        PM_CHIFFRE_HORS_PLAGE_TOLERANCE("Le chiffre est négatif ou nul . hors plage de tolérance dans le jeu PlusMoins");

        private String messageInformation;

        InfosMessages(String s) {
            messageInformation = s;
        }

        public String getMessageInfos() {
            return messageInformation;
        }
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */