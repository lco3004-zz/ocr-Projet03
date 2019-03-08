package fr.ocr.utiles;

public enum Constantes {
    ;

    public enum VersionPGM {
        VERSION_APPLICATION("V0.1");
        private String version;

        VersionPGM(String s) {
            version = s;
        }

        public String getVersion() {
            return version;
        }
    }

    public enum NomFichiersParametres {
        FICHIER_PARAM_MASTER_MIND("target/classes/params.properties");

        private String nomFichier;

        NomFichiersParametres(String s) {
            nomFichier = s;
        }

        public String getNomFichier() {
            return nomFichier;
        }
    }

}
