package at.aau.course.extractor;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class HSVExtractor implements IDescriptorWrapper {

    private String name = getClass().getSimpleName();
    private final int blockCount;
    private final int numberOfBins;

    // ************ optimization ************
    final int numOfBinsDivBy256;
    final int numberOfBinsMinusOne;
    // **************************************

    public HSVExtractor(int blockCount, int numberOfBins) {
        this.blockCount = blockCount;
        this.numberOfBins = numberOfBins;
        numberOfBinsMinusOne = numberOfBins - 1;
        numOfBinsDivBy256 = (int) (this.numberOfBins / 256);
    }

    @Override
    public double[] extract(BufferedImage image) {

        int blockWidth = image.getWidth() / blockCount;
        int blockHeight = image.getHeight() / blockCount;

        List<double[][][]> dataCollectors = new ArrayList<double[][][]>();
        List<Rectangle> rectangles = new ArrayList<Rectangle>();

        for (int i = 0; i < blockCount; i++) {
            for (int j = 0; j < blockCount; j++) {

                dataCollectors.add(new double[numberOfBins][numberOfBins][numberOfBins]);

                rectangles.add(new Rectangle(i * blockWidth, j * blockHeight,
                        blockWidth, blockHeight));

                double[][][] computedDataCollector = extractBlock(image,
                        rectangles.get(rectangles.size() - 1),
                        dataCollectors.get(dataCollectors.size() - 1));

                dataCollectors.set(dataCollectors.size() - 1, computedDataCollector);
            }
        }

        rectangles = null;

        double[] concatenatedCollector = new double[numberOfBins * numberOfBins * numberOfBins * blockCount * dataCollectors.size()];
        int index = 0;

        //TODO: optimize
        for (double dataCollector[][][] : dataCollectors) {
            for (int i = 0; i < dataCollector.length; i++) {
                double[][] dses = dataCollector[i];
                for (int j = 0; j < dses.length; j++) {
                    double[] ds = dses[j];
                    for (int k = 0; k < ds.length; k++) {
                    try {
                        concatenatedCollector[index] = ds[k];
                        index++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.err.println(numberOfBins);
                            System.err.println(blockCount);
                            System.err.println(dataCollector.length);
                            System.err.println(dataCollector[i].length);
                            System.err.println(dataCollector[i][j].length);
                            System.err.println("Index has a value:" + index);
                            System.err.println("ses[j].length:" + ds.length);
                            System.err.println("ses[j].length:" + dses.length);
                            System.err.println("ses[j].length:" + dataCollector.length);
                            index++;
                            System.exit(-1);
                        }                        
                    }
                }
                
            }        
        }
        
        return concatenatedCollector;
    }

    /**
     * HexCone. The hue value H runs from 0 to 360º. The saturation S is the
     * degree of strength or purity and is from 0 to 1. Purity is how much white
     * is added to the color, so S=1 makes the purest color (no white).
     * Brightness V also ranges from 0 to 1, where 0 is the black.
     *
     * @param image
     * @param rectangle
     * @param histogram
     * @return
     */
    private double[][][] extractBlock(BufferedImage image, Rectangle rectangle,
            double[][][] dataCollector) {

        // ************ optimization ************
//		final int upperBoundRow = rectangle.x + rectangle.width - 1;
//		final int upperBoundColumn = rectangle.y + (int) rectangle.getHeight() - 1;
        // **************************************
        // For int i = r.Left to r.Left + r.Width � 1
        for (int i = rectangle.x; i < rectangle.x + rectangle.width - 1; i++) {
            // For int j = r.Top to r.Top + r.Height � 1
            for (int j = rectangle.y; j < rectangle.y + (int) rectangle.getHeight() - 1; j++) {

                Color pixelRGBColor = new Color(image.getRGB(i, j));
                //double[] HSVValues = Converter.RGBtoHSV(pixelRGBColor.getRed(), pixelRGBColor.getGreen(), pixelRGBColor.getBlue());
                float[] HSB = new float[3];
                java.awt.Color.RGBtoHSB(pixelRGBColor.getRed(), pixelRGBColor.getGreen(), pixelRGBColor.getBlue(), HSB);

                // multiply by 1000 to get bigger values
                HSB[0] *= 1000; // <0;360>
                HSB[1] *= 1000; // <0;1000>
                HSB[2] *= 1000; // <0;1000>

                int indexH = (int) Math.min(
                        (numberOfBins * ((double) HSB[0])) / 360,
                        numberOfBinsMinusOne);
                int indexS = (int) Math.min(
                        (numberOfBins * ((double) HSB[1])) / 1000,
                        numberOfBinsMinusOne);
                int indexV = (int) Math.min(
                        (numberOfBins * ((double) HSB[2])) / 1000,
                        numberOfBinsMinusOne);

                try {
                    dataCollector[indexH][indexS][indexV] += 1;
                } catch (ArrayIndexOutOfBoundsException aioube) {
                    System.err.println("Index out of bounds: " + indexH + " " + indexS + " " + indexV);
                    System.exit(-1);
                }
            }
        }

        return dataCollector;
    }

    @Override
    public String toString() {
        return "HSVExtractor [name=" + name + ", blockCount="
                + blockCount + ", numberOfBins=" + numberOfBins + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFileName() {
        return this.name + "_" + this.blockCount + "_" + this.numberOfBins;
    }
}
