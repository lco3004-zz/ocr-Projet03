package fr.ocr.plusmoins;

import fr.ocr.modeconsole.IOConsole;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Messages;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
import static fr.ocr.utiles.Messages.InfosMessages.CTRL_C;
import static fr.ocr.utiles.Messages.InfosMessages.SORTIE_SUR_ESCAPECHAR;


/**
 * @author laurent
 */
public interface JeuPlusMoins {
    /**
     * mode Jeu CHALLLENGEUR  - creation de la classe qui gère ce mode
     *
     * @param modeJeu LibellesMenuSecondaire
     * @param sc      scanner
     */
    static void CHALLENGEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        (new ClasseJeuPlusMoins(modeJeu, sc)).runModeChallengeur();
    }

    /**
     * mode Jeu DEFENSEUR  - creation de la classe qui gère ce mode
     *
     * @param modeJeu LibellesMenuSecondaire
     * @param sc      scanner
     */
    static void DEFENSEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        (new ClasseJeuPlusMoins(modeJeu, sc)).runModeDefenseur();
    }

    /**
     * mode Jeu DUEL  - creation de la classe qui gère ce mode
     *
     * @param modeJeu LibellesMenuSecondaire
     * @param sc      scanner
     */
    static void DUEL(LibellesMenuSecondaire modeJeu, Scanner sc) {
        (new ClasseJeuPlusMoins(modeJeu, sc)).runModeDuel();
    }

}
/**
 * l'interface permet de regrouper le code commun au mode Challengeur et au mode Defenseur
 */
interface InterfRunPM {
    void FaitMoiUneSecret() throws AppExceptions;

    void FaitMoiUnEssai(int nbBoucle);

    //DonneScoreDuJoueur(essai, score, secret);
    // DonneScoreDeLOrdi(essai, score, secret, tablePM, "[+ \\- = K k]", charactersEscape);
    boolean FaitMoiUnScore(int nbBoucle);

    String FaitMoiUnMessageDeVictoire();

    String FaitMoiUnMessageDeEchec();

    boolean FaitMoiDebug();
}

/**
 * modele de patron en gloups qui est deprécié de nos jour , à tort car aucun compétiteur ne maitrise le gloups
 * ce qui offre une transmission du savoir gloupsien uniquement par asymétrie coplanaire non genrée
 */

class GloupseClasse {
    Consumer<Object> produireSecret;
    Function<Integer, Boolean> evaluerScore;
    Consumer<Integer> proposerEssai;
    Supplier<Boolean> valoriseDebug;
    Supplier<String> informerVictore;
    Supplier<String> informerDefaite;

    GloupseClasse(Consumer<Object> produireSecret,
                  Function<Integer, Boolean> evaluerScore,
                  Consumer<Integer> proposerEssai,
                  Supplier<Boolean> valoriseDebug,
                  Supplier<String> informerVictore,
                  Supplier<String> informerDefaite) {
        this.produireSecret = produireSecret;
        this.evaluerScore = evaluerScore;
        this.proposerEssai = proposerEssai;
        this.valoriseDebug = valoriseDebug;
        this.informerVictore = informerVictore;
        this.informerDefaite = informerDefaite;
    }
}

/**
 * Classe du jeu Plus Moins
 */

class ClasseJeuPlusMoins {

    private String strLibelleStatus;
    private String strLibelleInfos;
    private String strLibelleSaisie;
    private String strLibelleSecretHeader;
    private String strLibelleSecretTrailer;

    private char[] score;
    private char[] essai;
    private char[] secret;
    private char[][] tablePM;

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

    //nombre maxi de boucle a la recherche d'une combinaison secrete qui ne contiennent pas de zéro
    private int nombreMaxDeBoucles = (int) getParam(NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE);

    /**
     * Constructeur jeuPlusMoins
     */
    ClasseJeuPlusMoins(LibellesMenuSecondaire modeJeu, Scanner sc) {

        scanner = sc;

        modeDeJeu = modeJeu;

        initLibellesLignes();

        initDesTables(nombreDePositions);

    }

    /**
     * création des tables score,essai, secret et la table du jeu tablePM
     *
     * @param nbPos nombre de chiffres dans la ligne d'ssai (donc aussidans la ligne de score)
     */
    private void initDesTables(int nbPos) {
        //allocation en espérant que le garbage fait le job
        score = new char[nombreDePositions];
        essai = new char[nombreDePositions];
        secret = new char[nombreDePositions];
        tablePM = new char[nombreDeEssaisMax][2 * nbPos];
    }

