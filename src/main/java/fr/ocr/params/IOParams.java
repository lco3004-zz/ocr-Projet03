
/**
 * Lecture /ecriture d'un fichier Property dont le nom est passé en parametres du constructeur
 * Si le fichier n'existe pas, il est créé avec les valeurs par défaut des parametres.
 * IOParams est abstraite car un fichier property se distingue par les properties
 * qu'il contient : l'implémentation de la méthode getParamDefaut dépend du jeu concerné (mastermind ou jeu
 * plus/moins, mais aussi config des menus de l'applicaiton
 */
package fr.ocr.params;

import static fr.ocr.utiles.LogApplicatifs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.*;
import static fr.ocr.utiles.Messages.InfosMessages.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
 public abstract  class IOParams {

    private Properties listeParams; //liste des propriétés lue/ecrite dans le fichier property
    private String nomFichierParams; // hemin/nom du fichier property

    /**
     *
     * @param  nomFichierParams , nom du fichier de type property
     */
    protected IOParams(String nomFichierParams) {
        listeParams = new Properties();
        this.nomFichierParams= nomFichierParams;
    }

    /**
     * lecture du fichier property
     * Si incident de lecture, les paramètres valent leur valeur par défaut
     * Si incident sur fichier inexistant,  écriture du fichier avec les valeurs
     * par défaut des paramètres
     *
     * @return objet IOParams
     */
    protected IOParams lireParametre()  {
        try {
            FileInputStream fileInputStream = new FileInputStream(nomFichierParams);
            try {
                listeParams.load(fileInputStream);
            }catch (Exception e1){
                logger.error(ParametrageIllisible.getMessageErreur() +" "+ nomFichierParams);
                getParamDefaut(listeParams);
            }
        }catch (IOException e2) {
            logger.info(CreationFichierParametre.getMessageInfos() +" "+ nomFichierParams);
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
     IOParams ecrireParametre () {
        try {
            FileOutputStream fileOutputStream= new FileOutputStream(nomFichierParams);

                listeParams.store(fileOutputStream,nomFichierParams);

        }catch (IOException e2) {
            logger.error(EcritureParametresImpossible.getMessageErreur() + " "+nomFichierParams );
        }
        return this;
    }

    /**
     *méthode abstraite car le jeu de paramètres concernés n'est pas connu ici
     * peut être (e.g) :mastermind, jeux +/- , params du mode d'afficahge COnsole
     *
     * @param listeParams properties, charge la collection "Properties" avec valeur par défaut des paramètres
     */
    protected abstract  void getParamDefaut(Properties listeParams) ;

    /**
     * getter
     * @return Properties , collection de type properties , listeParams
     */
     Properties getListeParams() {
        return listeParams;
    }
}
