package at.aau.course.util.environment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import at.aau.course.extractor.IDescriptorWrapper;

public class EnvironmentPreparationUnit {

    String userDirSeparatorIncluded = System.getProperty("user.dir") + System.getProperty("file.separator");

    private final File INPUT_DIR;

    private final HashMap<String, File> VECTOR_DATA_FILES;

    private final File MAPPING_FILE;

    private final File PIVOT_TABLE_FILE;

    public EnvironmentPreparationUnit() {
        INPUT_DIR = new File(userDirSeparatorIncluded + "aloi_big");
        MAPPING_FILE = new File(userDirSeparatorIncluded + "map.txt");
        VECTOR_DATA_FILES = new HashMap<String, File>();
        PIVOT_TABLE_FILE = new File(userDirSeparatorIncluded + "pivot_table.txt");
    }

    public void prepareEnvironment(boolean isFileGenerationRequired, List<IDescriptorWrapper> descriptorWrappers)
            throws IOException {

        if (!isFileGenerationRequired) {
            return;
        }

        String descWrapperName;
        File descWrapperFile;

        // create single file for storing mapping names to classes information
        if ((MAPPING_FILE.exists() && !MAPPING_FILE.delete())
                || !MAPPING_FILE.createNewFile()) {
            throw new IOException("Mapping file already exists!");
        }

        // create a single file per extractor (descriptorWrapper)
        for (IDescriptorWrapper iDescriptorWrapper : descriptorWrappers) {
            descWrapperName = iDescriptorWrapper.getFileName();
            descWrapperFile = new File(userDirSeparatorIncluded + descWrapperName);

                        //System.out.println("Adding file " +descWrapperFile.getName() + " to " + descWrapperName + " at " + System.currentTimeMillis());
            VECTOR_DATA_FILES.put(
                    descWrapperName,
                    descWrapperFile);

            // delete file if exists
            if ((descWrapperFile.exists() && !descWrapperFile.delete())
                    || !descWrapperFile.createNewFile()) {
                throw new IOException("Vector data file recreation not successful!");
            }
        }

        //System.out.println("prepareEnvironment() " + VECTOR_DATA_FILES.size() );
    }

    public File getInputDir() {
        return INPUT_DIR;
    }

    public HashMap<String, File> getVectorDataFile() {
            //System.out.println("Asking for vector data file " + System.currentTimeMillis());
        //System.out.println("getVectorDataFile() " + VECTOR_DATA_FILES.size() );
        return VECTOR_DATA_FILES;
    }

    public File getMappingFile() {
        return MAPPING_FILE;
    }
    
    public File getPivotTableFile() {
        return PIVOT_TABLE_FILE;
    }    

    public int getFileCountInDir(File scannedDir) {
        int count = 0;

        File[] directories = scannedDir.listFiles();
        for (int i = 0; i < directories.length; i++) {
            File directory = directories[i];
            if (directory.isDirectory()) {
                count += directory.listFiles().length;
            } else {
                count++;
            }
        }

        return count;
    }
}
