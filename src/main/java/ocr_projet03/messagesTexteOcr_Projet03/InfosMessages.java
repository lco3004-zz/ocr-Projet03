package ocr_projet03.messagesTexteOcr_Projet03;

public enum InfosMessages {
    Lancement_Application("Lancement Application "),
    FinNormale_Applicaiton("Fin normale Application"),
    Lancement_GestionDesParametres("Lancement FournisseurParams"),
    CreationFichierParametre("Creation du fichier Parametres - "),
    FinNormale_GestionDesParametres("Fin Normale FournisseurParams");

    private String CodeInformation;
    InfosMessages(String s) {
        CodeInformation=s;
    }

    public String getMessageInfos() {
        return CodeInformation;
    }
}
