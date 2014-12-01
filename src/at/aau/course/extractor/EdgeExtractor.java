package at.aau.course.extractor;

import java.awt.image.BufferedImage;

public class EdgeExtractor implements IDescriptorWrapper {

	private String name = getClass().getSimpleName();
	
    private int mult = 1;
    private int threshold = 11;	
	
	
	
	@Override
	public double[] extract(BufferedImage image) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName() {
		return this.name + "_" + mult + "_" + threshold;
	}
}
