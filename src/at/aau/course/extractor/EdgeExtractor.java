package at.aau.course.extractor;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class EdgeExtractor implements IDescriptorWrapper {

    private final String name = getClass().getSimpleName();

    private final int mult = 1;
    private final int threshold = 11;

    final double sqrtTwo = Math.sqrt(2.0);
    final double[] ver_edge_filter = new double[]{1.0, -1.0, 1.0, -1.0};
    final double[] hor_edge_filter = new double[]{1.0, 1.0, -1.0, -1.0};
    final double[] diag45_edge_filter = new double[]{sqrtTwo, 0, 0, -sqrtTwo};
    final double[] diag135_edge_filter = new double[]{0, sqrtTwo, -sqrtTwo, 0};
    final double[] nond_edge_filter = new double[]{2.0, -2.0, -2.0, 2.0};

    private final int histogramSize = mult * mult * 5;

    // (y / heightDiv) * mult * 5
    final int yMultiplicator = mult * 5;

    public EdgeExtractor() {
    }

    @Override
    public double[] extract(BufferedImage image) {

        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();

        final int widthDiv = (int) Math.ceil((double) image.getWidth() / mult);
        final int heightDiv = (int) Math.ceil((double) image.getHeight() / mult);

        // (x / widthDiv) * 5
        final double xAddition = (5 / widthDiv);

        double[] histogram = new double[histogramSize];

        // 5 types of edges
        double[] values = new double[5];

        //System.out.println("Image properties are: width = " + imageWidth + " and hight = " + imageHeight);
        for (int y = 0; y < imageWidth - 1; ++y) {
            for (int x = 0; x < imageHeight - 1; ++x) {

                // reset values
                for (int v = 0; v < 5; ++v) {
                    values[v] = 0.0;
                }

                int i = 0;
                for (int yi = y; yi <= y + 1; yi++) {
                    for (int xi = x; xi <= x + 1; xi++) {
                        //int value = grayscaleImg.GetPixel(xi, yi).R;
                        try {
                            int value = new Color(image.getRGB(xi, yi)).getRed();
                            values[0] += value * ver_edge_filter[i];
                            values[1] += value * hor_edge_filter[i];
                            values[2] += value * diag45_edge_filter[i];
                            values[3] += value * diag135_edge_filter[i];
                            values[4] += value * nond_edge_filter[i];
                            ++i;
                        } catch (ArrayIndexOutOfBoundsException are) {
                            //System.out.println("Out of bounds:" + xi + " or " + yi);
                            //System.exit(-1);
                            continue;
                        }
                    }
                }

                int maxI = 0;
                for (int biggest = 1; biggest < 5; ++biggest) {
                    if (values[biggest] > values[maxI]) {
                        maxI = biggest;
                    }
                }

                if (values[maxI] > threshold) {
                    //int histInd = (y / heightDiv) * mult * 5 + (x / widthDiv) * 5 + maxI;
                    int histInd = (y / heightDiv) * yMultiplicator + (int) (x * xAddition) + maxI;
                    histogram[histInd]++;
                }
            }
        }

        final int pixelCount = imageWidth * imageHeight;

        for (int i = 0; i < histogram.length; ++i) {
            histogram[i] /= pixelCount;
        }

        return histogram;
    }

    @Override
    public String getFileName() {
        return this.name + "_" + mult + "_" + threshold;
    }

    @Override
    public String toString() {
        return "EdgeExtractor[" + "name=" + name + ", mult=" + mult + ", threshold=" + threshold + ']';
    }
}
