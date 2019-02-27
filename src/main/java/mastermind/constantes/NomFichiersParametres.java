package mastermind.constantes;

public enum NomFichiersParametres {
    FichierParamMasterMind("./target/classes/paramMasterMind.properties");

    private String nomFichier;
    NomFichiersParametres(String s) {
        nomFichier =s;
    }

    public String getNomFichier() {
        return nomFichier;
    }
}
