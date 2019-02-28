package ocr_projet03.parametrageApplication;

import java.util.Properties;

import ocr_projet03.messagesTexteMastermind.ErreurMessages;

import static ocr_projet03.logMastermind.logApplicatif.logger;


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
