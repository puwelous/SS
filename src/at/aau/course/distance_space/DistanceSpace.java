package at.aau.course.distance_space;

import at.aau.course.VectorData;
import at.aau.course.distance.IDistance;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DistanceSpace {

    VectorData[] dataDescriptors;

    // distance calculator
    IDistance distance;

    public DistanceSpace(VectorData[] dataDescriptors, IDistance distance) {
        super();
        this.dataDescriptors = dataDescriptors;
        this.distance = distance;
    }

    public VectorData[] getDataDescriptors() {
        return dataDescriptors;
    }

    public void setDataDescriptors(VectorData[] dataDescriptors) {
        this.dataDescriptors = dataDescriptors;
    }

    public IDistance getDistance() {
        return distance;
    }

    public void setDistance(IDistance distance) {
        this.distance = distance;
    }

    public RankedResult[] sortDBAccordingToQuery(VectorData query) {

        List<RankedResult> resultAsList = new ArrayList<RankedResult>();

        for (int i = 0; i < dataDescriptors.length; i++) {

            resultAsList.add(
                    new RankedResult(
                            dataDescriptors[i], distance.compute(query, dataDescriptors[i])
                    )
            );
        }

        // sort
        Collections.sort(resultAsList);

        // convert and return
        return resultAsList.toArray(new RankedResult[dataDescriptors.length]);
    }

    /**
     * How relevant is the descriptor? For all descriptors calculate relevance
     * separately.
     *
     * @param descriptors Whole set of descriptors.
     * @return
     */
    public double computeMAP(VectorData[] descriptors) {
        double MAP = 0;

        for (VectorData vectorData : descriptors) {
            MAP += avgPrecisionAsSum(vectorData);
        }

        return (MAP / descriptors.length);
    }

    private double avgPrecisionAsSum(VectorData vectorData) {

        RankedResult[] rankedResults = this.sortDBAccordingToQuery(vectorData);

        int i = 0;
        int match = 0;
        double AP = 0;

        /**
         * ****** optimization ************
         */
        final int vectorDataClassId = vectorData.getClassId();
        /**
         * ********************************
         */

        for (RankedResult rankedResult : rankedResults) {
            i++;
            if (vectorDataClassId == rankedResult.getVectorData().getClassId()) {
                match++;
//                                System.out.println("Match: " + match + " and i:" + i);
//                                System.out.println("Adding match addition: " + (match / i));
                AP += ((double) match) / i;
            }
        }

        //System.out.println("Matched " + match + " times!");
        return AP / match;
    }

    public RankedResult[] rangeQuery(VectorData query, double range) {
        RankedResult[] rankedResults = this.sortDBAccordingToQuery(query);

        for (int i = 0; i < rankedResults.length; i++) {
            RankedResult rankedResult = rankedResults[i];
            System.out.println("Comparing ranges:" + rankedResult.getDistance() + " > " + range + " is " + (rankedResult.getDistance() > range) );
            if (rankedResult.getDistance() > range) {
                return Arrays.copyOfRange(rankedResults, 0, i);
            }
        }

        return rankedResults;
    }

    public RankedResult[] kNNQuery(VectorData query, final int k) {
        RankedResult[] rankedResults = this.sortDBAccordingToQuery(query);
        return Arrays.copyOfRange(rankedResults, 0, k);
    }

    /**
     * ********************** indexing *************************
     */
    public double computeIntrinsicDimensionality() {
        double intrinsicDimensionality;

        // first randomly choose 2 points and compute
        int vectorDataIndexToCompare1, vectorDataIndexToCompare2, randIndex1, randIndex2;
        VectorData vectorDataToCompare1, vectorDataToCompare2;
        double point1, point2;

        double avgSigma;
        double sumOfAllDistances = 0.0;
        double[] distancesArray = new double[1000];
        for (int i = 0; i < distancesArray.length; i++) {

            // take randomly 2 vector representation of images                
            vectorDataIndexToCompare1 = (int) (Math.random() * this.dataDescriptors.length);
            vectorDataIndexToCompare2 = (int) (Math.random() * this.dataDescriptors.length);

            vectorDataToCompare1 = this.dataDescriptors[vectorDataIndexToCompare1];
            vectorDataToCompare2 = this.dataDescriptors[vectorDataIndexToCompare2];

            // take randomly 2 their points
            randIndex1 = (int) (Math.random() * vectorDataToCompare1.getData().length);
            randIndex2 = (int) (Math.random() * vectorDataToCompare2.getData().length);

            point1 = vectorDataToCompare1.getData()[randIndex1];
            point2 = vectorDataToCompare2.getData()[randIndex2];

            distancesArray[i] = Math.abs(point1 - point2);
            sumOfAllDistances += distancesArray[i];
        }

        avgSigma = sumOfAllDistances / distancesArray.length;

        // compute sigma^2
        //sigma^2=SUM(sigmai - avgSigma)^2
        double sigmaSquared = 0.0;
        for (int j = 0; j < distancesArray.length; j++) {
            sigmaSquared += Math.pow((distancesArray[j] - avgSigma), 2);
        }

        // mean = sigma
        intrinsicDimensionality = (Math.pow(avgSigma, 2) / (2 * sigmaSquared));

        return intrinsicDimensionality;
    }
}
