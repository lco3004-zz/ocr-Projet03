package ocr_projet03.exceptionOcr_Projet03;

import ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages;
import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;

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
