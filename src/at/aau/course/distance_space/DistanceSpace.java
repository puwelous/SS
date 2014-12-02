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

        /************************ indexing **************************/
        
        public double computeIntrinsicDimensionality(){
            double intrinsicDimensionality;
            
            // first randomly choose 2 points and compute
            int vectorDataIndexToCompare1, vectorDataIndexToCompare2, randIndex1, randIndex2;
            VectorData vectorDataToCompare1, vectorDataToCompare2;
            double point1, point2;
            
            double avgSigma;
            double sumOfAllDistances = 0.0;
            double[] distancesArray = new double[1000];
            for (int i = 0; i < distancesArray.length; i++) {
                
                // take randomly 2 vector representation of images                
                vectorDataIndexToCompare1 = (int) (Math.random() * this.dataDescriptors.length);
                vectorDataIndexToCompare2 = (int) (Math.random() * this.dataDescriptors.length);              
                

                vectorDataToCompare1 = this.dataDescriptors[vectorDataIndexToCompare1];
                vectorDataToCompare2 = this.dataDescriptors[vectorDataIndexToCompare2];
                
                // take randomly 2 their points
                randIndex1 = (int) (Math.random() * vectorDataToCompare1.getData().length);
                randIndex2 = (int) (Math.random() * vectorDataToCompare2.getData().length);       
                
                point1 =  vectorDataToCompare1.getData()[randIndex1];
                point2 =  vectorDataToCompare2.getData()[randIndex2];
                
                
                distancesArray[i] = Math.abs(point1 - point2);
                sumOfAllDistances += distancesArray[i];
            }
            
            avgSigma = sumOfAllDistances / distancesArray.length;
            
            // compute sigma^2
            //sigma^2=SUM(sigmai - avgSigma)^2
            double sigmaSquared = 0.0;
            for (int j = 0; j < distancesArray.length; j++) {
                sigmaSquared += Math.pow((distancesArray[j] - avgSigma), 2);
            }
            
            // mean = sigma
            
            intrinsicDimensionality = (Math.pow(avgSigma, 2) / (2*sigmaSquared));
            
            return intrinsicDimensionality;
        }
}
