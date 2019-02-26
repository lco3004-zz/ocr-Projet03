package mastermind.ioMastermind;

import mastermind.constantesMastermind.ModeIO;
import mastermind.exceptionMastermind.ExceptionMastermind;
import mastermind.messagesTexteMastermind.ErreurMessages;

import java.io.*;

public class FluxEtFichiers {
    //
    private DataOutputStream streamOutputFile;
    private DataInputStream streamInputFile;
    private String nomFichier;

    //
    FluxEtFichiers (ModeIO mode, String fileName) throws Exception {
        try {
            nomFichier = fileName;
            if (mode == ModeIO.READFILE) {
                openDataInputStream(fileName);
            }
            else if (mode==ModeIO.WRITEFILE)
                openDataOutputStream(fileName);
            else
                throw new ExceptionMastermind(ErreurMessages.ModeIOInconnu);
        }
        catch ( FileNotFoundException e) {
            throw new ExceptionMastermind(e);
        }
        catch (Exception e) {
            throw new ExceptionMastermind(e);
        }
    }
    //
    FluxEtFichiers (ModeIO mode) throws Exception {
        try {
            if (mode==ModeIO.WRITEFILE) {
                System.out.print("Nom du fichier :");
                String fileName = (new Clavier(128)).ReadLigne();
                System.out.println();
                nomFichier = fileName;
                openDataOutputStream(fileName);
            }
            else
                throw new ExceptionMastermind(ErreurMessages.ModeIOInconnu);

        }
        catch (Exception e) {
            throw new ExceptionMastermind(e);
        }

    }
    //
    private void openDataInputStream(String fileName) throws Exception {
        try {
            streamInputFile = new DataInputStream(new FileInputStream(fileName));
        }
        catch ( FileNotFoundException e) {
            throw new ExceptionMastermind(e);
        }
        catch (Exception e) {
            throw new ExceptionMastermind(e);
        }
    }
    //
    private void openDataOutputStream(String fileName) throws Exception {
        try {
            streamOutputFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)) );
        }
        catch ( FileNotFoundException e) {
            throw new ExceptionMastermind(e);
        }
        catch (Exception e) {
            throw new ExceptionMastermind(e);
        }
    }
    void testFichierLecture() throws Exception {
        try {
            String ligne ="";
            boolean eof=false;
            while (!eof ) {
                try {
                    ligne +=  String.valueOf((char)streamInputFile.readByte());
                }
                catch (EOFException z) {
                    eof=true;
                }
            }
            System.out.println("Ligne Lue ="+ligne+ " depuis fichier = " +nomFichier);
            streamInputFile.close();
        }
        catch (IOException x) {
            throw new ExceptionMastermind(x);
        }
    }
    void testFichierEcritureEntier(int codeDeFin) throws Exception {
        try {
            Clavier inputLigne = new  Clavier(256);
            int n;
            do {

                System.out.print("Une entier ? (fin="+codeDeFin+") -> ");
                if ((n = inputLigne.ReadInt()) != codeDeFin)
                    streamOutputFile.write(n);
                System.out.println();
            } while (n != codeDeFin) ;
            System.out.println();
            streamOutputFile.close();
        }
        catch ( NumberFormatException e) {
            streamOutputFile.close();
            throw new ExceptionMastermind(e);
        }
        catch (IOException x) {
            throw new ExceptionMastermind(x);
        }
    }
}
