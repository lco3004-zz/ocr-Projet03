package mastermind.parametrageApplication;

import mastermind.App;
import mastermind.constantes.NomFichiersParametres;
import mastermind.messagesTexteMastermind.InfosMessages;

import static mastermind.constantes.NomFichiersParametres.FichierParamMasterMind;
import static mastermind.logMastermind.logApplicatif.getInstance;
import static mastermind.logMastermind.logApplicatif.logger;
import static mastermind.parametrageApplication.ParametresDuMasterMind.*;

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
