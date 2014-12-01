package at.aau.course.distance;

import at.aau.course.VectorData;

public class LpNorm implements IDistance {

	private double p;

	public LpNorm(double p) {
		super();
		
//		if( p < 1 )
//		{
//			throw new IllegalArgumentException();
//		}
		
		this.p = p;
	}

	@Override
	public double compute(VectorData x, VectorData y) {

		double result = 0.0;

		double[] xData = x.getData();
		double[] yData = y.getData();

		for (int i = 0; i < xData.length && i < yData.length; i++) 
		{
			result += this.compute( xData[i], yData[i] );
		}
		
		return result;
	}
	
	@Override
	public double compute(double x, double y){
		// System.out.println(Math.pow(5, 2)); //25.0 5^2
		 return Math.pow( Math.pow(Math.abs(x - y), p), (1.0 / (double) p) );
	}
	
	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}
}
