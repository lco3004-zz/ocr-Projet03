package mastermind.parametrageMastermind;

/**
 *
 */
public class StructureDesParametreApplicatif {

    /**
     *
     */
    private NomsDesParametresApplicatif nomParam ;
    public NomsDesParametresApplicatif getNomParam() { return nomParam;}

    private Boolean optionnel = false;
    public Boolean getOptionnel() {return optionnel;}

    private TypesDesParametresApplicatifs typeParam ;
    public TypesDesParametresApplicatifs getTypeParam() {return typeParam;}

    private Object Valeur = 0;
    public Object  getValeur() {return Valeur;}

    /**
     *
     * @param nomParam
     * @param typeParam
     * @param valeur
     * @param optionnel
     */
    public StructureDesParametreApplicatif(NomsDesParametresApplicatif nomParam, TypesDesParametresApplicatifs typeParam, Object valeur, Boolean optionnel) {
        this.nomParam=nomParam;
        this.typeParam=typeParam;
        this.Valeur=valeur;
        this.optionnel=optionnel;
    }
}
