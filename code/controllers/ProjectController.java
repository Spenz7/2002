package controllers;

import models.BTOProject;
import models.HDBOfficer;
import models.Applicant;
import models.enums.FlatType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectController {
    private List<BTOProject> projects; // Holds all BTO project records

    // Constructor: Initialize projects
    public ProjectController(List<BTOProject> projects) {
        this.projects = projects; // Assign provided projects to the field
    }

    // Retrieve projects for a specific applicant based on visibility and eligibility
    public List<BTOProject> getProjectsForApplicant(String applicantNric, boolean isSingle, int age) {
        return projects.stream()
                .filter(BTOProject::isVisible) // Filter visible projects
                .collect(Collectors.toList()); // Add other filters for eligibility, if needed
    }

    // Adds a new BTO project to the list
    public void createProject(BTOProject project) {
        if (projects.stream().noneMatch(p -> p.getProjectName().equalsIgnoreCase(project.getProjectName()))) {
            projects.add(project);
            System.out.println("Project added: " + project.getProjectName());
        } else {
            System.out.println("Duplicate project ignored: " + project.getProjectName());
        }
    }


    // Toggles the visibility of a project
    public boolean toggleProjectVisibility(String projectName, boolean visibility) {
        for (BTOProject project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                project.setVisibility(visibility);
                System.out.println("Visibility of project '" + projectName + "' set to: " + visibility);
                return true;
            }
        }
        System.out.println("Project '" + projectName + "' not found.");
        return false;
    }

    // Retrieve available projects for officer registration
    public List<BTOProject> getAvailableProjectsForRegistration(HDBOfficer officer) {
        return projects.stream()
                .filter(BTOProject::isVisible) // Filter visible projects
                .filter(project -> officer.getAssignedProject() == null || // Not already assigned
                        !officer.getAssignedProject().overlaps(project)) // No overlap with current project
                .distinct() // Ensure no duplicates
                .collect(Collectors.toList());
    }


    // Retrieve visible projects for applicants based on eligibility
    public List<BTOProject> getVisibleProjects(Applicant applicant) {
        return projects.stream()
                .filter(project -> project.isVisible() // Check visibility
                        && isEligibleForFlatType(applicant, project)) // Check applicant eligibility
                .collect(Collectors.toList());
    }

    // Helper method to check applicant eligibility for a flat type
    private boolean isEligibleForFlatType(Applicant applicant, BTOProject project) {
        if ("Single".equalsIgnoreCase(applicant.getMaritalStatus())) {
            return applicant.getAge() >= 35 && "2-Room".equalsIgnoreCase(project.getType1());
        } else if ("Married".equalsIgnoreCase(applicant.getMaritalStatus())) {
            return applicant.getAge() >= 21; // Married applicants must be >= 21
        }
        return false; // Default: Not eligible
    }

    // Retrieve projects managed by a specific manager based on NRIC
    public List<BTOProject> getProjectsByManager(String managerNric) {
        return projects.stream()
                .filter(project -> project.getManager().equalsIgnoreCase(managerNric))
                .collect(Collectors.toList());
    }

    // Delete a project by its name
    public boolean deleteProject(String projectName) {
        return projects.removeIf(project -> project.getProjectName().equalsIgnoreCase(projectName));
    }

    // Update flat availability in the project
    public boolean updateFlatAvailability(BTOProject project, String flatType, int newCount) {
        if (project.getFlatAvailability().containsKey(flatType)) {
            project.getFlatAvailability().put(flatType, newCount);
            System.out.println("Flat availability updated: " + flatType + " now has " + newCount + " unit(s).");
            return true;
        } else {
            System.out.println("Invalid flat type: " + flatType);
            return false;
        }
    }

    // Retrieve a project by its name
    public BTOProject getProjectByName(String projectName) {
        for (BTOProject project : projects) { // Use the `projects` list to find the project
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }
        return null; // Return null if no matching project is found
    }
}
