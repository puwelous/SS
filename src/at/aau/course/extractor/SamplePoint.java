package at.aau.course.extractor;

import at.aau.course.Converter;
import java.awt.Color;

import at.aau.course.distance.IDistance;
import at.aau.course.distance.LpNorm;

public class SamplePoint {

    double weight;

    // dimensions x, y, L, a, b
    double[] dimensions;

    public SamplePoint(double x, double y, Color c) {
        weight = 0.0;
        dimensions = new double[5];
        this.initialize(x, y, c);
    }

    private void initialize(double x, double y, Color c) {

        // convert RGB to Lab
        double[] RGBinLab = Converter.RGBToLab(new double[]{(double) c.getRed(), (double) c.getGreen(), (double) c.getBlue()});

        // initialize values
        dimensions[0] = x;
        dimensions[1] = y;
        dimensions[2] = RGBinLab[0];
        dimensions[3] = RGBinLab[1];
        dimensions[4] = RGBinLab[2];
    }

    public double L2Distance(SamplePoint comparedSp) {

        double L2Distance = 0.0;

        IDistance iDistance = new LpNorm(2); // Euclidian distance

        L2Distance = iDistance.compute(getX(), comparedSp.getX());

        L2Distance += iDistance.compute(getY(), comparedSp.getY());

        return L2Distance;
    }

    public double getX() {
        return dimensions[0];
    }

    public double getY() {
        return dimensions[1];
    }
    
    public double getL(){
        return dimensions[2];
    }
    
    public double getA(){
        return dimensions[3];
    }
    
    public double getB(){
        return dimensions[4];
    }    

    public static void main(String[] args) {

        Color commonColor = new Color(152, 211, 65);

        SamplePoint sp1 = new SamplePoint(100, 100, commonColor);
        SamplePoint sp2 = new SamplePoint(-100, -100, commonColor);

        System.out.println(sp1.L2Distance(sp2));
    }
}
