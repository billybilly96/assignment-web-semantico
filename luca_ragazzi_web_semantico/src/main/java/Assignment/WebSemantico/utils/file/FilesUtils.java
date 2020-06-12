package Assignment.WebSemantico.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import Assignment.WebSemantico.utils.logger.Logger;
import Assignment.WebSemantico.utils.logger.MessageType;

public class FilesUtils {
	private static final String className = "FilesUtils";
	public static final String userHomePath = System.getProperty("user.home");
	public static final String fileSeparator = File.separator;
	
	private static ClassLoader getClassLoader() {
		return ClassLoader.getSystemClassLoader();
	}
	
	public static String getDirPathIntoUserHome(String directoryName) {
		return userHomePath + fileSeparator + directoryName;
	}
	
    /**
     * Reads given resource file as a string.
     * https://stackoverflow.com/questions/6068197/utils-to-read-resource-text-file-to-string-java
     *
     * @param fileName path to the resource file
     * @return the File
     * @throws IOException if read fails for any reason
     */
    public static File getResourceFileAsFile(String fileName) throws IOException {
        URL resource = getClassLoader().getResource(fileName);
        Logger.log(MessageType.DEBUG, className, "File readed from resource: " + resource);
        return new File(resource.getFile());
    }
    
    /**
     * Reads given resource file as a string.
     * https://stackoverflow.com/questions/6068197/utils-to-read-resource-text-file-to-string-java
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    public static String getResourceFileAsString(String fileName) throws IOException {
        try (InputStream is = getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
            		BufferedReader reader = new BufferedReader(isr)) {
                	return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
    
    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return deleteFile(directoryToBeDeleted);
    }
    
    public static boolean deleteFile(File fileToBeDeleted) {
    	return fileToBeDeleted.delete();
    }
    
    public static boolean createDirectoryIfDoesntExists(String dirPath) {
    	// If doesn't exists creates the dir into the user home.
    	Path path = Paths.get(dirPath);
    	if (!Files.exists(path)) {
	    	try {
	    		Files.createDirectory(path);
	    	} catch (IOException e) {
	    		Logger.log(MessageType.ERROR, className, e.getMessage());
	    		if(!Logger.disableStackTrace) e.printStackTrace();
	    		return false;
	    	}
    	}
		return true;
    }
    
    public static boolean createTxtFile(String fileDir, String fileName, String content) {
    	FilesUtils.createDirectoryIfDoesntExists(fileDir);
		File a = new File(fileDir, fileName);
		FileWriter fw = null;
		try	{
			fw = new FileWriter (a);
			fw.write (content);
			fw.close();
		} catch (IOException e)	{
			Logger.log(MessageType.ERROR, className, e.getMessage());
    		if(!Logger.disableStackTrace) e.printStackTrace();
			return false;
		}
		return true;
    }
    
    public static boolean checkFileExists(String fileDir, String fileName) {
    	return new File(fileDir, fileName).exists();
    }
}
