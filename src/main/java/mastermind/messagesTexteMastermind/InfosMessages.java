package mastermind.messagesTexteMastermind;

public enum InfosMessages {
    Lancement_Application("Lancement Application "),
    FinNormale_Applicaiton("Fin normale Application"),
    Lancement_GestionDesParametres("Lancement GestionDesParametres"),
    CreationFichierParametre("Creation du fichier Parametres - "),
    FinNormale_GestionDesParametres("Fin Normale GestionDesParametres");

    private String CodeInformation;
    InfosMessages(String s) {
        CodeInformation=s;
    }

    public String getMessageInfos() {
        return CodeInformation;
    }
}
