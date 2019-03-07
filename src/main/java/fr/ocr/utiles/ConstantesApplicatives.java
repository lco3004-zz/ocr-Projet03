package fr.ocr.utiles;

public enum ConstantesApplicatives {
    ;
    public enum VersionPGM {
        VERSION_PGM ("V0.1");
        private String version;
        VersionPGM(String s) {
            version=s;
        }

        public String getVersion() {
            return version;
        }
    }

    public enum NomFichiersParametres {
        FichierParamMasterMind("target/classes/params.properties");

        private String nomFichier;
        NomFichiersParametres(String s) {
            nomFichier =s;
        }

        public String getNomFichier() {
            return nomFichier;
        }
    }

}
