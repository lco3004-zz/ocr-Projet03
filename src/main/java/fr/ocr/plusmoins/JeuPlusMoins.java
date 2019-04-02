package fr.ocr.plusmoins;

import fr.ocr.utiles.AppExceptions;
import fr.ocr.utiles.Constantes;
import fr.ocr.utiles.Messages;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static fr.ocr.params.Parametres.NOMBRE_D_ESSAIS;
import static fr.ocr.utiles.Logs.logger;


/**
 * Classe du jeu Plus Moins
 */

public class JeuPlusMoins {
    private char[][] tablePM;
    private int nombreDessai;
    private int nombreDePositions;

    public JeuPlusMoins() {
        nombreDePositions = (int) getParam(NOMBRE_DE_POSITIONS);
        nombreDessai = (int) getParam(NOMBRE_D_ESSAIS);

        /*
        en ligne : le nombre d'essais possible dans ce jeu (dépaend u paramètrage)
        en colonne : le nombre de cases de jeu : le paramètre NOMBRE_DE_POSITIONS donne la valeur du nombre de case
        pour un essai  : il faut la place pour l'essai de taille NOMBRE_DE_POSITIONS + son score qui est lui aussi de
        taille identique (NOMBRE_DE_POSITIONS) .
        */
        tablePM = new char[nombreDessai][2 * nombreDePositions];
    }

    /**
     * @param rang  entier qui est égal au numéro de ligne du tableau tabelPM dans laquelle ilfaut ajouter l'essai
     * @param essai le tableau de char  dont les colonnes contiennent les valeurs de l'essai
     */
    public void AjouterunEssai(int rang, char[] essai) {
        try {
            for (char c : essai) {
                //leve uen exception si valeur est <=0
                ChiffreEstIlValide(c);
            }
            System.arraycopy(essai, 0, tablePM[rang], 0, essai.length);
        } catch (IndexOutOfBoundsException | ArrayStoreException | NullPointerException e) {
            logger.error(Messages.ErreurMessages.SORTIE_PGM_SUR_ERREURNONGEREE);
            throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
        }
    }

    /**
     * @param rang  entier qui est égal au numéro de ligne du tableau tabelPM dans laquelle ilfaut ajouter le score
     * @param score le tableau de char dont les colonnes contiennent les valeurs du score
     */
    public void AjouterunScore(int rang, char[] score) throws AppExceptions {
        try {
            for (char c : score) {
                //leve uen esception si valeur est <=0
                ChiffreEstIlValide(c);
            }
            System.arraycopy(score, 0, tablePM[rang], nombreDePositions, score.length);
        } catch (IndexOutOfBoundsException | ArrayStoreException | NullPointerException e) {
            logger.error(Messages.ErreurMessages.SORTIE_PGM_SUR_ERREURNONGEREE);
            throw new AppExceptions(Messages.ErreurMessages.ERREUR_GENERIC);
        }
    }

    /**
     * les lignes du tableau tablePm, sous forme affichable
     * modele primaire - pas de list pas de stream - non // (sauf ajout synchronized)
     * peut être aisément converti en assembleur 8086.
     *
     * @return String[] , les lignes du tableau tablePm, sous forme affichable
     */
    public String[] toListeStrings() {
        String[] strValRet = new String[nombreDessai];

        int i = 0;
        //parcours des lignes
        for (char[] uneLigne : tablePM) {
            strValRet[i] = "{ ";
            int countCol = 0;
            //parcours des colonnes
            for (char uneValeur : uneLigne) {
                //separateur entre zone essai et zone score
                if (countCol == nombreDePositions) {
                    strValRet[i] += " | ";
                }
                strValRet[i] += (uneValeur == 0) ? " x " : String.format(" %c ", uneValeur);
                countCol++;
            }
            strValRet[i] += "}";
            i++;
        }
        return strValRet;
    }

    /**
     * retourne la table du jeu tableau à deux dimension en ligne les essais, en colonne le contenu
     * et le score d'un essai
     *
     * @return char [][] , la table
     */
    public char[][] getTablePM() {
        return tablePM;
    }

    /**
     * vérification que la caractère passé en paramétre correspond à un chiffre et s'il est dans la plage 1-9
     *
     * @param c caractere à évaluae : doit être dans le range 1-9
     * @throws AppExceptions avec CharacterExceptionPM.A comme caractere de sortie (ce qui permet de gérer car c'est
     *                       une exception qui n'est pas fatale - il faut refaire une saisie.
     */
    private void ChiffreEstIlValide(char c) throws AppExceptions {
        int i;
        try {
            i = Integer.valueOf(String.valueOf(c));
            if (i <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new AppExceptions(Messages.InfosMessages.PM_CHIFFRE_HORS_PLAGE_TOLERANCE, Constantes.Libelles.CharacterExceptionPM.A.toString().charAt(0));
        }
    }

    private void SaisieUnEssaiJoueur() {

    }

    private void SaisieUnEssaiOrdi() {

    }

    private boolean DonneScoreJoueur() {
        return true;
    }

    private boolean DonneScoreOrdi() {
        return true;
    }

    private void FaitUnSecretJoueur() {

    }

    private void FaitUnSecretOrdi() {

    }

    public void runModeChallengeur() {
        FaitUnSecretOrdi();
        for (int i = 0; i < nombreDessai; i++) {
            SaisieUnEssaiJoueur();
            if (DonneScoreOrdi()) {
                break;
            }
        }
    }

    public void runModeDefenseur() {
        FaitUnSecretJoueur();
        for (int i = 0; i < nombreDessai; i++) {
            SaisieUnEssaiOrdi();
            if (DonneScoreJoueur()) {
                break;
            }
        }
    }

    public void runModeDuel() {
        FaitUnSecretOrdi();
        for (int i = 0; i < nombreDessai; i++) {
            SaisieUnEssaiOrdi();
            if (DonneScoreJoueur()) {
                break;
            }
            SaisieUnEssaiJoueur();
            if (DonneScoreOrdi()) {
                break;
            }
        }
    }

}
