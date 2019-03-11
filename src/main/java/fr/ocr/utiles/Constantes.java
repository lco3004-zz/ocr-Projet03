package fr.ocr.utiles;

public enum Constantes {
    ;

    public enum VersionPGM {
        VERSION_APPLICATION("V0.1");
        private String version;

        VersionPGM(String s) {
            version = s;
        }

        public String getVersion() {
            return version;
        }
    }

    public enum NomFichiersParametres {
        FICHIER_PARAM_MASTER_MIND("target/params.properties");

        private String nomFichier;

        NomFichiersParametres(String s) {
            nomFichier = s;
        }

        public String getNomFichier() {
            return nomFichier;
        }
    }
    public interface ConstEvalPropale {
        //pour résultat evaluation proposition : Noir == pion bien place,  blanc == pion mal placé
        int NOIR_BIENPLACE = 0;
        int BLANC_MALPLACE = NOIR_BIENPLACE + 1;
    }
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
    /**
     * Constantes pour les menus
     */
    public enum Libelles {
        ;

        public enum CharactersEscape {
            X, //quitter
            K  //retour menu principal
        }

        /**
         * noms des jeux proposés
         */
        public enum LibellesJeux {
            MASTERMIND,
            PLUSMOINS
        }

        /**
         * libelle des lignes du menu principal
         * LIGNE_ETAT : contenu variable en fonction de l'action effectuée
         * par l'utilisateur
         */
        public enum LibellesMenuPrincipal {
            TITRE,
            LIGNE_ETAT,
            CHOISIR_MASTERMIND,
            CHOISIR_PLUS_MOINS,
            QUITTER,
            SAISIR_CHOIX
        }

        /**
         * libelle des lignes du menu secondaire
         * LIGNE_ETAT : contenu variable en fonction de l'action effectuée
         * par l'utilisateur .
         * TITRE  varie selon le choix effectué dans le menu principal (Mastemrind ou PlusMoins
         */
        public enum LibellesMenuSecondaire {

            TITRE,
            LIGNE_ETAT,
            JEUX,
            MODE_CHALLENGER,
            MODE_DEFENSEUR,
            MODE_DUEL,
            RETOUR,
            LOGGER_PARAMETRES,
            QUITTER,
            SAISIR_CHOIX
        }
    }
}
