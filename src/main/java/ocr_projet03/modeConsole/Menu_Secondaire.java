package ocr_projet03.modeConsole;

import java.util.ArrayList;

import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ParamInconnu;
import static ocr_projet03.modeConsole.LibellesMenu_Principal.*;
import static ocr_projet03.modeConsole.LibellesMenu_Principal.Quitter;
import static ocr_projet03.modeConsole.LibellesMenu_Secondaire.*;

public class Menu_Secondaire {
    private String pattern_MenuSecondaire = "[1-2]PpQq";
    private ArrayList<LigneMenu> lignesMenuSecondaire = new ArrayList<>(LibellesMenu_Secondaire.values().length);

    public Menu_Secondaire(String titre) {
        Character c;
        for (LibellesMenu_Secondaire libellesMenu_secondaire: LibellesMenu_Secondaire.values()) {
            switch (libellesMenu_secondaire) {

                case TITRE:
                    lignesMenuSecondaire.add(new LigneMenu(TITRE,titre));
                    break;
                case Jouer:
                    c = pattern_MenuSecondaire.charAt(1);
                    lignesMenuSecondaire.add(new LigneMenu(Jouer,String.format("%c -> Jouer",c)));

                    break;
                case Retour:
                    c = pattern_MenuSecondaire.charAt(3);
                    lignesMenuSecondaire.add(new LigneMenu(Jouer,String.format("%c -> Retour Menu Principal",c)));
                    break;
                case VoiParametre:
                    c = pattern_MenuSecondaire.charAt(5);
                    lignesMenuSecondaire.add(new LigneMenu(VoiParametre,String.format("%c -> Voir Paramètres",c)));
                    break;
                case Quitter:
                    lignesMenuSecondaire.add(new LigneMenu(Quitter,String.format("%c ->Quitter",'Q')));
                    break;
                default:
                    logger.error(ParamInconnu.getMessageErreur());
            }
        }
    }
    private void affiche() {
        for (LigneMenu ligneMenu: lignesMenuSecondaire) {
            System.out.println(ligneMenu.getLibelle_Ligne());
        }
    }

    public String getpattern_MenuSecondaire() {
        return pattern_MenuSecondaire;
    }

    public void RunMenuSecondaire () {
        affiche();
    }

}
