package fr.ocr.modeconsole;
/**
 * @author laurentCordier
 *
 *
 * Un Menu contient un tableau d'instances de LigneMenu.
 * chaque ligne de menu qui correspond à une option possible a un selecteur non null
 * (i.e "R- retour" est une ligne option avec selecteur valant 'R)
 *
 * chaque ligne  du menu a un champ non null,referenceEnumLibelle, qui contient une instance d'une des inner classes
 * de l'Enum passé en paramètre au constructeur de la classe ligneMenu.
 * (i.e " Q - Quitter",  referenceEnumLibelle vaudra " Libelles.LibellesMenuSecondaire#QUITTER ")
 *
 * Un Menu contient un pattern de saisie contruit avec tous les selecteurs non null des instannces de LigneMenu
 * (i.e : [1|3 Q q] accepte les caractres 1,2,3,Q et q ,
 *  pour un menu comme :
 *  1 - choix A
 *  2 - choix B
 *  3 - revenir menu principal
 *  Q - quitter "
 *
 * flow :
 * afficher les libelles de chaque instance de LigneMenu de ce Menu
 * attendre saisie opérateur avec filtre saisie par pattern du Menu
 * retrouver l'instance de  ligneMenu dont le selecteur est egal au caractere saisi,
 * retourner le "referenceEnumLibelle" de cette instance de  ligneMenu
 * la valeur de referenceEnumLibelle correspond à l'action à exécuter (QUITTER, JOUER, ...
 *
 */

import fr.ocr.utiles.ApplicationExceptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
import static fr.ocr.utiles.Messages.InfosMessages.CTRL_C;

/**
 * La ligne d'un menu est :
 *   soit un libellé d'une option (i.e "1 - choisir Mastermind")
 *   soit un libellé d'une action de saisie (i.e  " votre choix  ?"
 *   soit une zone variable d'information
 *
 * un objet LigneMenu qui correspond à un libellé d'option contient le libellé et le selecteur de l'option
 * (i.e "1 - choisir mastemind" a pour valeur de selecteur le caractere '1')
 * (i.e "Q - Quitter" a pour valeur de selecteur le caractere '{Q|q})
 *
 * un objet LigneMenu qui correspond à une action de saisie ou une zone variable d'information a un selecteur null
 *
 *
 * @param <T> : Enum  qui vaut soit LibellesMMenuPrincipal , soit LibellesMenuSecondaire
 */
class LigneMenu <T extends Enum>{
    private String libelleLigne;

    private T referenceEnumLibelle;

    private Character selecteur;

    /**
     *
     * @param refEnumLibelle  : l'instance d'une inner  Classe d'une Enum ( i.e  QUITTER est une inner classe de
     *                     l' Enum LibellesMenuPrincipal !!! ce n'est pas une constante mais bien une instance)
     * @param chaineLibelle   : chaine de caractére qui sera affichée
     * @param selecteurLigne  : choix associé à une ligne de menu (i.e : "Q - Quitter", 'Q' est le selecteur.)
     *
     */
    LigneMenu (  T  refEnumLibelle, String chaineLibelle,Character selecteurLigne) {
        this.libelleLigne =chaineLibelle;
        referenceEnumLibelle = refEnumLibelle;
        selecteur=selecteurLigne;
    }

    String getLibelle_Ligne() {
        return libelleLigne;
    }
    T getReferenceEnumLibelle() {
        return referenceEnumLibelle;
    }
    Character getSelecteur() { return selecteur; }
    void setLibelleLigne(String libelleLigne) {
        this.libelleLigne = libelleLigne;
    }
}

/**
 * efface l'afficage console . Clear ou Cls sont appellés selon le système
 * sur lequel le programme est exécuté
 */
