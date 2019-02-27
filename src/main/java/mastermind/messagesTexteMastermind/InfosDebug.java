package mastermind.messagesTexteMastermind;

public enum InfosDebug {
    X("");
    private String CodeDebug;
    InfosDebug(String s) {
        CodeDebug=s;
    }

    public String getMessageInfos() {
        return CodeDebug;
    }
}
