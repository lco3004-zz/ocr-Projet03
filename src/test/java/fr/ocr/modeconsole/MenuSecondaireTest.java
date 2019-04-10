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

class MenuSecondaireTest {

    private final ByteArrayOutputStream outContent;
    Constantes.Libelles.LibellesMenuSecondaire ch_Sec;
    private Scanner scanner;
    private MenuSecondaire menu_secondaire;

    MenuSecondaireTest() {
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
    void Given_MenuSecondaireMMAfficher_When_ChoisirModeChallengeur_Then_ReponseEstModeChallengeur() {

        System.setIn(new ByteArrayInputStream("1\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);

        ch_Sec = menu_secondaire.runMenu();

        assertEquals(ch_Sec, Constantes.Libelles.LibellesMenuSecondaire.MODE_CHALLENGER);

    }

    @Test
    void Given_MenuSecondaireMMAfficher_When_ChoisirDefenseur_Then_ReponseEstModeDefenseur() {

        System.setIn(new ByteArrayInputStream("2\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);

        ch_Sec = menu_secondaire.runMenu();

        assertEquals(ch_Sec, Constantes.Libelles.LibellesMenuSecondaire.MODE_DEFENSEUR);

    }

    @Test
    void Given_MenuSecondaireMMAfficher_When_ChoisirModeDuel_Then_ReponseEstModeDuel() {

        System.setIn(new ByteArrayInputStream("3\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);

        ch_Sec = menu_secondaire.runMenu();

        assertEquals(ch_Sec, Constantes.Libelles.LibellesMenuSecondaire.MODE_DUEL);

    }

    @Test
    void Given_MenuSecondaireMMAfficher_When_ChoisirRetour_Then_ReponseEstRetour() {

        System.setIn(new ByteArrayInputStream("k\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);

        ch_Sec = menu_secondaire.runMenu();

        assertEquals(ch_Sec, Constantes.Libelles.LibellesMenuSecondaire.RETOUR);

    }

    @Test
    void Given_MenuSecondaireMMAfficher_When_ChoisirQuitter_Then_ReponseEstQuitter() {

        System.setIn(new ByteArrayInputStream("x\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);

        ch_Sec = menu_secondaire.runMenu();

        assertEquals(ch_Sec, Constantes.Libelles.LibellesMenuSecondaire.QUITTER);

    }

    @Test
    void Given_MenuSecondaireMMAfficher_When_ChoisirVoirParametres_Then_ReponseEstVoirParametres() {

        System.setIn(new ByteArrayInputStream("v\n".getBytes()));

        scanner = new Scanner(System.in);

        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);

        ch_Sec = menu_secondaire.runMenu();

        assertEquals(ch_Sec, Constantes.Libelles.LibellesMenuSecondaire.LOGGER_PARAMETRES);

    }


    /*
    Verification de l'affichage du menu secondaire
     */
    @Test
    void Given_SelectionJeuDansMenuPrincipal_When_ChoisirMasterMind_Then_AfficheMenuSecondaireMasterMind() {

        System.setIn(new ByteArrayInputStream("1\n1\nk\n".getBytes()));

        Scanner scanner = new Scanner(System.in);

        menu_secondaire = new MenuSecondaire(Constantes.Libelles.LibellesJeux.MASTERMIND.toString(), scanner);

        Constantes.Libelles.LibellesMenuSecondaire ch_Sec = menu_secondaire.runMenu();

        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");

        assertEquals("MASTERMIND", output[0]);
        assertEquals("[                 ]", output[1]);
        assertEquals("    MODE de JEUX  : ", output[2]);
        assertEquals("        1 -> MODE_CHALLENGER", output[3]);
        assertEquals("        2 -> MODE_DEFENSEUR", output[4]);
        assertEquals("        3 -> MODE_DUEL", output[5]);
        assertEquals("    K -> RETOUR Menu Principal", output[6]);
        assertEquals("    V -> LOGGER les Paramètres", output[7]);
        assertEquals("    X -> QUITTER", output[8]);
        assertEquals("Votre Choix  : ", output[9]);
    }
}