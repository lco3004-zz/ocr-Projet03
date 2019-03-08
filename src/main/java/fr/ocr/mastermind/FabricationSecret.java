package fr.ocr.mastermind;


import fr.ocr.params.CouleursMastermind;
import fr.ocr.utiles.AppExceptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;


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
public class FabricationSecret {

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
     *
     * @param chiffresSecretsFournis la table des chiffes secrets est fourni par utilisateur
     *                               cas où c'est l'ordinateur qui doit trouver la ligne secrete
     * @throws AppExceptions incohérence parametre ou tableau des chiffres passé en parametre
     */
    public FabricationSecret(ArrayList<Integer> chiffresSecretsFournis) throws AppExceptions {
        Object tmpRetour;


        if ((chiffresSecretsFournis == null) || chiffresSecretsFournis.size() > nombreDePositions)
            throw new AppExceptions(ERREUR_GENERIC);

        chiffresSecrets = chiffresSecretsFournis;

        BijecterCouleurChiffres();
    }

    /**
     * la table des chiffes secrets est à construire - cas où c'est l'odinateur qui propose la ligne secrete
     * cas où c'est le joueur qui doit deviner la ligne secrete, ou bien en mode duel
     *
     * @throws AppExceptions . Exception levée sur erreur cohérence entre
     *                       le type de paramètre demandé et le type lu depuis la source des parametres (fichier paramètre)
     */
    public FabricationSecret() throws AppExceptions {

        Object tmpRetour;

        int valeurAleatoire;

        DecimalFormat df = new DecimalFormat("#");


        chiffresSecrets = new ArrayList<>();

        // "graine" pour le random.
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (int placeOccupee = 0, nbreDeBoucles = 0; (placeOccupee < nombreDePositions) && (nbreDeBoucles < nombreDebBoucleMax); nbreDeBoucles++) {
            valeurAleatoire =  (Integer.parseInt(df.format(Math.random() * 100)) % nombreDeCouleurs);

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

        BijecterCouleurChiffres();
    }

    /**
     * renseigne la ligne secrete des couleurs (bijection couleurs / chiffres)
     */
    private void BijecterCouleurChiffres() {
         couleursSecretes = new CouleursMastermind[nombreDePositions];
         int i = 0;
         for (int v : chiffresSecrets) {
             couleursSecretes[i++] = CouleursMastermind.values()[v];
         }
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

