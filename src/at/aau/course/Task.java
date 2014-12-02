package at.aau.course;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import at.aau.course.distance.LpNorm;
import at.aau.course.distance_space.DistanceSpace;
import at.aau.course.distance_space.RankedResult;
import at.aau.course.extractor.GrayScaleHistogram;
import at.aau.course.extractor.IDescriptorWrapper;
import at.aau.course.extractor.IExtractor;
import at.aau.course.util.environment.EnvironmentPreparationUnit;

public class Task {

    int extractorsCount = 0;
    boolean isFileGenerationRequired = false;
    boolean isDistanceSpaceComputationRequired = false;
    List<IDescriptorWrapper> extractors;
    List<VectorData> queryObjectsList;
    VectorData[] vectorDataArray = null;

    private EnvironmentPreparationUnit environment = null;

    public Task(List<IDescriptorWrapper> extractors,
            boolean fileGenerationRequired, boolean computeDistanceSpace)
            throws Exception {

        this.queryObjectsList = new ArrayList<VectorData>();

        this.extractors = extractors;
        this.extractorsCount = extractors.size();
        this.isFileGenerationRequired = fileGenerationRequired;
        this.isDistanceSpaceComputationRequired = computeDistanceSpace;
    }

    public RankedResult[] compute(List<File> queryObjectsAsFiles) throws Exception {

        if (this.environment == null) {
            throw new IllegalStateException("Environment has NOT been prepared yet!");
        }

        // compute or load descriptors
        vectorDataArray = this.prepareVectorData();

        this.retrieveQueryObjectsByFiles(queryObjectsAsFiles);

        // take random query object:
        VectorData queryObject = this.queryObjectsList.get(0);

        System.out.println("'Randomly' selected query object: "
                + queryObject.toString());

        // calculating distances for histogram
        double[] LpNormPValues = new double[]{1, 2, 5, 0.5};

        RankedResult[] rankedResults = null;

        for (double LpNormValue : LpNormPValues) {

            System.out.println("Computing the distance space for p value = "
                    + LpNormValue);

            // initialization
            DistanceSpace distanceSpaceLpNorm = new DistanceSpace(
                    vectorDataArray, new LpNorm(LpNormValue));

            // calculation of distance space
            rankedResults = distanceSpaceLpNorm
                    .sortDBAccordingToQuery(queryObject);

            System.out.println("Distance space computation for p "
                    + LpNormValue + " finished.");

            System.out.println(".....publishing results.....");
            this.publishResults(rankedResults, 10);
            System.out.println("............................");
        }

        System.out.println("... done ...");

        DistanceSpace distanceSpaceLpNorm = new DistanceSpace(
                vectorDataArray, new LpNorm(2));
        double iDim = distanceSpaceLpNorm.computeIntrinsicDimensionality();
        System.out.println("iDim: " + iDim);

        return rankedResults;
    }

    private void publishResults(RankedResult[] rankedResults,
            int countOfPublishedResults) {

        for (int rankedResultIndex = 0; rankedResultIndex < countOfPublishedResults; rankedResultIndex++) {
            RankedResult singleRankedResult = rankedResults[rankedResultIndex];
            System.out.println("Ranked result at index " + rankedResultIndex
                    + " is of a class: "
                    + singleRankedResult.getVectorData().getClassId()
                    + " and has an ID: "
                    + singleRankedResult.getVectorData().getId());
        }
    }

    private VectorData[] prepareVectorData() throws IOException, Exception {

        VectorData[] loadedOrComputedVectorData = null;

        if (this.isFileGenerationRequired) {
            System.out
                    .println("Descriptors generation has been initialized...");
            loadedOrComputedVectorData = this.generateDescriptors();
            System.out.println("Descriptors generation has been finished...");
        } else {
            System.out.println("Reloading descriptors has been initialized...");
            loadedOrComputedVectorData = this.reloadDescriptors();
            System.out.println("Reloading descriptors has been finished...");
        }

        return loadedOrComputedVectorData;
    }

    private VectorData[] reloadDescriptors() throws IOException {

        // get the number of images processed in a file (let's assume the number has not
        // been changed in the meantime
        final int fileCount = this.environment.getFileCountInDir(this.environment.getInputDir());

        //List<VectorData> reloadedVectorData = new ArrayList<VectorData>();
        VectorData[] reloadedDataAsArray = new VectorData[fileCount * this.extractorsCount];

        String fileNameToReadFrom = null;
        String readedLine = null;
        int reloadedDataArrayActIndex = 0;
        VectorData loadedVectorData = null;

        for (IDescriptorWrapper iDescriptorWrapper : extractors) {

            // retrieve a file name of the file whose descriptor data is saved in
            fileNameToReadFrom = iDescriptorWrapper.getFileName();

            BufferedReader br = new BufferedReader(new FileReader(
                    fileNameToReadFrom));

            while ((readedLine = br.readLine()) != null) {

                if (((reloadedDataArrayActIndex % fileCount) % 1000) == 0) {
                    System.out.println("Loaded: " + reloadedDataArrayActIndex + " descriptors... Now reading from file " + fileNameToReadFrom);
                }

                loadedVectorData = new VectorData().readFromString(readedLine);

                reloadedDataAsArray[reloadedDataArrayActIndex] = loadedVectorData;
                reloadedDataArrayActIndex++;
            }

            br.close();
        }

        return reloadedDataAsArray;
    }

