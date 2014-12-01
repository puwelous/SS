package at.aau.course.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RankedResultsUI extends javax.swing.JFrame {

    public RankedResultsUI(File queryObject, File[] rankedResultsAsFiles) throws IOException {
        initComponents();

        int rankedResultCount = 10;

        for (File rankedResult : rankedResultsAsFiles) {

            if (rankedResultCount <= 0) {
                break;
            }

            if (!rankedResult.exists()) {
                continue;
            }

            System.out.println("Rendering file " + rankedResult.getAbsolutePath());

            BufferedImage myPicture = ImageIO.read(rankedResult);
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            add(picLabel);

            rankedResultCount--;
        }

        this.validate();
        this.repaint();
        this.setSize(800, 600);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ranked Results");
        getContentPane().setLayout(new java.awt.GridLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
