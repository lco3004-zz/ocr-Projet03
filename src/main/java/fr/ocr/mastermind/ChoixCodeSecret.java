package fr.ocr.mastermind;

/**
 * Création du Code Secret comprenant X positions , X étant égal au paramètre "nombreDePositions"
 * Chaque position contient une couleur représentée sous forme de nombre pris parmi le segment 0..nombreDeCouleurs-1
 * Le choix de couleur est aléatoire
 */

import fr.ocr.params.mastermind.CouleursMastermind;
import fr.ocr.params.mastermind.GroupParamsMM;
import fr.ocr.utiles.Exceptions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static fr.ocr.params.FournisseurParams.getParam;
import static fr.ocr.utiles.Messages.ErreurMessages.TYPE_PARAM_INCORRECT;

public class ChoixCodeSecret  {
    /**
     *
     */
    private Integer nombreDePositions, nombreDeCouleurs, nombreDebBoucleMax;
    private ArrayList<Byte> codeSecret ;
    private CouleursMastermind [] ligneSecrete ;

    /**
     *
     * @throws Exceptions
     */
    public ChoixCodeSecret() throws Exceptions {

        Object tmpRetour;
        Byte valeurAleatoire;
        Boolean doublonAutorise;

        codeSecret = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.HALF_UP);

        nombreDePositions = 0;
        tmpRetour=getParam(GroupParamsMM.NOMBRE_DE_POSITIONS);
        if ( tmpRetour instanceof Integer) {
            nombreDePositions = (Integer) tmpRetour;
        } else {
            throw new Exceptions(TYPE_PARAM_INCORRECT);
        }


        nombreDeCouleurs =  0;
        tmpRetour=getParam(GroupParamsMM.NOMBRE_DE_COULEURS);
        if ( tmpRetour instanceof Integer) {
            nombreDeCouleurs = (Integer)tmpRetour;
        } else {
            throw new Exceptions(TYPE_PARAM_INCORRECT);
        }

        tmpRetour = getParam(GroupParamsMM.DOUBLON_AUTORISE);
        if ( tmpRetour instanceof Boolean) {
            doublonAutorise = (Boolean) tmpRetour;
        } else {
            throw new Exceptions(TYPE_PARAM_INCORRECT);
        }

        nombreDebBoucleMax =0;
        tmpRetour=getParam(GroupParamsMM.NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE);
        if ( tmpRetour instanceof Integer) {
            nombreDebBoucleMax = (Integer)tmpRetour;
        } else {
            throw new Exceptions(TYPE_PARAM_INCORRECT);
        }

        for (int placeOccupee = 0, nbreDeBoucles = 0; (placeOccupee < this.nombreDePositions) && (nbreDeBoucles < nombreDebBoucleMax); nbreDeBoucles++) {
            valeurAleatoire = (byte)(Byte.parseByte(df.format(Math.random()*100)) % nombreDeCouleurs );

            if (!doublonAutorise) {
                if (!codeSecret.contains(valeurAleatoire)) {
                    codeSecret.add(valeurAleatoire);
                    placeOccupee++;
                }
            }
            else {
                codeSecret.add(valeurAleatoire);
                placeOccupee++;
            }
        }
        //pas assez de positions remplies - le random n'a pas marché
        if (codeSecret.size() < nombreDePositions) {

            CouleursMastermind[] couleursMastermind = CouleursMastermind.values();
            codeSecret.clear();
            for (int i =0; i<nombreDePositions;i++ ) {
                codeSecret.add(couleursMastermind[i].getValeurFacialeDeLaCouleur());
            }
        }

        ligneSecrete  = new CouleursMastermind[this.nombreDePositions ];
        int i=0;
        for (Byte v: codeSecret) {
            ligneSecrete[i++] = CouleursMastermind.values()[(int)v];
        }
    }

    /**
     *
     * @return ArrayList<Byte> Tableau des index des couleurs de la ligne secrète
     */
    public ArrayList<Byte> getCodeSecret() {

        return codeSecret;
    }

    /**
     *
     * @return  CouleursMastermind[] Tableau des couleurs de la ligne secrète
     */
    public CouleursMastermind[] getLigneSecrete() {

        return ligneSecrete;
    }
}

