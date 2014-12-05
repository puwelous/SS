package at.aau.course.ui.Phys;

import at.aau.course.VectorData;
import java.awt.Color;
import java.io.File;
import physics_model.PhysicsModel;

public class PhysicsModelThread implements Runnable {

    VectorData[] vectorData;
    File inputDir;
    PhysicsModel physicsModel;

    public PhysicsModelThread(PhysicsModel model, VectorData[] vectorData, File inputDir) {
        this.physicsModel = model;
        this.vectorData = vectorData;
        this.inputDir = inputDir;
    }

    @Override
    public void run() {
        ViewFrame view = new ViewFrame(vectorData, inputDir, physicsModel);
        
        try {
            view.build(Color.white, 15);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        view.setVisible(true);

        //Kluge; time to get images read/rendered
        try {
            Thread.sleep(1500);
            //view.repaint();
        } catch (InterruptedException ie) {
        }

        for (;;) {
            //Prompt.forString("Press enter to move (hold down for animation)");
            try {
                Thread.sleep(1500);
                view.repaint();
            } catch (InterruptedException ie) {
            }
        }

    }

}
