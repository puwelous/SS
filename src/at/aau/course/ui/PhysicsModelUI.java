/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.aau.course.ui;

import at.aau.course.VectorData;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author PC
 */
class PhysicsModelUI {

    public PhysicsModelUI(Point[] coordinates, VectorData[] objectsPerFeature, File inputDir) {

        JFrame gui = new JFrame("RoundTableWorld");

        Container contentPane = gui.getContentPane();
        contentPane.setLayout(null);
        for (int i = 0; i < coordinates.length; i++) {
            VectorData vectorData = objectsPerFeature[i];
            JLabel img = new JLabel(new ImageIcon(new File(inputDir, vectorData.getFileName()).getAbsolutePath()));
            System.out.println("Added");
            img.setSize(100, 100);
            img.setBounds(50, 50, 48, 36); // x, y, width, height
            contentPane.add(img);
            if (i == 0) {
                break;
            }
        }

        System.out.println("Comp counts:" + contentPane.getComponentCount());
        System.out.println(contentPane.getComponent(0).isVisible());

        gui.setSize(640, 480);
        gui.repaint();
        //gui.setLocation(0, 0);
        gui.setVisible(true);
        
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
