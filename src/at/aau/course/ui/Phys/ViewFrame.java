package at.aau.course.ui.Phys;

import at.aau.course.VectorData;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import physics_model.PhysicsModel;

class ViewFrame extends JFrame {

    final VectorData[] vectorData;
    final File inputDir;
    final PhysicsModel physicsModel;
    final int width;
    final int height;

     public ViewFrame(VectorData[] vectorData, File inputDir, PhysicsModel physicsModel) {
        this.vectorData = vectorData;
        this.inputDir = inputDir;
        this.physicsModel = physicsModel;
        this.width = physicsModel.getWidthBound();
        this.height = physicsModel.getHeightBound();
    }


    public void build(Color background, int objectCount) throws IOException {
        setSize(width, height);
        setContentPane(new CanvasPanel(this.physicsModel,background, objectCount, vectorData, inputDir));
    }
}
