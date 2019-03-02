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


    public  Menu ( Scanner sc) {

        scanner =sc;
    }
    public  void InitialiseMenu(T [] x, String pattern, ArrayList<LigneMenu> m ) {
        libellesMenu = x;
        pattern_Menu = pattern;
        ligneMenus =m;
    }
    public T RunMenu () {

        T z = null;
        try {
            Character codeRet = LectureClavier();
            for ( LigneMenu ligneMenu :ligneMenus) {
                if (ligneMenu.getSelecteur() ==codeRet) {
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
    public Character LectureClavier () throws IOException, InterruptedException{
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

    public void DisplayMenu () {
        int j =0;
        for (LigneMenu ligneMenu: ligneMenus) {
            System.out.println(ligneMenu.getLibelle_Ligne());
        }
    }
}
