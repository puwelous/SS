package at.aau.course;

import at.aau.course.distance.IDistance;
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
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Task {

    int extractorsCount = 0;
    boolean isFileGenerationRequired = false;
    boolean isDistanceSpaceComputationRequired = false;
    List<IDescriptorWrapper> extractors;
    List<VectorData> queryObjectsList;
    //VectorData[] vectorDataArray = null;
    HashMap<String, List<VectorData>> vectorDataMappedToDescriptors = null;
    private EnvironmentPreparationUnit environment = null;

    public Task() {
    }

    public HashMap<String, List<VectorData>> getVectorDataMappedToDescriptors() {
        return vectorDataMappedToDescriptors;
    }

    public Task(
            List<IDescriptorWrapper> extractors,
            boolean fileGenerationRequired, boolean computeDistanceSpace)
            throws Exception {

        this.queryObjectsList = new ArrayList<VectorData>();

        this.extractors = extractors;
        this.extractorsCount = extractors.size();
        this.isFileGenerationRequired = fileGenerationRequired;
        this.isDistanceSpaceComputationRequired = computeDistanceSpace;
    }

    public void generateDescriptors() throws Exception {

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

        HashMap<String, List<VectorData>> mappedVectorDataToDescriptorName = new HashMap<String, List<VectorData>>();

        int imageId = 0;
        int classId;

        //List<VectorData> allVectorDataAsList = new ArrayList<VectorData>();
        final int fileCount = this.environment
                .getFileCountInDir(this.environment.getInputDir());

        // fileCount * (extractors) / 100 (%) + rounding
        final int step = new BigDecimal((fileCount > 100 ? ((double) (fileCount * this.extractorsCount) / 100) : 1)).setScale(0, RoundingMode.CEILING).intValue();

        File[] directories = this.environment.getInputDir().listFiles();

        BufferedWriter writerToWriteTo = null;

        int actuallyComputedCount = 0;

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

                        if (!mappedVectorDataToDescriptorName.containsKey(singleExtractor.getFileName())) {
                            mappedVectorDataToDescriptorName.put(singleExtractor.getFileName(), new ArrayList<VectorData>());
                        }
                        List<VectorData> vectorDataPerDesc = mappedVectorDataToDescriptorName.get(singleExtractor.getFileName());
                        vectorDataPerDesc.add(vectorData);
                        mappedVectorDataToDescriptorName.put(singleExtractor.getFileName(), vectorDataPerDesc);

                        // save to memory
                        //allVectorDataAsList.add(vectorData);
                        //System.out.println(singleExtractor.getFileName());
                        writerToWriteTo = descriptorsMappedToWriters
                                .get(singleExtractor.getFileName());

                        // save to file
                        writerToWriteTo.write(vectorData.saveToString());
                        writerToWriteTo.newLine();
                        writerToWriteTo.flush();

                    }
                    if (actuallyComputedCount % step == 0) {
                        System.out.println("Complete: "
                                + (actuallyComputedCount / step) + "%");
                    }
                    pwOutDataMapping.println(classId + ";" + imageId + ";"
                            + directoryName + "/" + imageFile.getName());
                    pwOutDataMapping.flush();

                    imageId++;
                    actuallyComputedCount++;
                }
            } else {
                System.err.println("File " + directory.getName()
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
//        return allVectorDataAsList.toArray(new VectorData[allVectorDataAsList
//                .size()]);
        this.vectorDataMappedToDescriptors = mappedVectorDataToDescriptorName;
    }

    public RankedResult[] searchForSimilarQueryObjectsBykNN(
            List<File> queryObjectsAsFiles,
            String extractorFileName,
            LpNorm LpNorm,
            int kNN
    ) throws Exception {

        assert (!queryObjectsAsFiles.isEmpty());

        RankedResult[] rankedResults = null;

        // clear list
        if (queryObjectsList == null) {
            queryObjectsList = new ArrayList<>();
        } else {
            this.queryObjectsList.clear();
        }
        assert (queryObjectsList.isEmpty());
        this.retrieveQueryObjectsByFiles(queryObjectsAsFiles, extractorFileName);
        assert (!queryObjectsList.isEmpty());

        VectorData queryObject = this.queryObjectsList.get(0);

        // VectorData for all images computed by @extractorFileName@ feature extractor
        List<VectorData> l_vectorDataPerDescriptor = this.vectorDataMappedToDescriptors.get(extractorFileName);

        DistanceSpace distanceSpaceLpNorm = new DistanceSpace(
                l_vectorDataPerDescriptor.toArray(new VectorData[l_vectorDataPerDescriptor.size()]), // convert List->Array
                LpNorm
        );

        rankedResults = distanceSpaceLpNorm
                .kNNQuery(queryObject, kNN);

        return rankedResults;
    }

    public RankedResult[] searchForSimilarQueryObjectsByRange(
            List<File> queryObjectsAsFiles,
            String extractorFileName,
            LpNorm LpNorm,
            double range
    ) throws Exception {

        assert (!queryObjectsAsFiles.isEmpty());

        RankedResult[] rankedResults = null;

        // clear list
        if (queryObjectsList == null) {
            queryObjectsList = new ArrayList<>();
        } else {
            this.queryObjectsList.clear();
        }
        assert (queryObjectsList.isEmpty());
        this.retrieveQueryObjectsByFiles(queryObjectsAsFiles, extractorFileName);
        assert (!queryObjectsList.isEmpty());

        VectorData queryObject = this.queryObjectsList.get(0);

        // VectorData for all images computed by @extractorFileName@ feature extractor
        List<VectorData> l_vectorDataPerDescriptor = this.vectorDataMappedToDescriptors.get(extractorFileName);

        DistanceSpace distanceSpaceLpNorm = new DistanceSpace(
                l_vectorDataPerDescriptor.toArray(new VectorData[l_vectorDataPerDescriptor.size()]), // convert List->Array
                LpNorm
        );

        rankedResults = distanceSpaceLpNorm
                .rangeQuery(queryObject, range);

        return rankedResults;
    }

//    public RankedResult[] compute(List<File> queryObjectsAsFiles) throws Exception {
//
//        if (this.environment == null) {
//            throw new IllegalStateException("Environment has NOT been prepared yet!");
//        }
//
//        // compute or load descriptors
//        //vectorDataMappedToDescriptors = this.prepareVectorData();
//        this.retrieveQueryObjectsByFiles(queryObjectsAsFiles);
//
//        // take random query object:
//        VectorData queryObject = this.queryObjectsList.get(0);
//
//        System.out.println("'Randomly' selected query object: "
//                + queryObject.toString());
//
//        // calculating distances for histogram
//        double[] LpNormPValues = new double[]{1, 2, 5, 0.5};
//
//        RankedResult[] rankedResults = null;
//
//        VectorData[] a_vectorDataPerDescriptor;
//
//        for (Map.Entry<String, List<VectorData>> entry : vectorDataMappedToDescriptors.entrySet()) {
//            String descriptorFileName = entry.getKey();
//            List<VectorData> l_vectorDataPerDescriptor = entry.getValue();
//
//            a_vectorDataPerDescriptor = l_vectorDataPerDescriptor.toArray(new VectorData[l_vectorDataPerDescriptor.size()]);
//
//            for (double LpNormValue : LpNormPValues) {
//
//                System.out.println("Computing the distance space for p value = "
//                        + LpNormValue + " and descriptor " + descriptorFileName);
//
//                // initialization
//                DistanceSpace distanceSpaceLpNorm = new DistanceSpace(
//                        a_vectorDataPerDescriptor, new LpNorm(LpNormValue));
//
//                // calculation of distance space
////                rankedResults = distanceSpaceLpNorm
////                        .sortDBAccordingToQuery(queryObject);
////                System.out.println(".....publishing results.....");
////                this.publishResults(rankedResults, 10);
////                System.out.println("............................");
//                double map = distanceSpaceLpNorm.computeMAP(new VectorData[]{queryObject});
//                System.out.println("ComputeMAP for p value = " + LpNormValue + " and single query object: " + queryObject.toString() + " is = " + map);
//
//                // pivot table
//                PivotTable pt = new PivotTable(new DistanceSpace(a_vectorDataPerDescriptor, new LpNorm(LpNormValue)));
//                if (false) {
//                    pt.create(a_vectorDataPerDescriptor);
//                    pt.save(this.environment.getPivotTableFile());
//                } else {
//                    pt.load(this.environment.getPivotTableFile());
//                    System.out.println(Arrays.deepToString(pt.getDistanceMatrix()));
//                }
//            }
//
//            // computing intrinsic dimensionality Lp = 2
//            DistanceSpace distanceSpaceLpNorm = new DistanceSpace(
//                    a_vectorDataPerDescriptor, new LpNorm(2));
//            double iDim = distanceSpaceLpNorm.computeIntrinsicDimensionality();
//            System.out.println("iDim for descriptor " + descriptorFileName + ": " + iDim);
//
//        }
//
//        System.out.println("... done ...");
//
//        return rankedResults;
//    }
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

    public void reloadDescriptors() throws IOException {

        // get the number of images processed in a file (let's assume the number has not
        // been changed in the meantime
        final int fileCount = this.environment.getFileCountInDir(this.environment.getInputDir());

        //List<VectorData> reloadedVectorData = new ArrayList<VectorData>();
        HashMap<String, List<VectorData>> reloadedDataMappedToDescriptors = new HashMap<>(this.extractorsCount);
        //VectorData[] reloadedDataAsArray = new VectorData[fileCount * this.extractorsCount];

        String fileNameToReadFrom = null;
        String readedLine = null;
        //int reloadedDataArrayActIndex = 0;
        List<VectorData> listofVectorDataPerDesc;

        for (IDescriptorWrapper iDescriptorWrapper : extractors) {

            // retrieve a file name of the file whose descriptor data is saved in
            fileNameToReadFrom = iDescriptorWrapper.getFileName();

            BufferedReader br = new BufferedReader(new FileReader(
                    fileNameToReadFrom));

            listofVectorDataPerDesc = new ArrayList<VectorData>();

            while ((readedLine = br.readLine()) != null) {

//                if (((reloadedDataArrayActIndex % fileCount) % 1000) == 0) {
//                    System.out.println("Loaded: " + reloadedDataArrayActIndex + " descriptors... Now reading from file " + fileNameToReadFrom);
//                }
                listofVectorDataPerDesc.add(new VectorData().readFromString(readedLine));

//                reloadedDataAsArray[reloadedDataArrayActIndex] = loadedVectorData;
//                reloadedDataArrayActIndex++;
            }

            br.close();

            //System.out.println("Putting into " + iDescriptorWrapper.getFileName() + " List of a size " + listofVectorDataPerDesc.size());
            // add loaded vector data to a hashmap
            reloadedDataMappedToDescriptors.put(iDescriptorWrapper.getFileName(), listofVectorDataPerDesc);
        }

        this.vectorDataMappedToDescriptors = reloadedDataMappedToDescriptors;
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

//    /**
//     * Computing mean average precision
//     *
//     * @param selectedQueryObjects query objects per a single similarity search
//     * and computation relevance evaluation
//     * @param allDescriptors whole image set described by descriptors
//     */
//    public void computeMapForAll(VectorData[] selectedQueryObjects, VectorData[] allDescriptors) {
//
//        if (selectedQueryObjects == null || selectedQueryObjects.length == 0) {
//            // read file names from CSV files
//
//            // find suitable descriptors in the array of all descriptors
//        }
//
//        DistanceSpace[] allDistanceSpaces = null;
//
//        for (int i = 0; i < allDistanceSpaces.length; i++) {
//
//            String distanceSpaceName = allDistanceSpaces[i].getDistance().getName();
//            double avgPrecision = allDistanceSpaces[i].computeMAP(selectedQueryObjects);
//
//        }
//    }
    /**
     * Computing mean average precision
     *
     * @param selectedQueryObjects query objects per a single similarity search
     * and computation relevance evaluation
     * @param allDescriptors whole image set described by descriptors
     * @param iDistance distance computation interface
     * @return Average precision.
     */
    public double computeMapForDistanceSpace(VectorData[] selectedQueryObjects, VectorData[] allDescriptors, IDistance iDistance) {

        if (selectedQueryObjects == null || selectedQueryObjects.length == 0) {
            // read file names from CSV files

            // find suitable descriptors in the array of all descriptors
            
            throw new IllegalArgumentException("Empty selectedQueryObjects array!");
        }

        DistanceSpace distanceSpace = new DistanceSpace(allDescriptors, iDistance);

        return distanceSpace.computeMAP(selectedQueryObjects);
    }

    public List<File> loadFilenamesFromCSV() throws FileNotFoundException, IOException {

        List<File> loadedFileNames = new ArrayList<File>(1000);

        BufferedReader br = new BufferedReader(new FileReader(
                this.environment.getQueryObjectsCVSFile()));

        File picturesDir = this.environment.getInputDir();

        String readedLine;
        File pictureDir, pictureFile;

        while ((readedLine = br.readLine()) != null) {

            String[] readedLineParsed = readedLine.split("\\\\");

            pictureDir = new File(picturesDir, readedLineParsed[0]);

            if (!pictureDir.exists()) {
                System.err.println("Directory " + pictureDir.getAbsolutePath() + " does not exist!");
                continue; // skip checking whether the file itself exist with a directory, continue with other entries
            }

            pictureFile = new File(pictureDir, readedLineParsed[1]);

            if (!pictureFile.exists()) {
                System.err.println("Image " + pictureFile.getAbsolutePath() + " does not exist!");
                continue; // do not add!
            }

            loadedFileNames.add(pictureFile);
        }

        br.close();

        return loadedFileNames;
    }

    public void addQueryObject(File queryObjectAsFile, String descriptorFileName) throws Exception {

        final int queryObjectClassId = Integer.parseInt(new File(queryObjectAsFile.getParent()).getName());
        final String queryObjectFileName = queryObjectAsFile.getName();

        VectorData queryObjectReferenced = null;
        VectorData queryObjectByFile = new VectorData();

        queryObjectByFile.setClassId(queryObjectClassId);
        queryObjectByFile.setFileName(queryObjectFileName);

        List<VectorData> l_vectorDataPerDescriptor = vectorDataMappedToDescriptors.get(descriptorFileName);

//        for (Map.Entry<String, List<VectorData>> entry : vectorDataMappedToDescriptors.entrySet()) {
        //String descriptorFileName = entry.getKey();
//            List<VectorData> l_vectorDataPerDescriptor = entry.getValue();
        int indexOfFound = l_vectorDataPerDescriptor.indexOf(queryObjectByFile);
        if (indexOfFound == -1) {
            // found
            throw new Exception("Query object not found among generated image descriptors!");
        }
        queryObjectReferenced = l_vectorDataPerDescriptor.get(indexOfFound);
        if (!queryObjectsList.contains(queryObjectReferenced)) {
            this.queryObjectsList.add(queryObjectReferenced);
        }
//        }

//        for (VectorData generatedVectorData : vectorDataArray) {
//
//            //System.out.println("Comparing: " + generatedVectorData + " AND " +  queryObjectByFile);
//            if (generatedVectorData.equals(queryObjectByFile)) {
//                System.out.println(generatedVectorData + " AND " + queryObjectByFile + " are  equal!");
//                if (!queryObjectsList.contains(generatedVectorData)) {
//                    this.queryObjectsList.add(generatedVectorData);
//                }
//            }
//        }
    }

    public void setVectorDataMappedToDescriptors(HashMap<String, List<VectorData>> vectorDataMappedToDescriptors) {
        this.vectorDataMappedToDescriptors = vectorDataMappedToDescriptors;
    }

    public void setEnvironment(EnvironmentPreparationUnit epu) {
        this.environment = epu;
    }

    private void retrieveQueryObjectsByFiles(List<File> queryObjectsAsFiles, String descFileName) throws Exception {

//        for (Map.Entry<String, List<VectorData>> entry : vectorDataMappedToDescriptors.entrySet()) {
//            String descriptorFileName = entry.getKey();
//            List<VectorData> l_vectorDataPerDescriptor = entry.getValue();
//        }
        for (File queryObjectsAsFile : queryObjectsAsFiles) {
            System.out.println(queryObjectsAsFile.getAbsoluteFile());
            this.addQueryObject(queryObjectsAsFile, descFileName);
        }
    }

    public void setExtractors(List<IDescriptorWrapper> descriptionWrappers) {
        this.extractors = descriptionWrappers;
        this.extractorsCount = descriptionWrappers.size();
    }
}
