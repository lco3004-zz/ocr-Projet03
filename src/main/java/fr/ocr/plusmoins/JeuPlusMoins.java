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

    //MOCK
    int MockesaisNb = 0;
    char[][] lesEssais;
    //fin mock



    final private String strLibelleStatus = "[     Status                ]";
    final private String strLibelleInfos = "[     Informations          ] ";
    final private String strLibelleSaisie = ". Saisie -> (K : retour)    ] ";
    final private String strLibelleSecretHeader = "[ Secret = ";
    final private String strLibelleSecretTrailer = " ]";

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

        //MOCK
        lesEssais = new char[nombreDeEssaisMax][nombreDePositions];
        for (int i = 0; i < nombreDeEssaisMax; i++)
            for (int j = 0; j < nombreDePositions; j++)
                lesEssais[j][i] = (char) (j + '0');

        //MOCK
    }

    /**
     * @param rang  entier qui est égal au numéro de ligne du tableau tabelPM dans laquelle ilfaut ajouter l'essai
     * @param essai le tableau de char  dont les colonnes contiennent les valeurs de l'essai
     */
    public void AjouterUnEssai(int rang, char[] essai, char[][] tablePM) {
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
    public void AjouterunScore(int rang, char[] score, char[][] tablePM) throws AppExceptions {
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
    public StringBuilder[] getLignesJeu(char[] secret, char[][] tablePM) {

        int rangLigne = 0;
        //titre
        lignesJeu[rangLigne++].append(modeDeJeu.toString());

        //espace
        lignesJeu[rangLigne++].append(" ");


        //zone de status
        if (modeDebug) {
            lignesJeu[rangLigne++].append(strLibelleSecretHeader).append(String.valueOf(secret)).append(strLibelleSecretTrailer);
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

    private void SaisieUnEssaiJoueur(char[] nouvelEssai) {

        System.arraycopy(lesEssais[MockesaisNb], 0, nouvelEssai, 0, nouvelEssai.length);

        MockesaisNb++;
    }

    private char[] CalculUnNouvelEssaiOrdi(int rang, char[] nouvelEssai, char[] score, char[][] tablePM) {
        int defautMin = 0, defautMax = 9;


        for (int i = 0; i < nombreDePositions; i++) {
            //selon le score obtenu, le calcul est différent car il faut tenir compte du sens + ou -
            switch (score[i]) {
                case '=': {
                    //le chiffres est le bon don con reporte à l'identique
                    nouvelEssai[i] = tablePM[rang][i];
                }
                break;
                case '-': {
                    //le chiffre est inférieur au secret
                    // essai[i] < nouvelEssai[i] < BorneSUPx
                    // BorneSupx = min(essai[y],score[y] == '+'
                    int borneSup = 9;
                    for (int m = rang - 1; m > tablePM.length; m--) {
                        if (tablePM[m][i + nombreDePositions] == '+') {
                            borneSup = Math.min(borneSup, (int) tablePM[m][i]);
                        }
                    }
                    if (borneSup <= tablePM[rang][i]) {
                        logger.error(Messages.ErreurMessages.ERREUR_GENERIC.toString() + "calcul  borneSup erreur");
                        throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
                    }
                    // prend le milieu du segmznt essai[i] .. borneSup, valeur superieur car on "monte" vers la soluce
                    nouvelEssai[i] = (char) (Math.ceil((borneSup - tablePM[rang][i]) / 2) + '0');

                }
                break;
                case '+': {
                    //le chiffre est supérieur au secret
                    //  BorenInfx <  nouvelEssai[i] < essai[i]
                    // BorenInfx = max(essai[y],score[y] == '-'
                    int borneInf = 0;
                    for (int m = rang - 1; m > tablePM.length; m--) {
                        if (tablePM[m][i + nombreDePositions] == '-') {
                            borneInf = Math.max(borneInf, (int) tablePM[m][i]);
                        }
                    }
                    if (borneInf >= tablePM[rang][i]) {
                        logger.error(Messages.ErreurMessages.ERREUR_GENERIC.toString() + "calcul  borneInf erreur");
                        throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
                    }
                    // prend le milieu du segmznt essai[i] .. borneSup, valeur inferieur car on "descend" vers la soluce
                    nouvelEssai[i] = (char) (Math.floor((tablePM[rang][i] - borneInf) / 2) + '0');

                }
                break;
                default:
                    logger.error(Messages.ErreurMessages.ERREUR_GENERIC.toString());
                    throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
            }
        }
        return nouvelEssai;
    }

    private boolean DonneScoreDuJoueur(char[] essai, char[] score, char[] secret) {
        return CalculScore(essai, score, secret);
    }

    private boolean DonneScoreDeLOrdi(char[] essai, char[] score, char[] secret) {
        //saisir score et presente resultat de CalculScore(essai, score, secret);
        //validation par escapeChar
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
    public boolean CalculScore(char[] essai, char[] score, char[] secret) {
        int compteEgal = 0;
        for (int i = 0; i < essai.length; i++) {
            score[i] = (essai[i] == secret[i]) ? '=' : ((essai[i] < secret[i]) ? '+' : '-');
            compteEgal += (score[i] == '=' ? 1 : 0);
        }
        return compteEgal == nombreDePositions;
    }

    /**
     * renseigne le secret avec le nombre à trouver, sous forme de caractères
     * saisie par le Joueur
     */
    private void FaitUnSecretParLeJoueur(char[] secret) {

    }

    /**
     * renseigne l'attibut secretJeuPM avec le nombre à trouver, sous forme de caractères
     * par calcul
     */
    private void FaitUnSecretParLOrdi(int nbPositions, int boucleMax, char[] charsValRet) {

        DecimalFormat df = new DecimalFormat("#");
        // "graine" pour le random.
        df.setRoundingMode(RoundingMode.HALF_UP);

        int valTmp;
        for (int limite = 0, k = 0; k < nbPositions && limite < boucleMax; limite++) {
            valTmp = ((Integer.parseInt(df.format(Math.random() * 100)) % 10));
            if (valTmp > 0) {
                charsValRet[k++] = (char) (valTmp + '0');
            }
        }
    }

    private void AfiicheJeuPM(char[] secret, char[][] tablePM) {
        for (StringBuilder s : getLignesJeu(secret, tablePM)) {
            if (s.charAt(0) == '.') {
                s.replace(0, 1, "[");
                System.out.print(s);
            } else {
                System.out.println(s);
            }
        }
        scanner.next();

    }

    /**
     * joue  en mode Challengeur
     */
    public void runModeChallengeur() {

        char[] score = new char[nombreDePositions];
        char[] essai = new char[nombreDePositions];
        char[] secret = new char[nombreDePositions];
        char[][] tablePM = new char[nombreDeEssaisMax][nombreDePositions];

        //creation du secret par calcul
        FaitUnSecretParLOrdi(nombreDePositions, (int) getParam(NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE), secret);

        for (int i = 0; i < nombreDeEssaisMax; i++) {
            SaisieUnEssaiJoueur(essai);
            if (DonneScoreDuJoueur(essai, score, secret)) {
                break;
            }
            AjouterUnEssai(i, essai, tablePM);
            AjouterunScore(i, score, tablePM);
        }
    }

    public void runModeDefenseur() {
        char[] score = new char[nombreDePositions];
        char[] essai = new char[nombreDePositions];
        char[] secret = new char[nombreDePositions];
        char[][] tablePM = new char[nombreDeEssaisMax][nombreDePositions];
        int rangTablePm = 0;

        FaitUnSecretParLeJoueur(secret);
        for (int i = 0; i < nombreDeEssaisMax; i++) {
            CalculUnNouvelEssaiOrdi(rangTablePm, essai, score, tablePM);
            if (DonneScoreDeLOrdi(essai, score, secret)) {
                break;
            }
        }

    }

    public void runModeDuel() {
        /*
        FaitUnSecretParLOrdi();
        for (int i = 0; i < nombreDeEssaisMax; i++) {
            CalculUnEssaiOrdi(0);
            if (DonneScoreDeLOrdi()) {
                break;
            }
            SaisieUnEssaiJoueur();
            if (DonneScoreDuJoueur()) {
                break;
            }
        }*/
    }
}
