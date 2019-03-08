package fr.ocr.params;

import fr.ocr.params.mastermind.GroupParamsMM;
import fr.ocr.params.mastermind.IOParamsMM;
import fr.ocr.utiles.AppExceptions;

import java.util.Locale;
import java.util.Properties;

import static fr.ocr.utiles.Constantes.NomFichiersParametres.FICHIER_PARAM_MASTER_MIND;
import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.TYPE_PARAM_INCORRECT;
import static fr.ocr.utiles.Messages.ErreurMessages.VALEUR_PARAM_INCORRECT;
import static fr.ocr.utiles.Messages.InfosMessages.*;

public final class FournisseurParams {

    public static Object getParam(GroupParamsMM nomDuParamtreARecuperer) {

        logger.debug(LANCEMENT_GESTION_DES_PARAMETRES.getMessageInfos());

        IOParamsMM parametrageMasterMind = new IOParamsMM(FICHIER_PARAM_MASTER_MIND.getNomFichier());
        Properties parametreMasterMindLu = parametrageMasterMind.lireParametre().getListeParams();


        Object retVal;
        GroupParamsMM groupParamsMM = GroupParamsMM.valueOf(nomDuParamtreARecuperer.toString());
        try {
            String leTypeIci = Integer.class.getSimpleName();
            if (groupParamsMM.getTypeParam().equals(Integer.class.getSimpleName())) {
                Integer valLue = Integer.valueOf(parametreMasterMindLu.getProperty(groupParamsMM.name()));
                Integer borneMin = (Integer) groupParamsMM.getValeurMin();
                Integer borneMax = (Integer) groupParamsMM.getValeurMax();
                if (valLue >= borneMin && valLue <= borneMax) {
                    retVal = valLue;
                } else {
                    throw new AppExceptions(VALEUR_PARAM_INCORRECT);
                }
            } else if (groupParamsMM.getTypeParam().equals(Boolean.class.getSimpleName())) {
                String valLue = parametreMasterMindLu.getProperty(groupParamsMM.name());

                //valLue = valLue.toUpperCase(Locale.forLanguageTag("fr")).trim();

                String valeurVraie = String.format("%b", true).toUpperCase(Locale.forLanguageTag("fr"));
                String valeurFausse = String.format("%b", false).toUpperCase(Locale.forLanguageTag("fr"));

                if (valLue.equalsIgnoreCase(valeurFausse) || valLue.equalsIgnoreCase(valeurVraie)) {
                    retVal = Boolean.valueOf(parametreMasterMindLu.getProperty(groupParamsMM.name()));
                } else {
                    throw new AppExceptions(VALEUR_PARAM_INCORRECT);
                }
            } else {
                logger.error(TYPE_PARAM_INCORRECT.getMessageErreur());
                retVal = null;
            }
        } catch (Exception e) {
            retVal = groupParamsMM.getValeurDefaut();
            if (groupParamsMM.getTypeParam().equals(Boolean.class.getSimpleName())) {
                logger.error(REMPLACEMENT_PAR_VALEUR_DEFAUT.getMessageInfos() + ((Boolean) retVal).toString());
            } else if (groupParamsMM.getTypeParam().equals(Integer.class.getSimpleName())) {
                logger.error(REMPLACEMENT_PAR_VALEUR_DEFAUT.getMessageInfos() + ((Integer) retVal).toString());
            } else {
                logger.error(REMPLACEMENT_PAR_VALEUR_DEFAUT.getMessageInfos() + VALEUR_PARAM_INCORRECT.getMessageErreur());
            }
        }

        logger.debug(FIN_NORMALE_GESTION_DES_PARAMETRES.getMessageInfos());

        return retVal;
    }
}
