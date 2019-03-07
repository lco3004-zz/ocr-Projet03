package fr.ocr.modeConsole;

import static  fr.ocr.utiles.LogApplicatifs.logger;

import java.util.Scanner;

import static fr.ocr.utiles.Messages.ErreurMessages.*;

import static fr.ocr.modeConsole.Libelles.LibellesMenu_Secondaire.*;
import  fr.ocr.modeConsole.Libelles.LibellesMenu_Secondaire;

public class Menu_Secondaire extends Menu {

    public Menu_Secondaire(String titre, Scanner sc) {

        super(LibellesMenu_Secondaire.values(),"[1-3 R r V v Q q]",LIGNE_ETAT,sc);

        Character c;
        for (LibellesMenu_Secondaire libellesMenu_secondaire: LibellesMenu_Secondaire.values()) {
            switch (libellesMenu_secondaire) {

                case TITRE:
                    add(TITRE,titre);
                    break;
                case JEUX:
                    add(JEUX,String.format("    %s : ","MODE de JEUX "));
                    break;
                case MODE_CHALLENGER:
                    c = '1';
                    add(MODE_CHALLENGER,String.format("        %c -> MODE_CHALLENGER",c),c);

                    break;
                case MODE_DEFENSEUR:
                    c = '2';
                    add(MODE_DEFENSEUR,String.format("        %c -> MODE_DEFENSEUR",c),c);

                    break;
                case MODE_DUEL:
                    c = '3';
                    add(MODE_DUEL,String.format("        %c -> MODE_DUEL",c),c);

                    break;
                case RETOUR:
                    c = 'R';
                    add(RETOUR,String.format("    %c -> RETOUR Menu Principal",c),c);
                    break;
                case LOGGER_PARAMETRES:
                    c = 'V';
                    add(LOGGER_PARAMETRES,String.format("    %c -> LOGGER les Paramètres",c),c);
                    break;
                case QUITTER:
                    c = 'Q';
                    add(QUITTER,String.format("    %c -> QUITTER",c),c);
                    break;
                case LIGNE_ETAT:
                    add(LIGNE_ETAT,"[                 ]");
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
    public LibellesMenu_Secondaire RunMenu() {
        return  (LibellesMenu_Secondaire) super.RunMenu();
    }
}
