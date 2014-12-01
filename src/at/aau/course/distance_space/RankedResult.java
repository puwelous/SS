package at.aau.course.distance_space;

import java.util.Comparator;

import at.aau.course.VectorData;

public class RankedResult implements Comparable<RankedResult> {

	private VectorData vectorData;

	private double distance;

	public VectorData getVectorData() {
		return vectorData;
	}

	public void setVectorData(VectorData vectorData) {
		this.vectorData = vectorData;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public RankedResult(VectorData vectorData, double distance) {
		super();
		this.vectorData = vectorData;
		this.distance = distance;
	}

	@Override
	public int compareTo(RankedResult rr2) {
		return (int) (this.getDistance() - rr2.getDistance());
	}

	public class RankedResultComparator implements Comparator<RankedResult> {

		@Override
		public int compare(RankedResult rr1, RankedResult rr2) {

			return rr1.compareTo(rr2);
		}
	}
}
