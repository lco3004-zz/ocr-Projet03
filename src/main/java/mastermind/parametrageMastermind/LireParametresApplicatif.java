package mastermind.parametrageMastermind;


import mastermind.constantesMastermind.ValeursConstantes;
import mastermind.messagesTexteMastermind.ErreurMessages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;

import static mastermind.parametrageMastermind.NomsDesParametresApplicatif.*;


/**
 * uniquement vue de ce package
 */
 class LireParametresApplicatif  {

    private EnumMap<NomsDesParametresApplicatif, StructureDesParametreApplicatif> listeDesParametres;
    private ErreurMessages errMsg ;
    private static Logger LOGGER = LogManager.getLogger(LireParametresApplicatif.class.getName());

    /**
     *
     */
    LireParametresApplicatif() {
        listeDesParametres = new EnumMap<> (NomsDesParametresApplicatif.class);
    }

    /**
     * à changer lors de l'implémentation de lecture param depuis une source (xml, datasource...)
     */

    private Boolean RecupererParametres() {
        return false;
    }

    /**
     *
     */
    private void RecupereParamParDefaut() {
        StructureDesParametreApplicatif x ;
        for (NomsDesParametresApplicatif nomParam : NomsDesParametresApplicatif.values() ) {
            switch (nomParam) {
                case NbDePositions:
                    x = new StructureDesParametreApplicatif(
                            NbDePositions,
                            TypesDesParametresApplicatifs.Entier,
                            ValeursConstantes.nombrePositionsMax,
                            false);
                    listeDesParametres.putIfAbsent(NbDePositions,x);
                    break;
                case NbCouleurs:
                    x = new StructureDesParametreApplicatif(
                            NbCouleurs,
                            TypesDesParametresApplicatifs.Entier,
                            ValeursConstantes.nombreCouleursMax,
                            false);
                    listeDesParametres.putIfAbsent(NbCouleurs,x);

                    break;
                case DoublonAutorise:
                    x = new StructureDesParametreApplicatif(
                            DoublonAutorise,
                            TypesDesParametresApplicatifs.Booleen,
                            ValeursConstantes.DoublonnagePion,
                            false);
                    listeDesParametres.putIfAbsent(DoublonAutorise,x);
                    break;
                case CaseVideAUtorise:
                    x = new StructureDesParametreApplicatif(
                            CaseVideAUtorise,
                            TypesDesParametresApplicatifs.Booleen,
                            ValeursConstantes.CaseVide,
                            false);
                    listeDesParametres.putIfAbsent(CaseVideAUtorise,x);
                    break;
                default:
                    LOGGER.error(ErreurMessages.ParamInconnu);

            }
        }
    }


    /**
     *
     * @return EnumMap<NomsDesParametresApplicatif, StructureDesParametreApplicatif>
     */
    EnumMap<NomsDesParametresApplicatif, StructureDesParametreApplicatif> getParams() {
        if (!RecupererParametres())
            RecupereParamParDefaut();
        return listeDesParametres;
    }

}
