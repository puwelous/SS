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


public class Application implements Runnable
{
    public Application(){}
    
    @Override
   public void run()
   {
     View view = new View();
     view.build(Color.white,15);
     view.setVisible(true);
     
     //Kluge; time to get images read/rendered
     try {
       Thread.sleep(300);
       view.repaint();
     } catch (InterruptedException ie) {}
      
     for(;;) {
       //Prompt.forString("Press enter to move (hold down for animation)");
       view.repaint();
     }
     
   }


}
