package ocr_projet03.modeConsole;

import java.util.ArrayList;

import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ParamInconnu;
import static ocr_projet03.modeConsole.LibellesMenu_Principal.*;

public class Menu_Principal {

    private String pattern_MenuPrincipal = "[1-2]Qq";
    private ArrayList<LigneMenu> lignesMenuPrincipal = new ArrayList<>(LibellesMenu_Principal.values().length);

    public Menu_Principal() {
        Character c;
        for (LibellesMenu_Principal libellesMenu_principal: LibellesMenu_Principal.values()) {
            switch (libellesMenu_principal) {

                case Titre:
                    lignesMenuPrincipal.add(new LigneMenu(Titre,"OCR-Projet03 - Menu Principal"));
                    break;
                case Choisir_Mastermind:
                    c = pattern_MenuPrincipal.charAt(1);
                    lignesMenuPrincipal.add(new LigneMenu(Choisir_Mastermind,String.format("%c -> pour jouer au MasterMind",c)));
                    break;
                case Choisir_PlusMoins:
                    c = pattern_MenuPrincipal.charAt(3);
                    lignesMenuPrincipal.add(new LigneMenu(Choisir_PlusMoins,String.format("%c -> pour jouer au PlusMoins",c)));
                    break;
                case Quitter:
                    c = pattern_MenuPrincipal.charAt(5);
                    lignesMenuPrincipal.add(new LigneMenu(Quitter,String.format("%c -> Quitter",c)));
                    break;
                default:
                    logger.error(ParamInconnu.getMessageErreur());
            }
        }
    }
    private void affiche() {
        for (LigneMenu ligneMenu: lignesMenuPrincipal) {
            System.out.println(ligneMenu.getLibelle_Ligne());
        }
    }

    public String getPattern_MenuPrincipal() {
        return pattern_MenuPrincipal;
    }

    public void RunMenuPrincipal () {
        affiche();
    }
}
