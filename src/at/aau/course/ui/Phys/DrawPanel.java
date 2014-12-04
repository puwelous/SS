//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
//
// Program        : JPanel Image Demo
//
// Author         : Richard E. Pattis
//                  Computer Science Department
//                  Carnegie Mellon University
//                  5000 Forbes Avenue
//                  Pittsburgh, PA 15213-3891
//                  e-mail: pattis@cs.cmu.edu
//
// Maintainer     : Author
//
//
// Description:
//
//   A simple program that illustrates how to use javax.swing.JPanel
// for images (hot dogs). I've simplified the JFrame to the bare minimum.
// The images are redrawn (at a specific size) whenever the user presses
// enter in the console window.
//
// Things to try:
//   Draw different images (e.g., ".gif" files) at different sizes    
//
// 
// 
// 
// Known Bugs     : None
//
// Future Plans   : None
//
// Program History:
//    11/21/04: R. Pattis - Operational for 15-200
//
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////


package at.aau.course.ui.Phys;


import java.awt.Point;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;

import java.io.File;

import javax.swing.JPanel;


class DrawPanel extends JPanel {

  private Toolkit tk   = Toolkit.getDefaultToolkit(); 
  private Image   icon;
  private Color   background;
  private Point[] objects;
  private Point[] deltas;
  
  public DrawPanel (Color background,int objectCount)
  {
  	try {
  	  icon = tk.getImage((new File("C:\\Users\\PC\\Documents\\NetBeansProjects\\SimilaritySearch\\aloi_big\\105\\105_r30.png")).getCanonicalPath());
  	  } catch (Exception IO) {}
     this.background = background;
     objects = new Point[objectCount];
     deltas  = new Point[objectCount];
     
     //Choose random coordinate (in 500x500 panel) and motions
     for (int i=0; i<objectCount; i++) {
       objects[i] = new Point( (int)(Math.random()*300+100),
                               (int)(Math.random()*300+100));
       deltas [i] = new Point( (int)(Math.random()*5-3),
                               (int)(Math.random()*5-3));
     } 
  }
  
  public void paint (Graphics g)
  {
    super.paint(g);
    
    setBackground(background);
    
    //Display all the hot dogs and then update their coordinates
    ///  for the next display
    for (int i=0; i<objects.length; i++) {
      //g.fillRect(objects[i].x,objects[i].y,50,50);
      g.drawImage(icon,objects[i].x,objects[i].y,50,50,null);
      objects[i].x += deltas[i].x;
      objects[i].y += deltas[i].y;
    }
  }

}
