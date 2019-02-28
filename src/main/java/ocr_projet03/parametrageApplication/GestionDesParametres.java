package ocr_projet03.parametrageApplication;

import ocr_projet03.messagesTexteMastermind.InfosMessages;

import static ocr_projet03.constantes.NomFichiersParametres.FichierParamMasterMind;
import static ocr_projet03.logMastermind.logApplicatif.logger;

public final class GestionDesParametres {

    public static Object getParam(ParametresDuMasterMind x) {

        logger.debug(InfosMessages.Lancement_GestionDesParametres);
        Object retVal= new Object();
        switch (x) {
            case NbDePositions:
                retVal = (short)4;
                break;
            case NbCouleurs:
                retVal = (short)10;
                break;
            case DoublonAutorise:
                retVal = false;
                break;
            case CaseVideAUtorise:
                retVal = false;
                break;
        }
        //
        ParametrageMasterMind parametrageMasterMind=new ParametrageMasterMind(FichierParamMasterMind.getNomFichier());
        parametrageMasterMind.lireParametre();

        //
        logger.debug(InfosMessages.FinNormale_GestionDesParametres);

        return  retVal;
    }
}
