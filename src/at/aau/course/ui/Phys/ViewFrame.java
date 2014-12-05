package at.aau.course.ui.Phys;

import at.aau.course.VectorData;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import physics_model.PhysicsModel;

class ViewFrame extends JFrame {

    VectorData[] vectorData;
    File inputDir;
    PhysicsModel physicsModel;

    public void setVectorData(VectorData[] vectorData) {
        this.vectorData = vectorData;
    }

    public void setInputDir(File inputDir) {
        this.inputDir = inputDir;
    }

    void setPhysicsModel(PhysicsModel physicsModel) {
        this.physicsModel = physicsModel;
    }

    public void build(Color background, int objectCount) throws IOException {
        setSize(800, 600);
        setContentPane(new CanvasPanel(this.physicsModel,background, objectCount, vectorData, inputDir));
    }
}
