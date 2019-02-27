package mastermind.logMastermind;

import mastermind.App;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContext;

import javax.security.auth.login.LoginContext;

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

