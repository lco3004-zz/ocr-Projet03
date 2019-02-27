package mastermind.exceptionMastermind;

import mastermind.messagesTexteMastermind.ErreurMessages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionMastermind extends Exception {

    private static Logger LOGGER = LogManager.getLogger(ExceptionMastermind.class.getName());

    public ExceptionMastermind(IOException e) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        LOGGER.error(errMsg.getMessageErreur());

    }

    public ExceptionMastermind(ErreurMessages err) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        LOGGER.error(errMsg.getMessageErreur());
    }

    public ExceptionMastermind(FileNotFoundException err) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        LOGGER.error(errMsg.getMessageErreur());
    }


    public ExceptionMastermind(Exception e) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        LOGGER.error(errMsg.getMessageErreur());

    }


}
