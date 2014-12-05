package at.aau.course.ui.Phys;

import at.aau.course.VectorData;
import at.aau.course.distance.LpNorm;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JPanel;
import physics_model.PhysicsModel;

class CanvasPanel extends JPanel {

    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Image[] icons;
    private Color background;
    //private Point[] objects;
    private Point[] deltas;

    PhysicsModel physicsModel;
    private Point[] coordinates;
    int renderedImageCount = 100;
    
    int recomputed = 1;

    public CanvasPanel(PhysicsModel physicsModel, Color background, int objectCount, VectorData[] vectorData, File inputDir) throws IOException {

        this.physicsModel = physicsModel;
        this.coordinates = this.physicsModel.computeCoordinates(new LpNorm(2.0), vectorData);

        icons = new Image[vectorData.length];

        for (int i = 0; i < vectorData.length; i++) {
            VectorData vectorData1 = vectorData[i];

            File imageFile = new File(inputDir, String.valueOf(vectorData1.getClassId()) + System.getProperty("file.separator") + vectorData1.getFileName());

            if (!imageFile.exists()) {
                System.out.println("Does not exists!" + new File(inputDir, vectorData1.getFileName()).getCanonicalPath());
                i--;
                continue;
            }

            icons[i] = tk.getImage(imageFile.getCanonicalPath());
        }

        this.background = background;
        //objects = new Point[objectCount];
        deltas = new Point[objectCount];

        //Choose random coordinate (in 500x500 panel) and motions
//        for (int i = 0; i < objectCount; i++) {
//            objects[i] = new Point((int) (Math.random() * 300 + 100),
//                    (int) (Math.random() * 300 + 100));
//            deltas[i] = new Point((int) (Math.random() * 5 - 3),
//                    (int) (Math.random() * 5 - 3));
//        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        setBackground(background);
        
        System.out.println("Coord before" + Arrays.toString(coordinates));
        if( recomputed <= 100 ){
            this.coordinates = this.physicsModel.recomputeCoordinates(this.coordinates);
            recomputed++;
        }
        System.out.println("Coord after" + Arrays.toString(coordinates));         
        
        for (int i = 0; (i < coordinates.length && i < this.renderedImageCount); i++) {
            Point updatedPoint = coordinates[i];
            //g.drawImage(icons[i], updatedPoint.x, updatedPoint.y, icons[i].getWidth(null), icons[i].getHeight(null),  null);
            g.drawImage(icons[i], updatedPoint.x, updatedPoint.y, 192/4, 144/4, null);
        }
        
       

//        for (int i = 0; i < objects.length; i++) {
//            g.drawImage(icons[0], objects[i].x, objects[i].y, 50, 50, null);
//            objects[i].x += deltas[i].x;
//            objects[i].y += deltas[i].y;
//        }
    }

}
