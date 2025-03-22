package project.dao;

import java.math.BigDecimal;  //handling my decimal values(estimated, hours)
import java.sql.Connection;   //managing SQL connections
import java.sql.PreparedStatement;  //executing pre-compiled SQL statements
import java.sql.ResultSet;
import java.sql.SQLException;      //handling SQL exceptions
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import project.entity.Category;
import project.entity.Material;
import project.entity.Project;    //project entity class
import project.entity.Step;
import project.exception.DbException;  //custom data exceptions
import provided.util.DaoBase;         //DAO utilities that provide common functionality


//This class uses JDBC to perform CRUD operations on the project tables.
//Here are the constants for table names used in SQL queries
public class ProjectDao extends DaoBase {
	private static final String CATEGORY_TABLE = "category";
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
	  
	 public List<Project> fetchAllProjects() {
		    String sql = "SELECT * FROM " + PROJECT_TABLE + " ORDER BY project_name";

		    try(Connection conn = DbConnection.getConnection()) {
		      startTransaction(conn);

		      try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		        try(ResultSet rs = stmt.executeQuery()) {
		          List<Project> projects = new LinkedList<>();

		          while(rs.next()) {
		            projects.add(extract(rs, Project.class));
		          }
		          return projects;
		        }
		      }
		      catch(Exception e) {
		        rollbackTransaction(conn);
		        throw new DbException(e);
		      }
		    }
		    catch(SQLException e) {
		      throw new DbException(e);
		    }
		  }
	
	 
	 public Optional<Project> fetchProjectById(Integer projectId) {
		    String sql = "SELECT * FROM " + PROJECT_TABLE + " WHERE project_id = ?";

		    try(Connection conn = DbConnection.getConnection()) {
		      startTransaction(conn);
	
		     
		      try {
		        Project project = null;

		        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		          setParameter(stmt, 1, projectId, Integer.class);
		          
		          try(ResultSet rs = stmt.executeQuery()) {
		              if(rs.next()) {
		                project = extract(rs, Project.class);
		              }
		            }
		          }
		        
		      
		        if(Objects.nonNull(project)) {
		          project.getMaterials().addAll(fetchMaterialsForProject(conn, projectId));
		          project.getSteps().addAll(fetchStepsForProject(conn, projectId));
		          project.getCategories().addAll(fetchCategoriesForProject(conn, projectId));
		        }

		        commitTransaction(conn);

		     
		        return Optional.ofNullable(project);
		      }
		      catch(Exception e) {
		        rollbackTransaction(conn);
		        throw new DbException(e);
		      }
		    }
		    catch(SQLException e) {
		      throw new DbException(e);
		    }
		  }
	 
	 
	 private List<Category> fetchCategoriesForProject(Connection conn, Integer projectId) {
		    // @formatter:off
		    String sql = ""
		        + "SELECT c.* FROM " + CATEGORY_TABLE + " c "
		        + "JOIN " + PROJECT_CATEGORY_TABLE + " pc USING (category_id) "
		        + "WHERE project_id = ?";
		    // @formatter:on

		    try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		      setParameter(stmt, 1, projectId, Integer.class);

		      try(ResultSet rs = stmt.executeQuery()) {
		        List<Category> categories = new LinkedList<>();

		        while(rs.next()) {
		          categories.add(extract(rs, Category.class));
		        }

		        return categories;
		      }
		    }
		    catch(SQLException e) {
		      throw new DbException(e);
		    }
		  }
	
	 
	 private List<Step> fetchStepsForProject(Connection conn, Integer projectId) throws SQLException {
		    String sql = "SELECT * FROM " + STEP_TABLE + " WHERE project_id = ?";

		    try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		      setParameter(stmt, 1, projectId, Integer.class);

		      try(ResultSet rs = stmt.executeQuery()) {
		        List<Step> steps = new LinkedList<>();

		        while(rs.next()) {
		          steps.add(extract(rs, Step.class));
		        }

		        return steps;
		      }
		    }
		  }
	
	 
	 private List<Material> fetchMaterialsForProject(Connection conn, Integer projectId)
		      throws SQLException {
		    String sql = "SELECT * FROM " + MATERIAL_TABLE + " WHERE project_id = ?";

		    try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		      setParameter(stmt, 1, projectId, Integer.class);

		      try(ResultSet rs = stmt.executeQuery()) {
		        List<Material> materials = new LinkedList<>();

		        while(rs.next()) {
		          materials.add(extract(rs, Material.class));
		        }

		        return materials;
		      }
		    }
		  }
	
	 
	 public boolean modifyProjectDetails(Project project) {
		    // @formatter:off
		    String sql = ""
		        + "UPDATE " + PROJECT_TABLE + " SET "
		        + "project_name = ?, "
		        + "estimated_hours = ?, "
		        + "actual_hours = ?, "
		        + "difficulty = ?, "
		        + "notes = ? "
		        + "WHERE project_id = ?";
		    // @formatter:on

		    try(Connection conn = DbConnection.getConnection()) {
		      startTransaction(conn);

		      try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		        setParameter(stmt, 1, project.getProjectName(), String.class);
		        setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
		        setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
		        setParameter(stmt, 4, project.getDifficulty(), Integer.class);
		        setParameter(stmt, 5, project.getNotes(), String.class);
		        setParameter(stmt, 6, project.getProjectId(), Integer.class);

		        boolean modified = stmt.executeUpdate() == 1;
		        commitTransaction(conn);

		        return modified;
		      }
		      catch(Exception e) {
		        rollbackTransaction(conn);
		        throw new DbException(e);
		      }
		    }
		    catch(SQLException e) {
		      throw new DbException(e);
		    }
		  }
	 
	 
	 public boolean deleteProject(Integer projectId) {
		    String sql = "DELETE FROM " + PROJECT_TABLE + " WHERE project_id = ?";

		    try(Connection conn = DbConnection.getConnection()) {
		      startTransaction(conn);

		      try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		        setParameter(stmt, 1, projectId, Integer.class);

		        /*
		         * If the project ID is correct, the number of rows modified will be 1. This is the value
		         * returned by executeUpdate(). The value will be 1 even if child rows are deleted because
		         * ON DELETE CASCADE is specified.
		         */
		        boolean deleted = stmt.executeUpdate() == 1;

		        commitTransaction(conn);
		        return deleted;
		      }
		      catch(Exception e) {
		        rollbackTransaction(conn);
		        throw new DbException(e);
		      }
		    }
		    catch(SQLException e) {
		      throw new DbException(e);
		    }
		  }
}
