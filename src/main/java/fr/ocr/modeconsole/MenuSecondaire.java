package fr.ocr.modeconsole;

import fr.ocr.modeconsole.Libelles.LibellesMenuSecondaire;
import fr.ocr.utiles.AppExceptions;

import java.util.Scanner;

import static fr.ocr.modeconsole.Libelles.LibellesMenuSecondaire.*;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.PARAM_INCONNU;

/**
 * Menu secondaire de l'application - menu de Mastermind ou de PlusMoins
 * <p>
 * hérite de la class Menu qui se charge du comportement d'un menu (afffichage, saisie)
 */
public class MenuSecondaire extends Menu<LibellesMenuSecondaire> {

    /**
     * construit le menu secondaire en fournissant les chaines de caractères à afficher,
     * le pattern de controle de saisie, la référence d'instance Enum LibellesMenuSecondaire qui
     * est la ligne qui sert de stausbar 'LIGNE_ETAT et le scanner pour la saisie clavier
     *
     * @param sc scanner (lib java)
     */
    public MenuSecondaire(String titre, Scanner sc) {

        super(LibellesMenuSecondaire.values(), "[1-3 R r V v Q q]", LIGNE_ETAT, sc);

        Character c;
        for (LibellesMenuSecondaire libellesMenu_secondaire : LibellesMenuSecondaire.values()) {
            switch (libellesMenu_secondaire) {

                case TITRE:
                    addLigneMenu(TITRE, titre);
                    break;
                case JEUX:
                    addLigneMenu(JEUX, String.format("    %s : ", "MODE de JEUX "));
                    break;
                case MODE_CHALLENGER:
                    c = '1';
                    addLigneMenu(MODE_CHALLENGER, String.format("        %c -> MODE_CHALLENGER", c), c);

                    break;
                case MODE_DEFENSEUR:
                    c = '2';
                    addLigneMenu(MODE_DEFENSEUR, String.format("        %c -> MODE_DEFENSEUR", c), c);

                    break;
                case MODE_DUEL:
                    c = '3';
                    addLigneMenu(MODE_DUEL, String.format("        %c -> MODE_DUEL", c), c);

                    break;
                case RETOUR:
                    c = Libelles.CharactersEscape.R.toString().charAt(0);
                    addLigneMenu(RETOUR, String.format("    %c -> RETOUR Menu Principal", c), c);
                    break;
                case LOGGER_PARAMETRES:
                    c = 'V';
                    addLigneMenu(LOGGER_PARAMETRES, String.format("    %c -> LOGGER les Paramètres", c), c);
                    break;
                case QUITTER:
                    c = Libelles.CharactersEscape.Q.toString().charAt(0);
                    addLigneMenu(QUITTER, String.format("    %c -> QUITTER", c), c);
                    break;
                case LIGNE_ETAT:
                    addLigneMenu(LIGNE_ETAT, "[                 ]");
                    break;
                case SAISIR_CHOIX:
                    addLigneMenu(SAISIR_CHOIX, String.format("%s : ", "Votre Choix "));
                    break;
                default:
                    logger.error(PARAM_INCONNU.getMessageErreur());
            }
        }

    }

    /**
     * @return instance d'une classe de LibellesMenuPSecondaie qui correspond à  l'action à réaliser
     * 'i.e : QUITTER, JOUER ...)
     *
     * @throws AppExceptions : sur erreur non gérée
     */
    @Override
    public LibellesMenuSecondaire RunMenu() throws AppExceptions {
        return super.RunMenu();
    }
}
