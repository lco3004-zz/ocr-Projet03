package fr.ocr.mastermind;


import fr.ocr.params.mastermind.CouleursMastermind;
import fr.ocr.params.mastermind.GroupParamsMM;
import fr.ocr.utiles.ApplicationExceptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static fr.ocr.params.FournisseurParams.getParam;
import static fr.ocr.utiles.Messages.ErreurMessages.TYPE_PARAM_INCORRECT;



/**
 * Création de la ligne secrète à découvrir
 * la ligne secrète contient des couleurs .
 * La composition de la ligne  est faite à partir  d'une liste prédéfinie de couleurs
 *
 * Méthode :
 *  tirage aléatoire d'un nombre modulo  le nombre de couleurs prédéfinies
 *  cette valeur est ajoutée au tableau chiffresSecrets
 *   et
 *  pour chacune de ces valeurs
 *  le tableau couleurSecretes est renseigné avec : ListeDeCouleursPrédéfinies [valeur]
 *
 *  il y a donc bijection entre le tableau chiffresSecrets et le tableau couleursSecretes.
 *
 *  Si le paramètre applicatif  DOUBLON_AUTORISE est vrai, il peut y avoir plusieurs couleurs identiques dans la ligne secrète (sinon, non)
 *  le paramètre applicatif  NOMBRE_DE_POSITIONS indique la taille des tableaux  chiffresSecrets et  couleursSecretes
 *  le parametre applicatif NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE limite de le nombre de boucles effectuées dans la recherche
 *  d'un nombre aléatoire unique (cas ou DOUBLON_AUTORISE est faux car il faut alors une couleur/chiffre unique dans la ligne secrète)
 *
 * Jeu MasterMind : fabrication de la ligne de couleur à trouver
 * Utilisé lorsque le jeu se fait contre l'ordinateur
 */
public class ChoixCodeSecret  {

    //
    private ArrayList<Byte> chiffresSecrets;

    //
    private CouleursMastermind [] couleursSecretes;

    /**
     *
     * @throws ApplicationExceptions . Exception levée sur erreur cohérence entre
     * le type de paramètre demandé et le type lu depuis la source des parametres (fichier paramètre)
     */
    public ChoixCodeSecret() throws ApplicationExceptions {

        Object tmpRetour;

        Byte valeurAleatoire;

        Boolean doublonAutorise;

        Integer nombreDePositions;

        Integer nombreDeCouleurs;

        Integer nombreDebBoucleMax;

        chiffresSecrets = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#");

        //
        df.setRoundingMode(RoundingMode.HALF_UP);

        tmpRetour=getParam(GroupParamsMM.NOMBRE_DE_POSITIONS);
        if ( tmpRetour instanceof Integer) {
            nombreDePositions = (Integer) tmpRetour;
        } else {
            throw new ApplicationExceptions(TYPE_PARAM_INCORRECT);
        }

        tmpRetour=getParam(GroupParamsMM.NOMBRE_DE_COULEURS);
        if ( tmpRetour instanceof Integer) {
            nombreDeCouleurs = (Integer)tmpRetour;
        } else {
            throw new ApplicationExceptions(TYPE_PARAM_INCORRECT);
        }

        tmpRetour = getParam(GroupParamsMM.DOUBLON_AUTORISE);
        if ( tmpRetour instanceof Boolean) {
            doublonAutorise = (Boolean) tmpRetour;
        } else {
            throw new ApplicationExceptions(TYPE_PARAM_INCORRECT);
        }

        tmpRetour=getParam(GroupParamsMM.NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE);
        //
        if ( tmpRetour instanceof Integer) {
            nombreDebBoucleMax = (Integer)tmpRetour;
        } else {
            throw new ApplicationExceptions(TYPE_PARAM_INCORRECT);
        }

        for (int placeOccupee = 0, nbreDeBoucles = 0; (placeOccupee < nombreDePositions) && (nbreDeBoucles < nombreDebBoucleMax); nbreDeBoucles++) {
            valeurAleatoire = (byte)(Byte.parseByte(df.format(Math.random()*100)) % nombreDeCouleurs);

            if (!doublonAutorise) {
                if (!chiffresSecrets.contains(valeurAleatoire)) {
                    chiffresSecrets.add(valeurAleatoire);
                    placeOccupee++;
                }
            }
            else {
                chiffresSecrets.add(valeurAleatoire);
                placeOccupee++;
            }
        }
        //pas assez de positions remplies - le random n'a pas marché
        if (chiffresSecrets.size() < nombreDePositions) {

            CouleursMastermind[] couleursMastermind = CouleursMastermind.values();
            chiffresSecrets.clear();
            for (int i = 0; i < nombreDePositions; i++ ) {
                chiffresSecrets.add(couleursMastermind[i].getValeurFacialeDeLaCouleur());
            }
        }

        couleursSecretes = new CouleursMastermind[nombreDePositions];
        int i=0;
        for (Byte v: chiffresSecrets) {
            couleursSecretes[i++] = CouleursMastermind.values()[ (int)v ];
        }
    }

    /**
     *
     * @return ArrayList<Byte> Tableau des index des couleurs de la ligne secrète
     */
    public ArrayList<Byte> getChiffresSecrets() {

        return chiffresSecrets;
    }

    /**
     *
     * @return  CouleursMastermind[] Tableau des couleurs de la ligne secrète
     */
    public CouleursMastermind[] getCouleursSecretes() {

        return couleursSecretes;
    }
}

