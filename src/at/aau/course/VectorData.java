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
		
		 List<String> list = Arrays.asList(dataArrayString.substring(0, dataArrayString.length() - 1).split(", "));		
		
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
    
    
    public static void main(String[] args){
        
        String str = "0;1;1_r0.png;[15.0, 3.0, 7.0, 89.0, 169.0, 26.0, 24.0, 48.0, 1644.0, 1253.0, 323.0, 20.0, 889.0, 512.0, 479.0, 0.0, 97.0, 67.0, 248.0, 540.0, 7218.0, 99.0, 35.0, 4.0, 11521.0, 7.0, 0.0, 0.0, 1358.0, 0.0, 0.0, 0.0, 10.0, 4.0, 0.0, 0.0, 43.0, 0.0, 0.0, 0.0, 98.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 33.0, 0.0, 1.0, 3.0, 1.0, 1.0, 17.0, 27.0, 0.0, 27.0, 313.0, 33.0]";
        
        VectorData vd = new VectorData();
        vd.readFromString(str);
        
        System.out.println(vd.toString());
        System.out.println(Arrays.toString(vd.data));
    }
}