    /**
     * affichage des lignes du jeu
     *
     * @param secret  char [] , le secret qui est affiché en mode debug
     * @param tablePM char [] [] tableau de lignes essai et score
     */
    private void AfiicheJeuPM(char[] secret, char[][] tablePM) {
        IOConsole.ClearScreen.cls();
        for (StringBuilder s : getLignesJeu(secret, tablePM)) {
            if (s.charAt(0) == '.') {
                s.replace(0, 1, "[");
                System.out.print(s);
            } else {
                System.out.println(s);
            }
        }
    }

    /**
     * attente saisie escapeChar avant retour au menu superieur
     * "vide" le'input (system.in )
     *
     * @param charactersEscape Character : escapechar
     */
    private void AttenteNettoyageUInput(Character charactersEscape) {
        strLibelleSaisie = ".    Retour (->K)           ] ";
        AfiicheJeuPM(secret, tablePM);
        //tentaive de nettoyage du System.in
        int locaCountGuard = 0;
        do {
            try {
                if (scanner.hasNext()) {
                    if (scanner.next().toUpperCase().contains(charactersEscape.toString()))
                        break;
                }
            } catch (NoSuchElementException f) {
                break;
            }
            locaCountGuard++;
        } while (locaCountGuard < 10);
    }

    /**
     * chargement libelles des lignes du jeux
     */
    private void initLibellesLignes() {
        strLibelleStatus = "[     Status                ]";
        strLibelleInfos = "[     Informations          ] ";
        strLibelleSaisie = ". Saisie -> (K : retour)    ] ";
        strLibelleSecretHeader = "{ ";
        strLibelleSecretTrailer = " }";
    }

    /**
     * @param rang  entier qui est égal au numéro de ligne du tableau tabelPM dans laquelle ilfaut ajouter l'essai
     * @param essai le tableau de char  dont les colonnes contiennent les valeurs de l'essai
     */
    private void AjouterUnEssai(int rang, char[] essai, char[][] tablePM) {
        try {
            for (char c : essai) {
                //leve uen exception si valeur est <=0
                logger.debug(String.format("AjouterUnessai rang=%d , essai = %s, chaCourant = %c", rang, String.valueOf(essai), c));
                ChiffreEstIlValide(c);
            }

            System.arraycopy(essai, 0, tablePM[rang], 0, essai.length);

        } catch (IndexOutOfBoundsException | ArrayStoreException | NullPointerException e) {
            logger.error(Messages.ErreurMessages.SORTIE_PGM_SUR_ERREURNONGEREE);
            logger.error(String.format("%s %s", ERREUR_GENERIC, e.getClass().getSimpleName()));
            throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
        }
    }

