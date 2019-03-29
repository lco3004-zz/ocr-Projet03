package fr.ocr.mastermind;

import fr.ocr.modeconsole.EcrireSurEcran;
import fr.ocr.modeconsole.IOConsole;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.FabPattSais;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.*;
import static fr.ocr.utiles.Constantes.ConstTailleStringBuilder.TAIILE_INITIALE;
import static fr.ocr.utiles.Constantes.ConstTypeDeLigne.*;
import static fr.ocr.utiles.Constantes.CouleursMastermind;
import static fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
import static fr.ocr.utiles.Messages.InfosMessages.SORTIE_ESCAPE_SAISIE_SCORE;
import static fr.ocr.utiles.Messages.InfosMessages.SORTIE_SUR_ESCAPECHAR;

/*
 ********************************************************************************************************************
 */
/**
 *
 * @author Laurent Cordier
 *
 * mode CHALLENGEUR  :  JeuMasterMind jeuMasterMind = JeuMasterMind.CHALLENGEUR(ch_Sec, scanner);
 * mode DEFENSEUR    :  JeuMasterMind jeuMasterMind = JeuMasterMind.DEFENSEUR(ch_Sec, scanner);
 * mode DUEL         :  JeuMasterMind jeuMasterMind = JeuMasterMind.DUEL(ch_Sec, scanner);
 *
 * lancement du jeu (Challengeur ou Defenseur ou Duel)  : jeuMasterMind.runJeuMM();
 *
 * <p>
 * Interface qui donne acc&egrave;s aux mode de jeu MasterMind.
 * </p>
 *<p>
 *     interface d'accès au deux modes principaux du jeudefenseur, challengeur)
 *</p>
 *
 */
public interface JeuMasterMind {

    /**
     *
     *  mode Jeu CHALLLENGEUR  - creation de la classe qui gère ce mode
     *
     * @param modeJeu  LibellesMenuSecondaire
     * @param sc  scanner
     *
     */
    static void CHALLENGEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        (new JeuMMChallengeur(modeJeu, sc)).runJeuMM();
    }

    /**
     *
     *  mode Jeu DEFENSEUR  - creation de la classe qui gère ce mode
     *
     * @param modeJeu LibellesMenuSecondaire
     * @param sc      scanner
     *
     */
    static void DEFENSEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        (new JeuMMDefenseur(modeJeu, sc)).runJeuMM();
    }

    /**
     *
     *  mode Jeu DUEL  - creation de la classe qui gère ce mode
     *
     * @param modeJeu LibellesMenuSecondaire
     * @param sc scanner
     *
     */
    static void DUEL(LibellesMenuSecondaire modeJeu, Scanner sc) {
        (new JeuMMDuel(modeJeu, sc)).runJeuMM();
    }

    /**
     *
     * Point d'entrée ( un "exit" disaient les ancêtres  qui travaillaient en carte perforée sur un IBM ou un un Bull)
     *
     */
    void runJeuMM();

}
/*
 ********************************************************************************************************************
 */

/**
 *
 * Classe du mode jeu Duel mastermind - alternance code/decode pour Joueur et Ordinateur
 *
 */
class JeuMMDuel extends JeuMM {

    /**
     *
     * construteur
     *
     * @param modeJeu : challengeur, defenseur, duel
     * @param sc      scanner saisie clavier
     *
     */
    JeuMMDuel(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
    }

    @Override
    public void runJeuMM() {
        //génération du secret sous forme de liste d'initiales des couleurs de la combinaison secrete
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();

        //l'ordinateur valide la proposition du Joueur : le Joueur est alternativement
        //  -- une saisie clavier (physique ou simulée par JUnit)
        //  -- une proposition calculée
        controleProposition = new ScorerProposition();

        // la variable interface "pointe" sur l'objet qui génère une proposition automatiquement (l'ordinateur joue)
        produirePropaleDefenseur = new ProduirePropaleMMDefenseur();

        // la variable interface "pointe" sur l'objet qui sert à saisir au clavier une proposition (le Joueur joue)
        produirePropaleChallengeur = new ProduirePropaleMMChallengeur(lignesSimpleMM, lignesPropaleMM);

        // lance le jeu en mode duel
        RunJeuMMDuel(fabricationSecretMM);
    }

}

/*
 ********************************************************************************************************************
 */

/**
 *
 * Classe du mode de jeu Defenseur mastermind - Ordinateur décode la propostion du joueur
 *
 */
class JeuMMDefenseur extends JeuMM {

