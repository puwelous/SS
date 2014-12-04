package physics_model;

import at.aau.course.VectorData;
import at.aau.course.distance.IDistance;
import java.awt.Point;

public class PhysicsModel {

    private static final int ITERATION_COUNT = 10;
    private static final double REPULSION_FORCE = 1.1;
    private static final double ATTRACTION_FORCE = 1.1;

    double[][] distanceMatrix;

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }
    
    public PhysicsModel() {
    }

    public Point[] computeCoordinates(IDistance distance, VectorData[] objects) {

        double[][] distanceMatrix;
        final double threshold;

        distanceMatrix = computeMatrix(distance, objects);
        threshold = (findMaximum(distanceMatrix) * 0.5); // 50%
        distanceMatrix = pruneMatrix(distanceMatrix, threshold);

        Point[] coordinates = new Point[objects.length];

        coordinates = initCoordinates(coordinates);

        for (int iterations = 0; iterations < ITERATION_COUNT; iterations++) {

            for (int i = 0; i < coordinates.length; i++) {
                for (int j = i + 1; j < coordinates.length; j++) {

                    double[] vectorV = computeVector(coordinates[i], coordinates[j]);

                    repulse(coordinates[i], coordinates[j], vectorV, REPULSION_FORCE);

                    if (distanceMatrix[i][j] > 0.0) {
                        attract(coordinates[i], coordinates[j], vectorV, ATTRACTION_FORCE);
                    }
                }
            }
        }

        return coordinates;
    }

    private double[][] computeMatrix(IDistance iDistance, VectorData[] objects) {

        double[][] distanceMatrix = new double[objects.length][objects.length];

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = i + 1; j < distanceMatrix[i].length; j++) {
                distanceMatrix[i][j] = iDistance.compute(objects[i], objects[j]);
            }
        }

        return distanceMatrix;
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

        System.out.println("Total: " + totalObjectsCount + " objects..");totalObjectsCount++;
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

    private void repulse(Point pointA, Point pointB, double[] vectorV, double repulsionForce) {

        pointA.x = pointA.x + (int)(vectorV[0] * repulsionForce);
        pointA.y = pointA.y + (int)(vectorV[1] * repulsionForce);
        
        pointB.x = pointB.x + (int)(vectorV[0] * repulsionForce);
        pointB.y = pointB.y + (int)(vectorV[1] * repulsionForce);
    }

    private void attract(Point pointA, Point pointB, double[] vectorV, double attractionForce) {
        pointA.x = pointA.x - (int)(vectorV[0] * attractionForce);
        pointA.y = pointA.y - (int)(vectorV[1] * attractionForce);
        
        pointB.x = pointB.x - (int)(vectorV[0] * attractionForce);
        pointB.y = pointB.y - (int)(vectorV[1] * attractionForce);
    }

    private Point[] initCoordinates(Point[] coordinates) {
        final int boardSizeWidth = 1024;
        final int boardSizeHeight = 1024;

        for (int i = 0; i < coordinates.length; i++) {

            coordinates[i] = new Point(
                    (int) (Math.random() * boardSizeWidth),
                    (int) (Math.random() * boardSizeHeight));
        }

        return coordinates;
    }
}
