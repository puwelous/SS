package at.aau.course.distance_space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.aau.course.VectorData;
import at.aau.course.distance.IDistance;

public class DistanceSpace {

	VectorData[] dataDescriptors;

	// distance calculator
	IDistance distance;

	public DistanceSpace(VectorData[] dataDescriptors, IDistance distance) {
		super();
		this.dataDescriptors = dataDescriptors;
		this.distance = distance;
	}
	
	public VectorData[] getDataDescriptors() {
		return dataDescriptors;
	}

	public void setDataDescriptors(VectorData[] dataDescriptors) {
		this.dataDescriptors = dataDescriptors;
	}

	public IDistance getDistance() {
		return distance;
	}

	public void setDistance(IDistance distance) {
		this.distance = distance;
	}

	public RankedResult[] sortDBAccordingToQuery(VectorData query) {
		
		List<RankedResult> resultAsList = new ArrayList<RankedResult>();
		
		for (int i = 0; i < dataDescriptors.length; i++) {
			
			resultAsList.add(
					new RankedResult(
							dataDescriptors[i], distance.compute(query, dataDescriptors[i])
							)
					);
		}

		// sort
		Collections.sort(resultAsList);
		
		// convert and return
		return resultAsList.toArray(new RankedResult[dataDescriptors.length] );
	}
	
	double ComputeMAP(VectorData[] queries){
		double MAP = 0;
		
		for (VectorData vectorData : queries) {
			MAP += avgPrecisionAsSum(vectorData);
		}
		
		return (MAP / queries.length);
	}

	private double avgPrecisionAsSum(VectorData vectorData) {

		RankedResult[] rankedResults = this.sortDBAccordingToQuery(vectorData);
		
		int i = 0;
		int match = 0;
		double AP = 0;
		
		/******** optimization *************/
		final int vectorDataClassId = vectorData.getClassId();
		/***********************************/
		
		for (RankedResult rankedResult : rankedResults) {
			i++;
			if( vectorDataClassId ==  rankedResult.getVectorData().getClassId() ){
				match++;
				AP += match / i;
			}
		}
		
		return AP/match;
	}


}
