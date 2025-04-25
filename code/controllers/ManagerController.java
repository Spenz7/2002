package controllers;

import models.enums.ApplicationStatus;
import models.enums.FlatType;
import models.HDBOfficer;
import models.Application;
import models.BTOProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ManagerController {
    private List<HDBOfficer> officers; // List of all registered HDB officers

    public ManagerController(List<HDBOfficer> officers) {
        this.officers = officers;
    }

    // Approves or denies officer registrations based on eligibility criteria
    public void handleOfficerRegistration(String officerNric, boolean approve) {
        
        String registrationsFilePath = "data/OfficerRegistrations.csv";
        String officersFilePath = "data/OfficerList.csv";


        boolean officerFound = false;
        boolean registrationFound = false;
        String projectName = "";
        List<String[]> registrations = new ArrayList<>();

        // Read OfficerList.csv to confirm if the officer exists
        try (BufferedReader br = new BufferedReader(new FileReader(officersFilePath.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 2 && values[1].equalsIgnoreCase(officerNric)) {
                    officerFound = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading officer list file: " + officersFilePath.toString() + " - " + e.getMessage());
        }

        // Read OfficerRegistrations.csv to find the matching registration
        try (BufferedReader br = new BufferedReader(new FileReader(registrationsFilePath.toString()))) {
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
            System.out.println("Error reading registration file: " + registrationsFilePath.toString() + " - " + e.getMessage());
        }


        // Handle officer registration result
        if (!officerFound) {
            System.out.println("Officer '" + officerNric + "' not found in OfficerList.");
        } else if (!registrationFound) {
            System.out.println("Registration request for officer '" + officerNric + "' not found.");
        } else {
            // Write updated registrations back to the file
            try (PrintWriter pw = new PrintWriter(registrationsFilePath.toString())) {
                for (String[] registration : registrations) {
                    pw.println(String.join(",", registration));
                }
                System.out.println("Registration updated successfully.");
            } catch (IOException e) {
                System.out.println("Error writing to registration file: " + registrationsFilePath.toString() + " - " + e.getMessage());
            }
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
