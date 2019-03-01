package ocr_projet03.exceptionOcr_Projet03;

import ocr_projet03.messagesTexteOcr_Projet03.ErreurMessages;
import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionMastermind extends Exception {

    public ExceptionMastermind(IOException e) {
        logger.error(e.getMessage());
    }

    public ExceptionMastermind(ErreurMessages err) {
        logger.error(err.getMessageErreur());
    }

    public ExceptionMastermind(FileNotFoundException e) {
        logger.error(e.getMessage());
    }


    public ExceptionMastermind(Exception e) {
        logger.error(e.getMessage());
    }

}
