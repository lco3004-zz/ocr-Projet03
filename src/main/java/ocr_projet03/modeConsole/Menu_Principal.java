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

public class Menu_Principal {
    private Locale FRENCH = Locale.forLanguageTag("fr");
    private Scanner scanner ;
    private Pattern patternChoix ;
    private String pattern_MenuPrincipal = "[1-2 Q q]";

    private ArrayList<LigneMenu> lignesMenuPrincipal = new ArrayList<>(LibellesMenu_Principal.values().length);

    public Menu_Principal(Scanner sc) {
        scanner =sc;
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
    }

    public String getPattern_MenuPrincipal() {
        return pattern_MenuPrincipal;
    }
    private void affiche() {
        for (LigneMenu ligneMenu: lignesMenuPrincipal) {
            System.out.println(ligneMenu.getLibelle_Ligne());
        }
    }
    private Character acquiert() throws IOException, InterruptedException {
        patternChoix = Pattern.compile(pattern_MenuPrincipal);
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


    public LibellesMenu_Principal RunMenu () {
        LibellesMenu_Principal libellesMenu_principal =null;
        try {
            Character codeRet = acquiert();
            for ( LigneMenu ligneMenu :lignesMenuPrincipal) {
                if (ligneMenu.getSelecteur() ==codeRet)
                    libellesMenu_principal = (LibellesMenu_Principal)ligneMenu.getReferenceLibelle();
            }
        }
        catch (Exception e) {
            logger.error(ErreurGeneric);
        }
        return libellesMenu_principal;

    }
}
