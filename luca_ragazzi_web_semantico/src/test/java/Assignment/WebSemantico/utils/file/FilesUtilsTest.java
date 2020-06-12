package Assignment.WebSemantico.utils.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import org.junit.Test;

public class FilesUtilsTest {
	private static final String testDirPath = FilesUtils.getDirPathIntoUserHome("AP_WSAssignment_Test");
	private static final String testFileName = "TEST_FILES_UTILS.txt";
	
	@Test public void testCreateAndDeleteDirectoryWithFile() {		
		assertFalse("Delete directory should return 'false' since the directory doesn't exists yet", 
				FilesUtils.deleteDirectory(new File(testDirPath)));
		
		FilesUtils.createTxtFile(testDirPath, testFileName, "Simple txt file...");
		
		assertTrue("File exists should return 'true'", 
				FilesUtils.checkFileExists(testDirPath, testFileName));
		
		assertTrue("Delete directory should return 'true'", 
				FilesUtils.deleteDirectory(new File(testDirPath)));
		
		assertFalse("File exists should return 'false' since the file and the directory are been deleted", 
				FilesUtils.checkFileExists(testDirPath, testFileName));
    } 
	
	@Test public void testCreateAndDeleteFile() {		
		assertFalse("Delete file should return 'false' since the file doesn't exists yet", 
				FilesUtils.deleteFile(new File(testDirPath, testFileName)));
		
		FilesUtils.createTxtFile(testDirPath, testFileName, "Simple txt file...");
		
		assertTrue("File exists should return 'true'", 
				FilesUtils.checkFileExists(testDirPath, testFileName));
		
		assertTrue("Delete file should return 'true'", 
				FilesUtils.deleteFile(new File(testDirPath, testFileName)));
		
		assertTrue("Delete directory should return 'true'", 
				FilesUtils.deleteDirectory(new File(testDirPath)));
		
		assertFalse("File exists should return 'false' since the file and the directory are been deleted", 
				FilesUtils.checkFileExists(testDirPath, testFileName));
    } 
}
