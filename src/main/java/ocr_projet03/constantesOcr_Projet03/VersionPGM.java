package ocr_projet03.constantesOcr_Projet03;

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
