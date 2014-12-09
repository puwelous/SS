package at.aau.course;

import java.awt.image.BufferedImage;

public class FeatureSignatureExtractor {

    final int NUMBER_OF_POINTS = 2000;
    final int NUMBER_OF_SEEDS = 500;
    final int ITERATION_COUNT = 5;

    SamplePoint[] points;
    SamplePoint[] seeds;

    BufferedImage image;

    //minimal weight = (numberofiteration * 3)
    public FeatureSignatureExtractor() {

    }

    public double extract(BufferedImage image) {

        this.image = image;

        // initialize points
        points = new SamplePoint[NUMBER_OF_POINTS];

        // initialize seeds
        seeds = new SamplePoint[NUMBER_OF_SEEDS];

        // select points and seeds randomly
        this.randomlySamplePointsAndSeeds();

        for (int i = 0; i < ITERATION_COUNT; i++) {
            setSeedWeightsToZero();
            //removeDuplicateSeeds(minimalDistance);
            // AssignPointsToTheirClosestSeed()
            // RemoveTooSmallSeeds(minimalWeight)
            // RecomputeSeedCentersAndSetWeights()
        }

        // TODO
        return 0.0;
    }

    private void randomlySamplePointsAndSeeds() {

        double x, y;
        java.awt.Color c;

        for (int i = 0; i < NUMBER_OF_POINTS; i++) {

            x = Math.random() * this.image.getWidth();
            y = Math.random() * this.image.getHeight();

            c = new java.awt.Color(this.image.getRGB((int) x, (int) y));

            points[i] = new SamplePoint(x, y, c);
        }
    }

    private void setSeedWeightsToZero() {
        // TODO use optimal way of doing it, Arrays. ArrayUtils or sth like that
        for (int i = 0; i < NUMBER_OF_SEEDS; i++) {
            seeds[i].weight = 0.0;
        }
    }

}
