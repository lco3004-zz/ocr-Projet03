package ocr_projet03.messagesTexteOcr_Projet03;

public enum DebugMessages {
    X("");
    private String MessageDebug;
    DebugMessages(String s) {
        MessageDebug =s;
    }

    public String getMessageDebug() {
        return MessageDebug;
    }
}