    private VectorData[] generateDescriptors() throws Exception {

        long a = System.currentTimeMillis();

        HashMap<String, File> descriptorsMappedToFiles = this.environment
                .getVectorDataFile();

        HashMap<String, BufferedWriter> descriptorsMappedToWriters = new HashMap<String, BufferedWriter>(
                descriptorsMappedToFiles.size());

        for (Map.Entry<String, File> entry : descriptorsMappedToFiles
                .entrySet()) {
            String key = entry.getKey();
            File value = entry.getValue();
            //System.out.println("Adding value " + value + " assigned to a key " + key);
            descriptorsMappedToWriters.put(key, new BufferedWriter(
                    new FileWriter(value)));
        }

        PrintWriter pwOutDataMapping = new PrintWriter(new FileWriter(
                this.environment.getMappingFile()));

        int imageId = 0;
        int classId = 0;

        List<VectorData> allVectorDataAsList = new ArrayList<VectorData>();

        final int fileCount = this.environment
                .getFileCountInDir(this.environment.getInputDir());

        // fileCount * (extractors) / 100 (%) + rounding
        final int step = new BigDecimal((fileCount > 100 ? ((double) (fileCount * this.extractorsCount) / 100) : 1)).setScale(0, RoundingMode.CEILING).intValue();

        File[] directories = this.environment.getInputDir().listFiles();

        BufferedWriter writerToWriteTo = null;

        for (int i = 0; i < directories.length; i++) {

            File directory = directories[i];
            String directoryName = directory.getName();

            classId = Integer.parseInt(directoryName);

            if (directory.isDirectory()) {

                File directoryImageFiles[] = directory.listFiles();
                for (int j = 0; j < directoryImageFiles.length; j++) {

                    File imageFile = directoryImageFiles[j];

                    // file to buffered image
                    BufferedImage image = ImageIO.read(imageFile);

                    // extract data per each extractor
                    for (IDescriptorWrapper singleExtractor : extractors) {
                        // use extractor to get the data
                        double[] result = ((IExtractor) singleExtractor)
                                .extract(image);

                        VectorData vectorData = new VectorData(
                                imageId,
                                classId,
                                imageFile.getName(),
                                result);

                        // save to memory
                        allVectorDataAsList.add(vectorData);

                        //System.out.println(singleExtractor.getFileName());
                        writerToWriteTo = descriptorsMappedToWriters
                                .get(singleExtractor.getFileName());

                        // save to file
                        writerToWriteTo.write(vectorData.saveToString());
                        writerToWriteTo.newLine();
                        writerToWriteTo.flush();

                        if (allVectorDataAsList.size() % step == 0) {
                            System.out.println("Complete: "
                                    + (allVectorDataAsList.size() / step) + "%");
                        }
                    }

                    pwOutDataMapping.println(classId + ";" + imageId + ";"
                            + directoryName + "/" + imageFile.getName());
                    pwOutDataMapping.flush();

                    imageId++;
                }
            } else {
                System.out.println("File " + directory.getName()
                        + " found but directory expected!");
            }

        }

        // pwOutData.close();
        for (Map.Entry<String, BufferedWriter> entry : descriptorsMappedToWriters
                .entrySet()) {
            BufferedWriter value = entry.getValue();
            value.close();
        }

        pwOutDataMapping.close();

//		System.out.println(System.currentTimeMillis() - a);
        return allVectorDataAsList.toArray(new VectorData[allVectorDataAsList
                .size()]);
    }

    public static void main(String[] arg) {
        try {
            boolean generateFilesAsWell = false;
            boolean conductDistanceSpaceComputation = true;

            int[] quantinizations = new int[]{4, 8 /*, 16, 32, 64, 128 /*
             * ,
             * 256
             */}; // bins

            int[] dimensions = new int[]{1, 2, /*3, 4 */}; // blocks expressed
            // by
            // elements per
            // a row
            // (or per a
            // column)

            int numberOfDescriptors = dimensions.length
                    * quantinizations.length;

            List<IDescriptorWrapper> extractors = new ArrayList<IDescriptorWrapper>(
                    numberOfDescriptors);

            for (int i = 0; i < quantinizations.length; i++) {
                for (int j = 0; j < dimensions.length; j++) {

                    // gray scale descriptors
                    extractors.add((IDescriptorWrapper) new GrayScaleHistogram(
                            dimensions[j],
                            quantinizations[i]));
                }
            }

            new Task(extractors, generateFilesAsWell,
                    conductDistanceSpaceComputation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void addQueryObject(VectorData queryObject) {
//        this.queryObjectsList.add(queryObject);
//    }
    public void addQueryObject(File queryObjectAsFile) {

        final int queryObjectClassId = Integer.parseInt(new File(queryObjectAsFile.getParent()).getName());
        final String queryObjectFileName = queryObjectAsFile.getName();

        VectorData queryObjectByFile = new VectorData();

        queryObjectByFile.setClassId(queryObjectClassId);
        queryObjectByFile.setFileName(queryObjectFileName);

        for (VectorData generatedVectorData : vectorDataArray) {

            //System.out.println("Comparing: " + generatedVectorData + " AND " +  queryObjectByFile);
            if (generatedVectorData.equals(queryObjectByFile)) {
                System.out.println(generatedVectorData + " AND " + queryObjectByFile + " are  equal!");
                if (!queryObjectsList.contains(generatedVectorData)) {
                    this.queryObjectsList.add(generatedVectorData);
                }
            }
        }
    }

    public void setEnvironment(EnvironmentPreparationUnit epu) {
        this.environment = epu;
    }

    private void retrieveQueryObjectsByFiles(List<File> queryObjectsAsFiles) {
        for (File queryObjectsAsFile : queryObjectsAsFiles) {
            this.addQueryObject(queryObjectsAsFile);
        }
    }
}
