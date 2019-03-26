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
import static fr.ocr.utiles.Messages.InfosMessages.SORTIE_SUR_ESCAPECHAR;

/**
 * @author Laurent Cordier
 * mode challenegeur  :  JeuMasterMind jeuMasterMind = JeuMasterMind.CHALLENGEUR(ch_Sec, scanner);
 * mode Defenseur :  JeuMasterMind jeuMasterMind = JeuMasterMind.DEFENSEUR(ch_Sec, scanner);
 * lancement du jeu (Challengeur ou Defenseur) jeuMasterMind.runJeuMM();
 * <p>
 * Interface qui donne acc&egrave;s aux mode de jeu MasterMind.
 * <p>
 *<p>
 *     interface d'accès au deux modes principaux du jeudefenseur, challengeur)
 *</p>
 */
public interface JeuMasterMind {

    /**
     *
     * @param modeJeu  LibellesMenuSecondaire
     * @param sc  scanner
     * @return JeuMMChallengeur
     */
    static JeuMMChallengeur CHALLENGEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        return new JeuMMChallengeur(modeJeu, sc);
    }

    /**
     *
     * @param modeJeu LibellesMenuSecondaire
     * @param sc scanner
     * @return JeuMMDefenseur
     */
    static JeuMMDefenseur DEFENSEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        return new JeuMMDefenseur(modeJeu, sc);
    }

    void runJeuMM();


}

/**
 * Classe du mode de jeu Defenseur mastermind - Ordinateur décode propostion du joueur
 */
class JeuMMDefenseur extends JeuMM {

    JeuMMDefenseur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
    }

    @Override
    public void runJeuMM() {

        //génération du secret sous forme de liste d'initiales des couleurs de la combinaison secrete
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();

        //le parametre fabricationSecretMM permet de suggerer une combinaison secrete au Defenseur
        ArrayList<Integer> combinaisonSecrete = super.RunSaisieSecretMM(fabricationSecretMM);

        // suite du worklflow si Defenseur n'est pas sorti la saisie de  'EscapeChar'
        //si c'est le cas la combinaison secerte est vide
        if (combinaisonSecrete.size() == (int) getParam(NOMBRE_DE_POSITIONS)) {
            //on fabrique le secret avec la combinaison saisie par le Defenseur
            fabricationSecretMM = new FabricationSecretMM(combinaisonSecrete);

            //affectation de JeuMM.produirePropaleMM avec instance classe dans laquelle l'ordinateur génère une proposition
            produirePropaleMM = new ProduirePropaleMMDefenseur();

            //affectation de JeuMM.produirePropaleMM avec instance classe dans laquelle le joueur valide la proposition de l'ordinateur

            controleProposition = new ScorerProposition();

            // lance le jeu en mode defenseur
            RunJeuMMDefenseur(fabricationSecretMM);
        }
    }
}

/**
 * classe du mode Challengeur - ordianteur fabrique un secret, le Joeur essaie de la découvrir
 */
class JeuMMChallengeur extends JeuMM {

    JeuMMChallengeur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
    }

    @Override
    public void runJeuMM() {
        //génération du secret sous forme de liste d'initiales des couleurs de la combinaison secrete
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();

        //affectation de JeuMM.produirePropaleMM avec instance classe dans laquelle l'ordinateur valide la proposition du Joueur
        controleProposition = new ScorerProposition();

        //affectation de JeuMM.produirePropaleMM avec instance classe dans laquelle le Joueur saisie une proposition
        produirePropaleMM = new ProduirePropaleMMChallengeur(lignesSimpleMM, lignesPropaleMM);

        // lance le jeu en mode challengeur
        RunJeuMMChallengeur(fabricationSecretMM);
    }
}

/**
 * "Modele" du jeuMastermind
 * Note :
 * * la combinaison secrete est
 * * * soit calculée par ordinateur en mode challeger
 * * * soit saisie par le joueur
 * *la fabrication de la composition secrete 'S' dépend de :
 * * * NOMBRE_DE_COULEURS : le nombre de couleurs disponibles  'N'
 * * * * limité à 18 max par construction, valeur min 6 couleurs qui est une valeur std)
 * * * NOMBRE_DE_POSITIONS : le nombre de couleurs 'P'  constituant la composition secrete S,
 * * * * 8 max par construction, min 4 qui est une valeur Std
 * * * DOUBLON_AUTORISE
 */
