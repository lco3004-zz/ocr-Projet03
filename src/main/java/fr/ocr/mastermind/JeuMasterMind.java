package fr.ocr.mastermind;

import fr.ocr.modeconsole.IhmChallengeurMM;
import fr.ocr.modeconsole.IhmDefenseurMM;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.PIONS_BIENPLACES;
import static fr.ocr.utiles.Constantes.ConstEvalPropale.PIONS_MALPLACES;
import static fr.ocr.utiles.Constantes.ConstLigneSimple.*;
import static fr.ocr.utiles.Constantes.CouleursMastermind;
import static fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

public interface JeuMasterMind {

    static JeuMMChallengeur CHALLENGEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        return new JeuMMChallengeur(modeJeu, sc);
    }

    static JeuMMDefenseur DEFENSEUR(LibellesMenuSecondaire modeJeu, Scanner sc) {
        return new JeuMMDefenseur(modeJeu, sc);
    }

    void runJeuMM();
}

/**
 *
 */

class JeuMMDefenseur extends JeuMM {

    List<Integer[]> lesScoresPossibles;
    ArrayList<ArrayList<Character>> lesCombinaisonsPossibles;

    JeuMMDefenseur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);
    }

    @Override
    public void runJeuMM() {

        //MenuSaisieSecret menuSaisieSecret = new MenuSaisieSecret();
        //FabricationSecretMM fabricationSecretMM = new FabricationSecretMM(menuSaisieSecret.saisirCombinaisonSecrete());

        // DEBUT MOCK
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();
        //FIN MOCK
        this.produirePropale = new ProduirePropaleDefenseur();
        this.validerProposition = new EvalPropaleDefenseur();

        super.RunJeuMMDefenseur(fabricationSecretMM);
    }
}

/**
 *
 */
class JeuMMChallengeur extends JeuMM {

    JeuMMChallengeur(LibellesMenuSecondaire modeJeu, Scanner sc) {
        super(modeJeu, sc);

    }

    @Override
    public void runJeuMM() {
        FabricationSecretMM fabricationSecretMM = new FabricationSecretMM();
        this.validerProposition = new EvalPropaleChallengeur();

        this.produirePropale = new ProduirePropaleChallengeur(lignesSimpleMM, lignesPropaleMM);
        super.RunJeuMMChallengeur(fabricationSecretMM);
    }
}

/**
 * "Modele" du jeuMastermind
 * Note :
 * * la combinaison secrete est soit calculée par ordinateur en mode challeger
 * * soit saisie par le joueur
 * *la fabrication de la composition secrete 'S' dépend de :
 * * * NOMBRE_DE_COULEURS : le nombre de couleurs disponibles  'N'
 * * * * limité à 18 max par construction, valeur min 6 couleurs qui est une valeur std)
 * * * NOMBRE_DE_POSITIONS : le nombre de couleurs 'P'  constituant la composition secrete S,
 * * * * 8 max par construction, min 4 qui est une valeur Std
 * * * DOUBLON_AUTORISE
 */
abstract class JeuMM implements JeuMasterMind {

    LignePropaleMM[] lignesPropaleMM = new LignePropaleMM[(Integer) getParam(NOMBRE_D_ESSAIS)];
    LigneSimpleMM[] lignesSimpleMM = new LigneSimpleMM[NBRE_LIGNESTABLEMM];
    ValiderProposition validerProposition = new ValiderProposition() {
        @Override
        public Boolean apply(ArrayList<Character> proposition, ArrayList<Character> secret, Integer nombreDePositions, int[] zoneEvaluation) {
            return null;
        }
    };
    ProduirePropale produirePropale;
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
    private Boolean modeDebug = (Boolean) getParam(MODE_DEBUG);

    private Constantes.Libelles.LibellesMenuSecondaire modeJeu;
    private Scanner scanner;


    JeuMM(Constantes.Libelles.LibellesMenuSecondaire modeJeu, Scanner sc) {
        scanner = sc;
        this.modeJeu = modeJeu;

    }

