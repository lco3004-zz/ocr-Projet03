package ocr_projet03.modeConsole;

import java.util.ArrayList;
import java.util.Scanner;

import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ParamInconnu;
import static ocr_projet03.modeConsole.LibellesMenu_Secondaire.*;

public class Menu_Secondaire extends Menu {

    private String pattern_MenuSecondaire = "[1-3 R r V v Q q]";
    private ArrayList<LigneMenu> lignesMenuSecondaire = new ArrayList<>(LibellesMenu_Secondaire.values().length);
    private String statusbar;


    public Menu_Secondaire(String titre, Scanner sc) {
        super(sc);

        statusbar="[                 ]";

        Character c;
        for (LibellesMenu_Secondaire libellesMenu_secondaire: LibellesMenu_Secondaire.values()) {
            switch (libellesMenu_secondaire) {

                case TITRE:
                    lignesMenuSecondaire.add(new LigneMenu(TITRE,titre));
                    break;
                case JEUX:
                    lignesMenuSecondaire.add(new LigneMenu(JEUX,String.format("    %s : ","MODE de JEUX ")));
                    break;
                case MODE_CHALLENGER:
                    c = '1';
                    lignesMenuSecondaire.add(new LigneMenu(MODE_CHALLENGER,String.format("        %c -> MODE_CHALLENGER",c),c));

                    break;
                case MODE_DEFENSEUR:
                    c = '2';
                    lignesMenuSecondaire.add(new LigneMenu(MODE_DEFENSEUR,String.format("        %c -> MODE_DEFENSEUR",c),c));

                    break;
                case MODE_DUEL:
                    c = '3';
                    lignesMenuSecondaire.add(new LigneMenu(MODE_DUEL,String.format("        %c -> MODE_DUEL",c),c));

                    break;
                case RETOUR:
                    c = 'R';
                    lignesMenuSecondaire.add(new LigneMenu(RETOUR,String.format("    %c -> RETOUR Menu Principal",c),c));
                    break;
                case LOGGER_PARAMETRES:
                    c = 'V';
                    lignesMenuSecondaire.add(new LigneMenu(LOGGER_PARAMETRES,String.format("    %c -> LOGGER les Paramètres",c),c));
                    break;
                case QUITTER:
                    c = 'Q';
                    lignesMenuSecondaire.add(new LigneMenu(QUITTER,String.format("    %c -> QUITTER",c),c));
                    break;
                case LIGNE_ETAT:
                    lignesMenuSecondaire.add(new LigneMenu(LIGNE_ETAT,statusbar));
                    break;
                case SAISIR_CHOIX:
                    lignesMenuSecondaire.add(new LigneMenu(SAISIR_CHOIX,String.format("%s : ", "Votre Choix ")));
                    break;
                default:
                    logger.error(ParamInconnu.getMessageErreur());
            }
        }

        initSuperClasseMenu(LibellesMenu_Secondaire.values());
    }
    private  void initSuperClasseMenu(LibellesMenu_Secondaire [] t) {
        super.InitialiseMenu(t,pattern_MenuSecondaire,lignesMenuSecondaire,LIGNE_ETAT);
    }
    @Override
    public LibellesMenu_Secondaire RunMenu() {
        return  (LibellesMenu_Secondaire) super.RunMenu();
    }
}
