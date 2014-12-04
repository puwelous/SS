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



import java.awt.Color;

import javax.swing.JFrame;


class View extends JFrame {

  public void build (Color background, int objectCount)
  {
    setSize(500,500);
    setContentPane(new DrawPanel(background, objectCount));
  }

}
