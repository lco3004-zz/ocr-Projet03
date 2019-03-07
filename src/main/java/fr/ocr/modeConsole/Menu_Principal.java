package fr.ocr.modeConsole;

import static fr.ocr.utiles.LogApplicatifs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.*;

import java.util.Scanner;

import static fr.ocr.modeConsole.Libelles.LibellesMenu_Principal.*;
import  fr.ocr.modeConsole.Libelles.LibellesMenu_Principal;


public class Menu_Principal extends Menu<LibellesMenu_Principal> {

    public Menu_Principal(Scanner sc) {
        super(LibellesMenu_Principal.values(),"[1-2 Q q]",LIGNE_ETAT,sc);
        Character c;
        for (LibellesMenu_Principal libellesMenu_principal: LibellesMenu_Principal.values()) {
            switch (libellesMenu_principal) {
                case TITRE:
                    add(TITRE,"OCR-Projet03 - Menu Principal");
                    break;
                case Choisir_Mastermind:
                    c = '1';
                    add(Choisir_Mastermind,String.format("    %c -> JOUER au MASTERMIND",c),c);
                    break;
                case Choisir_PlusMoins:
                    c = '2';
                    add(Choisir_PlusMoins,String.format("    %c -> JOUER au PLUSMOINS",c),c);
                    break;
                case QUITTER:
                    c = 'Q';
                    add(QUITTER,String.format("    %c -> QUITTER",c),c);
                    break;
                case LIGNE_ETAT:
                    add(LIGNE_ETAT,"[-- pgm prêt--]\n");
                    break;
                case SAISIR_CHOIX:
                    add(SAISIR_CHOIX,String.format("%s : ", "Votre Choix "));
                    break;
                default:
                    logger.error(ParamInconnu.getMessageErreur());
            }
        }

    }

    @Override
    public LibellesMenu_Principal RunMenu() {
        return   super.RunMenu();
    }
}
