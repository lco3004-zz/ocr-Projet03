package ocr_projet03.modeConsole;

import java.io.IOException;

final  class ClearScreen {
      static  void cls() throws IOException, InterruptedException {

         try
         {
             final String os = System.getProperty("os.name");

             if (os.contains("Windows"))
                 new ProcessBuilder("cmd", "/c", "cmd.exe /c cls").inheritIO().start().waitFor();
             else
                 Runtime.getRuntime().exec("clear");
         }
         catch (  InterruptedException | IOException e)
         {
             System.out.println(e.getMessage());
         }

    }
}
