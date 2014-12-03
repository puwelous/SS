package at.aau.course;

import at.aau.course.distance_space.DistanceSpace;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PivotTable {

    static final int PIVOT_COUNT = 10;

    DistanceSpace distanceSpace;

    VectorData[] pivots;

    double[][] distanceMatrix = null;

    int descriptorsCount;

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public PivotTable(DistanceSpace distanceSpace) {
        this.distanceSpace = distanceSpace;
        this.descriptorsCount = distanceSpace.getDataDescriptors().length;
        
    }

    public void create(VectorData[] vectorData) {
        // generate pivots
        this.generatePivots(vectorData);

        this.calculateMatrix(vectorData);
    }

    public void save(String fileName) throws IOException {
        this.save(new File(fileName));
    }

    public void save(File pivotsFile) throws IOException {

        if (pivotsFile.exists()) {
            pivotsFile.delete();
        }

        try (BufferedWriter pivotFileWriter = new BufferedWriter(new FileWriter(pivotsFile))) {
            // save pivots
            for (VectorData pivot : this.pivots) {
                pivotFileWriter.write(pivot.saveToString());
                pivotFileWriter.newLine();
            }

            // save matrix
            for (int i = 0; i < distanceMatrix.length; i++) {
                double[] rowOrColumn = distanceMatrix[i];
                pivotFileWriter.write(Arrays.toString(rowOrColumn));
                pivotFileWriter.newLine();
            }
        }
    }

    public void load(String fileName) throws Exception {
        load(new File(fileName));
    }

    public void load(File pivotsFile) throws FileNotFoundException, IOException, Exception {
        if (!pivotsFile.exists()) {
            throw new IllegalArgumentException("Pivot file called " + pivotsFile.getName() + " does not exist!");
        }

        List<VectorData> listOfPivotsAsVectorData = new ArrayList<VectorData>();

        BufferedReader br = new BufferedReader(new FileReader(
                pivotsFile.getAbsoluteFile()));

        String readedLine = null;
        int linesRead = 1;

        // load pivots
        while (((readedLine = br.readLine()) != null) && (linesRead <= PIVOT_COUNT)) {

            try {
                listOfPivotsAsVectorData.add(new VectorData().readFromString(readedLine));
            } catch (java.lang.StringIndexOutOfBoundsException e) {
                System.err.println("Could not read VectorData from a line: " + readedLine);
                throw e;
            }
            linesRead++;
        }

        pivots = listOfPivotsAsVectorData.toArray(new VectorData[listOfPivotsAsVectorData.size()]);

        distanceMatrix = new double[descriptorsCount][pivots.length];
        
        listOfPivotsAsVectorData = null; // forget

        int i = 0;
        // load matrix
        while ((readedLine = br.readLine()) != null) {

            readedLine = readedLine.substring(1, readedLine.length()-1);
            String stringOfNumbers[] = readedLine.split(",");
            if (distanceMatrix[i].length != stringOfNumbers.length) {
                throw new Exception("Hm.. different amount of doubles read from file than expected!");
            }
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                try {
                    double d = Double.parseDouble(stringOfNumbers[j]);
                    distanceMatrix[i][j] = d;
                } catch (NumberFormatException e) {
                    System.err.println("Could not parse a string " + stringOfNumbers[j] + " to a double!");
                    throw e;
                }
            }
            i++;
        }

        br.close();
    }

    private void generatePivots(VectorData[] vectorData) {
        pivots = new VectorData[PIVOT_COUNT];
        for (int i = 0; i < PIVOT_COUNT; i++) {
            int randIndex = (int) (Math.random() * vectorData.length);
            pivots[i] = vectorData[randIndex];
        }
    }

    private void calculateMatrix(VectorData[] vectorData) {
        distanceMatrix = new double[vectorData.length][pivots.length];
        // let's store it into matrix
        //List<PivotObjectTupple> listOfTupples = new ArrayList<PivotObjectTupple>(pivots.length*vectorData.length);
        // compute pivot matrix for each pivot and each object in the space
        for (int iVDO = 0; iVDO < vectorData.length; iVDO++) {
            for (int jP = 0; jP < this.pivots.length; jP++) {
                distanceMatrix[iVDO][jP] = this.distanceSpace.getDistance().compute(
                        vectorData[iVDO], this.pivots[jP]
                );
            }
        }
    }

//    private class PivotObjectTupple {
//
//        private VectorData pivot;
//        private VectorData object;
//        private double distance;
//
//        public PivotObjectTupple(VectorData pivot, VectorData object, double distance) {
//            this.pivot = pivot;
//            this.object = object;
//            this.distance = distance;
//        }
//
//        public double getDistance() {
//            return distance;
//        }
//
//    }
}
