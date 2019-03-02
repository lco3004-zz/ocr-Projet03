package ocr_projet03.modeConsole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ErreurGeneric;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ParamInconnu;
import static ocr_projet03.modeConsole.LibellesMenu_Principal.*;

public class Menu_Principal extends Menu<LibellesMenu_Principal> {

    private String pattern_MenuPrincipal = "[1-2 Q q]";

    private ArrayList<LigneMenu> lignesMenuPrincipal = new ArrayList<>(LibellesMenu_Principal.values().length);

    public Menu_Principal(Scanner sc) {
        super(sc);

        Character c;
        for (LibellesMenu_Principal libellesMenu_principal: LibellesMenu_Principal.values()) {
            switch (libellesMenu_principal) {

                case Titre:
                    lignesMenuPrincipal.add(new LigneMenu(Titre,"OCR-Projet03 - Menu Principal"));
                    break;
                case Choisir_Mastermind:
                    c = '1';
                    lignesMenuPrincipal.add(new LigneMenu(Choisir_Mastermind,String.format("%c -> pour jouer au MasterMind",c),c));
                    break;
                case Choisir_PlusMoins:
                    c = '2';
                    lignesMenuPrincipal.add(new LigneMenu(Choisir_PlusMoins,String.format("%c -> pour jouer au PlusMoins",c),c));
                    break;
                case Quitter:
                    c = 'Q';
                    lignesMenuPrincipal.add(new LigneMenu(Quitter,String.format("%c -> Quitter",c),c));
                    break;
                default:
                    logger.error(ParamInconnu.getMessageErreur());
            }
        }

        initSuperClasseMenu(LibellesMenu_Principal.values());
    }
    private  void initSuperClasseMenu(LibellesMenu_Principal [] t) {
        super.InitialiseMenu(t,pattern_MenuPrincipal,lignesMenuPrincipal);
    }
    @Override
    public LibellesMenu_Principal RunMenu() {
        return  (LibellesMenu_Principal) super.RunMenu();
    }
}
