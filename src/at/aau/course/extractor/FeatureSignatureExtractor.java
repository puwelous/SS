package at.aau.course.extractor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureSignatureExtractor implements IDescriptorWrapper {

    private final String name = getClass().getSimpleName();

    final int NUMBER_OF_POINTS = 2000;
    final int NUMBER_OF_SEEDS = 500;
    final int ITERATION_COUNT = 5;

    int imageWidth;
    int imageHeight;

    SamplePoint[] points;
    SamplePoint[] seeds;

    BufferedImage image;

    double minimalDistance = 10;
    double minimalWeight = 10;

    HashMap<SamplePoint, List<SamplePoint>> pointsAssignedToSeeds;

    //minimal weight = (numberofiteration * 3)
    public FeatureSignatureExtractor() {

    }

    @Override
    public double[] extract(BufferedImage image) {

        double[] result;

        this.image = image;
        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();

        // initialize points
        points = new SamplePoint[NUMBER_OF_POINTS];

        // initialize seeds
        seeds = new SamplePoint[NUMBER_OF_SEEDS];
        
        pointsAssignedToSeeds = new HashMap<>(NUMBER_OF_SEEDS);

        // select points and seeds randomly
        this.randomlySamplePointsAndSeeds();

        for (int i = 0; i < ITERATION_COUNT; i++) {
            setSeedWeightsToZero();
            removeDuplicateSeeds(minimalDistance);// 2.)
            assignPointsToTheirClosestSeed();// 3.)
            removeTooSmallSeeds(minimalWeight);
            recomputeSeedCentersAndSetWeights();
        }

        result = new double[pointsAssignedToSeeds.size() * 6];
        int resultArrayIndex = 0;

        for (Map.Entry<SamplePoint, List<SamplePoint>> entrySet : pointsAssignedToSeeds.entrySet()) {
            SamplePoint seed = entrySet.getKey();
            // w, x, y, L, a, b
            result[resultArrayIndex++] = seed.weight;
            result[resultArrayIndex++] = seed.getX();
            result[resultArrayIndex++] = seed.getY();
            result[resultArrayIndex++] = seed.getL();
            result[resultArrayIndex++] = seed.getA();
            result[resultArrayIndex++] = seed.getB();
        }

        return result;
    }

    /**
     * Note: we do not consider duplicate points or seeds. Probability is small
     * and should have only a little impact on correctness.
     */
    private void randomlySamplePointsAndSeeds() {

        int x, y;
        java.awt.Color c;

        // points generation
        for (int pointsIndex = 0; pointsIndex < NUMBER_OF_POINTS; pointsIndex++) {

            x = (int) (Math.random() * imageWidth);
            y = (int) (Math.random() * imageHeight);

            try {
                c = new java.awt.Color(this.image.getRGB(x, y));
            } catch (Exception e) {
                c = new java.awt.Color(this.image.getRGB(x - 1, y - 1));
            }

            points[pointsIndex] = new SamplePoint(x, y, c);
        }

        // seeds generation
        for (int seedsIndex = 0; seedsIndex < NUMBER_OF_SEEDS; seedsIndex++) {

            x = (int) (Math.random() * imageWidth);
            y = (int) (Math.random() * imageHeight);

            try {
                c = new java.awt.Color(this.image.getRGB(x, y));
            } catch (Exception e) {
                c = new java.awt.Color(this.image.getRGB(x - 1, y - 1));
            }

            seeds[seedsIndex] = new SamplePoint(x, y, c);

            // init mapping from seed to points
            pointsAssignedToSeeds.put(seeds[seedsIndex], new ArrayList<SamplePoint>());
        }
    }

    private void setSeedWeightsToZero() {
        for (SamplePoint seed : seeds) {
            if (seed != null) {
                seed.weight = 0.0;
            }
        }
    }

    private void assignPointsToTheirClosestSeed() {

        SamplePoint closestSeed = null;
        double distanceToClosestSeed = Double.MAX_VALUE;

        for (SamplePoint point : points) {

            if (point == null) {
                continue;
            }

            for (SamplePoint seed : seeds) {

                if (seed == null) {
                    continue;
                }

                // evaluate distance between a point and a seed
                double distance = point.L2Distance(seed);
                if (distance < distanceToClosestSeed) {
                    distanceToClosestSeed = distance;
                    closestSeed = seed;
                }
            }

            if (closestSeed != null) {
                closestSeed.weight = closestSeed.weight + 1;
                if (pointsAssignedToSeeds.containsKey(closestSeed)) {
                    pointsAssignedToSeeds.get(closestSeed).add(point);
                } else {
                    ArrayList<SamplePoint> nal = new ArrayList<SamplePoint>();
                    nal.add(point);
                    pointsAssignedToSeeds.put(closestSeed, nal);
                }

            }
        }
    }

    private void recomputeSeedCentersAndSetWeights() {

        HashMap<SamplePoint, List<SamplePoint>> newPointsAssignedToSeeds = new HashMap<>(pointsAssignedToSeeds.size());

        for (Map.Entry<SamplePoint, List<SamplePoint>> entrySet : pointsAssignedToSeeds.entrySet()) {
            SamplePoint seed = entrySet.getKey();
            List<SamplePoint> listOfAssignedPoints = entrySet.getValue();

            int recomputedX = 0;
            int recomputedY = 0;

            for (SamplePoint singleAssignedPoint : listOfAssignedPoints) {
                recomputedX += singleAssignedPoint.getX();
                recomputedY += singleAssignedPoint.getY();
            }

            recomputedX = recomputedX / listOfAssignedPoints.size();
            recomputedY = recomputedY / listOfAssignedPoints.size();

            SamplePoint newSeed = new SamplePoint(recomputedX, recomputedY, new java.awt.Color(image.getRGB(recomputedX, recomputedY)));

            newSeed.weight = (double) listOfAssignedPoints.size();
            
            // remove old seed from hashmap
            //pointsAssignedToSeeds.remove(seed);
            newPointsAssignedToSeeds.put(newSeed, listOfAssignedPoints);
        }

        pointsAssignedToSeeds = newPointsAssignedToSeeds;
    }

    private void removeDuplicateSeeds(double minimalDistance) {

        int removed = 0;

        for (int seedIndex = 0; seedIndex < seeds.length; seedIndex++) {

            SamplePoint seedBase = seeds[seedIndex];

            if (seedBase == null) {
                continue;
            }

            for (int seedIndexInner = seedIndex + 1; seedIndexInner < seeds.length; seedIndexInner++) {

                SamplePoint seedComparedTo = seeds[seedIndexInner];

                // skip already removed seeds
                if (seedComparedTo == null) {
                    continue;
                }

                if (seedBase.L2Distance(seedComparedTo) < minimalDistance) {
                    // remove some seed. But which one? Better the seed with lower weight!
                    if (seedBase.weight < seedComparedTo.weight) {
                        pointsAssignedToSeeds.remove(seeds[seedIndex]);
                        seeds[seedIndex] = null;
                    } else {
                        pointsAssignedToSeeds.remove(seeds[seedIndexInner]);
                        seeds[seedIndexInner] = null;
                    }

                    removed++;
                }
            }
        }

        //System.out.println("Removed seeds amount: " + removed);
    }

    private void removeTooSmallSeeds(double minimalWeight) {

        for (int seedIndex = 0; seedIndex < seeds.length; seedIndex++) {

            if (seeds[seedIndex] == null) {
                continue;
            }

            if (seeds[seedIndex].weight < minimalWeight) {
                // remove the seed from hashmap
                pointsAssignedToSeeds.remove(seeds[seedIndex]);
                // remove the seed itself
                seeds[seedIndex] = null;
            }
        }
    }

    @Override
    public String getFileName() {
        return this.name + "_" + NUMBER_OF_POINTS + "_" + NUMBER_OF_SEEDS + "_" + ITERATION_COUNT + "_" + ((int)minimalDistance) + "_" + ((int)minimalWeight);
    }

    @Override
    public String toString() {
        return "FeatureSignatureExtractor[" + "name=" + name + ", NUMBER_OF_POINTS=" + NUMBER_OF_POINTS + ", NUMBER_OF_SEEDS=" + NUMBER_OF_SEEDS + ", ITERATION_COUNT=" + ITERATION_COUNT + ", imageWidth=" + minimalDistance + ", minimalWeight=" + minimalWeight + ']';
    }

}