    private void PreparationMenu(LibellesMenuSecondaire modeDeJeu, ArrayList<Integer> chiffresSecrets,
                                 CouleursMastermind[] couleursSecretes) {


        lignesSimpleMM[TITRE] = new LigneSimpleMM(true, true, TITRE, TITRE, modeDeJeu.toString());

        lignesSimpleMM[LIGNE_STATUS] = new LigneSimpleMM(true, true, LIGNE_STATUS, LIGNE_STATUS, String.format(" Mode debug = %s", getParam(MODE_DEBUG).toString()));

        lignesSimpleMM[LIGNE_SECRETE] = new LigneSimpleMM(true, false, LIGNE_SECRETE, LIGNE_SECRETE, " -------SECRET--------");

        String champBlancNoir;
        StringBuilder lesCroixEtVirgules = new StringBuilder(256);
        for (int nbPositions = 0; nbPositions < nombreDePositions; nbPositions++) {
            lesCroixEtVirgules.append(' ');
            lesCroixEtVirgules.append('x');
            lesCroixEtVirgules.append(',');
        }
        lignesSimpleMM[LIGNE_TOUTES_COULEURS] = new LigneSimpleMM(true, true, LIGNE_TOUTES_COULEURS, LIGNE_TOUTES_COULEURS, " ");

        champBlancNoir = String.format(" ## [%s ] %c %c", lesCroixEtVirgules.substring(0, lesCroixEtVirgules.length() - 1), PIONS_BIENPLACES, PIONS_MALPLACES);
        lignesSimpleMM[LIGNE_ENTETE] = new LigneSimpleMM(true, true, LIGNE_ENTETE, LIGNE_ENTETE, champBlancNoir);

        lignesSimpleMM[LIGNE_BLANCH01] = new LigneSimpleMM(true, true, LIGNE_BLANCH01, LIGNE_BLANCH01, " ");
        lignesSimpleMM[LIGNE_BLANCH02] = new LigneSimpleMM(true, true, LIGNE_BLANCH02, LIGNE_BLANCH02, " ");

        Character c = Constantes.Libelles.CharactersEscape.K.toString().charAt(0);

        lignesSimpleMM[LIGNE_DE_SAISIE] = new LigneSimpleMM(true, true, LIGNE_DE_SAISIE, LIGNE_DE_SAISIE, String.format(" Votre choix (%c : Retour): ", c));
        for (int k = 0, indexLignesJeuMM = LIGNE_PROPOSITION; k < (Integer) getParam(NOMBRE_D_ESSAIS); k++, indexLignesJeuMM++) {
            lignesPropaleMM[k] = new LignePropaleMM(couleursSecretes,
                    chiffresSecrets,
                    true,
                    true,
                    k,
                    LIGNE_PROPOSITION,
                    "", validerProposition);
            lignesPropaleMM[k].Clear().setLibelleLigne();

            lignesSimpleMM[indexLignesJeuMM] = lignesPropaleMM[k];
        }
    }

    private void PrepareRunJeuMM(FabricationSecretMM fabricationSecretMM) {

        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes(), nombreDeCouleurs);

        PreparationMenu(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes());
    }

    void RunJeuMMChallengeur(FabricationSecretMM fabricationSecretMM) {

        PrepareRunJeuMM(fabricationSecretMM);
        lignesSimpleMM[LIGNE_SECRETE].setLibelleLigne(fabricationSecretMM.getCouleursSecretes());

        if (modeDebug) {
            lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);
        }
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(CouleursMastermind.values(), nombreDeCouleurs);

        new IhmChallengeurMM(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes(),
                validerProposition,
                lignesSimpleMM,
                lignesPropaleMM)
                .runIhmMM(scanner, produirePropale);
    }


    void RunJeuMMDefenseur(FabricationSecretMM fabricationSecretMM) {

        PrepareRunJeuMM(fabricationSecretMM);
        int nbColSec = fabricationSecretMM.getCouleursSecretes().length;
        lignesSimpleMM[LIGNE_TOUTES_COULEURS].setLibelleLigne(fabricationSecretMM.getCouleursSecretes(), nbColSec, " Combinaison secrete : ");
        lignesSimpleMM[LIGNE_SECRETE].setEstVisible(true);

        new IhmDefenseurMM(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes(),
                validerProposition,
                lignesSimpleMM,
                lignesPropaleMM)
                .runIhmMM(scanner, produirePropale);
    }

    /**
     * @return le code secret (String)
     */
    private String LogLaCombinaisonSecrete(CouleursMastermind[] couleursSecretes, int nombreDeCouleurs) {

        CouleursMastermind[] toutes = CouleursMastermind.values();
        StringBuilder s = new StringBuilder(4096);
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
        String valRet = String.format("%s %s", "Combinaison secrete = ", s.substring(0, s.lastIndexOf(", ")));
        logger.info("Combinaison secrete = " + s.substring(0, s.lastIndexOf(",")));
        return valRet;
    }

}


/**
 * Création de la combinaison secrète de couleurs à découvrir
 * construite à partir  de la liste prédéfinie de couleurs "CouleursMastermind"
 * <p>
 * Méthode :
 * tirage aléatoire d'un nombre modulo  le nombre de couleurs prédéfinies
 * cette valeur est ajoutée au tableau chiffresSecrets
 * et
 * pour chacune de ces valeurs
 * le tableau couleurSecretes est renseigné avec : ListeDeCouleursPrédéfinies [valeur]
 * <p>
 * il y a donc bijection entre le tableau chiffresSecrets et le tableau couleursSecretes.
 * <p>
 * Si le paramètre applicatif  DOUBLON_AUTORISE est vrai, il peut y avoir plusieurs couleurs identiques dans la combinaison (sinon, non)
 * le paramètre applicatif  NOMBRE_DE_POSITIONS indique la taille des tableaux  chiffresSecrets et  couleursSecretes
 * le parametre applicatif NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE limite de le nombre de boucles effectuées dans la recherche
 * d'un nombre aléatoire unique (cas ou DOUBLON_AUTORISE est faux car il faut alors une couleur/chiffre unique dans la ligne secrète)
 * <p>
 * Classe utilisée lorsque le jeu se fait contre l'ordinateur
 */
