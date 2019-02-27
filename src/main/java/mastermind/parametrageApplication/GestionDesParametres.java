package mastermind.parametrageApplication;

import mastermind.App;
import mastermind.messagesTexteMastermind.InfosMessages;

import static mastermind.logMastermind.logApplicatif.getInstance;
import static mastermind.logMastermind.logApplicatif.logger;
import static mastermind.parametrageApplication.ParametresDuMasterMind.*;

public final class GestionDesParametres {

    public static Object getParam(ParametresDuMasterMind x) {
        //renseigne le nom de la classe appelante , ici GestionDesParametres.
        getInstance(GestionDesParametres.class.getName());

        logger.info(InfosMessages.Lancement_GestionDesParametres);
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

        logger.info(InfosMessages.FinNormale_GestionDesParametres);

        return  retVal;
    }
}
