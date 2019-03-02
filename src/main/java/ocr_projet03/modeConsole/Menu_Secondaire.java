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
import static ocr_projet03.modeConsole.LibellesMenu_Secondaire.*;

public class Menu_Secondaire extends Menu {

    private String pattern_MenuSecondaire = "[1-2 P p Q q]";
    private ArrayList<LigneMenu> lignesMenuSecondaire = new ArrayList<>(LibellesMenu_Secondaire.values().length);

    public Menu_Secondaire(String titre, Scanner sc) {
        super(sc);

        Character c;
        for (LibellesMenu_Secondaire libellesMenu_secondaire: LibellesMenu_Secondaire.values()) {
            switch (libellesMenu_secondaire) {

                case TITRE:
                    lignesMenuSecondaire.add(new LigneMenu(TITRE,titre));
                    break;
                case Jouer:
                    c = '1';
                    lignesMenuSecondaire.add(new LigneMenu(Jouer,String.format("%c -> Jouer",c),c));

                    break;
                case Retour:
                    c = '2';
                    lignesMenuSecondaire.add(new LigneMenu(Retour,String.format("%c -> Retour Menu Principal",c),c));
                    break;
                case VoirParametres:
                    c = 'P';
                    lignesMenuSecondaire.add(new LigneMenu(VoirParametres,String.format("%c -> Voir Paramètres",c),c));
                    break;
                case Quoitter:
                    c = 'Q';
                    lignesMenuSecondaire.add(new LigneMenu(Quoitter,String.format("%c ->Quitter",c),c));
                    break;
                default:
                    logger.error(ParamInconnu.getMessageErreur());
            }
        }

        initSuperClasseMenu(LibellesMenu_Secondaire.values());
    }
    private  void initSuperClasseMenu(LibellesMenu_Secondaire [] t) {
        super.InitialiseMenu(t,pattern_MenuSecondaire,lignesMenuSecondaire);
    }
    @Override
    public LibellesMenu_Secondaire RunMenu() {
        return  (LibellesMenu_Secondaire) super.RunMenu();
    }
}