abstract class JeuMM implements JeuMasterMind {

    /* tableau de lignes de type Proposition
     * chaque ligne Propostion contient la proposition soumise, le score de cette proposition, la méthode de calcul du score
     * la méthode de calcul du socre est passé en paramétre à la ligne, la méthode n'étant pas cosntante selon le mode de jeu
     * mode challengeur : méthode par calcul/comparaison proposition vs secret
     * mode defenseur  : méthode par saisie clavier
     */
    LignePropaleMM[] lignesPropaleMM = new LignePropaleMM[(Integer) getParam(NOMBRE_D_ESSAIS)];

    /*
     * tableau de lignes de type affichage simple  . pas d'autre fonction que de stocker une chaine de car. à afficher
     */
    LigneMM[] lignesSimpleMM = new LigneMM[NBRE_LIGNESTABLEMM];

    // variable d'interface qui polymorphise - la validation dépend du mode du jeu en cours
    ControleProposition controleProposition;

    // variable d'interface qui polymorphise - la fabrication de la proposition dépend du mode du jeu en cours
    ProduirePropaleMM produirePropaleMM;
    // nombre lignes de jeu de la table du MasterMind
    private Integer nombreDeEssaisMax = (Integer) getParam(NOMBRE_D_ESSAIS);
    //le caractère de retour au menu superieur - choisit tel que  c !€ à ensemble des initiales des couleurs
    private Character charactersEscape = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);
    // paramètre du fichier properties - nombre de positions de pion par ligne de la table de jeu Mastermind
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);

    // paramètre du fichier properties - nombre de positions de couleurs disponibles pour construire une proposition et la combinaison secrete
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);

    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);

    // paramètre du fichier properties - affiche (DEBUG=Vrai) ou pas la combinaison secrete
    private Boolean modeDebug = (Boolean) getParam(MODE_DEBUG);

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
     * création des objets  des lignes de la table du jeu Mastermind
     * affectation des données affichables, de la méthode de validation
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

        lignesSimpleMM[LIGNE_STATUS] = new LigneMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format("Mode debug = %s", getParam(MODE_DEBUG).toString()));

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

        //affectation des lignes de type proposition
        for (int k = 0, indexLignesJeuMM = LIGNE_PROPOSITION; k < (Integer) getParam(NOMBRE_D_ESSAIS); k++, indexLignesJeuMM++) {
            /*
             * en paramètres
             * initiales des couleurs de la combinaison secrete,
             * chiffres de la combinaison secrete (en bijection  avec les couleurs de la combinaison secrete)
             * la ligne est disponible pour affichage de la paroposition courante (réservé futur use - toujours à true , dans cette version, qui ne gère pas le rollback)
             * la ligne est visible par défaut (toujours vrai)
             * type de ligne
             * infos à afficher - rien par défaut
             * méthode de validation de la proposition faite  par Ordinateur (Defenseur) ou par Joueur (Challengeur)
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

        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes(), nombreDeCouleurs);

        PreparationMenu(modeJeu, fabricationSecretMM.getCouleursSecretes());
    }

    /**
     * lance le jeu en mode challengeur
     * @param fabricationSecretMM instance qui dépend du mode de jeu (saisie ou calcul)
     */
    void RunJeuMMChallengeur(FabricationSecretMM fabricationSecretMM) {

        boolean SecretTrouve = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        // pattern destiné à l'objet scanner : les initiales des couleurs disponibles pour la proposition + 'escapechar'
        String pattern = FabPattSais.ConstruitPatternSaisie(CouleursMastermind.values(), nombreDeCouleurs, charactersEscape);

        // la proposition du joueur
        ArrayList<Character> propaleChallengeur;

        // gere l'increment affichage des lignes propositions
        int indexLignesProposition = 0;

        // creation des lignes du jeu
        PrepareRunJeuMM(fabricationSecretMM);

        // charge la ligne de rang LIGNE_SECRETE avec la combinaison secrete dans sa forme composée d'initiales de
        // couleurs
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(fabricationSecretMM.getCouleursSecretes());

        //debug , on affiche la combinaison secrete
        if (modeDebug) {
            lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);
        }

        //affiche  les couleurs qui peuvent être choisie pour faire une propositon : fonction paramètre du jeu "nombre de couleur"
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);

        IOConsole.ClearScreen.cls();

        // boucle tant que joueur ne saisit pas escapeChar
        while (!isEscape) {
            //si la combinaison ne correspnd pas au secret et si pas la dernière de proposition possible
            if (!SecretTrouve && nbreEssaisConsommes < nombreDeEssaisMax) {

                //affichage par defaut de la ligne en cours , destinée à recevoir la proposition du joueur
                lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

                // saisie de la propsotion du joueur
                propaleChallengeur = produirePropaleMM.getPropaleChallengeur(scanner, pattern, charactersEscape);

                //si le joueur a saisie escapechar, bye
                if (propaleChallengeur.contains(charactersEscape)) {

                    isEscape = true;

                } else {

                    //evaluation de la proposition du joueur : la propostion est "scorée" en nombre de Blanc/Noir

                    SecretTrouve = lignesPropaleMM[indexLignesProposition++].setPropositionJoueur(propaleChallengeur).setZoneProposition().EvalProposition();

                    nbreEssaisConsommes++;
                }

                //secret trouvé ou limite d'essais atteinte
            } else {
                //retour a affichage par defaut
                lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());

                // proposition match avc combinaisosn secrete
                if (SecretTrouve) {

                    lignesSimpleMM[LIGNE_STATUS].setLibelleLigne(" ----   VICTOIRE !!---");

                    lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");

                } else {

                    lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);

                    lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(String.format("-- Perdu. Soluce = %s", lignesSimpleMM[LIGNE_SECRETE].getLibelleLigne()));

                    lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne("");
                }

                //demande saisie escapechar unqiuement, pour conserver le resultat à l'écran.
                propaleChallengeur = produirePropaleMM.getPropaleChallengeur(scanner, FabPattSais.ConstruitPatternSaisie(charactersEscape), charactersEscape);

                // bye !
                if (propaleChallengeur.contains(charactersEscape)) {
                    isEscape = true;
                }
            }
        }
    }

    /**
     *lance le jeu en mode Defenseur
     * @param fabricationSecretMM FabricationSecretMM
     */
    void RunJeuMMDefenseur(FabricationSecretMM fabricationSecretMM) {

        PrepareRunJeuMM(fabricationSecretMM);

        // nombre de couleurs dans la combinaison secrete
        int nbColSec = fabricationSecretMM.getCouleursSecretes().length;

        //dans ce mode , la combinaision est toujours affichée
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(fabricationSecretMM.getCouleursSecretes(), nbColSec, " Combinaison secrete : ");
        lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);


        boolean isSecretTrouveSaisie = false, isSecretTrouveCalcul = false, isEscape = false;

        Integer nbreEssaisConsommes = 0;

        //dans ce mode , la seule saisie est escapechar, c'est l'ordinateur qui fabrique la proposition
        String patternEscape = FabPattSais.ConstruitPatternSaisie(charactersEscape);

        // proposition de l'ordinateur
        ArrayList<Character> propalOrdinateur;

        int indexLignesProposition = 0;

        IOConsole.ClearScreen.cls();

        //tant que la proposition ne correspond à la combinaison secrete et que le nombre d'essais possibles n'est pas atteint
        while (!isSecretTrouveSaisie && nbreEssaisConsommes < nombreDeEssaisMax) {

            //ordinateur fournit une proposition
            propalOrdinateur = produirePropaleMM.getPropaleDefenseur();

            //via appel de EvalPropostion,  la propsotion est scorée - ce resultat est présenté pour conseil au Joueur
            lignesPropaleMM[indexLignesProposition].setPropositionJoueur(propalOrdinateur).setZoneProposition().EvalProposition();

            int[] suggestioNB = lignesPropaleMM[indexLignesProposition].getZoneEvaluation();
            lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(String.format(" Suggestion :  Noirs = %d , Blancs = %d", suggestioNB[NOIR_BIENPLACE], suggestioNB[BLANC_MALPLACE]));

            int[] zoneEvaluation = new int[2];

            isSecretTrouveSaisie = RunSaisirScore(zoneEvaluation);
            lignesPropaleMM[indexLignesProposition].setZoneEvaluation(zoneEvaluation);

            //si propale est différent de secret, affiche la proposition
            if (!isSecretTrouveSaisie) {
                produirePropaleMM.setScorePropale(propalOrdinateur, lignesPropaleMM[indexLignesProposition].getZoneEvaluation());
            }

            indexLignesProposition++;
            nbreEssaisConsommes++;
        }

        // affichage de fin
        if (isSecretTrouveSaisie) {
            lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne("!! Ordinateur Gagne !!");

        } else {
            lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne("!! Ordinateur Perd !!");
        }

        lignesSimpleMM[LIGNE_DE_SAISIE].setLibelleLigne(lignesSimpleMM[LIGNE_DE_SAISIE].getLibelleLigneOriginal());
        //
        // pour confirmation sortie du jeu , par le defenseur (sinon - pas d'affichage et retour direct au menu superieur
        // seule saise possible 'escapeChar'
        //
        IOConsole.LectureClavierChar(patternEscape, scanner, new EcrireSurEcran() {
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
     * Saise le score de la propositon donné par l'ordianteur (mode defenseur)
     */

    public Boolean RunSaisirScore(int[] zoneEvaluation) throws AppExceptions {

        Boolean valRet = false;
        int index = 0;
        String pattern = String.format("[0-%d] K k", nombreDePositions);
        try {

            Character saisieChar;
            do {
                saisieChar = IOConsole.LectureClavierChar(pattern, scanner, new EcrireSurEcran() {
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

                if (saisieChar != charactersEscape) {
                    if (index < zoneEvaluation.length) {
                        zoneEvaluation[index] = Integer.parseInt(String.valueOf(saisieChar));
                        index++;
                    } else {
                        throw new AppExceptions(ERREUR_GENERIC);
                    }
                } else {
                    throw new AppExceptions(SORTIE_SUR_ESCAPECHAR, charactersEscape);
                }
            }
            while ((saisieChar != charactersEscape) && (index < zoneEvaluation.length));

            if (zoneEvaluation[NOIR_BIENPLACE] == nombreDePositions)
                valRet = true;

        } catch (Exception e) {
            throw new AppExceptions(ERREUR_GENERIC);
        }
        return valRet;
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

        lignesSimpleMM[LIGNE_ENTETE] = new LigneMM(true, false, LIGNE_ENTETE, LIGNE_ENTETE, "");

        // ligne qui indique si mode debug
        lignesSimpleMM[LIGNE_STATUS] = new LigneMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format(" Mode debug = %s", getParam(MODE_DEBUG).toString()));


        // presentation d'une combinsaison secrete suggeree
        lignesSimpleMM[LIGNE_SECRETE] = new LigneMM(true, true, LIGNE_SECRETE, LIGNE_SECRETE, "");
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(fabricationSecretMM.getCouleursSecretes(), nombreDeCouleurs, "Suggestion : ");

        //affiche  les couleurs qui peuvent être choisie pour faire un secret  : fonction paramètre du jeu "nombre de couleur"
        lignesSimpleMM[LIGNE_TOUTES_COULEURS] = new LigneMM(true, true, LIGNE_TOUTES_COULEURS, LIGNE_TOUTES_COULEURS, " ");
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);

        //ligne de bas de table , habituelle 'i.e votre choix ?'
        lignesSimpleMM[LIGNE_DE_SAISIE] = new LigneMM(true, true, LIGNE_DE_SAISIE, LIGNE_DE_SAISIE, String.format(" Votre choix (%c : Retour): ", charactersEscape));


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
     * @return
     */

    String ReduirePattern(String pattern, Boolean doublonAutorise, Character saisieUneCouleur) {
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