    /**
     * construteur
     *
     * @param modeJeu : challengeur, defenseur, duel
     * @param sc      scanner saisie clavier
     */
    JeuMMDefenseur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
    }

    @Override
    public void runJeuMM() {

        //génération du secret sous forme de liste d'initiales des couleurs de la combinaison secrete
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();

        //le parametre fabricationSecretMM permet de suggerer une combinaison secrete au Defenseur
        ArrayList<Integer> combinaisonSecrete = super.RunSaisieSecretMM(fabricationSecretMM);

        // suite du worklflow si Defenseur n'est pas sorti la saisie par 'EscapeChar'
        //si c'est le cas la combinaison secrete est vide
        if (combinaisonSecrete.size() == (int) getParam(NOMBRE_DE_POSITIONS)) {
            //on fabrique le secret avec la combinaison saisie par le Defenseur
            //pourra être simplifié : le secret est composé d'une combinaison de couleurs et d'une
            //combinaison de chiffres qui sont en bijection l'un avec l'autre.
            //la combinaison de chiffre pourra etre supprimé dans une future release car n'est pas utilisée
            // dans ce modèle du jeu
            fabricationSecretMM = new FabricationSecretMM(combinaisonSecrete);

            //la variable interface "pointe vers l'objet qui permet de génèrer une proposition automatiquement
            produirePropaleDefenseur = new ProduirePropaleMMDefenseur();

            //la variable interface "pointe" vers l'objet qui permet de compparer la proposition du "joueur" vs combinaison secrete
            //rappel le "joueur" est soit une saisie clavier (réelle ou simulée par junit) ou un calcul
            controleProposition = new ScorerProposition();

            // lance le jeu en mode defenseur
            RunJeuMMDefenseur(fabricationSecretMM);
        }
    }
}

/*
 ********************************************************************************************************************
 */

/**
 * classe du mode Challengeur mastermind - orinateur  fabrique un secret (combinaison de couleurs), le Joueur essaie de la découvrir
 */
class JeuMMChallengeur extends JeuMM {


    /**
     * construteur
     *
     * @param modeJeu : challengeur, defenseur, duel
     * @param sc      scanner saisie clavier
     */
    JeuMMChallengeur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
    }

    @Override
    public void runJeuMM() {
        //génération du secret sous forme de liste d'initiales des couleurs de la combinaison secrete
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();

        //la variable interface "pointe" vers l'objet qui permet de compparer la proposition du "joueur" vs combinaison secrete
        //rappel le "joueur" est soit une saisie clavier (réelle ou simulée par junit) ou un calcul
        controleProposition = new ScorerProposition();

        //la variable interface "pointe vers l'objet qui permet de génèrer une proposition par saisie
        produirePropaleChallengeur = new ProduirePropaleMMChallengeur(lignesSimpleMM, lignesPropaleMM);

        // lance le jeu en mode challengeur
        RunJeuMMChallengeur(fabricationSecretMM);
    }
}

/*
 ********************************************************************************************************************
 */

/**
 * "Modele" du jeuMastermind
 * <p>
 * Note :
 * * la combinaison secrete est
 * * * soit calculée par ordinateur en mode CHALLENGEUR ou mode DUEL
 * * * soit saisie par le joueur  en mode DEFENSEUR
 * </p>
 * <li>
 *     <p>
 * * la fabrication de la composition secrete 'S' dépend de :
 * * * * NOMBRE_DE_COULEURS : le nombre de couleurs disponibles  'N'
 * * * * * limité à 18 max par construction, valeur min 6 couleurs qui est une valeur std)
 * * * * NOMBRE_DE_POSITIONS : le nombre de couleurs 'P'  constituant la composition secrete S,
 * * * * * 8 max par construction, min 4 qui est une valeur Std
 * * * *  et DOUBLON_AUTORISE (mis à faux par défaut)
 *     </p>
 * </li>
 */
abstract class JeuMM implements JeuMasterMind {

    /* tableau de lignes de type Proposition
     * chaque ligne Propostion contient la proposition soumise, le score de cette proposition, la méthode de calcul du score
     * la méthode de calcul du socre est passée en paramétre à la ligne, la méthode n'étant pas constante selon le mode de jeu
     * * mode challengeur ou duel : méthode par calcul/comparaison proposition vs secret
     * * mode defenseur  : méthode par saisie clavier
     */
    LignePropaleMM[] lignesPropaleMM = new LignePropaleMM[(Integer) getParam(NOMBRE_D_ESSAIS)];

    /*
     * tableau de lignes de type affichage simple  . pas d'autre fonction que de stocker une chaine de car. à afficher
     */
    LigneMM[] lignesSimpleMM = new LigneMM[NBRE_LIGNESTABLEMM];

    // variable d'interface qui polymorphise - la validation dépend du mode du jeu en cours
    ControleProposition controleProposition;

    //
    // variable d'interface qui polymorphise - la fabrication de la proposition dépend du mode du jeu en cours
    // dans cette architecture, il faut deux variables pour traiter le mode duel
    // sans ce mode qui alterne Challengeur et Defenseur, une seule variable d'interface suffit (c'est même le principe de base)
    //
    ProduirePropaleMM produirePropaleDefenseur;
    ProduirePropaleMM produirePropaleChallengeur;

