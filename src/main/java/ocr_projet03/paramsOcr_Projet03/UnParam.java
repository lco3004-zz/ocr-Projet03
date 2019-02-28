package ocr_projet03.paramsOcr_Projet03;

public class UnParam<T extends Object> {

    private T valeurDefaut;

    public T getValeurDefaut() {
        return valeurDefaut;
    }

    private String typeParam;

    public String getTypeParam() {
        return typeParam;
    }

    private Number valeurMin;

    public Number getValeurMax() {
        return valeurMax;
    }

    private  Number valeurMax;

    public Number getValeurMin() {
        return valeurMin;
    }

    public  UnParam  (T valdef, String type) {

        typeParam = type;
        valeurDefaut = valdef;
    }
    public  UnParam  (T valdef,String type, Integer valMin, Integer valMax) {

        typeParam = type;
        valeurDefaut = valdef;
        valeurMin =valMin;
        valeurMax = valMax;
    }
    public UnParam <T>  getUnParam () {
        return this;
    }
}
