package mastermind.parametrageApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static mastermind.logMastermind.logApplicatif.logger;
import static mastermind.messagesTexteMastermind.ErreurMessages.EcritureParametresImpossible;
import static mastermind.messagesTexteMastermind.ErreurMessages.ParametrageIllisible;
import static mastermind.messagesTexteMastermind.InfosMessages.CreationFichierParametre;

public abstract class Parametrage {

    private Properties listeParams;
    private String nomFichierParams;

    public Parametrage(String s) {
        listeParams = new Properties();
        nomFichierParams= s;
    }
    public void lireParametre()  {
        try {
            FileInputStream fileInputStream = new FileInputStream(nomFichierParams);
            try {
                listeParams.load(fileInputStream);
            }catch (Exception e1){
                logger.error(ParametrageIllisible +" "+ nomFichierParams);
                getParamDefaut(listeParams);
            }
        }catch (IOException e2) {
            logger.info(CreationFichierParametre +" "+ nomFichierParams);
            getParamDefaut(listeParams);
            ecrireParametre();
        }

    }
    public void ecrireParametre () {
        try {
            FileOutputStream fileOutputStream= new FileOutputStream(nomFichierParams);

                listeParams.store(fileOutputStream,nomFichierParams);

        }catch (IOException e2) {
            logger.error(EcritureParametresImpossible + " "+nomFichierParams );
        }

    }

    public Properties getListeParams() {
        return listeParams;
    }

    public void setListeParams(Properties listeParams) {
        this.listeParams = listeParams;
    }

    abstract  void getParamDefaut (Properties listeParams) ;

}
