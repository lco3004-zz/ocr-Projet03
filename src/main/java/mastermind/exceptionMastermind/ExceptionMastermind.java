package mastermind.exceptionMastermind;

import mastermind.messagesTexteMastermind.ErreurMessages;
import mastermind.logMastermind.TracesLog;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionMastermind extends Exception {

    public ExceptionMastermind(IOException e) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        TracesLog.getInstance().LogaMoi(errMsg.getMessageDuCodeErreur());
    }

    public ExceptionMastermind(ErreurMessages err) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        TracesLog.getInstance().LogaMoi(errMsg.getMessageDuCodeErreur());
    }

    public ExceptionMastermind(FileNotFoundException err) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        TracesLog.getInstance().LogaMoi(errMsg.getMessageDuCodeErreur());
    }


    public ExceptionMastermind(Exception e) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        TracesLog.getInstance().LogaMoi(errMsg.getMessageDuCodeErreur());

    }


}
