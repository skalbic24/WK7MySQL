package project.dao;

import java.math.BigDecimal;  //handling my decimal values(estimated, hours)
import java.sql.Connection;   //managing SQL connections
import java.sql.PreparedStatement;  //executing pre-compiled SQL statements
import java.sql.SQLException;      //handling SQL exceptions
import project.entity.Project;    //project entity class
import project.exception.DbException;  //custom data exceptions
import provided.util.DaoBase;         //DAO utilities that provide common functionality


//This class uses JDBC to perform CRUD operations on the project tables.

//@SuppressWarnings("unused")    //Here are the constants for table names used in SQL queries
public class ProjectDao extends DaoBase {
	private static final String CATERGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "project";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String STEP_TABLE = "step";
	
	/**Insert a project row into the project table.
	 * @param project object with the primary key
	 * @param sqlBatch
	 * @return The project object with the primary key.
	 * @throws DbException Thrown if an error occurs inserting the row
	 */
	public Project insertProject(Project project) {
		//SQL query to insert a new project into the PROJECT_TABLE
		//@formatter:off
		String sql = ""
				+ " INSERT INTO " + PROJECT_TABLE + " "
				+ " (project_name, estimated_hours, actual_hours, difficulty, notes) "
				+ " VALUES "
				+ " (?, ?, ?, ?, ?) ";
		//@formatter:on
		
		//Establish a database connection
		try(Connection conn= DbConnection.getConnection()){
			startTransaction(conn);   //Start a new database transaction 
			
			//Prepare the SQL statement for execution
			try(PreparedStatement stmt = conn.prepareStatement(sql)) {
				
		//Set the parameters for the prepared statement from the projects object
				
				setParameter(stmt, 1, project.getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);
				
				//Executes the update, which inserts the project into the database
				stmt.executeUpdate();
				
				//Retrieves the last inserted project's ID
				Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
				commitTransaction(conn);
				
				//Sets the project ID in the project object and returns it
				project.setProjectId(projectId);
				return project;
			}
			catch(Exception e) {
				rollbackTransaction(conn);   //Rollback the transaction in case of an error
				throw new DbException(e);  //wrap and throws a custom exception
			}
		}
		catch(SQLException e) {
			throw new DbException(e);  //wrap and throw a custom exception for SQL errors
		}
		
		
	}
	
//	public void executeBatch(List<String> sqlBatch) {
//		
//	}

}