final class ClearScreen {
    static  void cls()  {
        try {

            final String os = System.getProperty("os.name");

            // la commande a exécuté par le shell windows "cmd qui appelle une autre cmd" est la seule
            //qui fonctione (Windows 10 pro - insiders build 17134.523 27/08/2018
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
 * @param <T> un ENum qui vaut soit LibellesMenuPrincipal soit LibellesMenuSecondaire
 */
public  abstract class  Menu <T extends Enum>{

    // tableau des instances d'inner classe de l'Enum passé en paramètre
    private T [] libellesMenu;
    // tabeleau d'instances de la classe LigneMenu (lignes du Menu )
    private ArrayList<LigneMenu> lignesMenu;

    // Lib Java : saisie sur System.in
    private Scanner scanner ;

    // seuls les caractères présents dans le pattern sont acceptés lors de la saisie
    private String pattern_Menu  ;
    // l'instance est construite avec patter_mMnu
    private Pattern patternChoix ;

    // ligne de menu dont le contenu est variable qui contient les infos selon l'action en cours
    private LigneMenu <T> statusBar;
    // pour retrouver la statusbar dans le tableau des lignes de ligneMenu
    private T clefStatusBar;
    /**
     *
     * @param instancesLibelle : tableau des instances de l'Enum soit LibellesMenuPrincipal soit LibellesMenuSecondaire
     * @param pattern  : liste des caractères autorisés lors de la saisie
     * @param refStatusBar : instance de l'Enum T qui sera attribuée à la ligne statusBar
     * @param sc scanner (lib java)
     */
    Menu(T [] instancesLibelle , String pattern, T refStatusBar,Scanner sc) {
        scanner =sc;
        lignesMenu = new  ArrayList<>(256);
        libellesMenu = instancesLibelle;
        pattern_Menu = pattern;
        clefStatusBar = refStatusBar;
    }

    /**
     *
     * @param instanceLibelle instance de l'Enum à affecter à la ligne a ajouter au menu
     * @param chaineLibelle  libelle à affecter à la ligne a ajouter au menu
     */
    void addLigneMenu(T instanceLibelle, String chaineLibelle) {
        this.addLigneMenu(instanceLibelle,chaineLibelle,null);
    }

    /**
     *
     * @param instanceLibelle instance de l'Enum à affecter à la ligne a ajouter au menu
     * @param chaineLibelle libelle à affecter à la ligne a ajouter au menu
     * @param selecteur caractère null ou non null qui sert à retrouver la ligne qui correspond au choix
     *                  (saisie clavier) fait par l'utilisateur.
     */
    void addLigneMenu(T instanceLibelle, String chaineLibelle, Character selecteur) {
        LigneMenu ligneMenu = new LigneMenu(instanceLibelle,chaineLibelle,selecteur);
        lignesMenu.add(ligneMenu );
        if (instanceLibelle.equals(clefStatusBar))
            statusBar =ligneMenu;
    }

    /**
     *
     * @return le caractère qui correspond à la saisie utilisateur (filtré par pattern )
     */
    Character LectureClavier () {
        patternChoix = Pattern.compile(pattern_Menu);
        String choix="";
        ClearScreen.cls();
        DisplayMenu();
        while ( choix.equals("") && scanner.hasNext()) {

            try {
                    choix = scanner.next(patternChoix).toUpperCase();

            }catch (InputMismatchException  | StringIndexOutOfBoundsException e1) {
                    String tmp = scanner.next();
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
    private T retrouveLigneMenu(Character codeRet) {
        T z = null;
        for ( LigneMenu ligneMenu : lignesMenu) {
            if ((ligneMenu.getSelecteur() != null) && (ligneMenu.getSelecteur() ==codeRet)) {
                for (T q :libellesMenu) {
                    if (q == ligneMenu.getReferenceEnumLibelle()) {
                        z = q;
                    }
                }
            }
        }
        return z;
    }

    public T RunMenu () throws ApplicationExceptions {
        T z = null;
        try {
            z= retrouveLigneMenu(LectureClavier());
        }
        catch (StringIndexOutOfBoundsException e1) {
            logger.info(CTRL_C);
            z= retrouveLigneMenu( Libelles.CharactersEscape.Q.toString().charAt(0));
        }
        catch (Exception e) {
            logger.error(String.format ("%s %s ",ERREUR_GENERIC,e.getClass().getSimpleName()));
            throw new ApplicationExceptions(ERREUR_GENERIC);
        }
        return z;

    }

    public void majLigneEtat(String s) {
        String modelStatusBar = "[-- "+s+" --]";
        statusBar.setLibelleLigne(modelStatusBar);
        DisplayMenu();
    }

 }
