package fr.ocr.modeconsole;

import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import static fr.ocr.modeconsole.IOConsole.LectureClavier;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

/**
 * @param <T> : Enum  qui vaut soit LibellesMMenuPrincipal , soit LibellesMenuSecondaire
 *
 * @author laurentCordier
 * <p>
 * <p>
 * Un Menu contient un tableau d'instances de LigneMenu.
 * chaque ligne de menu qui correspond à une option possible a un selecteur non null
 * (i.e "R- retour" est une ligne option avec selecteur valant 'R)
 * <p>
 * chaque ligne  du menu a un champ non null,referenceEnumLibelle, qui contient une instance d'une des inner classes
 * de l'Enum passé en paramètre au constructeur de la classe ligneMenu.
 * (i.e " Q - Quitter",  referenceEnumLibelle vaudra " Libelles.LibellesMenuSecondaire#QUITTER ")
 * <p>
 * Un Menu contient un pattern de saisie contruit avec tous les selecteurs non null des instannces de LigneMenu
 * (i.e : [1|3 Q q] accepte les caractres 1,2,3,Q et q ,
 * pour un menu comme :
 * 1 - choix A
 * 2 - choix B
 * 3 - revenir menu principal
 * Q - quitter "
 * <p>
 * flow :
 * afficher les libelles de chaque instance de LigneMenu de ce Menu
 * attendre saisie opérateur avec filtre saisie par pattern du Menu
 * retrouver l'instance de  ligneMenu dont le selecteur est egal au caractere saisi,
 * retourner le "referenceEnumLibelle" de cette instance de  ligneMenu
 * la valeur de referenceEnumLibelle correspond à l'action à exécuter (QUITTER, JOUER, ...
 * <p>
 * La ligne d'un menu est :
 * soit un libellé d'une option (i.e "1 - choisir Mastermind")
 * soit un libellé d'une action de saisie (i.e  " votre choix  ?"
 * soit une zone variable d'information
 * <p>
 * un objet LigneMenu qui correspond à un libellé d'option contient le libellé et le selecteur de l'option
 * (i.e "1 - choisir mastemind" a pour valeur de selecteur le caractere '1')
 * (i.e "Q - Quitter" a pour valeur de selecteur le caractere '{Q|q})
 * <p>
 * un objet LigneMenu qui correspond à une action de saisie ou une zone variable d'information a un selecteur null
 */
class LigneMenu<T extends Enum>   {
    private String libelleLigne;

    private T referenceEnumLibelle;

    private Character selecteur;

    /**
     * @param refEnumLibelle : l'instance d'une inner  Classe d'une Enum ( i.e  QUITTER est une inner classe de
     *                       l' Enum LibellesMenuPrincipal !!! ce n'est pas une constante mais bien une instance)
     * @param chaineLibelle  : chaine de caractére qui sera affichée
     * @param selecteurLigne : choix associé à une ligne de menu (i.e : "Q - Quitter", 'Q' est le selecteur.)
     */
    LigneMenu(T refEnumLibelle, String chaineLibelle, Character selecteurLigne) {
        this.libelleLigne = chaineLibelle;
        referenceEnumLibelle = refEnumLibelle;
        selecteur = selecteurLigne;
    }

    String getLibelle_Ligne() {

        return libelleLigne;
    }

    String getLibelleLigne() {
        return libelleLigne;
    }

    T getReferenceEnumLibelle() {
        return referenceEnumLibelle;
    }

    Character getSelecteur() {
        return selecteur;
    }

    void setLibelleLigne(String libelleLigne) {
        this.libelleLigne = libelleLigne;
    }


}

/**
 * @param <T> un ENum qui vaut soit LibellesMenuPrincipal soit LibellesMenuSecondaire
 */
public abstract class Menu<T extends Enum> {

    // tableau des instances d'inner classe de l'Enum passé en paramètre
    private T[] enumsLibellesMenu;
    // tabeleau d'instances de la classe LigneMenu (lignes du Menu )
    private ArrayList<LigneMenu<T>> lignesMenu;

    // Lib Java : saisie sur System.in
    private Scanner scanner;

    // seuls les caractères présents dans le pattern sont acceptés lors de la saisie
    private String pattern_Menu;
    // l'instance est construite avec patter_mMnu
    private Pattern patternChoix;

    // ligne de menu dont le contenu est variable qui contient les infos selon l'action en cours
    private LigneMenu<T> statusBar;

    // pour retrouver la statusbar dans le tableau des lignes de ligneMenu
    private T clefStatusBar;

    private  DisplayMenu displayMenu ;

