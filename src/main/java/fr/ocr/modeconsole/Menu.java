package fr.ocr.modeconsole;
/**
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

/**
 *
 * @param <T>
 */
class LigneMenu <T extends Enum>{
    /**
     *
     */
    private String libelleLigne;

    /**
     *
     */
    private T referenceLibelle;

    /**
     *
     */
    private Character selecteur;

    /**
     *
     * @param x
     * @param s
     * @param c
     */
    LigneMenu (  T  x, String s,Character c) {
        libelleLigne =s;
        referenceLibelle = x;
        selecteur=c;
    }

    /**
     *
     * @return
     */
    String getLibelle_Ligne() {
        return libelleLigne;
    }

    /**
     *
     * @return
     */
    T getReferenceLibelle() {
        return referenceLibelle;
    }

    /**
     *
     * @return
     */
    Character getSelecteur() { return selecteur; }

    /**
     *
     * @param libelleLigne
     */
    void setLibelleLigne(String libelleLigne) {
        this.libelleLigne = libelleLigne;
    }
}


/**
 *
 */
final class ClearScreen {
    /**
     *
     */
    static  void cls()  {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cmd.exe /c cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }  catch (  InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

/**
 *
 * @param <T>
 */
public  abstract class  Menu <T extends Enum>{
    /**
     *
     */
    private T [] libellesMenu;

    /**
     *
     */
    private ArrayList<LigneMenu> lignesMenu;

    /**
     *
     */
    private Scanner scanner ;

    /**
     *
     */
    private Pattern patternChoix ;

    /**
     *
     */
    private String pattern_Menu  ;

    /**
     *
     */
    private LigneMenu <T> statusBar;

    /**
     *
     */
    private T clefStatusBar;


    /**
     *
     * @param t
     * @param pattern
     * @param refStatusBar
     * @param sc
     */
    Menu(T [] t, String pattern, T refStatusBar,Scanner sc) {
        scanner =sc;
        lignesMenu = new  ArrayList<>(256);
        libellesMenu = t;
        pattern_Menu = pattern;
        clefStatusBar = refStatusBar;
    }

    /**
     *
     * @param x
     * @param v
     */
    void addLigneMenu(T x, String v) {
        this.addLigneMenu(x,v,null);
    }

    /**
     *
     * @param x
     * @param v
     * @param c
     */
    void addLigneMenu(T x, String v, Character c) {
        LigneMenu ligneMenu = new LigneMenu(x,v,c);
        lignesMenu.add(ligneMenu );
        if (x.equals(clefStatusBar))
            statusBar =ligneMenu;
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
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
            logger.error(ERREUR_GENERIC);
        }
        return z;

    }

    public void majLigneEtat(String s) {
        String modelStatusBar = "[-- "+s+" --]";
        statusBar.setLibelleLigne(modelStatusBar);
        DisplayMenu();
    }


 }