    /**
     * @param rang  entier qui est égal au numéro de ligne du tableau tabelPM dans laquelle ilfaut ajouter le score
     * @param score le tableau de char dont les colonnes contiennent les valeurs du score
     */
    private void AjouterunScore(int rang, char[] score, char[][] tablePM) throws AppExceptions {
        try {
            for (char c : score) {
                //leve uen esception si valeur est <=0
                ScoreEstIlValide(c);
            }
            System.arraycopy(score, 0, tablePM[rang], nombreDePositions, score.length);

        } catch (IndexOutOfBoundsException | ArrayStoreException | NullPointerException e) {
            logger.error(Messages.ErreurMessages.SORTIE_PGM_SUR_ERREURNONGEREE);
            logger.error(String.format("%s %s", ERREUR_GENERIC, e.getClass().getSimpleName()));
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
    private StringBuilder[] getLignesJeu(char[] secret, char[][] tablePM) {

        //tableau de chaine de caractères pour affichage du jeu PlusMoins
        StringBuilder[] lignesJeu = new StringBuilder[256];

        //creation de chaque variable StringBuilder dans le tableau qui correspond aux lignes  affichablse, du jeu
        for (int n = 0; n < lignesJeu.length; n++) {
            lignesJeu[n] = new StringBuilder(48);
        }
        int rangLigne = 0;
        //titre
        lignesJeu[rangLigne++].append(modeDeJeu.toString());

        //espace
        lignesJeu[rangLigne++].append(" ");


        //zone de status
        if (modeDebug) {
            StringBuilder stmp = new StringBuilder(16);
            for (char c : secret) {
                stmp.append(String.format(" %c ", c));
            }
            lignesJeu[rangLigne++].append(strLibelleSecretHeader).append(stmp.toString()).append(strLibelleSecretTrailer);
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
        logger.debug(String.format("ChiffreEstValide char = %c", c));

        int i;
        try {
            i = Integer.valueOf(String.valueOf(c));
            logger.debug(String.format("ChiffreEstValide Conversion ENtier = i = %d ", i));
            if (i <= 0) {
                logger.debug(String.format("ChiffreEstValide  i <=0  %d ", i));
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            logger.error(Messages.ErreurMessages.PM_CHIFFRE_HORS_PLAGE_TOLERANCE + " Car= " + c);
            throw new AppExceptions(Messages.ErreurMessages.PM_CHIFFRE_HORS_PLAGE_TOLERANCE);
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
            logger.error(Messages.ErreurMessages.PM_CARSCORE_HORS_PLAGE_TOLERANCE + " Car= " + c);
            throw new AppExceptions(Messages.ErreurMessages.PM_CARSCORE_HORS_PLAGE_TOLERANCE);
        }
    }

    /**
     * calcule le score de l'assai passé en parametre (sert dans tous les mode du jeu)
     * renvoi vrai si l'essai est égal au secret
     *
     * @param essai tableau de char qui contient l'essai - non modifié par la méthode
     * @param score tableau de char vide mais qui doit être alloué (renseigné avec le scorre en sortie de méthode
     * @return boolean - vrai si secret trouvé , sinon faux
     */
    private boolean CalculScore(char[] essai, char[] score, char[] secret) {
        int compteEgal = 0;
        for (int i = 0; i < essai.length; i++) {
            score[i] = (essai[i] == secret[i]) ? '=' : ((essai[i] < secret[i]) ? '-' : '+');
            compteEgal += (score[i] == '=' ? 1 : 0);
        }
        return compteEgal == nombreDePositions;
    }

    /**
     * Saisie clavier - supporte les méthodes de saisies suivantes
     * Si x et y sont des valeurs acceptables (donc pattern == [x y K k] avec k étant escapeChar
     * x return  y return   => donne x y
     * x y return           => donne x y
     * xyreturn             => donne x y
     * x{n'importe quoi sauf escapeChar}y  => x y
     * x{n'importe quoi sauf escapeChar}Ky => escapeChar donc break
     * *  EscapeChar : soit retour au niveau d'appel soit saisie par défaut (i.e score suggéré est saisi par défaut
     * et devient le score renvoyé)
     *
     * @param lesCharsAFournir char [] table de char renseigné par la saisie
     * @param secret           char []  sert à l'affichage
     * @param tablePM          char [][] sert à l'affichage
     * @param pattern_Menu     String  , pattern de saisie utilisé par méthode Next de la classe Scanner
     * @param escapeChar       : Character  - charactère qui s'il est présent dans la saisie, stop le process de saisie
     * @throws AppExceptions : déclenché sur erreur non géré ou sur lecture de EscapeChar dans ce cas l'escape est transmis
     *                       à l'exception et est récupérable par la méthode getCharacterSortie() de AppException
     */
    private void SaisirDesChars(char[] lesCharsAFournir, char[] secret, char[][] tablePM, String pattern_Menu, Character escapeChar) throws AppExceptions {

        Pattern patternChoix = Pattern.compile(pattern_Menu);

        StringBuilder saisieParChaine = new StringBuilder(nombreDePositions);

        Character cRet = '?';

        AfiicheJeuPM(secret, tablePM);
        // scanner en mode hasNext, next - avec pattern de carteres autorisés

        int localCount = 0;

        try {
            while (localCount < nombreDePositions && cRet != escapeChar && scanner.hasNext()) {

                try {
                    saisieParChaine.delete(0, saisieParChaine.length());

                    for (char c : scanner.next().toCharArray()) {
                        if (c != ' ')
                            saisieParChaine.append(c).append(' ');
                    }

                    //on tente une lecture sur cette chaine ave cloture auto sur ce scanner
                    try (Scanner monScan = new Scanner(saisieParChaine.toString())) {

                        while (localCount < nombreDePositions && monScan.hasNext()) {

                            try {
                                cRet = monScan.next(patternChoix).toUpperCase().charAt(0);

                                if (cRet != escapeChar) {

                                    lesCharsAFournir[localCount++] = cRet;

                                    strLibelleSaisie += " " + cRet;

                                } else {
                                    break;
                                }

                            } catch (InputMismatchException e) {

                                monScan.next();
                            } catch (NoSuchElementException e) {

                                cRet = escapeChar; //Ctrl-C

                                break;
                            }
                        }
                    }

                } catch (NoSuchElementException e) {
                    // one devrait pas arrive la ou alors pas compris donc : on sort proprement
                    cRet = escapeChar;

                    break;
                } catch (IllegalStateException e) {

                    logger.error(String.format("%s %s %s", ERREUR_GENERIC, e.getClass().getSimpleName(), " scanner fermé"));

                    throw new AppExceptions(ERREUR_GENERIC);
                }

            }

            AfiicheJeuPM(secret, tablePM);

            //soirtie par escapeChar ou CTRL-C
            if (cRet == '?' || cRet == escapeChar) {

                logger.info(CTRL_C.toString() + " ou " + escapeChar.toString());

                throw new AppExceptions(SORTIE_SUR_ESCAPECHAR, charactersEscape);

            }

        } catch (IllegalStateException e) {

            logger.error(String.format("%s %s %s", ERREUR_GENERIC, e.getClass().getSimpleName(), " scanner fermé"));

            throw new AppExceptions(ERREUR_GENERIC);
        }
    }

    /**
     * renseigne le secret avec le nombre à trouver, sous forme de caractères
     * saisie par le Joueur     *
     * @throws AppExceptions  : déclenché sur erreur non géré ou sur lecture de EscapeChar dans ce cas l'escape est transmis
     * à l'exception et est récupérable par la méthode getCharacterSortie() de AppException
     */
    private void ProduireSecretModeDefenseur() throws AppExceptions {
        modeDebug = true;
        try {
            strLibelleSaisie = ". Saisie du Secret -> (K : retour)    ] ";
            SaisirDesChars(secret, new char[secret.length], tablePM, "[1-9 K k]", charactersEscape);
            strLibelleSaisie = ". Saisie -> (K : retour)    ] ";
        } catch (Exception e) {
            if (e instanceof AppExceptions) {
                if (((AppExceptions) e).getCharacterSortie() == charactersEscape)
                    return;
            }
            logger.error(ERREUR_GENERIC);
            logger.error(String.format("%s %s", ERREUR_GENERIC, e.getClass().getSimpleName()));
            throw new AppExceptions(ERREUR_GENERIC);
        }
    }

    /**
     * renseigne l'attibut secretJeuPM avec le nombre à trouver, sous forme de caractères
     * par calcul
     */
    private void ProduireSecretModeChallengeur() {
        DecimalFormat df = new DecimalFormat("#");
        // "graine" pour le random.
        df.setRoundingMode(RoundingMode.HALF_UP);

        int valTmp;
        for (int limite = 0, k = 0; k < nombreDePositions && limite < nombreMaxDeBoucles; limite++) {
            valTmp = ((Integer.parseInt(df.format(StrictMath.random() * 100)) % 10));
            if (valTmp > 0) {
                secret[k++] = (char) (valTmp + '0');
            }
        }
        //on passe par la "case" assignation par défaut car pas réussi à fabriquer un secret
        if (secret.length != nombreDePositions) {
            logger.info(Messages.InfosMessages.REMPLACEMENT_PAR_VALEUR_DEFAUT.toString() + " pas réussi à générer un secret ");
            for (int m = 0; m < nombreDePositions; m++)
                secret[m] = (char) (m + '0');
        }
    }

    /**
     * actions pour saisir essai joueur
     *
     * @param nbBoucle int
     */
    private void ProduireEssaiModeChallengeur(int nbBoucle) {
        strLibelleInfos = "[     Informations          ] ";
        strLibelleSaisie = ". Saisie -> (K : retour)    ] ";
        SaisirDesChars(essai, secret, tablePM, "[1-9 K k]", charactersEscape);
        strLibelleSaisie = ". Saisie -> (K : retour)    ] ";
        logger.debug(String.format("Mode Challenegeur FaitMoiUnEssai boucle = %d  essai = %s  secret = %s ", nbBoucle, String.valueOf(essai), String.valueOf(secret)));
        AjouterUnEssai(nbBoucle, essai, tablePM);
        AfiicheJeuPM(secret, tablePM);
    }

    /**
     * actions pour calculer un essai de l'ordinateur
     * renvoie un nouvel essai calculé
     *  minmax:
     * si l'essai est plus petit que le secret, recherche la plus petite borne supérieure
     * si l'essai est plus gand que le secret , recherche la plus grande borne inférieure
     *
     * @param nbBoucle int
     */
    private void ProduireEssaiModeDefenseur(int nbBoucle) {

        int defautMin = 0, defautMax = 9;

        for (int i = 0; i < nombreDePositions; i++) {

            //selon le score obtenu, le calcul est différent car il faut tenir compte du sens + ou -
            switch (score[i]) {
                case '=': {
                    //le chiffres est le bon donc on reporte à l'identique
                    essai[i] = tablePM[nbBoucle - 1][i];
                }
                break;

                case '-': {
                    //le chiffre est inférieur au secret
                    // essai[i] < nouvelEssai[i] < BorneSUPx
                    // BorneSupx = min(essai[y],score[y] == '+'
                    int borneSup = 9;

                    for (int m = nbBoucle - 1; m >= 0; m--) {
                        if (tablePM[m][i + nombreDePositions] == '+') {
                            int val2Tmp = tablePM[m][i] - '0';
                            borneSup = StrictMath.min(borneSup, val2Tmp);
                        }
                    }

                    if (borneSup < (tablePM[nbBoucle - 1][i] - '0')) {
                        logger.error(Messages.ErreurMessages.ERREUR_GENERIC.toString() + "calcul  borneSup erreur");
                        throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
                    }

                    // prend le milieu du segmznt essai[i] .. borneSup, valeur superieur car on "monte" vers la soluce
                    essai[i] = (char) ((int) StrictMath.ceil(((double) borneSup + (double) (tablePM[nbBoucle - 1][i] - '0')) / 2.0) + '0');

                }
                break;

                case '+': {
                    //le chiffre est supérieur au secret
                    //  BorenInfx <  nouvelEssai[i] < essai[i]
                    // BorenInfx = max(essai[y],score[y] == '-'
                    int borneInf = 1;

                    for (int m = nbBoucle - 1; m >= 0; m--) {
                        if (tablePM[m][i + nombreDePositions] == '-') {
                            int valTmp = tablePM[m][i] - '0';
                            borneInf = StrictMath.max(borneInf, valTmp);
                        }
                    }

                    if (borneInf > (tablePM[nbBoucle - 1][i] - '0')) {
                        logger.error(Messages.ErreurMessages.ERREUR_GENERIC.toString() + "calcul  borneInf erreur");
                        throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
                    }

                    // prend le milieu du segmznt essai[i] .. borneSup, valeur inferieur car on "descend" vers la soluce
                    essai[i] = (char) ((int) StrictMath.floor(((double) (tablePM[nbBoucle - 1][i] - '0') + (double) borneInf) / 2.0) + '0');
                }
                break;

                //section init car c'est le premier essai
                default: {
                    essai[i] = (char) ('0' + (16 * (i * 3 + 1)) % 9);
                }
            }
        }
        logger.debug(String.format("Mode Defenseur FaitMoiUnEssai boucle = %d  essai = %s  secret = %s ", nbBoucle, String.valueOf(essai), String.valueOf(secret)));
        AjouterUnEssai(nbBoucle, essai, tablePM);
        AfiicheJeuPM(secret, tablePM);
    }

    /**
     * actions pour scorer un essai de l'odinateur
     *
     * @param nbBoucle int  -
     * @return boolean , vrai si secret trouvve
     */
    private boolean EvalueScoreModeDefenseur(int nbBoucle) {

        boolean isTrouve;
        int locaCount = 0;

        strLibelleSaisie = ". Saisie du Score  -> (K : automatique)    ] ";

        logger.debug(String.format("Mode Defenseur FaitMoiUnScore boucle = %d  essai = %s  score = %s  secret = %s", nbBoucle, String.valueOf(essai), String.valueOf(score), String.valueOf(secret)));

        //saisir score et presente resultat de CalculScore(essai, score, secret); validation par escapeChar

        char[] scoretmp = new char[nombreDePositions];

        CalculScore(essai, scoretmp, secret);

        strLibelleInfos = "suggestion : " + String.valueOf(scoretmp);

        try {
            SaisirDesChars(score, secret, tablePM, "[+ \\- = K k]", charactersEscape);
        } catch (AppExceptions e) {
            if (e.getCharacterSortie() == charactersEscape) {
                System.arraycopy(scoretmp, 0, score, 0, score.length);
            } else {
                logger.error(ERREUR_GENERIC + "sortie saisie score ordi anormale");
                throw new AppExceptions(ERREUR_GENERIC);
            }
        }

        for (char c : score) {
            if (c == '=') locaCount++;
        }

        isTrouve = (locaCount == nombreDePositions);

        strLibelleSaisie = ". Saisie -> (K : retour)    ] ";

        logger.debug(String.format("Mode Defenseur FaitMoiUnScore boucle = %d  essai = %s  score = %s  secret = %s ", nbBoucle, String.valueOf(essai), String.valueOf(score), String.valueOf(secret)));

        AjouterunScore(nbBoucle, score, tablePM);

        AfiicheJeuPM(secret, tablePM);

        return isTrouve;
    }

    /**
     * actions pour scorer essai du joueur
     *
     * @param nbBoucle int
     * @return boolean vrai si secret trouve
     */
    private boolean EvaluerScoreModeChallengeur(int nbBoucle) {
        boolean isTrouve = CalculScore(essai, score, secret);
        AjouterunScore(nbBoucle, score, tablePM);
        AfiicheJeuPM(secret, tablePM);
        return isTrouve;
    }

    /*
     *
     */
    private void runAllModeFunctionnal(GloupseClasse gloupseClasse) {
        boolean isTrouve = false;

        modeDebug = gloupseClasse.valoriseDebug.get();

        //recharge les libelles - cas ou on enchaine les mode de jeu du jeu PM
        initLibellesLignes();

        gloupseClasse.produireSecret.accept(null);

        for (int nbBoucle = 0; nbBoucle < nombreDeEssaisMax; nbBoucle++) {

            try {

                gloupseClasse.proposerEssai.accept(nbBoucle);

                isTrouve = gloupseClasse.evaluerScore.apply(nbBoucle);

                if (isTrouve) {
                    break;
                }

            } catch (Exception e) {
                if (e instanceof AppExceptions) {
                    if (((AppExceptions) e).getCharacterSortie() == charactersEscape)
                        return;
                }
                logger.error(String.format("%s %s", ERREUR_GENERIC, e.getClass().getSimpleName()));
                throw new AppExceptions(ERREUR_GENERIC);

            }
        }
        //fin de ce jeu
        if (isTrouve) {
            strLibelleInfos = gloupseClasse.informerVictore.get();
        } else {
            strLibelleInfos = gloupseClasse.informerDefaite.get();
        }

        AttenteNettoyageUInput(charactersEscape);
    }

    /**
     * regroupe les modes challengeur et defenseur
     *
     * @param interfRunPM interface interne
     */
    private void runAllMode(InterfRunPM interfRunPM) {

        boolean isTrouve = false;

        modeDebug = interfRunPM.FaitMoiDebug();

        //recharge les libelles - cas ou on enchaine les mode de jeu du jeu PM
        initLibellesLignes();

        interfRunPM.FaitMoiUneSecret();

        for (int nbBoucle = 0; nbBoucle < nombreDeEssaisMax; nbBoucle++) {

            try {

                interfRunPM.FaitMoiUnEssai(nbBoucle);

                isTrouve = interfRunPM.FaitMoiUnScore(nbBoucle);

                if (isTrouve) {
                    break;
                }

            } catch (Exception e) {
                if (e instanceof AppExceptions) {
                    if (((AppExceptions) e).getCharacterSortie() == charactersEscape)
                        return;
                }
                logger.error(String.format("%s %s", ERREUR_GENERIC, e.getClass().getSimpleName()));
                throw new AppExceptions(ERREUR_GENERIC);

            }
        }
        //fin de ce jeu
        if (isTrouve) {
            strLibelleInfos = interfRunPM.FaitMoiUnMessageDeVictoire();
        } else {
            strLibelleInfos = interfRunPM.FaitMoiUnMessageDeEchec();
        }

        AttenteNettoyageUInput(charactersEscape);
    }

    /**
     * joue  en mode Challengeur
     */
    void runModeChallengeur() {

        runAllModeFunctionnal(new GloupseClasse(
                (x) -> ProduireSecretModeChallengeur(),
                this::EvaluerScoreModeChallengeur,
                this::ProduireEssaiModeChallengeur,
                () -> modeDebug,
                () -> "[     Vous avez Gagné       ] ",
                () -> "[     Vous avez Perdu       ] "));

/*        runAllMode(new InterfRunPM() {
            @Override
            public void FaitMoiUneSecret() {
                //creation du secret par calcul
                ProduireSecretModeChallengeur();
            }

            @Override
            public void FaitMoiUnEssai(int nbBoucle) {
                ProduireEssaiModeChallengeur(nbBoucle);
            }

            @Override
            public boolean FaitMoiUnScore(int nbBoucle) {
                return EvaluerScoreModeChallengeur(nbBoucle);
            }

            @Override
            public String FaitMoiUnMessageDeVictoire() {
                return "[     Vous avez Gagné       ] ";
            }

            @Override
            public String FaitMoiUnMessageDeEchec() {
                return "[     Vous avez Perdu       ] ";
            }

            @Override
            public boolean FaitMoiDebug() {
                return modeDebug;
            }
        });
  */
    }

    /**
     * joue en mode defenseur
     */
    void runModeDefenseur() {

        runAllModeFunctionnal(new GloupseClasse(
                (x) -> ProduireSecretModeDefenseur(),
                this::EvalueScoreModeDefenseur,
                this::ProduireEssaiModeDefenseur,
                () -> true,
                () -> "[    Ordinateur a  Gagné    ] ",
                () -> "[    Ordinateur a  Perdu    ] "));
/*
        runAllMode(new InterfRunPM() {

            @Override
            public void FaitMoiUneSecret() throws AppExceptions{
                ProduireSecretModeDefenseur();
            }

            @Override
            public void FaitMoiUnEssai(int nbBoucle) {
                ProduireEssaiModeDefenseur(nbBoucle);
            }

            @Override
            public boolean FaitMoiUnScore(int nbBoucle) {
                return EvalueScoreModeDefenseur(nbBoucle);
            }

            @Override
            public String FaitMoiUnMessageDeVictoire() {
                return "[    Ordinateur a  Gagné    ] ";
            }

            @Override
            public String FaitMoiUnMessageDeEchec() {
                return "[    Ordinateur a  Perdu    ] ";
            }

            @Override
            public boolean FaitMoiDebug() {
                return true;
            }
        });
*/

    }

    /**
     * joue en mode duel
     */
    void runModeDuel() {

        boolean isTrouveOrdi = false;
        boolean isTrouveJoueur = false;

        //recharge les libelles - cas ou on enchaine les mode de jeu du jeu PM
        initLibellesLignes();

        //creation du secret par calcul
        ProduireSecretModeChallengeur();

        for (int nbBoucle = 0; nbBoucle < nombreDeEssaisMax; ) {

            try {
                //ordinateur Joue
                ProduireEssaiModeDefenseur(nbBoucle);

                //evalue score Ordinateur
                isTrouveOrdi = EvalueScoreModeDefenseur(nbBoucle++);

                if (isTrouveOrdi) {
                    break;
                }

                //Joueur joue
                ProduireEssaiModeChallengeur(nbBoucle);

                //evalue score du joueur
                isTrouveJoueur = EvaluerScoreModeChallengeur(nbBoucle++);

                if (isTrouveJoueur) {
                    break;
                }

            } catch (Exception e) {
                if (e instanceof AppExceptions) {
                    if (((AppExceptions) e).getCharacterSortie() == charactersEscape)
                        return;
                }
                logger.error(String.format("%s %s", ERREUR_GENERIC, e.getClass().getSimpleName()));
                throw new AppExceptions(ERREUR_GENERIC);

            }
        }
        //sortie de ce jeu
        if (isTrouveOrdi) {
            strLibelleInfos = "[    Ordinateur a  Gagné    ] ";
        } else if (isTrouveJoueur) {
            strLibelleInfos = "[     Vous avez Gagné       ] ";
        } else {
            strLibelleInfos = "[  Pas un.e pour rattraper l'autr.e ] ";
        }

        AttenteNettoyageUInput(charactersEscape);
    }
}
/*
 *********************************************************************
 * the end
 * *******************************************************************
 */
