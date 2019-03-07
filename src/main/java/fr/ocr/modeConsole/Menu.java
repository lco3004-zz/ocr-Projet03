package fr.ocr.modeConsole;


import static fr.ocr.utiles.LogApplicatifs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;


class LigneMenu <T extends Enum>{
    private String libelleLigne;
    private T referenceLibelle;
    private Character selecteur;

    LigneMenu (  T  x, String s,Character c) {
        libelleLigne =s;
        referenceLibelle = x;
        selecteur=c;

    }
    String getLibelle_Ligne() {
        return libelleLigne;
    }

    T getReferenceLibelle() {
        return referenceLibelle;
    }
    Character getSelecteur() { return selecteur; }

    void setLibelleLigne(String libelleLigne) {
        this.libelleLigne = libelleLigne;
    }
}


final class ClearScreen {
    static  void cls()  {

        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cmd.exe /c cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (  InterruptedException | IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
public  abstract class  Menu <T extends Enum>{
    private T [] libellesMenu;
    private ArrayList<LigneMenu> lignesMenu;
    private Scanner scanner ;
    private Pattern patternChoix ;
    private String pattern_Menu  ;
    private LigneMenu <T> statusBar;
    private T clefStatusBar;


    Menu(T [] t, String pattern, T refStatusBar,Scanner sc) {
        scanner =sc;
        lignesMenu = new  ArrayList<>(256);
        libellesMenu = t;
        pattern_Menu = pattern;
        clefStatusBar = refStatusBar;
    }

    void add(T x, String v) {
        this.add(x,v,null);
    }
    void add(T x, String v,Character c) {
        LigneMenu ligneMenu = new LigneMenu(x,v,c);
        lignesMenu.add(ligneMenu );
        if (x.equals(clefStatusBar))
            statusBar =ligneMenu;
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

    void DisplayMenu () {
        int j =0;
        for (LigneMenu ligneMenu: lignesMenu) {
            if (lignesMenu.get(lignesMenu.size()-1) == ligneMenu) {
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
            for ( LigneMenu ligneMenu : lignesMenu) {
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
