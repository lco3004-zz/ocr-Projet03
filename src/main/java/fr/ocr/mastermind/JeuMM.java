package fr.ocr.mastermind;

import fr.ocr.modeconsole.IhmMasterMind;
import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes.CouleursMastermind;
import fr.ocr.utiles.Constantes.Libelles.LibellesMenuSecondaire;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;

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
abstract class JeuMM {

    protected ValiderPropositionChallengeur validerPropositionChallengeur = (ArrayList<Character> propaleJoueur,
                                                                             ArrayList<Character> combinaisonSecrete,
                                                                             Integer nombreDePositions,
                                                                             int[] zoneEvaluation) -> false;

    protected ObtenirPropaleDefenseur obtenirPropaleDefenseur = () -> null;



    private LibellesMenuSecondaire modeJeu;
    private Scanner scanner;

    /**
     * @param sc
     */
    public JeuMM(LibellesMenuSecondaire modeJeu, Scanner sc) {
        scanner = sc;
        this.modeJeu = modeJeu;
    }

    /**
     * @param fabricationSecretMM
     */
    public void runJeuMMChallengeur(FabricationSecretMM fabricationSecretMM) {


        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes());

        new IhmMasterMind(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes(),
                validerPropositionChallengeur)
                .runIhmMMChallengeur(scanner);
    }

    /**
     * @param fabricationSecretMM
     */
    public void runJeuMMDefenseur(FabricationSecretMM fabricationSecretMM) {

        LogLaCombinaisonSecrete(fabricationSecretMM.getCouleursSecretes());

        new IhmMasterMind(modeJeu,
                fabricationSecretMM.getChiffresSecrets(),
                fabricationSecretMM.getCouleursSecretes())
                .runIhmMMDefenseur(scanner, obtenirPropaleDefenseur);
    }

    /**
     * @return le code secret (String)
     */
    private String LogLaCombinaisonSecrete(CouleursMastermind[] couleursSecretes) {

        CouleursMastermind[] toutes = CouleursMastermind.values();
        StringBuilder s = new StringBuilder(4096);

        for (CouleursMastermind x : toutes) {
            s.append(String.format("%s%s", x.toString(), ", "));
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

    //tableau qui contient les chiffres tirés au hazard modulo le parametre NOMBRE_DE_POSITIONS
    private ArrayList<Integer> chiffresSecrets;

    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);

    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);

    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);

    private Integer nombreDebBoucleMax = (Integer) getParam(NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE);

    /**
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
        Object tmpRetour;
        if ((chiffresSecretsFournis == null) || chiffresSecretsFournis.size() > nombreDePositions)
            throw new AppExceptions(ERREUR_GENERIC);

        chiffresSecrets = chiffresSecretsFournis;

        couleursSecretes = BijecterCouleurChiffres(chiffresSecrets, nombreDePositions);
    }

    /**
     * la table des chiffes secrets est à construire - cas où c'est l'odinateur qui propose la ligne secrete
     * cas où c'est le joueur qui doit deviner la ligne secrete, ou bien en mode duel
     */
    public FabricationSecretMM() {

        Object tmpRetour;

        int valeurAleatoire;

        DecimalFormat df = new DecimalFormat("#");


        chiffresSecrets = new ArrayList<>();

        // "graine" pour le random.
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (int placeOccupee = 0, nbreDeBoucles = 0; (placeOccupee < nombreDePositions) && (nbreDeBoucles < nombreDebBoucleMax); nbreDeBoucles++) {
            valeurAleatoire = (Integer.parseInt(df.format(Math.random() * 100)) % nombreDeCouleurs);

            if (!doublonAutorise) {
                if (!chiffresSecrets.contains(valeurAleatoire)) {
                    chiffresSecrets.add(valeurAleatoire);
                    placeOccupee++;
                }
            } else {
                chiffresSecrets.add(valeurAleatoire);
                placeOccupee++;
            }
        }
        //pas assez de positions remplies - le random n'a pas marché
        if (chiffresSecrets.size() < nombreDePositions) {

            CouleursMastermind[] couleursMastermind = CouleursMastermind.values();
            chiffresSecrets.clear();
            for (int i = 0; i < nombreDePositions; i++) {
                chiffresSecrets.add(couleursMastermind[i].getValeurFacialeDeLaCouleur());
            }
        }

        couleursSecretes = BijecterCouleurChiffres(chiffresSecrets, nombreDePositions);
    }

    /**
     * renseigne la ligne secrete des couleurs (bijection couleurs / chiffres)
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
    public ArrayList<Integer> getChiffresSecrets() {

        return chiffresSecrets;
    }

    /**
     * @return CouleursMastermind[] Tableau des couleurs de la combinaison secrete
     */
    public CouleursMastermind[] getCouleursSecretes() {

        return couleursSecretes;
    }

}
