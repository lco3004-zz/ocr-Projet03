package fr.ocr.params;

public class UnParam<T> {

    private T valeurDefaut;
    private String typeParam;
    private Number valeurMin;
    private Number valeurMax;

    public UnParam(T valdef, String type) {

        typeParam = type;
        valeurDefaut = valdef;
    }

    public UnParam(T valdef, String type, Integer valMin, Integer valMax) {

        typeParam = type;
        valeurDefaut = valdef;
        valeurMin = valMin;
        valeurMax = valMax;
    }

    public T getValeurDefaut() {
        return valeurDefaut;
    }

    String getTypeParam() {
        return typeParam;
    }

    Number getValeurMax() {
        return valeurMax;
    }

    Number getValeurMin() {
        return valeurMin;
    }

    public UnParam<T> getUnParam() {
        return this;
    }
}
