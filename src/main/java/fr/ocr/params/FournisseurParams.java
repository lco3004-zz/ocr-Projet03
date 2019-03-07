package fr.ocr.params;

import static fr.ocr.utiles.ConstantesApplicatives.NomFichiersParametres.FichierParamMasterMind;
import fr.ocr.utiles.ExceptionsApplicatives;
import static fr.ocr.utiles.LogApplicatifs.logger;

import fr.ocr.params.paramsMM.GroupParamsMM;
import fr.ocr.params.paramsMM.IOParamsMM;

import static fr.ocr.utiles.Messages.InfosMessages.*;
import static fr.ocr.utiles.Messages.ErreurMessages.*;

import java.util.Locale;
import java.util.Properties;

public final class FournisseurParams {

    public static Object getParam(GroupParamsMM nomDuParamtreARecuperer) {

        logger.debug(Lancement_GestionDesParametres.getMessageInfos());

        IOParamsMM parametrageMasterMind=new IOParamsMM(FichierParamMasterMind.getNomFichier());
        Properties parametreMasterMindLu= parametrageMasterMind.lireParametre().getListeParams();


        Object retVal;
        GroupParamsMM groupParamsMM = GroupParamsMM.valueOf(nomDuParamtreARecuperer.toString());
        try {
            String leTypeIci = Integer.class.getSimpleName();
            if (groupParamsMM.getUnParam().getTypeParam().equals(Integer.class.getSimpleName())){
                Integer valLue = Integer.valueOf(parametreMasterMindLu.getProperty(groupParamsMM.name()));
                Integer borneMin =(Integer) groupParamsMM.getUnParam().getValeurMin();
                Integer borneMax =(Integer) groupParamsMM.getUnParam().getValeurMax();
                if ( valLue >= borneMin  && valLue <= borneMax) {
                    retVal = valLue;
                }
                else {
                    throw new ExceptionsApplicatives(ValeurParamIncorrect);
                }
            }
            else if (groupParamsMM.getUnParam().getTypeParam().equals( Boolean.class.getSimpleName())) {
                String valLue = parametreMasterMindLu.getProperty(groupParamsMM.name());

                //valLue = valLue.toUpperCase(Locale.forLanguageTag("fr")).trim();

                String valeurVraie = String.format("%b",true).toUpperCase(Locale.forLanguageTag("fr"));
                String valeurFausse = String.format("%b",false).toUpperCase(Locale.forLanguageTag("fr"));

                if (valLue.equalsIgnoreCase(valeurFausse) || valLue.equalsIgnoreCase(valeurVraie)) {
                    retVal = Boolean.valueOf(parametreMasterMindLu.getProperty(groupParamsMM.name()));
                }
                else {
                    throw new ExceptionsApplicatives(ValeurParamIncorrect);
                }
            }
            else {
                logger.error(TypeParamIncorrect.getMessageErreur());
                retVal =null;
            }
        }catch ( Exception e) {
            retVal = groupParamsMM.getUnParam().getValeurDefaut();
            if  (groupParamsMM.getUnParam().getTypeParam().equals(Boolean.class.getSimpleName())) {
                logger.error(RemplacementParValeurDefaut.getMessageInfos()+ ((Boolean) retVal).toString());
            }
            else if (groupParamsMM.getUnParam().getTypeParam().equals(Integer.class.getSimpleName()) ) {
                logger.error(RemplacementParValeurDefaut.getMessageInfos()+ ((Integer) retVal).toString());
            }
            else  {
                logger.error(RemplacementParValeurDefaut.getMessageInfos()+ ValeurParamIncorrect.getMessageErreur());
            }
        }

        logger.debug(FinNormale_GestionDesParametres.getMessageInfos());

        return  retVal;
    }
}
