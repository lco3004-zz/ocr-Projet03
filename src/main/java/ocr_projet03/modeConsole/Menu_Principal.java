package ocr_projet03.modeConsole;

import java.util.ArrayList;
import java.util.Scanner;

import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ParamInconnu;
import static ocr_projet03.modeConsole.LibellesMenu_Principal.*;

public class Menu_Principal extends Menu<LibellesMenu_Principal> {

    private String pattern_MenuPrincipal = "[1-2 Q q]";

    private ArrayList<LigneMenu> lignesMenuPrincipal = new ArrayList<>(LibellesMenu_Principal.values().length);
    private String statusbar;

    public Menu_Principal(Scanner sc) {
        super(sc);
        Character c;
        statusbar="[-- pgm prêt--]\n";
        for (LibellesMenu_Principal libellesMenu_principal: LibellesMenu_Principal.values()) {
            switch (libellesMenu_principal) {

                case TITRE:
                    lignesMenuPrincipal.add(new LigneMenu(TITRE,"OCR-Projet03 - Menu Principal"));
                    break;
                case Choisir_Mastermind:
                    c = '1';
                    lignesMenuPrincipal.add(new LigneMenu(Choisir_Mastermind,String.format("    %c -> JOUER au MASTERMIND",c),c));
                    break;
                case Choisir_PlusMoins:
                    c = '2';
                    lignesMenuPrincipal.add(new LigneMenu(Choisir_PlusMoins,String.format("    %c -> JOUER au PLUSMOINS",c),c));
                    break;
                case QUITTER:
                    c = 'Q';
                    lignesMenuPrincipal.add(new LigneMenu(QUITTER,String.format("    %c -> QUITTER",c),c));
                    break;
                case LIGNE_ETAT:
                    lignesMenuPrincipal.add(new LigneMenu(LIGNE_ETAT,statusbar));
                    break;
                case SAISIR_CHOIX:
                    lignesMenuPrincipal.add(new LigneMenu(SAISIR_CHOIX,String.format("%s : ", "Votre Choix ")));
                    break;
                default:
                    logger.error(ParamInconnu.getMessageErreur());
            }
        }

        initSuperClasseMenu(LibellesMenu_Principal.values());
    }
    private  void initSuperClasseMenu(LibellesMenu_Principal [] t) {
        super.InitialiseMenu(t,pattern_MenuPrincipal,lignesMenuPrincipal,LIGNE_ETAT);
    }
    @Override
    public LibellesMenu_Principal RunMenu() {
        return  (LibellesMenu_Principal) super.RunMenu();
    }
}
