package mastermind;

import mastermind.constantesMastermind.CouleursMastermind;
import mastermind.exceptionMastermind.ExceptionMastermind;
import mastermind.partieMastermind.ChoixCodeSecret;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


/**
 * Hello world!
 *
 */

public class App 
{


    private static Logger logger = LogManager.getLogger(App.class.getName());

    public static void main( String[] args )
    {
        try {
            ChoixCodeSecret choixCodeSecret = new ChoixCodeSecret();

            ArrayList <Byte> arrayList = choixCodeSecret.getCodeSecret();
            CouleursMastermind[] ligneATrouver  = choixCodeSecret.getLigneSecrete();

            System.out.println();
            System.out.println();
            System.out.println("********************************");
            System.out.println();
            System.out.print("** Ligne Secrete = ");
            for (CouleursMastermind v: ligneATrouver) {
                System.out.print(" "+v.toString());
            }
            System.out.println();
            System.out.println();
            System.out.println("********************************");
            System.out.println();
            System.out.print("**Toutes les Couleurs du Jeu = ");
            CouleursMastermind [] toutes = CouleursMastermind.values();
            for (CouleursMastermind v: toutes) {
                System.out.print(" "+v.toString());
            }
            System.out.println();
            System.out.println("********************************");
            System.out.println();
            logger.debug("Debug Message Logged !!!");
            logger.info("Info Message Logged !!!");
            logger.error("Error Message Logged !!!", new NullPointerException("NullError"));
            if (logger.isDebugEnabled()) {
               logger.debug("Logging in user {} with birthday {}", "moi", "30/04/1959");
            }


        }
        catch (ExceptionMastermind e) {
            System.exit(1);
        }
    }
}
