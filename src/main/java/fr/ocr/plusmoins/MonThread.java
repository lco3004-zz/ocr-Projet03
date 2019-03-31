package fr.ocr.plusmoins;

import javax.swing.*;

public class MonThread extends Thread {
    String nomDuThread;
    public MonThread(String nomThread) {
        super(nomThread);
        nomDuThread= nomThread;
    }
    public void run() {
        try {
            MaFenetre maFenetre = new MaFenetre(nomDuThread);
            while (maFenetre.isActive()) {
                wait(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
  class MaFenetre extends JFrame {

    public MaFenetre(String titreFenetre, int tailleX, int tailleY, boolean isVisible) {
        super();
        this.setTitle(titreFenetre);
        this.setSize(tailleX,tailleY);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(isVisible);
    }

    public MaFenetre(String nomThread) {
          this(nomThread, 400,500,true);
    }


    public MaFenetre() {
          this("?", 400,500,true);
    }
}