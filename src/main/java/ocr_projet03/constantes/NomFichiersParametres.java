package ocr_projet03.constantes;

public enum NomFichiersParametres {
    FichierParamMasterMind("./params/paramMasterMind.properties");

    private String nomFichier;
    NomFichiersParametres(String s) {
        nomFichier =s;
    }

    public String getNomFichier() {
        return nomFichier;
    }
}
