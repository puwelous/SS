package at.aau.course.extractor;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GrayScaleHistogram implements IDescriptorWrapper {

	private String name = getClass().getSimpleName();
	private int blockCount;
	private final int numberOfBins;

	// ************ optimization ************
	final int numOfBinsDivBy256;
	final int numberOfBinsMinusOne;
	// **************************************
	
	public GrayScaleHistogram(int blockCount, int numberOfBins) {
		this.blockCount = blockCount;
		this.numberOfBins = numberOfBins;
		numberOfBinsMinusOne = numberOfBins - 1;
                numOfBinsDivBy256 = (int)(this.numberOfBins / 256);
	}

	@Override
	public double[] extract(BufferedImage image) {

		int blockWidth = image.getWidth() / blockCount;
		int blockHeight = image.getHeight() / blockCount;

		List<double[]> histograms = new ArrayList<double[]>();
		List<Rectangle> rectangles = new ArrayList<Rectangle>();

		for (int i = 0; i < blockCount; i++) {
			for (int j = 0; j < blockCount; j++) {

				histograms.add(new double[numberOfBins]);

				rectangles.add(new Rectangle(i * blockWidth, j * blockHeight,
						blockWidth, blockHeight));

				double[] computedHistogram = extractBlock(image,
						rectangles.get(rectangles.size() - 1),
						histograms.get(histograms.size() - 1));

				histograms.set(histograms.size() - 1, computedHistogram);
			}
		}
		
		rectangles = null;
		
		double[] concatenatedHistogram = new double[ blockCount * blockCount * numberOfBins];
		int index = 0;
		
		for (double[] histogramSingleArray : histograms) {
			System.arraycopy( histogramSingleArray, 0, concatenatedHistogram, index, histogramSingleArray.length );
			index +=  histogramSingleArray.length;
			histogramSingleArray = null;
		}
		//System.out.println("- "+Arrays.toString(concatenatedHistogram));
		return concatenatedHistogram;
	}

	private double[] extractBlock(BufferedImage image, Rectangle rectangle,
			double[] histogram) {

		// ************ optimization ************
//		final int upperBoundRow = rectangle.x + rectangle.width - 1;
//		final int upperBoundColumn = rectangle.y + (int) rectangle.getHeight() - 1;
		// **************************************
		
		// For int i = r.Left to r.Left + r.Width � 1
		for (int i = rectangle.x; i < rectangle.x + rectangle.width - 1; i++) {
			// For int j = r.Top to r.Top + r.Height � 1
			for (int j = rectangle.y; j < rectangle.y + (int) rectangle.getHeight() - 1; j++) {

				// Int grayScale = img.GetPixel(i, j).ToGrayScale()
				int grayScale = GrayScaleHistogram.colorToGrayScale(new Color(image
						.getRGB(i, j)));

				// histogram[Min(grayScale * numberOfBins / 256, numberOfBins -
				// 1)] += 1
				histogram[Math.min(grayScale * this.numberOfBins / 256,
						this.numberOfBins - 1)] += 1;
				// optimization:
//				histogram[Math.min(grayScale * numOfBinsDivBy256,
//						numberOfBinsMinusOne)] += 1;
			}
		}

		return histogram;
	}

	public static int colorToGrayScale(Color c) {
		return (int) ((c.getRed() * 0.299) + (c.getGreen() * 0.587) + (c
				.getBlue() * 0.114));
	}

	@Override
	public String toString() {
		return "GrayScaleHistogram [name=" + name + ", blockCount="
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

//package at.aau.course.extractor;
//
//import java.awt.Color;
//import java.awt.Rectangle;
//import java.util.List;
//import java.awt.image.BufferedImage;
//import java.util.ArrayList;
//
//public class GrayScaleHistogram implements IDescriptorWrapper {
//
//	private final int blockCount;
//	private final int numberOfBins;
//
//	public GrayScaleHistogram(int blockCount,final int numberOfBins) {
//		System.out.println(blockCount);
//		this.blockCount = blockCount;
//		this.numberOfBins = numberOfBins;
//	}
//
//	@Override
//	public double[] extract(BufferedImage image) {
//
//		int blockWidth = image.getWidth() / blockCount;
//		int blockHeight = image.getHeight() / blockCount;
//
//		List<double[]> histograms = new ArrayList<double[]>();
//		List<Rectangle> rectangles = new ArrayList<Rectangle>();
//
//		for (int i = 0; i < blockCount; i++) {
//			for (int j = 0; j < blockCount; j++) {
//
//				histograms.add(new double[numberOfBins]);
//
//				rectangles.add(new Rectangle(i * blockWidth, j * blockHeight,
//						blockWidth, blockHeight));
//
//				double[] computedHistogram = extractBlock(image,
//						rectangles.get(rectangles.size() - 1),
//						histograms.get(histograms.size() - 1));
//
//				histograms.set(histograms.size() - 1, computedHistogram);
//			}
//		}
//		
//		
//		double[] concatenatedHistogram = new double[ blockCount * blockCount * numberOfBins];
//		int concatHistIndex = 0;
//		
//		
//		for (double[] histogramSingleArray : histograms) {
//			for( int k = 0 ; k < histogramSingleArray.length ; k++ ){
//				concatenatedHistogram[ concatHistIndex ] = histogramSingleArray[k];
//				concatHistIndex++;
//			}
//		}
//		
//		return concatenatedHistogram;
//	}
//
//	private double[] extractBlock(BufferedImage image, Rectangle rectangle,
//			double[] histogram) {
//
//		// For int i = r.Left to r.Left + r.Width � 1
//		for (int i = rectangle.x; i < rectangle.x + rectangle.width - 1; i++) {
//			// For int j = r.Top to r.Top + r.Height � 1
//			for (int j = rectangle.y; j < rectangle.y + rectangle.getHeight()
//					- 1; j++) {
//
//				// Int grayScale = img.GetPixel(i, j).ToGrayScale()
//				int grayScale = GrayScaleHistogram.colorToGrayScale(new Color(image
//						.getRGB(i, j)));
//
//				// histogram[Min(grayScale * numberOfBins / 256, numberOfBins -
//				// 1)] += 1
//				histogram[Math.min(grayScale * this.numberOfBins / 256,
//						this.numberOfBins - 1)] += 1;
//			}
//		}
//
//		return histogram;
//	}
//
//	public static int colorToGrayScale(Color c) {
//		return (int) ((c.getRed() * 0.299) + (c.getGreen() * 0.587) + (c
//				.getBlue() * 0.114));
//	}
//
//	@Override
//	public String getFileName() {
//		return "GrayScaleHistogram" + "_" + this.blockCount + "_" + this.numberOfBins;
//	}
//	
//}

