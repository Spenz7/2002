package controllers;

import models.enums.ApplicationStatus;
import models.enums.FlatType;
import models.HDBOfficer;
import models.Application;
import models.BTOProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.util.Arrays;



public class ManagerController {
    private List<HDBOfficer> officers; // List of all registered HDB officers
    private List<BTOProject> availableProjects; // List of all available BTO projects

    public ManagerController(List<HDBOfficer> officers, List<BTOProject> availableProjects) {
        this.officers = officers;
        this.availableProjects = availableProjects; // Initialize availableProjects
    }

    // Method to retrieve the project by name
    private BTOProject getProjectByName(String projectName) {
        // Use availableProjects to find the project
        for (BTOProject project : availableProjects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }
        return null; // Return null if no matching project is found
    }

    // Method to retrieve the officer by NRIC
    private HDBOfficer getOfficerByNric(String officerNric) {
        // Use officers list to find the officer
        for (HDBOfficer officer : officers) {
            if (officer.getNric().equalsIgnoreCase(officerNric)) {
                return officer;
            }
        }
        return null; // Return null if no matching officer is found
    }

    // Approves or denies officer registrations based on eligibility criteria
    public void handleOfficerRegistration(String officerNric, boolean approve) {

        String registrationsFilePath = "data/OfficerRegistrations.csv";
        String officersFilePath = "data/OfficerList.csv";
        List<String[]> registrations = new ArrayList<>();
        boolean officerFound = false;
        boolean registrationFound = false;
        String projectName = "";

        // Read OfficerList.csv to confirm if the officer exists
        try (BufferedReader br = new BufferedReader(new FileReader(officersFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 2 && values[1].equalsIgnoreCase(officerNric)) {
                    officerFound = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading officer list file: " + officersFilePath + " - " + e.getMessage());
        }

        // Read OfficerRegistrations.csv to find the matching registration
        try (BufferedReader br = new BufferedReader(new FileReader(registrationsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                registrations.add(values);

                if (values.length >= 3 && values[0].equalsIgnoreCase(officerNric)) {
                    registrationFound = true;
                    projectName = values[1]; // Get the project name from the registration
                    if (approve) {
                        values[2] = "APPROVED"; // Update status to APPROVED
                    } else {
                        values[2] = "REJECTED"; // Update status to REJECTED
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading registration file: " + registrationsFilePath + " - " + e.getMessage());
        }

        // Handle officer registration result
        if (!officerFound) {
            System.out.println("Officer '" + officerNric + "' not found in OfficerList.");
        } else if (!registrationFound) {
            System.out.println("Registration request for officer '" + officerNric + "' not found.");
        } else {
            // Retrieve the project object corresponding to the projectName
            BTOProject approvedProject = getProjectByName(projectName);

            if (approvedProject != null) {
                // Assign the project to the officer
                HDBOfficer officer = getOfficerByNric(officerNric); // You need a method to get the officer object by NRIC
                officer.setAssignedProject(approvedProject); // Assign the project to the officer
                System.out.println("Project '" + approvedProject.getProjectName() + "' assigned to officer '" + officerNric + "'");
            }

            // Write updated registrations back to the file
            try (PrintWriter pw = new PrintWriter(new FileWriter(registrationsFilePath))) {
                for (String[] registration : registrations) {
                    pw.println(String.join(",", registration));
                }
                System.out.println("Registration updated successfully.");
            } catch (IOException e) {
                System.out.println("Error writing to registration file: " + registrationsFilePath + " - " + e.getMessage());
            }
        }
    }

    private void updateApplicantList(Application application) {
        String applicantListFilePath = "data/ApplicantList.csv";
        List<String[]> applicants = new ArrayList<>();
        boolean applicantFound = false;

        // Step 1: Read ApplicantList.csv
        try (BufferedReader br = new BufferedReader(new FileReader(applicantListFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Ensure each row has at least 8 columns (extend if necessary)
                if (values.length < 8) {
                    values = Arrays.copyOf(values, 8); // Extend to 8 columns, defaulting to empty strings
                }

                applicants.add(values);

                // Check for matching NRIC and update values
                if (values.length >= 8 && values[1].equalsIgnoreCase(application.getApplicant().getNric())) {
                    applicantFound = true;
                    values[5] = "Successful"; // Update Application Status column
                    values[6] = application.getFlatTypeString(); // Update Flat Type column
                    values[7] = application.getProjectName();    // Update Project Name column
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading applicants file: " + e.getMessage());
        }

        // Step 2: Write updated data back to ApplicantList.csv
        if (applicantFound) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(applicantListFilePath))) {
                for (String[] applicant : applicants) {
                    pw.println(String.join(",", applicant));
                }
                System.out.println("ApplicantList.csv updated successfully.");
            } catch (IOException e) {
                System.err.println("Error writing to applicants file: " + e.getMessage());
            }
        } else {
            System.out.println("Applicant with NRIC '" + application.getApplicant().getNric() + "' not found in ApplicantList.csv.");
        }
    }

    // Approves an application for a project
    public boolean approveApplication(Application application, BTOProject project) {
        // Retrieve the flat type as a string and convert to FlatType
        String flatTypeString = application.getFlatTypeString(); // Ensure this method returns a string representation
        System.out.println("Flat type requested: " + flatTypeString);
        FlatType flatType = FlatType.fromString(flatTypeString);

        // Check flat availability
        int availableFlats = project.getAvailableFlats(flatType);
        if (availableFlats <= 0) {
            return false; // No flats available
        }

        // Update project flat count
        if (flatType.getDisplayName().equalsIgnoreCase(project.getType1())) {
            project.setUnitsType1(project.getUnitsType1() - 1);
        } else if (flatType.getDisplayName().equalsIgnoreCase(project.getType2())) {
            project.setUnitsType2(project.getUnitsType2() - 1);
        }

        // Update application status
        application.setStatus(ApplicationStatus.SUCCESSFUL); // Ensure SUCCESSFUL exists in ApplicationStatus

        // Update ApplicantList.csv to reflect approval
        updateApplicantList(application);

        return true;
        }

    // Retrieves a list of all officers
    public List<HDBOfficer> getAllOfficers() {
        return officers; // Returns the entire list of officers for the manager to review
    }

    public boolean isProjectValid(String projectName) {
        // Skip the file reading logic
        return true;  // Always return true, allowing all projects
    }
}
