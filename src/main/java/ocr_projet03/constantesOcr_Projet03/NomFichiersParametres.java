package ocr_projet03.constantesOcr_Projet03;

public enum NomFichiersParametres {
    FichierParamMasterMind("out/production/main/resources/paramsMM.properties");

    private String nomFichier;
    NomFichiersParametres(String s) {
        nomFichier =s;
    }

    public String getNomFichier() {
        return nomFichier;
    }
}
