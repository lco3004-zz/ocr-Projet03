package fr.ocr.modeconsole;

import fr.ocr.modeconsole.Libelles.LibellesMenuPrincipal;
import fr.ocr.utiles.AppExceptions;

import java.util.Scanner;

import static fr.ocr.modeconsole.Libelles.LibellesMenuPrincipal.*;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.PARAM_INCONNU;

/**
 * Menu principal de l'application - selection du jeux
 * <p>
 * hérite de la class Menu qui se charge du comportement d'un menu (afffichage, saisie)
 */
public class MenuPrincipal extends Menu<LibellesMenuPrincipal> {

    /**
     * construit le menu principal en fournissant les chaines de caractères à afficher,
     * le pattern de controle de saisie, la référence d'instance Enum LibellesMenuPrincipal qui
     * est la ligne qui sert de stausbar 'LIGNE_ETAT et le scanner pour la saisie clavier
     *
     * @param sc scanner (lib java)
     */
    public MenuPrincipal(Scanner sc) {

        super(LibellesMenuPrincipal.values(), "[1-2 Q q]", LIGNE_ETAT, sc);

        Character c;

        for (LibellesMenuPrincipal libellesMenu_principal : LibellesMenuPrincipal.values()) {
            switch (libellesMenu_principal) {
                case TITRE:
                    addLigneMenu(TITRE, "OCR-Projet03 - Menu Principal");
                    break;
                case CHOISIR_MASTERMIND:
                    c = '1';
                    addLigneMenu(CHOISIR_MASTERMIND, String.format("    %c -> JOUER au MASTERMIND", c), c);
                    break;
                case CHOISIR_PLUS_MOINS:
                    c = '2';
                    addLigneMenu(CHOISIR_PLUS_MOINS, String.format("    %c -> JOUER au PLUSMOINS", c), c);
                    break;
                case QUITTER:
                    c = Libelles.CharactersEscape.Q.toString().charAt(0);
                    addLigneMenu(QUITTER, String.format("    %c -> QUITTER", c), c);
                    break;
                case LIGNE_ETAT:
                    addLigneMenu(LIGNE_ETAT, "[-- pgm prêt--]\n");
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
     * @return instance d'une classe de LibellesMenuPrincipal qui correspond à  l'action à réaliser
     * 'i.e : QUITTER, JOUER ...)
     *
     * @throws AppExceptions : sur erreur non gérée
     */
    @Override
    public LibellesMenuPrincipal RunMenu() throws AppExceptions {
        return super.RunMenu();
    }
}
