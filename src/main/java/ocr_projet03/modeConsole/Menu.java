package ocr_projet03.modeConsole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ErreurGeneric;
import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;


public abstract class Menu <T extends Enum> {
    private T [] libellesMenu;
    private ArrayList<LigneMenu> ligneMenus ;
    private Scanner scanner ;
    private Pattern patternChoix ;
    private String pattern_Menu  ;
    private LigneMenu <T> statusBar;




    Menu ( Scanner sc) {
        scanner =sc;
    }
    void InitialiseMenu(T [] x, String pattern, ArrayList<LigneMenu> m ,T refStatusBar) {
        libellesMenu = x;
        pattern_Menu = pattern;
        ligneMenus =m;

        statusBar = setStatusBar(refStatusBar);
    }

    Character LectureClavier () throws IOException, InterruptedException{
        patternChoix = Pattern.compile(pattern_Menu);
        String choix="";
        ClearScreen.cls();
        DisplayMenu();
        while ( choix.equals("") && scanner.hasNext()) {

            try {
                choix = scanner.next(patternChoix).toUpperCase();
            }catch (InputMismatchException e1) {
                String tmp =scanner.next();
                ClearScreen.cls();
                DisplayMenu();
            }
        }
        return choix.toUpperCase().charAt(0);
    }

    LigneMenu <T> setStatusBar(T refStatusBar) {
        LigneMenu <T> valRet =null;
        for (LigneMenu cetteligne: ligneMenus) {
            if (cetteligne.getReferenceLibelle() == refStatusBar) {
                valRet= cetteligne;
                break;
            }
        }
        return valRet;
    }

    void DisplayMenu () {
        int j =0;
        for (LigneMenu ligneMenu: ligneMenus) {
            if (ligneMenus.get(ligneMenus.size()-1) == ligneMenu) {
                System.out.print(ligneMenu.getLibelle_Ligne());
            }else {
                System.out.println(ligneMenu.getLibelle_Ligne());
            }
        }
    }

    public T RunMenu () {

        T z = null;
        try {
            Character codeRet = LectureClavier();
            for ( LigneMenu ligneMenu :ligneMenus) {
                if ((ligneMenu.getSelecteur() != null) && (ligneMenu.getSelecteur() ==codeRet)) {
                    for (T q :libellesMenu) {
                        if (q == ligneMenu.getReferenceLibelle()) {
                            z = q;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error(ErreurGeneric);
        }
        return z;

    }

    public void majLigneEtat(String s) {
        String modelStatusBar = "[-- "+s+" --]";
        statusBar.setLibelleLigne(modelStatusBar);
        DisplayMenu();
    }
}
