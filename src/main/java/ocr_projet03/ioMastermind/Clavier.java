package ocr_projet03.ioMastermind;

import ocr_projet03.exceptionMastermind.ExceptionMastermind;
import ocr_projet03.messagesTexteMastermind.ErreurMessages;

import java.io.IOException;

public class Clavier {
    Clavier(Integer sizeBuff) throws ExceptionMastermind {
        if (sizeBuff>0) {
            bufLoc = new byte[Integer.min(sizeBuff, 128)];

        }
        else {
            bufLoc = null;
            ExceptionMastermind e = new ExceptionMastermind(ErreurMessages.ErreurGeneric);
            e.setStackTrace(e.getStackTrace());
            throw e;
        }
    }
    //
    String ReadLigne() throws ExceptionMastermind {

        StringBuilder xLolo = new StringBuilder();
        try {
            if (System.in.read(bufLoc) > 0) {
                for (int index=0; index< bufLoc.length && bufLoc[index] !='\n' && bufLoc[index] != (byte)0;index++) {
                    xLolo.append((char) bufLoc[index]);
                }
            }
        } catch (IOException err) {
            throw new ExceptionMastermind(err);
        }
        return new String(xLolo) ;
    }
    //
    int ReadInt() throws ExceptionMastermind {
        int n ;
        try {
            n = Integer.parseInt(ReadLigne());
        } catch (NumberFormatException err) {
            throw new ExceptionMastermind(err);
        }
        return n;
    }
    //

    private byte[]  bufLoc;
}