class FabricationSecretMM {
    /*
     *    tableau qui contient les chiffres tirés au hazard modulo le parametre NOMBRE_DE_POSITIONS
     */
    private ArrayList<Integer> chiffresSecrets;

    /*
     * tableau qui contient les couleurs prises dans CouleursMastermind :
     * couleursSecretes[ i ] =  CouleursMastermind[ chiffresSecret[ i ]]
     */
    private CouleursMastermind[] couleursSecretes;

    /**
     * @param chiffresSecretsFournis la table des chiffes secrets est fourni par utilisateur
     *                               cas où c'est l'ordinateur qui doit trouver la ligne secrete
     * @throws AppExceptions incohérence parametre ou tableau des chiffres passé en parametre
     */
    FabricationSecretMM(ArrayList<Integer> chiffresSecretsFournis) throws AppExceptions {

        if ((chiffresSecretsFournis == null) || (chiffresSecretsFournis.size() > (Integer) getParam(NOMBRE_DE_POSITIONS))) {
            throw new AppExceptions(ERREUR_GENERIC);
        }

        chiffresSecrets = chiffresSecretsFournis;

        couleursSecretes = BijecterCouleurChiffres(chiffresSecrets, (Integer) getParam(NOMBRE_DE_POSITIONS));
    }

    /**
     * la table des chiffes secrets est à construire - cas où c'est l'odinateur qui propose la ligne secrete
     * cas où c'est le joueur qui doit deviner la ligne secrete, ou bien en mode duel
     */
    FabricationSecretMM() {

        int valeurAleatoire;

        DecimalFormat df = new DecimalFormat("#");

        chiffresSecrets = new ArrayList<>();

        // "graine" pour le random.
        df.setRoundingMode(RoundingMode.HALF_UP);

        Integer nombreDebBoucleMax = (Integer) getParam(NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE);

        int placeOccupee = 0, nbreDeBoucles = 0;

        while ((placeOccupee < (Integer) getParam(NOMBRE_DE_POSITIONS)) && (nbreDeBoucles < nombreDebBoucleMax)) {
            Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
            valeurAleatoire = (Integer.parseInt(df.format(Math.random() * 100)) % nombreDeCouleurs);

            Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
            if (!doublonAutorise) {
                if (!chiffresSecrets.contains(valeurAleatoire)) {
                    chiffresSecrets.add(valeurAleatoire);
                    placeOccupee++;
                }
            } else {
                chiffresSecrets.add(valeurAleatoire);
                placeOccupee++;
            }
            nbreDeBoucles++;
        }

        //pas assez de positions remplies - le random n'a pas marché
        if (chiffresSecrets.size() < (Integer) getParam(NOMBRE_DE_POSITIONS)) {

            CouleursMastermind[] couleursMastermind = CouleursMastermind.values();
            chiffresSecrets.clear();
            for (int i = 0; i < (Integer) getParam(NOMBRE_DE_POSITIONS); i++) {
                chiffresSecrets.add(couleursMastermind[i].getValeurFacialeDeLaCouleur());
            }
        }

        couleursSecretes = BijecterCouleurChiffres(chiffresSecrets, (Integer) getParam(NOMBRE_DE_POSITIONS));
    }

    /**
     * renseigne la ligne secrete des couleurs (bijection couleurs / chiffres)
     *
     * @param chiffresSec ArrayList<Integer> , combinaison secrete en chiffres
     * @param nbPos       Integer , nombre de positions par ligne de jeu
     * @return la ligne secrete sous forme de couleurs
     */
    private CouleursMastermind[] BijecterCouleurChiffres(ArrayList<Integer> chiffresSec, Integer nbPos) {
        CouleursMastermind[] coulSec = new CouleursMastermind[nbPos];
        int i = 0;
        for (int v : chiffresSec) {
            coulSec[i++] = CouleursMastermind.values()[v];
        }
        return coulSec;
    }

    /**
     * @return ArrayList<Byte> Tableau des chiffres de la combinaisons secrete
     */
    ArrayList<Integer> getChiffresSecrets() {
        return chiffresSecrets;
    }

    /**
     * @return CouleursMastermind[] Tableau des couleurs de la combinaison secrete
     */
    CouleursMastermind[] getCouleursSecretes() {
        return couleursSecretes;
    }
}
