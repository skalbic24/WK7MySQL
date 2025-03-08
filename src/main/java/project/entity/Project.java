package project.entity;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

//This class represents a project entity with relevant attributes and methods
public class Project {
  private Integer projectId;
  private String projectName;
  private BigDecimal estimatedHours;
  private BigDecimal actualHours;
  private Integer difficulty;
  private String notes;

  //List to hold associated materials, steps, and categories for the project.
  private List<Material> materials = new LinkedList<>();
  private List<Step> steps = new LinkedList<>();
  private List<Category> categories = new LinkedList<>();
  
  /**
   * Then we have bunch of getter and setter methods for each attribute 
   * are provided to access and modify the project properties.
   * 
   */

  public Integer getProjectId() {
    return projectId;
  }

  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public BigDecimal getEstimatedHours() {
    return estimatedHours;
  }

  public void setEstimatedHours(BigDecimal estimatedHours) {
    this.estimatedHours = estimatedHours;
  }

  public BigDecimal getActualHours() {
    return actualHours;
  }

  public void setActualHours(BigDecimal actualHours) {
    this.actualHours = actualHours;
  }

  public Integer getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Integer difficulty) {
    this.difficulty = difficulty;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public List<Material> getMaterials() {
    return materials;
  }

  public List<Step> getSteps() {
    return steps;
  }

  public List<Category> getCategories() {
    return categories;
  }

  /**
   * Override the toString method to provide a string 
   * representation of the Project object.
   */
  @Override
  public String toString() {
    String result = "";
    
    //Append project details to the results string.
    result += "\n   ID=" + projectId;
    result += "\n   name=" + projectName;
    result += "\n   estimatedHours=" + estimatedHours;
    result += "\n   actualHours=" + actualHours;
    result += "\n   difficulty=" + difficulty;
    result += "\n   notes=" + notes;
    
    
    //Append materials to the result string
    result += "\n   Materials:";
    for(Material material : materials) {
      result += "\n      " + material;  //Calls the toString method of Material
    }
    
    //Append steps to the result string
    result += "\n   Steps:";
    for(Step step : steps) {
      result += "\n      " + step;  //calls the toString method of Step
    }
    
    //Append categories to the result string
    result += "\n   Categories:";
    for(Category category : categories) {
      result += "\n      " + category;   //calls the toString method of Category
    }
    
    return result;     //return the complete string representation of the project
  }
}
