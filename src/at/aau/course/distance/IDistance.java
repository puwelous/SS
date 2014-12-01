package at.aau.course.distance;

import at.aau.course.VectorData;

public interface IDistance {

	// per sets of doubles
	double compute(VectorData x, VectorData y);
	
	// per tupple of doubles
	double compute(double x, double y);
}
