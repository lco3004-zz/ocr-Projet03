package ocr_projet03.messagesTexteOcr_Projet03;

public enum InfosMessages {
    Lancement_Application("Lancement Application "),
    FinNormale_Application("Fin normale Application"),
    Lancement_GestionDesParametres("Lancement FournisseurParams"),
    CreationFichierParametre("Creation du fichier Parametres - "),
    FinNormale_GestionDesParametres("Fin Normale FournisseurParams"),
    RemplacementParValeurDefaut("Remplacement par la valeur par défaut =  ");

    private String MessageInformation;
    InfosMessages(String s) {
        MessageInformation =s;
    }

    public String getMessageInfos() {
        return MessageInformation;
    }
}
