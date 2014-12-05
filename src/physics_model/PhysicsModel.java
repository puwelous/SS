package physics_model;

import at.aau.course.VectorData;
import at.aau.course.distance.IDistance;
import java.awt.Point;

public class PhysicsModel {

    private static final int INITIAL_ITERATION_COUNT = 1;
    private static final double REPULSION_FORCE = 1.005;
    private static final double ATTRACTION_FORCE = 1.5;

    double[][] distanceMatrix;
    
    int widthBound;
    int heightBound;

    public PhysicsModel() {
    }

    /**
     * Computing initial coordinates
     *
     * @param distance
     * @param objects
     * @return
     */
    public Point[] computeCoordinates(IDistance distance, VectorData[] objects) {

        //double[][] distanceMatrix;
        final double threshold;
        final double maximum;

        this.distanceMatrix = computeMatrix(distance, objects);
        maximum = findMaximum(distanceMatrix);
        threshold = (maximum * 0.2); // 50%
        distanceMatrix = pruneMatrix(distanceMatrix, threshold);
        
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                System.out.format("%7.1f", distanceMatrix[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
        
        Point[] coordinates = new Point[objects.length];

        coordinates = initCoordinatesRandomly(coordinates, 800, 600);
        //coordinates = initCoordinatesAlongCanvasCenter(coordinates, 800, 600);
        System.out.println(java.util.Arrays.toString(coordinates));

//        for (int iterations = 0; iterations < INITIAL_ITERATION_COUNT; iterations++) {
//
//            recomputeCoordinates(coordinates);
//            for (int i = 0; i < coordinates.length; i++) {
//                for (int j = i + 1; j < coordinates.length; j++) {
//
//                    double[] vectorV = computeVector(coordinates[i], coordinates[j]);
//
//                    repulse(coordinates[i], coordinates[j], vectorV, REPULSION_FORCE);
//
//                    if (distanceMatrix[i][j] > 0.0) {
//                        attract(coordinates[i], coordinates[j], vectorV, ATTRACTION_FORCE);
//                    }
//                }
//            }
//        }
//
//        System.out.println(java.util.Arrays.toString(coordinates));
        //System.exit(-1);
        return coordinates;
    }

    public Point[] recomputeCoordinates(Point[] coordinates) {
        int showAmount = 5; //return coordinates;

        for (int i = 0; i < coordinates.length; i++) {
            for (int j = i + 1; j < coordinates.length; j++) {

//                if (showAmount > 0) {
//                    System.out.println(coordinates[i] + " " + coordinates[j]);
//                }

                double[] vectorV = computeVector(coordinates[i], coordinates[j]);
                double[] unitVector = new double[2];
                unitVector[0] = vectorV[0] / vectorV[2];
                unitVector[1] = vectorV[1] / vectorV[2];

                repulse(coordinates[i], coordinates[j], unitVector, REPULSION_FORCE);

                if (distanceMatrix[i][j] > 0.0) {
                    attract(coordinates[i], coordinates[j], unitVector, ATTRACTION_FORCE);
                }

//                if (showAmount > 0) {
//                    System.out.println(coordinates[i] + " " + coordinates[j]);
//                    showAmount--;
//                }
            }
        }
        return coordinates;
    }

    private double[][] computeMatrix(IDistance iDistance, VectorData[] objects) {

        double[][] aa_distanceMatrix = new double[objects.length][objects.length];

        for (int i = 0; i < aa_distanceMatrix.length; i++) {
            for (int j = i + 1; j < aa_distanceMatrix[i].length; j++) {
                aa_distanceMatrix[i][j] = iDistance.compute(objects[i], objects[j]);
            }
        }

        return aa_distanceMatrix;
    }

    private double findMaximum(double[][] distanceMatrix) {

        double max = 0.0;

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = i + 1; j < distanceMatrix[i].length; j++) {
                if (max < distanceMatrix[i][j]) {
                    max = distanceMatrix[i][j];
                }
            }
        }

        return max;
    }

    private double[][] pruneMatrix(double[][] distanceMatrix, double threshold) {
        int prunedCount = 0;
        int totalObjectsCount = 0;

        System.out.println("Threshold is = " + threshold);

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = i + 1; j < distanceMatrix[i].length; j++) {
                if (threshold >= distanceMatrix[i][j]) {
                    distanceMatrix[i][j] = 0.0;
                    prunedCount++;
                }
                totalObjectsCount++;
            }
        }

        System.out.println("Total: " + totalObjectsCount + " objects..");
        totalObjectsCount++;
        System.out.println("Pruned: " + prunedCount + " objects..");

        return distanceMatrix;
    }

    private double[] computeVector(Point pointA, Point pointB) {
        double[] vectorV = new double[3];

        vectorV[0] = pointB.x - pointA.x; // "v.x"
        vectorV[1] = pointB.y - pointA.y; // "v.y"
        vectorV[2] = Math.sqrt(Math.pow(vectorV[0], 2) + Math.pow(vectorV[1], 2));

        return vectorV;
    }

    private void repulse(Point pointA, Point pointB, double[] unitVector, double repulsionForce) {

//        System.out.println("Bef: " +pointA + " " + pointB);
//        System.out.println("Bef1: " +vectorV[0] + " " + repulsionForce);
//        System.out.println("Bef2: " + (int) (vectorV[0] * repulsionForce));
        pointA.x = pointA.x + (int) (unitVector[0] * repulsionForce);
        pointA.y = pointA.y + (int) (unitVector[1] * repulsionForce);

        pointB.x = pointB.x + (int) (unitVector[0] * repulsionForce);
        pointB.y = pointB.y + (int) (unitVector[1] * repulsionForce);

//        System.out.println("Aft: " +pointA + " " + pointB);
//         System.out.println();
    }

    private void attract(Point pointA, Point pointB, double[] unitVector, double attractionForce) {
        pointA.x = pointA.x - (int) (unitVector[0] * attractionForce);
        pointA.y = pointA.y - (int) (unitVector[1] * attractionForce);

        pointB.x = pointB.x - (int) (unitVector[0] * attractionForce);
        pointB.y = pointB.y - (int) (unitVector[1] * attractionForce);
    }

    private Point[] initCoordinatesRandomly(Point[] coordinates, final int boardSizeWidth, final int boardSizeHeight) {

        for (int i = 0; i < coordinates.length; i++) {

            coordinates[i] = new Point(
                    (int) (Math.random() * boardSizeWidth),
                    (int) (Math.random() * boardSizeHeight));
        }

        return coordinates;
    }

    private Point[] initCoordinatesAlongCanvasCenter(Point[] coordinates, final int boardSizeWidth, final int boardSizeHeight) {

        final Point centrePoint = new Point((int) boardSizeWidth / 2, (int) boardSizeHeight / 2);
        Point pointCloseToCentrePoint = null;

        for (int i = 0; i < coordinates.length; i++) {

            pointCloseToCentrePoint = new Point(centrePoint);

            pointCloseToCentrePoint.x += (int) (Math.random() * 100) - 50;
            pointCloseToCentrePoint.y += (int) (Math.random() * 100) - 50;

            coordinates[i] = new Point(pointCloseToCentrePoint);
        }

        return coordinates;
    }
    
    
    public void setWidthBound(int widthBound) {
        this.widthBound = widthBound;
    }

    public void setHeightBound(int heightBound) {
        this.heightBound = heightBound;
    }    
    
    double[][] getDistanceMatrix() {
        return distanceMatrix;
    }
}
