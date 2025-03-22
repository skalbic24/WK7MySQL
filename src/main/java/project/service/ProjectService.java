package project.service;


import java.util.List;          //using list data structures 
import java.util.NoSuchElementException;
import project.dao.ProjectDao;  //data access object that interacts with the project table
import project.entity.Project;  //this is project entity class
import project.exception.DbException;


//This projects service represents the service layer of the Projects

public class ProjectService {
    private ProjectDao projectDao = new ProjectDao(); //accessing database operations
    


    //this method adds a project by delegating to my ProjectDao
    public Project addProject(Project project) {
    	return projectDao.insertProject(project);   //Calls the DAO to insert the project and return it.
    	
    }
    
    /**
     * This method calls the project DAO to retrieve all project rows without accompanying details
     * (materials, steps and categories).
     */
    public List<Project> fetchAllProjects() {
        return projectDao.fetchAllProjects();
      }
    
    /**
     * This method calls the project DAO to get all project details, including materials, steps, and
     * categories. If the project ID is invalid, it throws an exception.
     */
    public Project fetchProjectById(Integer projectId) {
        return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException(
            "Project with project ID=" + projectId + " does not exist."));
      }
    /**
     * This method calls the project DAO to modify the project details row in the project table. The
     * DAO will return {@code true} if the row was modified. It returns {@code false} if the row was
     * not modified (i.e., if an invalid project ID was specified). In this latter case, a DbException
     * is thrown.
     * 
     * @param project The Project object with modified details.
     * @throws DbException Thrown if an invalid project ID is specified.
     */
    public void modifyProjectDetails(Project project) {
      if(!projectDao.modifyProjectDetails(project)) {
        throw new DbException("Project with ID=" + project.getProjectId() + " does not exist.");
      }
    }

    /**
     * This method calls the project DAO to delete the project with the given project ID. If an
     * invalid project ID is given, an exception is thrown.
     * 
     * @param projectId The project ID of the project to delete.
     * @throws DbException Thrown if the project ID does not exist.
     */
    public void deleteProject(Integer projectId) {
      if(!projectDao.deleteProject(projectId)) {
        throw new DbException("Project with ID=" + projectId + " does not exist.");
      }
    }
}  
    
    
    
    