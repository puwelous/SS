package at.aau.course.physics_model;

import at.aau.course.VectorData;
import at.aau.course.distance.IDistance;
import java.awt.Point;

public class PhysicsModel {

    private static final int INITIAL_ITERATION_COUNT = 1000;
    private static final double REPULSION_FORCE = 1.5;
    private static final double ATTRACTION_FORCE = 1000;

    double[][] distanceMatrix;

    int widthBound;
    int heightBound;
    double maximum;
    double threshold;

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
        //threshold;

        this.distanceMatrix = computeMatrix(distance, objects);
        this.maximum = findMaximum(distanceMatrix);
        threshold = (maximum * 0.5); // 10%
        distanceMatrix = pruneMatrix(distanceMatrix, threshold);

//        for (int i = 0; i < 100; i++) {
//            for (int j = 0; j < 100; j++) {
//                System.out.format("%7.1f", distanceMatrix[i][j]);
//                System.out.print(" ");
//            }
//            System.out.println();
//        }
//        System.out.println();
        Point[] coordinates = new Point[objects.length];

        coordinates = initCoordinatesRandomly(coordinates, widthBound, heightBound);
        //coordinates = initCoordinatesAlongCanvasCenter(coordinates, widthBound, heightBound);
        System.out.println(java.util.Arrays.toString(coordinates));

        for (int iterations = 0; iterations < INITIAL_ITERATION_COUNT; iterations++) {

            coordinates = recomputeCoordinates(coordinates);
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
        }
//
//        System.out.println(java.util.Arrays.toString(coordinates));
        //System.exit(-1);
        return coordinates;
    }

    public Point[] recomputeCoordinates(Point[] coordinates) {
        int showAmount = 5; //return coordinates;

        for (int i = 0; i < coordinates.length; i++) {
            for (int j = i + 1; j < coordinates.length; j++) {

                double[] vectorV = computeVector(coordinates[i], coordinates[j]);
                double[] unitVector = new double[2];
                unitVector[0] = vectorV[0] / vectorV[2];
                unitVector[1] = vectorV[1] / vectorV[2];

                repulse(coordinates[i], coordinates[j], unitVector, REPULSION_FORCE);

                if (distanceMatrix[i][j] > threshold) {
                    attract(coordinates[i], coordinates[j], unitVector, (distanceMatrix[i][j] / maximum)*ATTRACTION_FORCE);
                }
            }
        }

        coordinates = optimizeCoordinatesToFitScreen(coordinates, this.widthBound, this.heightBound);

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

    public int getWidthBound() {
        return widthBound;
    }

    public int getHeightBound() {
        return heightBound;
    }

    double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    private Point[] optimizeCoordinatesToFitScreen(Point[] coordinates, final int screenWidth, final int screenHeight) {

        // maximum distances between the centre of a screen and Points
        int Xmax = 0;
        int Ymax = 0;

        // temp distances
        int absDistanceX = 0;
        int absDistanceY = 0;

        // ratios for X and Y scale
        double Xratio;
        double Yratio;

        final Point centreOfTheScreen = new Point((int) screenWidth / 2, (int) screenHeight / 2);

        //System.out.println(centreOfTheScreen);
        for (final Point point : coordinates) {

            if (point.x > screenWidth) {
                absDistanceX = point.x - centreOfTheScreen.x;
            } else if (point.x < 0) {
                absDistanceX = centreOfTheScreen.x - point.x; // centreOfTheScreen.x + |point.x|; 400 - (-20)
            }

            if (point.y > screenHeight) {
                absDistanceY = point.y - centreOfTheScreen.y;
            } else if (point.y < 0) {
                absDistanceY = centreOfTheScreen.y - point.y; // centreOfTheScreen.y + |point.y|; 400 - (-20)
            }

            //absDistanceX = Math.abs(Math.abs(point.x) - centreOfTheScreen.x);
            //absDistanceY = Math.abs(Math.abs(point.y) - centreOfTheScreen.y);
            if (absDistanceX > Xmax) {
                Xmax = absDistanceX;
            }
            if (absDistanceY > Ymax) {
                Ymax = absDistanceY;
            }
        }

        // return if max distances do not exceed the screen size
        if ((Xmax < (int) screenWidth / 2) || (Ymax < (int) screenHeight / 2)) {
            return coordinates;
        }

        // compute ratio
        Xratio = ((double) screenWidth / 2) / Xmax;
        Yratio = ((double) screenHeight / 2) / Ymax;

        // use ratios to shorten distances
        for (Point point : coordinates) {
            point.x = (point.x > centreOfTheScreen.x ? centreOfTheScreen.x - (int) (point.x * Xratio) : centreOfTheScreen.x + (int) (point.x * Xratio));
            point.y = (point.y > centreOfTheScreen.y ? centreOfTheScreen.y - (int) (point.y * Yratio) : centreOfTheScreen.y + (int) (point.y * Yratio));

            if (point.x > (int) screenWidth || point.y > (int) screenHeight) {
                System.out.println("Crazy!");
                System.out.println("screenWidth!" + screenWidth);
                System.out.println("screenHeight!" + screenHeight);
                System.out.println("Xmax!" + Xmax);
                System.out.println("Ymax!" + Ymax);
                System.out.println("Xratio!" + Xratio);
                System.out.println("Yratio!" + Yratio);
                System.exit(-1);
            }

            // corners
            if (point.x + 192 / 8 > screenWidth) {
                point.x = point.x - (192 / 8);
            }
            if (point.y + 144/8 > screenHeight) {
                point.y = point.y - (144 / 8);
            }
        }

        return coordinates;
    }
}