    // nombre lignes de jeu de la table du MasterMind
    private Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);

    //le caractère EscapeChar de retour au menu superieur - choisit tel que  EscapeChar  !€ à ensemble des initiales des couleurs
    private Character charactersEscape = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);

    // paramètre du fichier properties - nombre de positions de pion par ligne de la table de jeu Mastermind
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);

    // paramètre du fichier properties - nombre de positions de couleurs disponibles pour construire une proposition et la combinaison secrete
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);

    // paramètre du fichier properties - choix O/N pour autorise(Oui) de 0 à n occurences d'une même couleur dans le secret ou la proposition
    // ce choix influe sur le nombre de cas possibles -> le modele passe d'un calcul d'arrangement A(n,p) à un modele exposant puissance(n,p)
    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);

    // paramètre du fichier properties - affiche (DEBUG=Vrai) ou pas la combinaison secrete
    private Boolean modeDebug = (Boolean) getParam(MODE_DEVELOPPEUR);

    // les options du menu du jeu MasterMind
    private LibellesMenuSecondaire modeJeu;

    // scanner pour saisie clavier
    private Scanner scanner;

    /**
     * construteur
     * @param modeJeu : challengeur, defenseur, duel
     * @param sc      scanner saisie clavier
     */
    JeuMM(LibellesMenuSecondaire modeJeu, Scanner sc) {

        scanner = sc;

        this.modeJeu = modeJeu;
    }

    /**
     *
     * saisie d'un caractere controlé par Scanner avec pattern égal aux couleurs possibles +escapechar
     * avec affichage des lignes du jeu, la dernière ligne étant affichée sans retour chariot. pour que la saisie soit sur la meme ligne
     * que son libelle ( i.e  "votre choix -> " 45   , le libelle "votre choix" s'affiche sur la meme ligne écran que la saisie , ici le chiffre 45 )
     *
     * @param pattern pattern de saisie pour classe Scanner
     * @return char , caractere saisie clavier
     *
     */
    private char LitCharEtAfficheLignes(String pattern) {
        return IOConsole.LectureClavierChar(pattern, scanner, new EcrireSurEcran() {
            @Override
            public void Display() {
                for (int n = TITRE; n <= LIGNE_DE_SAISIE; n++) {
                    if (lignesSimpleMM[n].isEstVisible()) {
                        if (n == LIGNE_DE_SAISIE) {
                            System.out.print(lignesSimpleMM[n].toString());
                        } else {
                            System.out.println(lignesSimpleMM[n].toString());
                        }
                    }
                }
            }
        }, charactersEscape);
    }

    /**
     * création des lignes de la table du jeu Mastermind : ligneMM et lignePropaleMM
     * * affectation des données affichables pour ligneMM et lignePropaleMM
     * * affectation de la méthode de validation de la proposition  uniquement pour lignePropaleMM
     *
     * @param modeDeJeu        : challengeur, defenseur, duel
     * @param couleursSecretes combinaison secrete sous forme de mots  i.e (VERT, BLEU,...}
     *
     */
    private void PreparationMenu(LibellesMenuSecondaire modeDeJeu, CouleursMastermind[] couleursSecretes) {

        /*
         * affectation des chaines de caractères aux lignes du menu à afficher
         * chaque ligne du tableau est repéré par son indice indiqué dans un Enum
         */
        lignesSimpleMM[TITRE] = new LigneMM(true, true, TITRE, TITRE, modeDeJeu.toString());

        lignesSimpleMM[LIGNE_STATUS] = new LigneMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format("Mode debug = %b", modeDebug));

        lignesSimpleMM[LIGNE_SECRETE] = new LigneMM(true, false, LIGNE_SECRETE, LIGNE_SECRETE, " -------SECRET--------");

        //pattern uniquement visuel qui est utiliser pour afficher les lignes de type Propositons
        String champBlancNoir;

        StringBuilder lesCroixEtVirgules = new StringBuilder(TAIILE_INITIALE);

        for (int nbPositions = 0; nbPositions < nombreDePositions; nbPositions++) {

            lesCroixEtVirgules.append(' ');

            lesCroixEtVirgules.append('x');

            lesCroixEtVirgules.append(',');
        }

        lignesSimpleMM[LIGNE_TOUTES_COULEURS] = new LigneMM(true, true, LIGNE_TOUTES_COULEURS, LIGNE_TOUTES_COULEURS, " ");

        // definti le libelle d'entete de la table  de jeu , dépend du nombre de positions paramétrées.
        champBlancNoir = String.format(" ## [%s ] %c %c", lesCroixEtVirgules.substring(0, lesCroixEtVirgules.length() - 1), PIONS_BIENPLACES, PIONS_MALPLACES);

        lignesSimpleMM[LIGNE_ENTETE] = new LigneMM(true, true, LIGNE_ENTETE, LIGNE_ENTETE, champBlancNoir);

        //lignes separatrices pour affichage uniquement
        lignesSimpleMM[LIGNE_BLANCH01] = new LigneMM(true, true, LIGNE_BLANCH01, LIGNE_BLANCH01, " ");
        lignesSimpleMM[LIGNE_BLANCH02] = new LigneMM(true, true, LIGNE_BLANCH02, LIGNE_BLANCH02, " ");

        //ligne de bas de table , habituelle 'i.e votre choix ?'
        lignesSimpleMM[LIGNE_DE_SAISIE] = new LigneMM(true, true, LIGNE_DE_SAISIE, LIGNE_DE_SAISIE, String.format(" Votre choix (%c : Retour): ", charactersEscape));

        int nombreDessai = (Integer) getParam(NOMBRE_D_ESSAIS);
        //affectation des lignes de type proposition
        for (int k = 0, indexLignesJeuMM = LIGNE_PROPOSITION; k < nombreDessai; k++, indexLignesJeuMM++) {
            /*
             * en paramètres
             * initiales des couleurs de la combinaison secrete,
             * chiffres de la combinaison secrete (en bijection  avec les couleurs de la combinaison secrete)
             * la ligne est disponible pour affichage de la paroposition courante (réservé futur use - toujours à true , dans cette version, qui ne gère pas le rollback)
             * la ligne est visible par défaut (toujours vrai)
             * type de ligne
             * infos à afficher - rien par défaut
             * méthode de validation de la proposition faite  par Ordinateur (Defenseur,Duel) ou par Joueur (Challengeur,Duel)
             */
            lignesPropaleMM[k] = new LignePropaleMM(couleursSecretes, true, true,
                    k, LIGNE_PROPOSITION, "", controleProposition);

            // efface contenu lignes et affiche le contenu par defaut
            lignesPropaleMM[k].Clear().setLibelleLigne();

            /*
             * ajout de cette ligne Proposition au tableau de toutes les lignes affichables
             *  c'est le tableau de lignesSimpleMM qui est parcouru pour afficher la table du jeu
             */
            lignesSimpleMM[indexLignesJeuMM] = lignesPropaleMM[k];
        }
    }

    /**
     * evite duplcation code entre mode challengeur, defenseur et duel
     * @param fabricationSecretMM instance qui dépend du mode de jeu (saisie ou calcul)
     */
    private void PrepareRunJeuMM(FabricationSecretMM fabricationSecretMM) {

        //trace la combinaison secrete calculée - en mode defenseur, il ya saisie de la combinaison secerte
        // en mode Defenseur, celle qui est calculée est présentée pour info - on peut prendre cette suggestion ou faire un autre choix
        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes(), nombreDeCouleurs);

        PreparationMenu(modeJeu, fabricationSecretMM.getCouleursSecretes());

        // nombre de couleurs dans la combinaison secrete
        int nbColSec = fabricationSecretMM.getCouleursSecretes().length;

        // charge la ligne de rang LIGNE_SECRETE avec la combinaison secrete dans sa forme composée d'initiales de
        // couleurs
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(fabricationSecretMM.getCouleursSecretes());
        //debug , on affiche la combinaison secrete
        if (modeDebug) {
            lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);
        }
    }

    /**
     * lance le jeu en mode duel
     *
     * @param fabricationSecretMM instance qui dépend du mode de jeu (saisie ou calcul)
     */
    void RunJeuMMDuel(FabricationSecretMM fabricationSecretMM) {

        //sert à savoir qui a gagné pour présenter le message de félicitation adapté
        boolean isSecretTrouveJoueur = false;
        boolean isSecretTrouveOrdinateur = false;

        // est-on sortie de la saisie par escapeChar ?
        boolean isEscapeCharSaisie = false;

        //index qui doit rester < au nombre d'essais possible (définit par paramétrage)
        Integer nbreEssaisConsommes = 0;

        //sert à afficher le nombre de coups jouer par chaque "joueur"
        Integer nbreEssaisOrdianteur = 0;
        Integer nbreEssaisJoueur = 0;

        // proposition de l'ordinateur
        ArrayList<Character> propalOrdinateur;

        // la proposition du joueur
        ArrayList<Character> propaleJoueur;

        // gere l'increment affichage des lignes propositions
        int indexLignesProposition = 0;

        // creation des lignes du jeu
        PrepareRunJeuMM(fabricationSecretMM);

        // pattern destiné à l'objet scanner : les initiales des couleurs disponibles pour la proposition + 'charactersEscape'
        String patternAvecCouleurs = FabPattSais.ConstruitPatternSaisie(CouleursMastermind.values(), nombreDeCouleurs, charactersEscape);

        //affiche  les couleurs qui peuvent être choisies pour faire une propositon : fonction paramètre du jeu "nombre de couleurs"
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);

        //clear ou cls selon le système , de l'affichage de la console
        //sinon , les affichages se suivent et c'est illissible
        IOConsole.ClearScreen.cls();

        //tant que  le nombre d'essais possibles n'est pas atteint
        while (nbreEssaisConsommes < nombreDeEssaisMax) {
            /*
             * commence par faire jouer Ordinateur
             */
            //ordinateur fournit une proposition
            propalOrdinateur = produirePropaleDefenseur.getPropaleDefenseur();

            //erreur interne - car ce  cas impossible dans ce mode de jeu
            if (propalOrdinateur == null) {
                throw new AppExceptions(ERREUR_GENERIC);
            }

            //via appel de EvalPropostion,  la propsotion est scorée en nombre Blancs et de Noirs
            if (lignesPropaleMM[indexLignesProposition].setPropositionJoueur(propalOrdinateur).setZoneProposition().EvalProposition()) {
                //ordinateur gagne
                isSecretTrouveOrdinateur = true;
                break;
            }

            //Secret pas trouve donc, prepare la recherche d'une nouvellle propisition
            produirePropaleDefenseur.setScorePropale(propalOrdinateur, lignesPropaleMM[indexLignesProposition].getZoneEvaluation());

            //affichage du nombre d'essais consommés par l'ordinateur
            lignesPropaleMM[indexLignesProposition].setTrailerLibelle(String.format(" | Ordinateur : %02d", nbreEssaisOrdianteur));

            nbreEssaisOrdianteur++;

            //passe à la ligne suivante pour que le Joueur Humain ou JUnit n'écrase pas la proposition de l'ordinateur
            indexLignesProposition++;

            // un essai de consommé
            nbreEssaisConsommes++;

            /*
             * c'est autour du joueur humain ou JuNit  de jouer.
             */
            //affichage par defaut de la ligne de saisie , destinée à recevoir la proposition du joueur
            lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

            /*

             TODO :   ce code est doublonné avec le mode challengeur, à nettoyer dans une future release

             */

            // saisie de la proposition du joueur
            propaleJoueur = produirePropaleChallengeur.getPropaleChallengeur(scanner, patternAvecCouleurs, charactersEscape);

            //si le joueur a saisie escapechar, bye
            if (propaleJoueur.contains(charactersEscape)) {
                //abandon de cette partie
                isEscapeCharSaisie = true;
                break;
            }

            /*
            fin du code doublonné
             */

            //evaluation de la proposition du joueur : la propostion est "scorée" en nombre de Blanc/Noir
            if (lignesPropaleMM[indexLignesProposition].setPropositionJoueur(propaleJoueur).setZoneProposition().EvalProposition()) {
                // joueur humain  a gagné
                isSecretTrouveJoueur = true;
                break;
            }

            //joueur humain n'a pas trouvé, donc le jeu continue
            lignesPropaleMM[indexLignesProposition].setTrailerLibelle(String.format(" | Joueur     : %02d", nbreEssaisJoueur));

            //un essai de plus consommé par le joueur humain
            nbreEssaisJoueur++;

            //on continue  à jouer
            indexLignesProposition++;
            nbreEssaisConsommes++;
        }

        // l'ordinateur a gagné  : affichage idoine
        if (isSecretTrouveOrdinateur) {
            lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("!!************* Ordinateur Gagne ************!!");

        //le joueur a gagné : affichage idoine
        } else if (isSecretTrouveJoueur) {
            lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("!!************ Joeur Gagne ************!!");

        // personne n'a trouvé (ce cas est impossible avec les paramètres standards : ordinateur trouve en - de 5 coups (lié au pseudo
        // random qui ne mélange pas assez les combinaisons possibles (mais qui va vérifier ? nobody !!)
        } else if (nbreEssaisConsommes >= nombreDeEssaisMax) {
            lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("!!************ pas top tout le monde a perdu ************!!");

        //abandon de cette partie apr saisie de escapeChar
        } else if (isEscapeCharSaisie) {
            return;
        //pas normal , ne doit jamais arrivé ici
        } else {
            throw new AppExceptions(ERREUR_GENERIC);
        }

        //remise en l'état initiale des qui ne sont pas nécessaires à l'affichage de fin (met en valeur l'infos utile : qui a gagné ?)
        lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne("");
        lignesSimpleMM[LIGNE_STATUS].setLibelleLigne("");

        //pour visualiser fin de partie (sauf si erreur ou escapeChar saisi
        LitCharEtAfficheLignes(FabPattSais.ConstruitPatternSaisie(charactersEscape));

        //raz affichage - fin de partie
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");
        lignesSimpleMM[LIGNE_STATUS].setLibelleLigne(lignesSimpleMM[LIGNE_STATUS].getLibelleLigneOriginal());

    }
