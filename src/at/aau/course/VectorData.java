package at.aau.course;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class VectorData implements Serializable {

	private static final long serialVersionUID = 487426385267754094L;

	int id;

	int classId;

        String fileName;
        
	double[] data;

	public VectorData() {
		super();
	}	
	
	public VectorData(int id, int classId, String fileName, double[] data) {
		super();
		this.id = id;
		this.classId = classId;
                this.fileName = fileName;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public double[] getData() {
		return data;
	}

	public void setData(double[] data) {
		this.data = data;
	}

	public String saveToString() {
		return this.id + ";" + this.classId + ";" + this.fileName + ";"
				+ Arrays.toString(this.getData());
	}

	public VectorData readFromString(String string){
		
		final int indexOfFirstColon = string.indexOf(';', 0);
		final int indexOfSecondColon = string.indexOf(';', indexOfFirstColon + 1);
                final int indexOfThirdColon = string.indexOf(';', indexOfSecondColon + 1);

		final int thisId = Integer.parseInt(string.substring(0,indexOfFirstColon));
		final int thisClassid = Integer.parseInt(string.substring(indexOfFirstColon+1, indexOfSecondColon));
                final String thisFileName = string.substring(indexOfSecondColon+1, indexOfThirdColon);
		
		String dataArrayString = string.substring(indexOfThirdColon+2);
		
		 List<String> list = Arrays.asList(dataArrayString.substring(1, dataArrayString.length() - 1).split(", "));		
		
		double[] result = new double[ list.size() ];
		int i = 0;
		 
		for (String doubleAsString : list) {
			result[i] = Double.parseDouble(doubleAsString);
			i++;
		}
		 
		this.setId(thisId);
		this.setClassId(thisClassid);
                this.setFileName(thisFileName);
		this.setData(result);
		
		return this;

	}
	
	public boolean isRelevant(VectorData otherVectorData){
		return this.getClassId() == otherVectorData.getClassId();
	}

        
        @Override
        public boolean equals( Object object2){
            
            if( this.getClass() != object2.getClass() ){
                return false;
            }
            
            VectorData object2AsVectorData = (VectorData) object2;
            
            if( this.getClassId() != object2AsVectorData.getClassId() ){
                return false;
            }
            
            if( !this.getFileName().equalsIgnoreCase(object2AsVectorData.getFileName())){
                return false;
            }
            
            // else
            return true;
        }
        
	@Override
	public String toString() {
		return "VectorData [id=" + id + ", classId=" + classId + ", fileName=" + fileName + "]";
	}
        
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
