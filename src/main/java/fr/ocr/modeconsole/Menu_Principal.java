package fr.ocr.modeconsole;

import fr.ocr.modeconsole.Libelles.LibellesMenuPrincipal;
import fr.ocr.utiles.ApplicationExceptions;

import java.util.Scanner;

import static fr.ocr.modeconsole.Libelles.LibellesMenuPrincipal.*;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.PARAM_INCONNU;


public class Menu_Principal extends Menu<LibellesMenuPrincipal> {

    public Menu_Principal(Scanner sc) {
        super(LibellesMenuPrincipal.values(),"[1-2 Q q]",LIGNE_ETAT,sc);
        Character c;
        for (LibellesMenuPrincipal libellesMenu_principal: LibellesMenuPrincipal.values()) {
            switch (libellesMenu_principal) {
                case TITRE:
                    addLigneMenu(TITRE,"OCR-Projet03 - Menu Principal");
                    break;
                case CHOISIR_MASTERMIND:
                    c = '1';
                    addLigneMenu(CHOISIR_MASTERMIND,String.format("    %c -> JOUER au MASTERMIND",c),c);
                    break;
                case CHOISIR_PLUS_MOINS:
                    c = '2';
                    addLigneMenu(CHOISIR_PLUS_MOINS,String.format("    %c -> JOUER au PLUSMOINS",c),c);
                    break;
                case QUITTER:
                    c = Libelles.CharactersEscape.Q.toString().charAt(0);
                    addLigneMenu(QUITTER,String.format("    %c -> QUITTER",c),c);
                    break;
                case LIGNE_ETAT:
                    addLigneMenu(LIGNE_ETAT,"[-- pgm prêt--]\n");
                    break;
                case SAISIR_CHOIX:
                    addLigneMenu(SAISIR_CHOIX,String.format("%s : ", "Votre Choix "));
                    break;
                default:
                    logger.error(PARAM_INCONNU.getMessageErreur());
            }
        }

    }

    @Override
    public LibellesMenuPrincipal RunMenu() throws ApplicationExceptions {
        return   super.RunMenu();
    }
}
