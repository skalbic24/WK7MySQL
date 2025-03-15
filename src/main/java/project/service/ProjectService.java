package project.service;


import java.util.List;          //using list data structures 
import java.util.NoSuchElementException;
import project.dao.ProjectDao;  //data access object that interacts with the project table
import project.entity.Project;  //this is project entity class



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
    
    
    
}  
    
    
    
    