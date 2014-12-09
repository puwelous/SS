package at.aau.course.extractor;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class ColorHistogramLab implements IDescriptorWrapper {

    private final String name = getClass().getSimpleName();
    int quantization;
    int multiple;

    public ColorHistogramLab() {
        this(256, 1);
    }

    public ColorHistogramLab(int quantization, int multiple) {
        this.quantization = quantization;
        this.multiple = multiple;
    }

    @Override
    public double[] extract(BufferedImage img) {
        ArrayList<double[]> allHistogramms = new ArrayList<double[]>(multiple * multiple * 3);
        ArrayList<Integer> sumPixels = new ArrayList<Integer>(multiple * multiple);

        //initializing arraya
        for (int i = 0; i < multiple * multiple * 3; i++) {
            double[] histogramm = new double[quantization];
            for (int j = 0; j < histogramm.length; j++) {
                histogramm[j] = 0.0;
            }
            allHistogramms.add(histogramm);

            int zero = 0;
            sumPixels.add(zero);
        }

        //accessing pixels
        WritableRaster coloredImg = img.getRaster();
        int[] rgb = new int[3];

        //looping over every pixel
        for (int x = 0; x < coloredImg.getWidth(); x++) {
            for (int y = 0; y < coloredImg.getHeight(); y++) {
                coloredImg.getPixel(x, y, rgb);

                double rLinear = rgb[0] / 255.0;
                double gLinear = rgb[1] / 255.0;
                double bLinear = rgb[2] / 255.0;

                double rVal = rLinear > 0.04045 ? Math.pow((rLinear + 0.055) / (1.055), 2.2) : rLinear / 12.92;
                double gVal = gLinear > 0.04045 ? Math.pow((gLinear + 0.055) / (1.055), 2.2) : gLinear / 12.92;
                double bVal = bLinear > 0.04045 ? Math.pow((bLinear + 0.055) / (1.055), 2.2) : bLinear / 12.92;

                //calculate lab
                double xValue = 0.4124564 * rVal + 0.3575761 * rVal + 0.1804375 * rVal; //[0, 1]
                double yValue = 0.2126729 * gVal + 0.7151522 * gVal + 0.0721750 * gVal; //[0, 1]
                double zValue = 0.0193339 * bVal + 0.1191920 * bVal + 0.9503041 * bVal; //[0, 1]
                xValue = xValue / 0.95; //[0, 1]
                zValue = zValue / 1.09; //[0, 1]
                int l = (int) ((116 * yValue) - 16); //[-16, 100]
                int a = (int) (500 * (xValue - yValue)); //[-500, 500]
                int b = (int) (200 * (yValue - zValue)); //[-200, 200]

                int binL = quantization < 116 ? ((int) ((16 + l) / (116.1 / quantization))) : 16 + l;
                int binA = (int) ((500 + a) / (1000.1 / quantization));
                int binB = (int) ((200 + b) / (400.1 / quantization));
                int multX = x / ((coloredImg.getWidth() + multiple - 1) / multiple);
                int multY = y / ((coloredImg.getHeight() + multiple - 1) / multiple);
                int histNumber = (multX + multY * multiple) * 3;
                allHistogramms.get(histNumber)[binL]++;
                allHistogramms.get(histNumber + 1)[binA]++;
                allHistogramms.get(histNumber + 2)[binB]++;
                sumPixels.set(histNumber, sumPixels.get(histNumber) + 1);
            }
        }

        //normalize
        for (int i = 0; i < allHistogramms.size(); i++) {
            int totalPixels = sumPixels.get(i / 3);
            double[] hist = allHistogramms.get(i);
            for (int j = 0; j < hist.length; j++) {
                hist[j] = hist[j] / totalPixels * 100000; //multiply by 100000 to obtain bigger numbers
            }
        }

        double[] finalHistogramm = new double[quantization * multiple * multiple * 3];
        for (int i = 0; i < allHistogramms.size(); i++) {
            System.arraycopy(allHistogramms.get(i), 0, finalHistogramm, i * quantization, quantization);
        }

        return finalHistogramm;
    }

    @Override
    public String getFileName() {
        return this.name + "_" + quantization + "_" + multiple;
    }

    @Override
    public String toString() {
        return "ColorHistogramLab[" + "name=" + name + ", quantization=" + quantization + ", multiple=" + multiple + ']';
    }

}
