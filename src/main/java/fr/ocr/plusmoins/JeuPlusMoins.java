package fr.ocr.plusmoins;

import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Messages;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;
import static fr.ocr.utiles.Logs.logger;



/**
 * Classe du jeu Plus Moins
 */

public class JeuPlusMoins {

    final private String strLibelleStatus = "[     Status                ]";
    final private String strLibelleInfos = "[     Informations          ] ";
    final private String strLibelleSaisie = ". Saisie -> (K : retour)    ] ";
    final private String strLibelleSecretHeader = "[ Secret = ";
    final private String strLibelleSecretTrailer = " ]";

    private char[][] tablePM;
    private byte[] secretFormeByte;

    // les options du menu du jeu PlusMoins
    private LibellesMenuSecondaire modeDeJeu;

    // scanner pour saisie clavier
    private Scanner scanner;

    // nombre lignes de jeu de la table du jeu PlusMoins
    private Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);

    //le caractère EscapeChar de retour au menu superieur
    private Character charactersEscape = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);

    // paramètre du fichier properties - nombre de positions  par ligne de la table de jeu PlusMoins
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);

    // paramètre du fichier properties - affiche (DEBUG=Vrai) ou pas la combinaison secrete
    private Boolean modeDebug = (Boolean) getParam(MODE_DEVELOPPEUR);

    //tableau de chaine de caractères pour affichage du jeu PlusMoins
    private StringBuilder[] lignesJeu;


    /**
     * Constructeur jeuPlusMoins
     */
    public JeuPlusMoins(LibellesMenuSecondaire modeJeu, Scanner sc) {

        scanner = sc;
        modeDeJeu = modeJeu;

        lignesJeu = new StringBuilder[24];
        //creation de chaque variable StringBuilder dans le tableau qui correspond aux lignes  affichablse, du jeu
        for (int n = 0; n < lignesJeu.length; n++) {
            lignesJeu[n] = new StringBuilder(48);
        }
        secretFormeByte = InitsecretJeuPM();
        /*
        en ligne : le nombre d'essais possible dans ce jeu (dépaend u paramètrage)
        en colonne : le nombre de cases de jeu : le paramètre NOMBRE_DE_POSITIONS donne la valeur du nombre de case
        pour un essai  : il faut la place pour l'essai de taille NOMBRE_DE_POSITIONS + son score qui est lui aussi de
        taille identique (NOMBRE_DE_POSITIONS) .
        */
        tablePM = new char[nombreDeEssaisMax][2 * nombreDePositions];
    }


    /**
     * transforme le secret de byte en char
     *
     * @return tableau de char
     */
    public char[] getSecretFormeChar() {
        char[] vlRet = new char[secretFormeByte.length];
        for (int i = 0; i < secretFormeByte.length; i++) {
            vlRet[i] = (char) (secretFormeByte[i] + '0');
        }
        return vlRet;
    }

    /**
     * initialisation à vide du secret du jeu (pour affichage et reset)
     *
     * @return char [] , tableau de caracteres valorisé à '.'
     */
    private byte[] InitsecretJeuPM() {
        byte[] tabValRet = new byte[nombreDePositions];
        for (int p = 0; p < tabValRet.length; p++)
            tabValRet[p] = 0;

        return tabValRet;
    }


    /**
     * @param rang  entier qui est égal au numéro de ligne du tableau tabelPM dans laquelle ilfaut ajouter l'essai
     * @param essai le tableau de char  dont les colonnes contiennent les valeurs de l'essai
     */
    public void AjouterunEssai(int rang, char[] essai) {
        try {
            for (char c : essai) {
                //leve uen exception si valeur est <=0
                ChiffreEstIlValide(c);
            }
            System.arraycopy(essai, 0, tablePM[rang], 0, essai.length);

        } catch (IndexOutOfBoundsException | ArrayStoreException | NullPointerException e) {
            logger.error(Messages.ErreurMessages.SORTIE_PGM_SUR_ERREURNONGEREE);
            throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
        }
    }

    /**
     * @param rang  entier qui est égal au numéro de ligne du tableau tabelPM dans laquelle ilfaut ajouter le score
     * @param score le tableau de char dont les colonnes contiennent les valeurs du score
     */
    public void AjouterunScore(int rang, char[] score) throws AppExceptions {
        try {
            for (char c : score) {
                //leve uen esception si valeur est <=0
                ScoreEstIlValide(c);
            }
            System.arraycopy(score, 0, tablePM[rang], nombreDePositions, score.length);
        } catch (IndexOutOfBoundsException | ArrayStoreException | NullPointerException e) {
            logger.error(Messages.ErreurMessages.SORTIE_PGM_SUR_ERREURNONGEREE);
            throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
        }
    }


    /**
     * les lignes du tableau tablePm, sous forme affichable
     * avec
     * * - en tête
     * * - zone d'affichage du secret
     * * - zone de saisie
     * * - zone d'information
     * modele primaire - pas de list pas de stream - non // (sauf ajout synchronized)
     * peut être aisément converti en assembleur 8086.
     *
     * @return String[] , les lignes du tableau tablePm, sous forme affichable
     */

    public StringBuilder[] getLignesJeu() {

        int rangLigne = 0;
        //titre
        lignesJeu[rangLigne++].append(modeDeJeu.toString());

        //espace
        lignesJeu[rangLigne++].append(" ");


        //zone de status
        if (modeDebug) {
            lignesJeu[rangLigne++].append(strLibelleSecretHeader + String.valueOf(getSecretFormeChar()) + strLibelleSecretTrailer);
        } else {
            lignesJeu[rangLigne++].append(strLibelleStatus);
        }

        //espace
        lignesJeu[rangLigne++].append(" ");

        //parcours des lignes
        for (char[] uneLigne : tablePM) {
            lignesJeu[rangLigne].append("{ ");
            int countCol = 0;
            //parcours des colonnes
            for (char uneValeur : uneLigne) {
                //separateur entre zone essai et zone score
                if (countCol == nombreDePositions) {
                    lignesJeu[rangLigne].append(" | ");
                }
                lignesJeu[rangLigne].append((uneValeur == 0) ? " x " : String.format(" %c ", uneValeur));
                countCol++;
            }
            lignesJeu[rangLigne].append("}");
            rangLigne++;
        }

        //espace
        lignesJeu[rangLigne++].append(" ");

        //zone d'information
        lignesJeu[rangLigne++].append(strLibelleInfos);

        //zone de saisie
        lignesJeu[rangLigne].append(strLibelleSaisie);

        StringBuilder[] strValRet = new StringBuilder[rangLigne + 1];

        System.arraycopy(lignesJeu, 0, strValRet, 0, rangLigne + 1);

        return strValRet;
    }

    /**
     * vérification que la caractère passé en paramétre correspond à un chiffre et s'il est dans la plage 1-9
     *
     * @param c caractere à évaluae : doit être dans le range 1-9
     * @throws AppExceptions avec CharacterExceptionPM.A comme caractere de sortie (ce qui permet de gérer car c'est
     *                       une exception qui n'est pas fatale - il faut refaire une saisie.
     */
    private void ChiffreEstIlValide(char c) throws AppExceptions {
        int i;
        try {
            i = Integer.valueOf(String.valueOf(c));
            if (i <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new AppExceptions(Messages.InfosMessages.PM_CHIFFRE_HORS_PLAGE_TOLERANCE, Constantes.Libelles.CharacterExceptionPM.C.toString().charAt(0));
        }
    }

    /**
     * vérification que la caractère passé en paramétre correspond à = ou - ou +, seuls caractère possible pour  un score
     *
     * @param c caractere à évaluae : doit être dans l'ensemble {=,-,+}
     * @throws AppExceptions avec CharacterExceptionPM.A comme caractere de sortie (ce qui permet de gérer car c'est
     *                       une exception qui n'est pas fatale - il faut refaire une saisie.
     */
    private void ScoreEstIlValide(char c) {
        if (c != '=' && c != '-' && c != '+') {
            throw new AppExceptions(Messages.InfosMessages.PM_CARSCORE_HORS_PLAGE_TOLERANCE, Constantes.Libelles.CharacterExceptionPM.S.toString().charAt(0));
        }
    }

    private void SaisieUnEssaiJoueur() {

    }

    private void CalculUnEssaiOrdi() {

    }

    private boolean DonneScoreJoueur() {
        return true;
    }

    private boolean DonneScoreOrdi() {
        CalculScore(new byte[8], new char[8]);
        return true;
    }

    /**
     * calcule le score de l'assai passé en parametre
     * renvoi vrai si l'essai est égal au secret
     *
     * @param essai tableau de char qui contient l'essai - non modifié par la méthode
     * @param score tableau de char vide mais qui doit être alloué (renseigné avec le scorre en sortie de méthode
     * @return boolean - vrai si secret trouvé , sinon faux
     */
    public boolean CalculScore(byte[] essai, char[] score) {
        int compteEgal = 0;
        for (int i = 0; i < essai.length; i++) {
            score[i] = (essai[i] == secretFormeByte[i]) ? '=' : ((essai[i] < secretFormeByte[i]) ? '+' : '-');
            compteEgal += (score[i] == '=' ? 1 : 0);
        }
        return compteEgal == nombreDePositions;
    }

    private void FaitUnSecretJoueur() {

    }

    /**
     * renseigne l'attibut secretJeuPM avec le nombre à trouver, sous forme de caractères
     */
    public void FaitUnSecretOrdi() {

        DecimalFormat df = new DecimalFormat("#");
        // "graine" pour le random.
        df.setRoundingMode(RoundingMode.HALF_UP);

        int boucleMax = (int) getParam(NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE);
        int valTmp;
        for (int limite = 0, k = 0; k < nombreDePositions && limite < boucleMax; limite++) {
            valTmp = ((Integer.parseInt(df.format(Math.random() * 100)) % 10));
            if (valTmp > 0) {
                secretFormeByte[k++] = (byte) valTmp;
            }
        }
    }

    public void runModeChallengeur() {
        FaitUnSecretOrdi();
        for (int i = 0; i < nombreDeEssaisMax; i++) {
            SaisieUnEssaiJoueur();
            if (DonneScoreOrdi()) {
                break;
            }
        }
    }

    public void runModeDefenseur() {
        FaitUnSecretJoueur();
        for (int i = 0; i < nombreDeEssaisMax; i++) {
            CalculUnEssaiOrdi();
            if (DonneScoreJoueur()) {
                break;
            }
        }
    }

    public void runModeDuel() {
        FaitUnSecretOrdi();
        for (int i = 0; i < nombreDeEssaisMax; i++) {
            CalculUnEssaiOrdi();
            if (DonneScoreJoueur()) {
                break;
            }
            SaisieUnEssaiJoueur();
            if (DonneScoreOrdi()) {
                break;
            }
        }
    }

}
