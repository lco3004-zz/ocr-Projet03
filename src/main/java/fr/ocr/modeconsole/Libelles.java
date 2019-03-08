package fr.ocr.modeconsole;

/**
 * Constantes pour les menus
 */
public enum Libelles {
    ;

    public enum CharactersEscape {
        Q, //quitter
        R  //retour menu principal
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
