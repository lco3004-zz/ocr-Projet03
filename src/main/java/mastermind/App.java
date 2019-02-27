package mastermind;

import mastermind.constantesMastermind.CouleursMastermind;
import mastermind.exceptionMastermind.ExceptionMastermind;
import mastermind.messagesTexteMastermind.InfosMessages;

import mastermind.messagesTexteMastermind.InfosMessages;
import mastermind.partieMastermind.ChoixCodeSecret;

import java.util.ArrayList;

import static mastermind.logMastermind.logApplicatif.getInstance;
import static mastermind.logMastermind.logApplicatif.logger;

/**
 * Hello world!
 *
 */

public class App 
{
    public static void main( String[] args )
    {
        try {
            //Creation du Singleton qui gere les logs (log4j2)
            //renseigne le nom de la classe appelante , ici App.
            getInstance(App.class.getSimpleName());

            logger.info(InfosMessages.Lancement_Application);

            ChoixCodeSecret choixCodeSecret = new ChoixCodeSecret();
            CouleursMastermind [] toutes = CouleursMastermind.values();

            ArrayList <Byte> arrayList = choixCodeSecret.getCodeSecret();
            CouleursMastermind[] ligneATrouver  = choixCodeSecret.getLigneSecrete();


            logger.info(InfosMessages.FinNormale_Applicaiton);
        }
        catch (ExceptionMastermind e) {
            logger.fatal(e);
            System.exit(-1);
        }
    }
}