    /**
     * Constructeur Menu
     *
     * @param instancesLibelle : tableau des instances de l'Enum soit LibellesMenuPrincipal soit LibellesMenuSecondaire
     * @param pattern          : liste des caractères autorisés lors de la saisie
     * @param refStatusBar     : instance de l'Enum T qui sera attribuée à la ligne statusBar
     * @param sc               scanner (lib java)
     */
    Menu(T[] instancesLibelle, String pattern, T refStatusBar, Scanner sc) {
        scanner = sc;
        lignesMenu = new ArrayList<>(256);
        enumsLibellesMenu = instancesLibelle;
        pattern_Menu = pattern;
        clefStatusBar = refStatusBar;
        displayMenu = new DisplayMenu(lignesMenu);
    }

    /**
     * ajout d'une ligne de menu qui est soit une ligne d'information, soit une ligne d'action de  saisie
     * donc le selecteur est nul
     *
     * @param instanceLibelle instance de l'Enum à affecter à la ligne a ajouter au menu
     * @param chaineLibelle   libelle à affecter à la ligne a ajouter au menu
     */
    void addLigneMenu(T instanceLibelle, String chaineLibelle) {
        this.addLigneMenu(instanceLibelle, chaineLibelle, null);
    }

    /**
     * ajout d'une ligne de menu  (selecteur peut être null ou pas)
     *
     * @param instanceLibelle instance de l'Enum à affecter à la ligne a ajouter au menu
     * @param chaineLibelle   libelle à affecter à la ligne a ajouter au menu
     * @param selecteur       caractère null ou non null qui sert à retrouver la ligne qui correspond au choix
     *                        (saisie clavier) fait par l'utilisateur.
     */
    void addLigneMenu(T instanceLibelle, String chaineLibelle, Character selecteur) {
        LigneMenu<T> ligneMenu = new LigneMenu<>(instanceLibelle, chaineLibelle, selecteur);
        lignesMenu.add(ligneMenu);
        if (instanceLibelle.equals(clefStatusBar)) {
            statusBar = ligneMenu;
        }
    }


    /**
     * Recherche l'instance de LigneMenu dont le selecteur est égal au parametre codeRet
     *
     * @param codeRet : caractère saisi par l'utilisateur qui sert de clef
     *                pour retrouver la ligne LigneMenu qui a codeRet comme valeur de selecteur
     *
     * @return instance d'une classe d'un Enum (LibellesMenuSecondaire par exemple)
     */
    private T retrouveLigneMenu(Character codeRet) {

        T enumActionChoisie = null;

        Character c;

        for (LigneMenu<T> ligneMenu : lignesMenu) {

            c = ligneMenu.getSelecteur();

            if ((c != null) && (c.equals(codeRet))) {
                enumActionChoisie = ligneMenu.getReferenceEnumLibelle();

                break;
            }
        }

        return enumActionChoisie;
    }

    /**
     * Affiche le menu , attend la saisie clavier, renvoie le code de l'action choisie
     * c.a.d l'instance d'une classe d'un Enum  (QUITTER, RETOUR ....)
     *
     * @return instance d'une classe d'un Enum (LibellesMenuSecondaire par exemple)
     *
     * @throws AppExceptions sur erreur ou CTRL-C
     */
    public T RunMenu() throws AppExceptions {
        T enumActionChoisie;

        Character escapeChar = Constantes.Libelles.CharactersEscape.X.toString().charAt(0);

        try {
            enumActionChoisie = retrouveLigneMenu(LectureClavier(pattern_Menu,scanner,displayMenu, escapeChar));
        }
        catch (Exception e) {
            logger.error(String.format("%s %s ", ERREUR_GENERIC, e.getClass().getSimpleName()));
            throw new AppExceptions(ERREUR_GENERIC);
        }
        return enumActionChoisie;
    }

    /**
     * affichage d'une chaine dans la statusbar
     *
     * @param s message à placer dans la statusBar
     */
    public void majLigneEtat(String s) {
        String modelStatusBar = "[-- " + s + " --]";
        statusBar.setLibelleLigne(modelStatusBar);
        displayMenu.Display();
    }
}

/**
 * affichage lignes du menus (une classe pour gestion du passage de methode en
 * parametre
 *
 * @param <T> instances de Libelles....
 */
class DisplayMenu<T extends Enum> implements AffichageMenu {

    private ArrayList<LigneMenu<T>> lignesMenu;

    DisplayMenu(ArrayList<LigneMenu<T>> x) {
        lignesMenu=x;
    }

    /*
     * affichage écran des lignes du menu
     * la dernière ligne est affichée sans retour chariot pour que la saisie clavier soit en regard de la ligne
     * de saisie du choix
     */
    @Override
    public void Display() {
        int j = 0;

        for (LigneMenu<T> ligneMenu : lignesMenu) {

            //si c'est la dernière ligne du menu, pas de CR/LF pour que la saisie soit sur la même ligne que l'affichage
            if (lignesMenu.get(lignesMenu.size() - 1).equals(ligneMenu)) {
                System.out.print(ligneMenu.getLibelle_Ligne());
            } else {
                System.out.println(ligneMenu.getLibelle_Ligne());
            }
        }
    }
}