/*
TODO  JE SUIS ICI au NiVEAU COMMENTAIREs
 */

    /**
     * lance le jeu en mode challengeur
     * @param fabricationSecretMM instance qui dépend du mode de jeu (saisie ou calcul)
     */
    void RunJeuMMChallengeur(FabricationSecretMM fabricationSecretMM) {

        boolean isSecretTrouveJoueur = false;
        boolean isEscapeCharSaisie = false;

        Integer nbreEssaisConsommes = 0;

        // pattern destiné à l'objet scanner : les initiales des couleurs disponibles pour la proposition + 'escapechar'
        String pattern = FabPattSais.ConstruitPatternSaisie(CouleursMastermind.values(), nombreDeCouleurs, charactersEscape);

        // la proposition du joueur
        ArrayList<Character> propaleChallengeur;

        // gere l'increment affichage des lignes propositions
        int indexLignesProposition = 0;

        // creation des lignes du jeu
        PrepareRunJeuMM(fabricationSecretMM);

        //affiche  les couleurs qui peuvent être choisie pour faire une propositon : fonction paramètre du jeu "nombre de couleur"
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);

        IOConsole.ClearScreen.cls();

        // boucle tant que joueur ne saisit pas escapeChar
        while (nbreEssaisConsommes < nombreDeEssaisMax) {
            //affichage par defaut de la ligne en cours , destinée à recevoir la proposition du joueur
            lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

            // saisie de la propsotion du joueur
            propaleChallengeur = produirePropaleChallengeur.getPropaleChallengeur(scanner, pattern, charactersEscape);

            //si le joueur a saisie escapechar, bye
            if (propaleChallengeur.contains(charactersEscape)) {
                isEscapeCharSaisie = true;
                break;
            }

            //evaluation de la proposition du joueur : la propostion est "scorée" en nombre de Blanc/Noir
            if (lignesPropaleMM[indexLignesProposition].setPropositionJoueur(propaleChallengeur).setZoneProposition().EvalProposition()) {
                isSecretTrouveJoueur = true;
                break;
            }
            nbreEssaisConsommes++;
            indexLignesProposition++;
        }

        lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");

        // proposition match avc combinaisosn secrete
        if (isSecretTrouveJoueur) {
            lignesSimpleMM[LIGNE_STATUS].setLibelleLigne(" ----   VICTOIRE !!---");

        } else if (nbreEssaisConsommes >= nombreDeEssaisMax) {
            lignesSimpleMM[LIGNE_STATUS].setLibelleLigne(String.format("-- Perdu. Soluce = %s", lignesSimpleMM[LIGNE_SECRETE].getLibelleLigne()));

        } else if (isEscapeCharSaisie) {
            return;

        } else { //pb interne ne doit jamais arrivé jusqu'ici
            throw new AppExceptions(ERREUR_GENERIC);
        }
        //pour visualiser fin de partie (sauf si erreur ou escapeChar saisi
        LitCharEtAfficheLignes(FabPattSais.ConstruitPatternSaisie(charactersEscape));

        lignesSimpleMM[LIGNE_STATUS].setLibelleLigne(lignesSimpleMM[LIGNE_STATUS].getLibelleLigneOriginal());
    }

    /**
     *lance le jeu en mode Defenseur
     * @param fabricationSecretMM FabricationSecretMM
     */
    void RunJeuMMDefenseur(FabricationSecretMM fabricationSecretMM) {

        PrepareRunJeuMM(fabricationSecretMM);

        boolean isSecretTrouveOrdinateur = false;
        boolean isErreurScoring = false;

        Integer nbreEssaisConsommes = 0;

        //dans ce mode , la seule saisie est escapechar, c'est l'ordinateur qui fabrique la proposition
        String patternEscape = FabPattSais.ConstruitPatternSaisie(charactersEscape);

        // proposition de l'ordinateur
        ArrayList<Character> propalOrdinateur;

        int indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        //tant que la proposition ne correspond à la combinaison secrete et que le nombre d'essais possibles n'est pas atteint
        while (nbreEssaisConsommes < nombreDeEssaisMax) {

            //ordinateur fournit une proposition
            propalOrdinateur = produirePropaleDefenseur.getPropaleDefenseur();

            //si fraude par scoring erroné, pas de moyen avec cet algo de reprendre la recherche,  donc bye
            if (propalOrdinateur == null) {
                isErreurScoring = true;
                break;
            }

            //via appel de EvalPropostion,  la propsotion est scorée - ce resultat est présenté pour conseil au Joueur
            lignesPropaleMM[indexLignesProposition].setPropositionJoueur(propalOrdinateur).setZoneProposition().EvalProposition();

            int[] suggestioNB = lignesPropaleMM[indexLignesProposition].getZoneEvaluation();
            lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(String.format("( N=%d , B=%d )", suggestioNB[NOIR_BIENPLACE], suggestioNB[BLANC_MALPLACE]));


            int[] zoneEvaluation = new int[2];
            try {

                RunSaisirScore(zoneEvaluation);

                //si fraude interdite, et si ecart entre score saisi et score calculé,
                if (!(boolean) getParam(FRAUDE_AUTORISEE)) {
                    //recopie du bon score
                    if ((suggestioNB[NOIR_BIENPLACE] != zoneEvaluation[NOIR_BIENPLACE]) ||
                            (suggestioNB[BLANC_MALPLACE] != zoneEvaluation[BLANC_MALPLACE])) {
                        System.arraycopy(suggestioNB, 0, zoneEvaluation, 0, suggestioNB.length);
                    }
                }

            } catch (AppExceptions e) {
                if (e.getCharacterSortie() == charactersEscape) {
                    logger.info(SORTIE_ESCAPE_SAISIE_SCORE);
                    System.arraycopy(suggestioNB, 0, zoneEvaluation, 0, suggestioNB.length);
                }
            }

            lignesPropaleMM[indexLignesProposition].setZoneEvaluation(zoneEvaluation);

            if (zoneEvaluation[NOIR_BIENPLACE] == nombreDePositions) {
                isSecretTrouveOrdinateur = true;
                break;
            }

            //prepare recherche nouvelle propostion
            produirePropaleDefenseur.setScorePropale(propalOrdinateur, lignesPropaleMM[indexLignesProposition].getZoneEvaluation());

            indexLignesProposition++;
            nbreEssaisConsommes++;

            //la propsosition ordinateur est null - scoring incorrect
        }

        lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne("");

        // affichage de fin
        if (isErreurScoring) {
            lignesSimpleMM[LIGNE_STATUS].setLibelleLigne("!! Tricheur le Scoring est incorrect  !!");
        } else if (isSecretTrouveOrdinateur) {
            lignesSimpleMM[LIGNE_STATUS].setLibelleLigne("!! Ordinateur Gagne !!");
        } else if (nbreEssaisConsommes >= nombreDeEssaisMax) {
            lignesSimpleMM[LIGNE_STATUS].setLibelleLigne("!! Ordinateur Perd !!");
        } else { //pb interne ne doit jamais arrivé jusqu'ici
            throw new AppExceptions(ERREUR_GENERIC);
        }
        //pour visualiser fin de partie (sauf si erreur ou escapeChar saisi
        LitCharEtAfficheLignes(patternEscape);

        lignesSimpleMM[LIGNE_STATUS].setLibelleLigne(lignesSimpleMM[LIGNE_STATUS].getLibelleLigneOriginal());
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(lignesSimpleMM[LIGNE_SECRETE].getLibelleLigneOriginal());
    }

    /**
     * Saise le score de la propositon donné par l'ordianteur (mode defenseur)
     */

    private void RunSaisirScore(int[] zoneEvaluation) throws AppExceptions {

        int index = 0;
        String pattern = String.format("[0-%d K k]", nombreDePositions);

        String msgInfos;
        String msgInfosOriginal = lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigne();
        String msgPourReset = lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigne();
        int nbNoirs = 0;
        int nbBlancs = 0;

        try {

            Character saisieChar;
            do {
                msgInfos = lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigne();
                if (index == 0) {
                    msgInfos += " Nbre Noirs = ? ";
                } else {
                    msgInfos += " Nbre Blancs = ?";
                }
                lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(msgInfos);


                saisieChar = LitCharEtAfficheLignes(pattern);

                if (saisieChar != charactersEscape) {
                    if (index < zoneEvaluation.length) {
                        zoneEvaluation[index] = Integer.parseInt(String.valueOf(saisieChar));
                        //msgInfos = lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigne();
                        String tmpMsgInfos;

                        if (index == 0) {
                            tmpMsgInfos = msgInfosOriginal + " Noirs= " + saisieChar;
                            zoneEvaluation[NOIR_BIENPLACE] = Integer.parseInt(String.valueOf(saisieChar));
                            msgInfosOriginal = tmpMsgInfos;
                        } else {
                            tmpMsgInfos = msgInfosOriginal + " Blancs= " + saisieChar;
                            zoneEvaluation[BLANC_MALPLACE] = Integer.parseInt(String.valueOf(saisieChar));
                            msgInfosOriginal = tmpMsgInfos;
                        }
                        lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(tmpMsgInfos);

                    } else {
                        throw new AppExceptions(ERREUR_GENERIC);
                    }
                } else {
                    throw new AppExceptions(SORTIE_SUR_ESCAPECHAR, charactersEscape);
                }
                if (index == 0) {
                    nbNoirs = Integer.parseInt(String.valueOf(saisieChar));
                } else {
                    nbBlancs = Integer.parseInt(String.valueOf(saisieChar));
                }
                //reset
                if ((nbBlancs + nbNoirs) > nombreDePositions) {
                    lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(String.format("%s , Noirs+Blancs incorrect", msgPourReset));
                    msgInfosOriginal = msgPourReset;
                    zoneEvaluation[BLANC_MALPLACE] = 0;
                    zoneEvaluation[NOIR_BIENPLACE] = 0;
                    index = 0;
                    continue;
                }
                index++;
            } while ((saisieChar != charactersEscape) && (index < zoneEvaluation.length));

        } catch (Exception e) {
            if (e instanceof AppExceptions) {
                char c = ((AppExceptions) e).getCharacterSortie();
                if (c == charactersEscape) {
                    //throw new AppExceptions(SORTIE_SUR_ESCAPECHAR, charactersEscape);
                    throw e;
                } else {
                    throw new AppExceptions(ERREUR_GENERIC);
                }
            } else {
                throw new AppExceptions(ERREUR_GENERIC);
            }
        }
    }


    /**
     *  log des infos du jeu : le nombre de couleurs possibles, la combinaison secrete
     * @param couleursSecretes   combinaison secrete dans sa forme contenant les initiales des Couleurs
     * @param nombreDeCouleurs   le nombre de couleurs utilisées pour construire la combinaison secrete
     */
    private void LogLaCombinaisonSecrete(CouleursMastermind[] couleursSecretes, int nombreDeCouleurs) {

        CouleursMastermind[] toutes = CouleursMastermind.values();

        StringBuilder s = new StringBuilder(TAIILE_INITIALE);

        int nbCoulRetenue = 0;

        for (CouleursMastermind x : toutes) {

            s.append(String.format("%s%s", x.toString(), ", "));

            nbCoulRetenue++;

            if (nbCoulRetenue >= nombreDeCouleurs)
                break;
        }

        logger.info("Toutes les couleurs = " + s.substring(0, s.lastIndexOf(", ")));

        int tailleStringB = s.length();

        s.delete(0, tailleStringB - 1);

        for (CouleursMastermind x : couleursSecretes) {
            s.append(String.format("%s%s", x.toString(), ", "));
        }

        logger.info("Combinaison secrete = " + s.substring(0, s.lastIndexOf(",")));

    }

    /**
     * affiche le menu qui permet de saisir une combinaison secrete
     *
     * @return list de characters qui est la combinaison secrete - vide si sortie par escape (vide mais pas null)
     */
    ArrayList<Integer> RunSaisieSecretMM(FabricationSecretMM fabricationSecretMM) throws AppExceptions {

        boolean isEscape = false;


        ArrayList<Integer> chiffresSecretsDuDefenseur = new ArrayList<>(256);

        //secret sous forme initiale des Couleurs
        ArrayList<Character> initialesSecretesDuDefenseur = new ArrayList<>(256);


        //lignes de separaiton (presentation)
        lignesSimpleMM[LIGNE_BLANCH01] = new LigneMM(true, true, LIGNE_BLANCH01, LIGNE_BLANCH01, " ");
        lignesSimpleMM[LIGNE_BLANCH02] = new LigneMM(true, false, LIGNE_BLANCH02, LIGNE_BLANCH02, " ");

        //titre du jeu
        lignesSimpleMM[TITRE] = new LigneMM(true, true, TITRE, TITRE, modeJeu.toString());
        lignesSimpleMM[TITRE].setLibelleLigne("Mode Defenseur - Saisir une combinaison secrete de couleurs");

        lignesSimpleMM[LIGNE_ENTETE] = new LigneMM(true, false, LIGNE_ENTETE, LIGNE_ENTETE, "");

        // presentation d'une combinsaison secrete suggeree
        lignesSimpleMM[LIGNE_SECRETE] = new LigneMM(true, true, LIGNE_SECRETE, LIGNE_SECRETE, "");
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(fabricationSecretMM.getCouleursSecretes(), nombreDeCouleurs, "Suggestion : ");

        //affiche  les couleurs qui peuvent être choisie pour faire un secret  : fonction paramètre du jeu "nombre de couleur"
        lignesSimpleMM[LIGNE_TOUTES_COULEURS] = new LigneMM(true, true, LIGNE_TOUTES_COULEURS, LIGNE_TOUTES_COULEURS, " ");
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);

        //ligne de bas de table , habituelle 'i.e votre choix ?'
        lignesSimpleMM[LIGNE_DE_SAISIE] = new LigneMM(true, true, LIGNE_DE_SAISIE, LIGNE_DE_SAISIE, String.format(" Saisir votre combinaison secrete  (%c : Retour): ", charactersEscape));


        // pattern destiné à l'objet scanner : les initiales des couleurs disponibles pour la combinaison secrete + 'escapechar'
        String pattern = FabPattSais.ConstruitPatternSaisie(CouleursMastermind.values(), nombreDeCouleurs, charactersEscape);

        IOConsole.ClearScreen.cls();

        // boucle tant que joueur ne saisit pas escapeChar ou que la combinaison secrete n'est pâs complete
        try {

            Character saisieUneCouleur;
            do {
                saisieUneCouleur = IOConsole.LectureClavierChar(pattern, scanner, new EcrireSurEcran() {
                    @Override
                    public void Display() {
                        for (int n = TITRE; n <= LIGNE_DE_SAISIE; n++) {
                            if (lignesSimpleMM[n] != null && lignesSimpleMM[n].isEstVisible()) {
                                if (n == LIGNE_DE_SAISIE) {
                                    System.out.print(lignesSimpleMM[n].toString());
                                } else {
                                    System.out.println(lignesSimpleMM[n].toString());
                                }
                            }
                        }
                    }
                }, charactersEscape);

                // si le Defenseur ne souhaote pas sortir
                if (saisieUneCouleur != charactersEscape) {
                    //ajoute le caractere saisie à la lsite des intiiales saisies
                    initialesSecretesDuDefenseur.add(saisieUneCouleur);
                    pattern = ReduirePattern(pattern, doublonAutorise, saisieUneCouleur);
                    //choix escape
                } else {
                    initialesSecretesDuDefenseur.clear();
                }
            }
            while ((saisieUneCouleur != charactersEscape) && (initialesSecretesDuDefenseur.size() < nombreDePositions));

        } catch (AppExceptions appExceptions) {
            appExceptions.printStackTrace();
            initialesSecretesDuDefenseur.clear();
            initialesSecretesDuDefenseur.add(charactersEscape);
        }

        //si escape a ete saisie, cette liste est vide donc pas de boucle
        int valeurFaciale;
        for (char c : initialesSecretesDuDefenseur) {
            valeurFaciale = CouleursMastermind.getValeurParLettre(c);
            if (valeurFaciale < 0) {
                logger.error(ERREUR_GENERIC + " erreur conversion EnumCouleur - Lettre en Chiffre");
                throw new AppExceptions(ERREUR_GENERIC);
            }
            chiffresSecretsDuDefenseur.add(valeurFaciale);
        }
        // netoyage du tableau des lignes- les new fait dans cette méthode,  partent au garbage
        for (int n = 0; n < lignesSimpleMM.length; n++)
            lignesSimpleMM[n] = null;

        //sur un escape, le ArrayList<Integer> est vide
        return chiffresSecretsDuDefenseur;
    }

    /**
     * retrait de la couleur qui vient d'être saisie (saisieUneCouleur) , du pattern qui controle la saisie
     * cette fonction n'a de sens que pour le mode "doulon non autorisé"
     *
     * @param pattern          String , le pattern à re
     * @param doublonAutorise  Boolean , doublon oui/no - paramétrage
     * @param saisieUneCouleur Character , l'initiale de la couleur a retiré du pattern de saisie
     * @return String pattern  initial moins le caractere qui vient d'etre saisi donc n'est  plus dispo en mode sans doublon
     */

    private String ReduirePattern(String pattern, Boolean doublonAutorise, Character saisieUneCouleur) {
        String infosSaisie = lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigne() + saisieUneCouleur.toString() + " ";
        lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(infosSaisie);
        //si le mode est sans doublon
        if (!doublonAutorise) {
            //retrait  du  caractere saisi de la liste des caracteres disponibles
            int posCol = pattern.indexOf(saisieUneCouleur);
            int taille = pattern.length();
            pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
            taille = pattern.length();
            String pourLower = String.valueOf(saisieUneCouleur).toLowerCase(Locale.forLanguageTag("fr"));
            posCol = pattern.indexOf(pourLower.toCharArray()[0]);
            pattern = pattern.substring(0, posCol) + pattern.substring(posCol + 1, taille);
        }
        return pattern;
    }

}
/*
 * ***************************************************************************************************************
 * the end
 * ***************************************************************************************************************
 */

