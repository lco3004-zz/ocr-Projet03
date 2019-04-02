package fr.ocr.modeconsole;

import fr.ocr.App;
import fr.ocr.utiles.Constantes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getAllParams;
import static fr.ocr.utiles.Logs.getInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuPrincipalTest {

    private final ByteArrayOutputStream outContent;
    private Scanner scanner;

    private Constantes.Libelles.LibellesMenuSecondaire ch_Sec;

    private Constantes.Libelles.LibellesMenuPrincipal ch_Sup;

    private MenuSecondaire menu_secondaire;

    private MenuPrincipal menu_principal;

    MenuPrincipalTest() {
        outContent = new ByteArrayOutputStream();

        getInstance(App.class.getSimpleName());

        getAllParams();
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    void Given_MenuPrincipalAfficher_When_ChoisirMasterMind_Then_ReponseEstMasterMind() {

        System.setIn(new ByteArrayInputStream("1\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_principal = new MenuPrincipal(scanner);

        ch_Sup = menu_principal.RunMenu();

        assertEquals(ch_Sup, Constantes.Libelles.LibellesMenuPrincipal.CHOISIR_MASTERMIND);

    }


    @Test
    void Given_MenuPrincipalAfficher_When_ChoisirPlusMoins_Then_ReponseEstPlusMoins() {

        System.setIn(new ByteArrayInputStream("2\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_principal = new MenuPrincipal(scanner);

        ch_Sup = menu_principal.RunMenu();

        assertEquals(ch_Sup, Constantes.Libelles.LibellesMenuPrincipal.CHOISIR_PLUS_MOINS);

    }

    @Test
    void Given_MenuPrincipalAfficher_When_ChoisirExit_Then_ReponseEstExit() {

        System.setIn(new ByteArrayInputStream("X\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_principal = new MenuPrincipal(scanner);

        ch_Sup = menu_principal.RunMenu();

        assertEquals(ch_Sup, Constantes.Libelles.LibellesMenuPrincipal.QUITTER);

    }


    /*
    Verification de l'affichage du menu principal
     */
    @Test
    void Given_Nothing_When_CallingRunMenu_Then_FillMenuPrincipalCorrectly() {

        System.setIn(new ByteArrayInputStream("1\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_principal = new MenuPrincipal(scanner);

        ch_Sup = menu_principal.RunMenu();

        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");

        assertEquals("OCR-Projet03 - Menu Principal", output[0]);
        assertEquals("[-- pgm prêt--]", output[1]);
        assertEquals("    1 -> JOUER au MASTERMIND", output[3]);
        assertEquals("    2 -> JOUER au PLUSMOINS", output[4]);
        assertEquals("    X -> QUITTER", output[5]);
        assertEquals("Votre Choix  : ", output[6]);

    }
}