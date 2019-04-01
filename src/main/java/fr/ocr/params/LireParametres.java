package fr.ocr.params;

import fr.ocr.utiles.AppExceptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import static fr.ocr.utiles.Constantes.NomFichiersParametres.FICHIER_PARAM_MASTER_MIND;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.*;
import static fr.ocr.utiles.Messages.InfosMessages.CREATION_FICHIER_PARAMETRE;
import static fr.ocr.utiles.Messages.InfosMessages.REMPLACEMENT_PAR_VALEUR_DEFAUT;


/**
 * <p>
 * @author laurent cordier
 * Lecture /ecriture d'un fichier Property dont le nom est pass&eacute; en parametres du constructeur
 * Si le fichier n'existe pas, il est cr&eacute;&eacute; avec les valeurs par d&eacute;faut des parametres.
 * IOParams est abstraite car un fichier property se distingue par les properties
 * qu'il contient : l'impl&eacute;mentation de la m&eacute;thode getParamDefaut d&eacute;pend du jeu concern&eacute; (lesjeux ou jeu
 * plus/moins, mais aussi config des menus de l'applicaiton
 * </p>
 */
class IOParams {

    private Properties listeParams; //liste des propriétés lue/ecrite dans le fichier property
    private String nomFichierParams; // hemin/nom du fichier property

    /**
     * @param nomFichierParams , nom du fichier de type property
     */
    IOParams(String nomFichierParams) {
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
    IOParams lireParametre() {
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
     * peut être (e.g) :lesjeux, jeux +/- , params du mode d'afficahge COnsole
     *
     * @param listeParams properties, charge la collection "Properties" avec valeur par défaut des paramètres
     */
    private void getParamDefaut(Properties listeParams) {
        for (Parametres v : Parametres.values()) {
            listeParams.setProperty(v.name(), String.valueOf(v.getValeurDefaut()));
        }
    }

    /**
     * getter
     *
     * @return Properties , collection de type properties , listeParams
     */
    Properties getListeParams() {
        return listeParams;
    }
}

/**
 * lecture des properties (static final)
 */
public final class LireParametres {
    private static boolean overrideModeDeveloppeur = false;
    static private IOParams parametrageMasterMind = new IOParams(FICHIER_PARAM_MASTER_MIND.getNomFichier());
    static private Properties parametreMasterMindLu = parametrageMasterMind.lireParametre().getListeParams();

    /*

     */
    public static void OverrideParamModeDeveloppeur() {

        overrideModeDeveloppeur = true;
    }

    /*

     */
    public static void getAllParams() {

        for (Parametres p : Parametres.values()) {
            getParam(p);
        }

        // controle si le nombre de positons n'est pas superieur au nombre de couleur
        if ((Integer) getParam(Parametres.NOMBRE_DE_POSITIONS) > (Integer) getParam(Parametres.NOMBRE_DE_COULEURS)) {
            Integer valDefPos = (Integer) Parametres.NOMBRE_DE_POSITIONS.getValeurDefaut();
            parametreMasterMindLu.setProperty(Parametres.NOMBRE_DE_POSITIONS.toString(), valDefPos.toString());
            logger.warn(TYPE_PARAM_INCORRECT);
        }
        logAllProperties();
    }

    /*

     */
    public static Object getParam(Parametres nomDuParamtreARecuperer) {

        Object retVal;
        Parametres parametres = Parametres.valueOf(nomDuParamtreARecuperer.toString());

        try {

            String leTypeIci = Integer.class.getSimpleName();
            //lecture du parametre de type entier , de sa valeur min et max
            if (parametres.getTypeParam().equals(Integer.class.getSimpleName())) {

                Integer valLue = Integer.valueOf(parametreMasterMindLu.getProperty(parametres.name()));

                Integer borneMin = (Integer) parametres.getValeurMin();

                Integer borneMax = (Integer) parametres.getValeurMax();
                //si parmaetre hors plage , valeur par defaut prise
                if (valLue >= borneMin && valLue <= borneMax) {
                    retVal = valLue;
                } else if (valLue < borneMin) {
                    retVal = borneMin;
                } else {
                    retVal = borneMax;
                }
                //lecture du parametre de type boolean
            } else if (parametres.getTypeParam().equals(Boolean.class.getSimpleName())) {
                String valLue = parametreMasterMindLu.getProperty(parametres.name());

                String valeurVraie = String.format("%b", true).toUpperCase(Locale.forLanguageTag("fr"));
                String valeurFausse = String.format("%b", false).toUpperCase(Locale.forLanguageTag("fr"));

                //si valeur lue incorrecte, valeur par defaut attribuée
                if (valLue.equalsIgnoreCase(valeurFausse) || valLue.equalsIgnoreCase(valeurVraie)) {
                    retVal = Boolean.valueOf(parametreMasterMindLu.getProperty(parametres.name()));
                } else {
                    throw new AppExceptions(VALEUR_PARAM_INCORRECT);
                }
            } else {
                logger.error(TYPE_PARAM_INCORRECT.getMessageErreur());
                retVal = null;
            }
        } catch (Exception e) {
            //le fichier n'existe pas , onl ecree avec lesa valeurs par default
            retVal = parametres.getValeurDefaut();
            if (parametres.getTypeParam().equals(Boolean.class.getSimpleName())) {
                logger.error(REMPLACEMENT_PAR_VALEUR_DEFAUT.getMessageInfos() + ((Boolean) retVal).toString());
            } else if (parametres.getTypeParam().equals(Integer.class.getSimpleName())) {
                logger.error(REMPLACEMENT_PAR_VALEUR_DEFAUT.getMessageInfos() + ((Integer) retVal).toString());
            } else {
                logger.error(REMPLACEMENT_PAR_VALEUR_DEFAUT.getMessageInfos() + VALEUR_PARAM_INCORRECT.getMessageErreur());
            }
        }
        // controle surchagr valeur suite option en ligne de commande
        if (parametres.name().equals(Parametres.MODE_DEVELOPPEUR.toString())) {
            if (overrideModeDeveloppeur)
                retVal = true;
        }
        return retVal;
    }

    public static void logAllProperties() throws AppExceptions {

        Object tmpRetour;
        for (Parametres x : Parametres.values()) {
            tmpRetour = getParam(x);
            if (tmpRetour instanceof Integer) {
                logger.info(String.format("%s = %d", x.toString(), tmpRetour));
            } else if (tmpRetour instanceof Boolean) {
                logger.info(String.format("%s = %b", x.toString(), tmpRetour));
            } else {
                throw new AppExceptions(TYPE_PARAM_INCORRECT);
            }
        }
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */
