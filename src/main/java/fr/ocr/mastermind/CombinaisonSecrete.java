package fr.ocr.mastermind;


import fr.ocr.params.CouleursMastermind;
import fr.ocr.utiles.AppExceptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;
import static fr.ocr.utiles.Messages.ErreurMessages.TYPE_PARAM_INCORRECT;


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
public class CombinaisonSecrete {

    //tableau qui contient les chiffres tirés au hazard modulo le parametre NOMBRE_DE_POSITIONS
    private ArrayList<Byte> chiffresSecrets;

    /**
     * tableau qui contient les couleurs prises dans CouleursMastermind :
     * couleursSecretes[ i ] =  CouleursMastermind[ chiffresSecret[ i ]]
     */
    private CouleursMastermind[] couleursSecretes;

    /**
     * @throws AppExceptions . Exception levée sur erreur cohérence entre
     *                       le type de paramètre demandé et le type lu depuis la source des parametres (fichier paramètre)
     */
    public CombinaisonSecrete() throws AppExceptions {

        Object tmpRetour;

        Byte valeurAleatoire;

        Boolean doublonAutorise;

        Integer nombreDePositions;

        Integer nombreDeCouleurs;

        Integer nombreDebBoucleMax;

        DecimalFormat df = new DecimalFormat("#");

        tmpRetour = getParam(NOMBRE_DE_POSITIONS);
        if (tmpRetour instanceof Integer) {
            nombreDePositions = (Integer) tmpRetour;
        } else {
            throw new AppExceptions(TYPE_PARAM_INCORRECT);
        }

        tmpRetour = getParam(NOMBRE_DE_COULEURS);
        if (tmpRetour instanceof Integer) {
            nombreDeCouleurs = (Integer) tmpRetour;
        } else {
            throw new AppExceptions(TYPE_PARAM_INCORRECT);
        }

        tmpRetour = getParam(DOUBLON_AUTORISE);
        if (tmpRetour instanceof Boolean) {
            doublonAutorise = (Boolean) tmpRetour;
        } else {
            throw new AppExceptions(TYPE_PARAM_INCORRECT);
        }

        tmpRetour = getParam(NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE);

        if (tmpRetour instanceof Integer) {
            nombreDebBoucleMax = (Integer) tmpRetour;
        } else {
            throw new AppExceptions(TYPE_PARAM_INCORRECT);
        }

        chiffresSecrets = new ArrayList<>();

        // "graine" pour le random.
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (int placeOccupee = 0, nbreDeBoucles = 0; (placeOccupee < nombreDePositions) && (nbreDeBoucles < nombreDebBoucleMax); nbreDeBoucles++) {
            valeurAleatoire = (byte) (Byte.parseByte(df.format(Math.random() * 100)) % nombreDeCouleurs);

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

        couleursSecretes = new CouleursMastermind[nombreDePositions];
        int i = 0;
        for (Byte v : chiffresSecrets) {
            couleursSecretes[i++] = CouleursMastermind.values()[(int) v];
        }
    }

    /**
     * @return ArrayList<Byte> Tableau des chiffres de la combinaisons secrete
     */
    public ArrayList<Byte> getChiffresSecrets() {

        return chiffresSecrets;
    }

    /**
     * @return CouleursMastermind[] Tableau des couleurs de la combinaison secrete
     */
    public CouleursMastermind[] getCouleursSecretes() {

        return couleursSecretes;
    }
}

