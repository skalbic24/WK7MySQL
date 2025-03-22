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
    		"3) Select a project",
    		"4) Update project details",
    		"5) Delete a project"
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

        while(!done) {
          try {
            int selection = getUserSelection();

            switch(selection) {
              case -1:
                done = exitMenu();
                break;

              case 1:
                createProject();
                break;

              case 2:
                listProjects();
                break;

              case 3:
                selectProject();
                break;
                
              case 4:
            	updateProjectDetails();
            	break;
            	  
              case 5:
                deleteProject();
                break;
              

              default:
                System.out.println("\n" + selection + " is not a valid selection. Try again.");
                break;
            }
          }
          catch(Exception e) {
            System.out.println("\nError: " + e + " Try again.");
          }
        }
      }

    /**This method deletes a project and all project child records 
     * (materials, steps and categories).
     */
      private void deleteProject() {
    	  listProjects();

    	    Integer projectId = getIntInput("Enter the ID of the project to delete");

    	    projectService.deleteProject(projectId);
    	    System.out.println("Project " + projectId + " was deleted successfully.");
    	  
    	    if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
    	      curProject = null;
    	    }
	}
      
      /**
       * This method allows the user to modify project details. The user is asked to modify each field
       * in the project. The value in the current project is displayed as a default. If the user presses
       * Enter without entering a value, the value in the current project is unchanged.
       */

	private void updateProjectDetails() {
		//If there is no current project selected, return to the menu.
		if(Objects.isNull(curProject)) {
		      System.out.println("\nPlease select a project.");
		      return;
		    }
		/*
	     * Collect input from the user. If the user presses Enter without entering a value, the local
	     * variable will be null.
	     */
	    String projectName =
	        getStringInput("Enter the project name [" + curProject.getProjectName() + "]");

	    BigDecimal estimatedHours =
	        getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");

	    BigDecimal actualHours =
	        getDecimalInput("Enter the actual hours + [" + curProject.getActualHours() + "]");

	    Integer difficulty =
	        getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");

	    String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");

	    //Create and populate a Project object.
	    Project project = new Project();

	    project.setProjectId(curProject.getProjectId());
	    project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);

	    project.setEstimatedHours(
	        Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);

	    project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
	    project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
	    project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);

	    /* Call the project service to update the project details. */
	    projectService.modifyProjectDetails(project);

	    /* Re-read the current project, which will display the new details. */
	    curProject = projectService.fetchProjectById(curProject.getProjectId());
	
	}

	/**
       * This method allows the user to select a "current" project. The current project is one on which
       * you can add materials, steps, and categories.
       */
      private void selectProject() {
        listProjects();
        Integer projectId = getIntInput("Enter a project ID to select a project");

        /*
         * Un-select the current project. This must be done as a pre-step to fetching the project because
         * fetchProjectById() will throw an exception if an invalid project ID is entered, which would
         * leave the currently selected project intact.
         */
        curProject = null;

        /* This will throw an exception if an invalid project ID is entered. */
        curProject = projectService.fetchProjectById(projectId);
      }

      /**
       * This method calls the project service to retrieve a list of projects from the projects table.
       * It then uses a Lambda expression to print the project IDs and names on the console. 
       */
      private void listProjects() {
        List<Project> projects = projectService.fetchAllProjects();

        System.out.println("\nProjects:");

        projects.forEach(project -> System.out
            .println("   " + project.getProjectId() + ": " + project.getProjectName()));
      }

      /**
       * Gather user input for a project row then call the project service to create the row.
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
        System.out.println("You have successfully created project: " + dbProject);
      }

      /**
       * Gets the user's input from the console and converts it to a BigDecimal.
       * 
       * @param prompt The prompt to display on the console.
       * @return A BigDecimal value if successful.
       * @throws DbException Thrown if an error occurs converting the number to a BigDecimal.
       */
      private BigDecimal getDecimalInput(String prompt) {
        String input = getStringInput(prompt);

        if(Objects.isNull(input)) {
          return null;
        }

        try {
          /* Create the BigDecimal object and set it to two decimal places (the scale). */
          return new BigDecimal(input).setScale(2);
        }
        catch(NumberFormatException e) {
          throw new DbException(input + " is not a valid decimal number.");
        }
      }

      /**
       * Called when the user wants to exit the application. It prints a message and returns
       * {@code true} to terminate the app.
       * 
       * @return {@code true}
       */
      private boolean exitMenu() {
        System.out.println("Exiting the menu.");
        return true;
      }

      /**
       * This method prints the available menu selections. It then gets the user's menu selection from
       * the console and converts it to an int.
       * 
       * @return The menu selection as an int or -1 if nothing is selected.
       */
      private int getUserSelection() {
        printOperations();

        Integer input = getIntInput("Enter a menu selection");

        return Objects.isNull(input) ? -1 : input;
      }

      /**
       * Prints a prompt on the console and then gets the user's input from the console. It then
       * converts the input to an Integer.
       * 
       * @param prompt The prompt to print.
       * @return If the user enters nothing, {@code null} is returned. Otherwise, the input is converted
       *         to an Integer.
       * @throws DbException Thrown if the input is not a valid Integer.
       */
      private Integer getIntInput(String prompt) {
        String input = getStringInput(prompt);

        if(Objects.isNull(input)) {
          return null;
        }

        try {
          return Integer.valueOf(input);
        }
        catch(NumberFormatException e) {
          throw new DbException(input + " is not a valid number.");
        }
      }

      /**
       * Prints a prompt on the console and then gets the user's input from the console. If the user
       * enters nothing, {@code null} is returned. Otherwise, the trimmed input is returned.
       * 
       * @param prompt The prompt to print.
       * @return The user's input or {@code null}.
       */
      private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine();

        return input.isBlank() ? null : input.trim();
      }
      
      /**
       * Print the given line on the console. Indent the line by three spaces. This method is provided
       * so that the lines can be printed using a method reference.
       * 
       * @param line The line to front.
       */
      private void printWithIndent(String line) {
        System.out.println("   " + line);
      }

      /**
       * Print the menu selections, one per line.
       */
      private void printOperations() {
        System.out.println("\nThese are the available selections. Press the Enter key to quit:");

        /* With Lambda expression */
        operations.forEach(line -> printWithIndent(line));

        if(Objects.isNull(curProject)) {
          System.out.println("\nYou are not working with a project.");
        }
        else {
          System.out.println("\nYou are working with project: " + curProject);
        }
      }
    }

