package mastermind.parametrageMastermind;

import java.util.EnumMap;

public class GestionDesParametres {

    private static GestionDesParametres singletonInstance = new GestionDesParametres();
    private EnumMap<NomsDesParametresApplicatif, StructureDesParametreApplicatif> listeParametresApplicatifs;
    /**
     *
     * @return instance classe singleton
     */
    public static GestionDesParametres getInstance() {
        return singletonInstance;
    }
    /**
     *
     */
    private GestionDesParametres() { listeParametresApplicatifs= (new LireParametresApplicatif()).getParams();}
    /**
     *
     */
    public Object  getParam(NomsDesParametresApplicatif nomParam) {return listeParametresApplicatifs.get(nomParam).getValeur() ;}
}
