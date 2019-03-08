package fr.ocr.params;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ECRITURE_PARAMETRES_IMPOSSIBLE;
import static fr.ocr.utiles.Messages.ErreurMessages.PARAMETRAGE_ILLISIBLE;
import static fr.ocr.utiles.Messages.InfosMessages.CREATION_FICHIER_PARAMETRE;

/**
 * Lecture /ecriture d'un fichier Property dont le nom est pass&eacute; en parametres du constructeur
 * Si le fichier n'existe pas, il est cr&eacute;&eacute; avec les valeurs par d&eacute;faut des parametres.
 * IOParams est abstraite car un fichier property se distingue par les properties
 * qu'il contient : l'impl&eacute;mentation de la m&eacute;thode getParamDefaut d&eacute;pend du jeu concern&eacute; (mastermind ou jeu
 * plus/moins, mais aussi config des menus de l'applicaiton
 */
public abstract class IOParams {

    private Properties listeParams; //liste des propriétés lue/ecrite dans le fichier property
    private String nomFichierParams; // hemin/nom du fichier property

    /**
     * @param nomFichierParams , nom du fichier de type property
     */
    protected IOParams(String nomFichierParams) {
        listeParams = new Properties();
        this.nomFichierParams = nomFichierParams;
    }

    /**
     * lecture du fichier property
     * Si incident de lecture, les paramètres valent leur valeur par défaut
     * Si incident sur fichier inexistant,  écriture du fichier avec les valeurs
     * par défaut des paramètres
     *
     * @return objet IOParams
     */
    protected IOParams lireParametre() {
        try {
            FileInputStream fileInputStream = new FileInputStream(nomFichierParams);
            try {
                listeParams.load(fileInputStream);
            } catch (Exception e1) {
                logger.error(PARAMETRAGE_ILLISIBLE.getMessageErreur() + " " + nomFichierParams);
                getParamDefaut(listeParams);
            }
        } catch (IOException e2) {
            logger.info(CREATION_FICHIER_PARAMETRE.getMessageInfos() + " " + nomFichierParams);
            getParamDefaut(listeParams);
            ecrireParametre();
        }
        return this;
    }

    /**
     * écriture du fichier property
     * Si incident d'écriture, log incident -
     *
     * @return objet IOParams
     */
    private IOParams ecrireParametre() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(nomFichierParams);

            listeParams.store(fileOutputStream, nomFichierParams);

        } catch (IOException e2) {
            logger.error(ECRITURE_PARAMETRES_IMPOSSIBLE.getMessageErreur() + " " + nomFichierParams);
        }
        return this;
    }

    /**
     * méthode abstraite car le jeu de paramètres concernés n'est pas connu ici
     * peut être (e.g) :mastermind, jeux +/- , params du mode d'afficahge COnsole
     *
     * @param listeParams properties, charge la collection "Properties" avec valeur par défaut des paramètres
     */
    protected abstract void getParamDefaut(Properties listeParams);

    /**
     * getter
     *
     * @return Properties , collection de type properties , listeParams
     */
    Properties getListeParams() {
        return listeParams;
    }
}
