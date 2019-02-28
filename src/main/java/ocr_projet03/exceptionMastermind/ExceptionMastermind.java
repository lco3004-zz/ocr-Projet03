package ocr_projet03.exceptionMastermind;

import ocr_projet03.messagesTexteMastermind.ErreurMessages;
import static ocr_projet03.logMastermind.logApplicatif.logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionMastermind extends Exception {

    public ExceptionMastermind(IOException e) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        logger.error(errMsg.getMessageErreur());

    }

    public ExceptionMastermind(ErreurMessages err) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        logger.error(errMsg.getMessageErreur());
    }

    public ExceptionMastermind(FileNotFoundException err) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        logger.error(errMsg.getMessageErreur());
    }


    public ExceptionMastermind(Exception e) {
        ErreurMessages errMsg = ErreurMessages.ErreurGeneric;
        logger.error(errMsg.getMessageErreur());

    }


}
