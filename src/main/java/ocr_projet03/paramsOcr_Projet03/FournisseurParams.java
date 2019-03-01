package ocr_projet03.paramsOcr_Projet03;

import ocr_projet03.exceptionOcr_Projet03.ExceptionMastermind;
import ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages;
import ocr_projet03.messagesTexteOcr_Projet03.InfosMessages;
import ocr_projet03.paramsOcr_Projet03.paramsMM.GroupParamsMM;
import ocr_projet03.paramsOcr_Projet03.paramsMM.IOParamsMM;

import java.util.Locale;
import java.util.Properties;

import static ocr_projet03.constantesOcr_Projet03.NomFichiersParametres.FichierParamMasterMind;
import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.TypeParamIncorrect;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ValeurParamIncorrect;
import static ocr_projet03.messagesTexteOcr_Projet03.InfosMessages.*;

public final class FournisseurParams {

    public static Object getParam(GroupParamsMM nomDuParamtreARecuperer) {

        logger.debug(Lancement_GestionDesParametres.getMessageInfos());

        IOParamsMM parametrageMasterMind=new IOParamsMM(FichierParamMasterMind.getNomFichier());
        Properties parametreMasterMindLu= parametrageMasterMind.lireParametre().getListeParams();


        Object retVal= new Object();
        GroupParamsMM groupParamsMM = GroupParamsMM.valueOf(nomDuParamtreARecuperer.toString());
        try {
            if (groupParamsMM.getUnParam().getTypeParam() == Integer.class.getSimpleName()) {
                Integer valLue = Integer.valueOf(parametreMasterMindLu.getProperty(groupParamsMM.name()));
                Integer borneMin =(Integer) groupParamsMM.getUnParam().getValeurMin();
                Integer borneMax =(Integer) groupParamsMM.getUnParam().getValeurMax();
                if ( valLue >= borneMin  && valLue <= borneMax) {
                    retVal = valLue;
                }
                else {
                    throw new ExceptionMastermind(ValeurParamIncorrect);
                }
            }
            else if (groupParamsMM.getUnParam().getTypeParam() == Boolean.class.getSimpleName()) {
                String valLue = parametreMasterMindLu.getProperty(groupParamsMM.name());
                valLue = valLue.toUpperCase(Locale.forLanguageTag("fr")).trim();
                String valeurVraie = String.format("%b",true).toUpperCase(Locale.forLanguageTag("fr"));
                String valeurFausse = String.format("%b",false).toUpperCase(Locale.forLanguageTag("fr"));
                if (valLue.equals(valeurFausse) || valLue.equals(valeurVraie)) {
                    retVal = Boolean.valueOf(parametreMasterMindLu.getProperty(groupParamsMM.name()));
                }
                else {
                    throw new ExceptionMastermind(ValeurParamIncorrect);
                }
            }
            else {
                logger.error(TypeParamIncorrect.getMessageErreur());
                retVal =null;
            }
        }catch ( Exception e) {
            retVal = groupParamsMM.getUnParam().getValeurDefaut();
            if  (groupParamsMM.getUnParam().getTypeParam() == Boolean.class.getSimpleName()) {
                logger.error(RemplacementParValeurDefaut.getMessageInfos()+ ((Boolean) retVal).toString());
            }
            else if (groupParamsMM.getUnParam().getTypeParam() == Integer.class.getSimpleName()) {
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
