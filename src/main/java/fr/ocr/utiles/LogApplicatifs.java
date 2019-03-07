package fr.ocr.utiles;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * pour verificaiton commit github (a disparudu repo) 03/03/02019
 */
public class LogApplicatifs {
    private static LogApplicatifs ourInstance = null;

    public static Logger logger;

    public static LogApplicatifs getInstance(String nomAppelant) {
        if (ourInstance==null)
            ourInstance = new LogApplicatifs(nomAppelant);
        return ourInstance;
    }

    private LogApplicatifs(String nomAppelant) {
        logger = LogManager.getRootLogger();
    }

}
