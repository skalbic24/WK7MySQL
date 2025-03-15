package project;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import project.entity.Project;
import project.exception.DbException;
import project.service.ProjectService;

/**
 * This class is a menu-driven application that accepts user input from the console. It then
 * performs CRUD operations on the project tables.
 */

public class ProjectsAPP{
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject; //
	

    // @formatter:off
    private List<String> operations = List.of(
    		"1) Add a project",
    		"2) List projects",
    		"3) Select a project"
    );
    
    // @formatter:on
     //Entry point for Java application.
     
    public static void main(String[] args) {
        ProjectsAPP app = new ProjectsAPP();
        app.processUserSelections(); // Process user selections
    }

    // Method to display the menu and process user selections
    private void processUserSelections() {
        boolean done = false;
        
        while (!done) {
            try {
                int selection = getUserSelection(); // Get user selection
                switch (selection) {
                    case -1:
                        done = exitMenu(); // Exit if user selects -1
                        break;
                    case 1:
                        createProject(); // Call method to create project
                        break;
                    case 2:
                    	listProjects();
                    	break;
                    case 3:
                    	selectProject();
                    	break;
                    default:
                        System.out.println("\n" + selection + " is not a valid selection. Try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("\nError: " + e + "Try again."); //Prints exception message
            }
        }
    }

    private void selectProject() {
    	listProjects();
    	Integer projectId = getIntInput("Enter a project ID to select a project");
    }
    
    // Un-select the current project
    curProject = null;
    
    //This will throw an exception if an invalid project ID is entered
    curProject = projectService.fetchProjectById(projectId);
    
    private void listPorjects() {
    	List<Project> projects = projectService.fetchAllProjects();
    	
    	System.out.println("\nProjects:");
    	
    	projects.forEach(project -> System.out.println("  " + project.getProjectId() 
    	+ ": " + project.getProjectName()));
    	
    }
    
    /* This method gathers users input for a project row then calls the project service to 
     * create that row
     */
    private void createProject() {
    	String projectName = getStringInput("Enter the project name");
    	BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
    	BigDecimal actualHours = getDecimalInput("Enter the actual hours");
    	Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
    	String notes = getStringInput("Enter the project notes");
    	
    	Project project = new Project();
    	
       project.setProjectName(projectName);
       project.setEstimatedHours(estimatedHours);
       project.setActualHours(actualHours);
       project.setDifficulty(difficulty);
       project.setNotes(notes);
       
       
       Project dbProject = projectService.addProject(project);
       System.out.println("You have succefully created project: " + dbProject);
    }

    //Gets the user's input from the console and converts it to a BigDecimal
    private BigDecimal getDecimalInput(String prompt) {
    	String input = getStringInput(prompt);
    	
    	if(Objects.isNull(input)) {
    		return null;
    	}
    	try {
    		//this creates BigDecimal object and set it to two decimal places.
    		return new BigDecimal(input).setScale(2);
    	}
    	catch(NumberFormatException e) {
    		throw new DbException(input + "is not a valid decimal number");
    	}
    }
    /*This method calls when the user wants to exit the application. Then it prints
     * a message and returns to terminate the app.
     */
    //return code true
    private boolean exitMenu() {
    	System.out.println("Existing the menu.");
    	return true;
    }
    
    /*This method prints the available menu selection. It then gets the user's menu
     * selection from the console and converts it to an int.
     */
    //return the menu selection as an int or  -1 if nothing is selected
    private int getUserSelection() {
    	printOperations();
    	
    	Integer input = getIntInput("Enter a meny selection");
    	
    	return Objects.isNull(input) ? -1 : input;
    }
    

	//Prints a prompt on the console and then gets the user's input from the console.
    //it also converts it to input to an Integer.
    private Integer getIntInput(String prompt) {
    	String input = getStringInput(prompt);
    	
    	if(Objects.isNull(input)) {
    		return null;
    	}
    	try {
    		return Integer.valueOf(input);
    	}
    	catch(NumberFormatException e) {
    		throw new DbException(input + "is not a valid number.");
    	}
    }
    	/**
    	 * Prints a prompt on the console and then gets the user's input from 
    	 * the console.
    	 */
    	
    	private String getStringInput(String prompt) {
    		System.out.print(prompt + ": ");
    		String input = scanner.nextLine();
    		
    		return input.isBlank() ? null : input.trim();
    	}
    	
    	// This will print the menu selection, one per line.
    	private void printOperations() {
    		System.out.println("\nThese are the available selection. Press the Enter key to quit:");
    		
    		//with Lambda expression
    		operations.forEach(line -> System.out.println(" " + line));
    		
    		//Enhanced for loop
    		
//    		for(String line : operations) {
//    			System.out.println(" " +line);
//    		}
    		
    		if(Objects.isNull(curProject)) {
    			System.out.println("\nYou are not working with a project.");
    		}
    		else {
    			System.out.println("\nYou are working with project: " + curProject);
    		}
     }
}
