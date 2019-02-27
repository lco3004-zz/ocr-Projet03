package mastermind.parametrageApplication;

import java.util.Properties;

import mastermind.messagesTexteMastermind.ErreurMessages;

import static mastermind.logMastermind.logApplicatif.logger;


/**
 * uniquement vue de ce package
 */
public class ParametrageMasterMind extends Parametrage{
    Properties listeParams;

    public ParametrageMasterMind(String nomDuFichierParam) {
        super(nomDuFichierParam);
    }
    @Override
    void getParamDefaut(Properties listeParam) {
        for (ParametresDuMasterMind  v: ParametresDuMasterMind.values()) {
            String k = v.name();
            switch (v) {
                case NbDePositions:
                    listeParam.setProperty(v.name(), String.valueOf(v.getaInteger()));
                    break;
                case NbCouleurs:
                    listeParam.setProperty(v.name(), String.valueOf(v.getaInteger()));
                    break;
                case NbreMaxDeBoucleCherhceCodeSecret:
                    listeParam.setProperty(v.name(), String.valueOf(v.getaInteger()));
                    break;
                case DoublonAutorise:
                    listeParam.setProperty(v.name(), String.valueOf(v.getaBoolean()));
                    break;
                case CaseVideAUtorise:
                    listeParam.setProperty(v.name(), String.valueOf(v.getaBoolean()));
                    break;
                    default:
                        logger.error(ErreurMessages.ParamInconnu);
            }
        }
    }
}
