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
import static ocr_projet03.modeConsole.LibellesMenu_Principal.Quitter;
import static ocr_projet03.modeConsole.LibellesMenu_Secondaire.*;

public class Menu_Secondaire {
    private Locale FRENCH = Locale.forLanguageTag("fr");
    private Scanner scanner ;
    private Pattern patternChoix ;
    private String pattern_MenuSecondaire = "[1-2 P p Q q]";
    private ArrayList<LigneMenu> lignesMenuSecondaire = new ArrayList<>(LibellesMenu_Secondaire.values().length);

    public Menu_Secondaire(String titre, Scanner sc) {
        scanner = sc;
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
                case Quitter:
                    c = 'Q';
                    lignesMenuSecondaire.add(new LigneMenu(Quitter,String.format("%c ->Quitter",c),c));
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
    private Character acquiert() throws IOException, InterruptedException {
        patternChoix = Pattern.compile(pattern_MenuSecondaire);
        String choix="";
        ClearScreen.cls();
        affiche();
        while ( choix.equals("") && scanner.hasNext()) {

            try {
                choix = scanner.next(patternChoix).toUpperCase();
            }catch (InputMismatchException e1) {
                String tmp =scanner.next();
                ClearScreen.cls();
                affiche();
            }
        }
        return choix.toUpperCase().charAt(0);
    }


    public LibellesMenu_Secondaire RunMenu () {
        LibellesMenu_Secondaire libellesMenu_secondaire =null;
        try {
            Character codeRet = acquiert();
            for ( LigneMenu ligneMenu :lignesMenuSecondaire) {
                if (ligneMenu.getSelecteur() ==codeRet)
                    libellesMenu_secondaire = (LibellesMenu_Secondaire)ligneMenu.getReferenceLibelle();
            }
        }
        catch (Exception e) {
            logger.error(ErreurGeneric);
        }
        return libellesMenu_secondaire;

    }
}
