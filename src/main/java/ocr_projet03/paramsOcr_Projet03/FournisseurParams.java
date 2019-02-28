package ocr_projet03.paramsOcr_Projet03;

import ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages;
import ocr_projet03.messagesTexteOcr_Projet03.InfosMessages;
import ocr_projet03.paramsOcr_Projet03.paramsMM.GroupParamsMM;
import ocr_projet03.paramsOcr_Projet03.paramsMM.IOParamsMM;

import java.util.Properties;

import static ocr_projet03.constantesOcr_Projet03.NomFichiersParametres.FichierParamMasterMind;
import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.TypeParamIncorrect;
import static ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages.ValeurParamIncorrect;

public final class FournisseurParams {

    public static Object getParam(GroupParamsMM nomDuParamtreARecuperer) {

        logger.debug(InfosMessages.Lancement_GestionDesParametres);

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
                    logger.error(ValeurParamIncorrect);
                    retVal = groupParamsMM.getUnParam().getValeurDefaut();
                }

            }else if (groupParamsMM.getUnParam().getTypeParam() == Boolean.class.getSimpleName())
                retVal = Boolean.valueOf(parametreMasterMindLu.getProperty(groupParamsMM.name()));
            else
                logger.error(TypeParamIncorrect);
        }catch ( Exception e) {
            logger.error(TypeParamIncorrect);
            retVal = groupParamsMM.getUnParam().getValeurDefaut();
        }

        logger.debug(InfosMessages.FinNormale_GestionDesParametres);

        return  retVal;
    }
}
