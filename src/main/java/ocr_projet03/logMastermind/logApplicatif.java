package ocr_projet03.logMastermind;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class logApplicatif {
    private static logApplicatif ourInstance = null;
    public static Logger logger=null;

    public static logApplicatif getInstance(String nomAppelant) {
        if (ourInstance==null)
            ourInstance = new logApplicatif(nomAppelant);
        return ourInstance;
    }

    private logApplicatif(String nomAppelant) {
        logger = LogManager.getLogger(nomAppelant);
    }

}
