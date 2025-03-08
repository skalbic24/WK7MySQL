package project.service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import project.dao.ProjectDao;
import project.entity.Project;
import project.exception.DbException;



//This projects service represents the service layer of the Projects
public class ProjectService {
//    private static final String SCHEMA_FILE = "project-schema.sql";
//    private static final String DATA_FILE = "project_data.sql";
    private ProjectDao projectDao = new ProjectDao();
    
    // This method creates the projects schema, then populates the tables with data.

//    public void createAndPopulateTables() {
//        loadFromFile(SCHEMA_FILE);
//        loadFromFile(DATA_FILE);
//    }
//  
//    
//     // Loads a file from the class path. On the hard drive.
//    
//    private void loadFromFile(String fileName) {
//        String content = readFileContent(fileName);
//        List<String> sqlStatements = convertContentToSqlStatement(content);
//        
//        projectDao.executeBatch(sqlStatements);
//    }
//    
//    //This method converts the given file content to a list of SQL statements.
//    private List<String> convertContentToSqlStatement(String content) {
//        content = removeComments(content);
//        content = replaceWhitespaceSequencesWithSingleSpace(content);
//        return extractLinesFromContent(content);
//    }
//
//    //This method extracts the SQL statements in the file content. 
//    //Also a SQL statement the text is separated by semicolons.
//    private List<String> extractLinesFromContent(String content) {
//        List<String> lines = new LinkedList<>();
//        while (!content.isEmpty()) {
//            int semicolon = content.indexOf(";");
//            if (semicolon == -1) {
//                if (!content.isBlank()) {
//                    lines.add(content.trim());
//                }
//                content = "";
//            } else {
//                lines.add(content.substring(0, semicolon).trim());
//                content = content.substring(semicolon + 1).trim();
//            }
//        }
//        return lines;
//    }
//
//    //This uses a regular expression to replace all whitespace sequences with single spaces.
//    
//    private String replaceWhitespaceSequencesWithSingleSpace(String content) {
//        return content.replaceAll("\\s+", " ");
//    }
//
//    //This method removes the single-line comment from a .sql file
//    private String removeComments(String content) {
//        StringBuilder builder = new StringBuilder(content);
//        int commentPos = 0;
//
//        while ((commentPos = builder.indexOf("-- ", commentPos)) != -1) {
//          int eolPos = builder.indexOf("\n", commentPos + 1);
//
//          if (eolPos == -1) {
//            builder.replace(commentPos, builder.length(), "");
//          } else {
//            builder.replace(commentPos, eolPos + 1, "");
//          }
//        }
//
//        return builder.toString();
//      }
//
//    private String readFileContent(String fileName) {
//        try {
//        Path path =
//        	Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
//        return Files.readString(path);
//
//        } catch (Exception e) {
//          throw new DbException(e);
//        }
//       
//    }
    public Project addProject(Project project) {
    	return projectDao.insertProject(project);
    }
}  
    
    
    
    