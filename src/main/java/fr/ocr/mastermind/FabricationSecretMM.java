package fr.ocr.mastermind;

import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
import static fr.ocr.utiles.Messages.InfosMessages.RANDOM_ECHOUE_COLPARDEF;

/**
 * @author laurent Cordier
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

    // tableau qui contient les couleurs prises dans CouleursMastermind : couleursSecretes[ i ] =  CouleursMastermind[ chiffresSecret[ i ]]
    private Constantes.CouleursMastermind[] couleursSecretes;

    /**
     * @param chiffresSecretsFournis la table des chiffes secrets est fourni par utilisateur cas où c'est l'ordinateur qui doit trouver la ligne secrete
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

        //si pas d'unicité de chiffre dans la combinaison aléatoire, ré essai pour nombreDeBoucleMax -
        Integer nombreDebBoucleMax = (Integer) getParam(NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE);

        int placeOccupee = 0, nbreDeBoucles = 0;


        while ((placeOccupee < (Integer) getParam(NOMBRE_DE_POSITIONS)) && (nbreDeBoucles < nombreDebBoucleMax)) {
            Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
            valeurAleatoire = (Integer.parseInt(df.format(Math.random() * 100)) % nombreDeCouleurs);

            Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);

            //si les chiffres dans la proposition doivent être uniques (pas de doublons) , il faut vérifier unicité
            if (!doublonAutorise) {
                if (!chiffresSecrets.contains(valeurAleatoire)) {
                    chiffresSecrets.add(valeurAleatoire);
                    placeOccupee++;
                }
                // si les doublons sont autorisés, pas de recherche d'unicité
            } else {
                chiffresSecrets.add(valeurAleatoire);
                placeOccupee++;
            }
            nbreDeBoucles++;
        }

        /*
         * pas assez de positions remplies - le random n'a pas marché
         * Dans ce cas , les premières couleurs de la liste des couleurs, sont prises par défaut.
         */
        if (chiffresSecrets.size() < (Integer) getParam(NOMBRE_DE_POSITIONS)) {

            Constantes.CouleursMastermind[] couleursMastermind = Constantes.CouleursMastermind.values();
            chiffresSecrets.clear();
            for (int i = 0; i < (Integer) getParam(NOMBRE_DE_POSITIONS); i++) {
                chiffresSecrets.add(couleursMastermind[i].getValeurFacialeDeLaCouleur());
            }
            logger.info(RANDOM_ECHOUE_COLPARDEF);
        }

        couleursSecretes = BijecterCouleurChiffres(chiffresSecrets, (Integer) getParam(NOMBRE_DE_POSITIONS));
    }

    /**
     * renseigne la ligne secrete des couleurs (bijection couleurs / chiffres)
     * @param chiffresSec ArrayList  of  Integer  , combinaison secrete en chiffres
     * @param nbPos       Integer , nombre de positions par ligne de jeu
     * @return la ligne secrete sous forme de couleurs
     */
    private Constantes.CouleursMastermind[] BijecterCouleurChiffres(ArrayList<Integer> chiffresSec, Integer nbPos) {
        Constantes.CouleursMastermind[] coulSec = new Constantes.CouleursMastermind[nbPos];
        int i = 0;
        for (int v : chiffresSec) {
            coulSec[i++] = Constantes.CouleursMastermind.values()[v];
        }
        return coulSec;
    }

    /**
     * renvoie la combinaison secrete sous sa forme Chiffres
     * @return ArrayList of Byte Tableau des chiffres de la combinaisons secrete
     */
    ArrayList<Integer> getChiffresSecrets() {
        return chiffresSecrets;
    }

    /**
     * renvoie la combinaison secrete sous sa forme Couleur
     * @return CouleursMastermind[] Tableau des couleurs de la combinaison secrete
     */
    Constantes.CouleursMastermind[] getCouleursSecretes() {
        return couleursSecretes;
    }
}

