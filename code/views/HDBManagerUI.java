package views;

import controllers.ApplicationController;
import controllers.ManagerController;
import controllers.ProjectController;
import models.HDBManager;
import models.enums.FlatType;
import models.enums.ApplicationStatus;
import models.Application;
import models.BTOProject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

import java.io.*;

public class HDBManagerUI {
    private final Scanner scanner;
    private final HDBManager manager;
    private final ManagerController managerController;
    private final ProjectController projectController;
    private final ApplicationController applicationController;

    public HDBManagerUI(Scanner scanner, HDBManager manager, 
    ManagerController managerController, 
    ProjectController projectController,
    ApplicationController applicationController) {
        this.scanner = scanner;
        this.manager = manager;
        this.managerController = managerController;
        this.projectController = projectController;
        this.applicationController = applicationController;
    }

    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sdf.setLenient(false);  // Ensures strict date parsing
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Using current date.");
            return new Date();
        }
    }


    public void showMenu() {
        boolean shouldContinue = true;

        while (shouldContinue) {
            System.out.println("\n=== HDB Manager Dashboard (NRIC: " + manager.getNric() + ") ===");
            System.out.println("1. Create New BTO Project");
            System.out.println("2. Edit Existing Project");
            System.out.println("3. Delete Project");
            System.out.println("4. Toggle Project Visibility");
            System.out.println("5. Approve Officer Registrations");
            System.out.println("6. Approve Applications");
            System.out.println("7. Approve Withdrawal Requests");
            System.out.println("8. Generate Reports");
            System.out.println("9. Filter My Projects");
            System.out.println("10. Change Password");
            System.out.println("11. Logout");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> createNewProject();
                case 2 -> editProject();
                case 3 -> deleteProject();
                case 4 -> toggleProjectVisibility();
                case 5 -> approveOfficerRegistrations();
                case 6 -> approveApplications();
                //case 7 -> approveWithdrawalRequests();
                case 8 -> generateReports();
                case 9 -> filterMyProjects();
                case 10 -> changePassword(scanner);
                case 11 -> {
                    System.out.println("Logging out...");
                    shouldContinue = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void createNewProject() {
        System.out.println("\n=== Create New BTO Project ===");

        // Gather project details from the manager
        System.out.print("Project Name: ");
        String name = scanner.nextLine();

        System.out.print("Neighborhood: ");
        String neighborhood = scanner.nextLine();

        System.out.print("Flat Type 1 (e.g., 2-Room): ");
        String type1 = scanner.nextLine();
        if (!type1.equalsIgnoreCase("2-room") && !type1.equalsIgnoreCase("3-room")) {
            System.out.println("Invalid Flat Type 1. Only 2-room and 3-room are allowed.");
            return;
        }

        System.out.print("Units Available for " + type1 + ": ");
        int unitsType1 = scanner.nextInt();

        System.out.print("Price for " + type1 + ": ");
        double priceType1 = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Flat Type 2 (e.g., 3-Room): ");
        String type2 = scanner.nextLine();
        if (!type2.equalsIgnoreCase("2-room") && !type2.equalsIgnoreCase("3-room")) {
            System.out.println("Invalid Flat Type 2. Only 2-room and 3-room are allowed.");
            return;
        }

        System.out.print("Units Available for " + type2 + ": ");
        int unitsType2 = scanner.nextInt();

        System.out.print("Price for " + type2 + ": ");
        double priceType2 = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Application Opening Date (MM/dd/yyyy): ");
        String openingDateStr = scanner.nextLine();
        Date openingDate = parseDate(openingDateStr);

        System.out.print("Application Closing Date (MM/dd/yyyy): ");
        String closingDateStr = scanner.nextLine();
        Date closingDate = parseDate(closingDateStr);

        System.out.print("Officer Slots Available: ");
        int officerSlot = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Initialize the project with an empty list of officers
        BTOProject newProject = new BTOProject(
            name,
            neighborhood,
            type1,
            unitsType1,
            priceType1,
            type2,
            unitsType2,
            priceType2,
            openingDate,
            closingDate,
            manager.getNric(), // Manager in charge
            officerSlot,
            new String[0] // Empty officers array
        );

        projectController.createProject(newProject);
        System.out.println("Project created successfully!");
    }


    private void editProject() {
        System.out.println("\n=== Edit Project ===");

        // Fetch the manager's projects
        List<BTOProject> myProjects = projectController.getProjectsByManager(manager.getNric());
        if (myProjects.isEmpty()) {
            System.out.println("No projects found that you manage.");
            return;
        }

        // Display the list of projects to choose from
        System.out.println("\nYour Projects:");
        for (int i = 0; i < myProjects.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, myProjects.get(i).getName());
        }

        // Get user input for the project selection
        System.out.print("Select project to edit (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 0) return;
        if (choice < 1 || choice > myProjects.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        BTOProject project = myProjects.get(choice - 1); // Get the selected project

        // Show editing options
        System.out.println("\nEditing Project: " + project.getName());
        System.out.println("1. Edit Flat Type 1 (" + project.getType1() + ")");
        System.out.println("2. Edit Flat Type 2 (" + project.getType2() + ")");
        System.out.println("3. Edit Application Dates");
        System.out.println("4. Edit Officer Slots");
        System.out.println("5. Cancel");
        System.out.print("Choose what to edit: ");
        int editChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Process the editing choice
        switch (editChoice) {
            case 1 -> {
                System.out.print("New Flat Type 1 (e.g., 2-room): ");
                String newType1 = scanner.nextLine();
                if (!newType1.equalsIgnoreCase("2-room") && !newType1.equalsIgnoreCase("3-room")) {
                    System.out.println("Invalid Flat Type 1. Only 2-room and 3-room are allowed.");
                    return;
                }
                System.out.print("New Units for " + newType1 + ": ");
                int newUnits1 = scanner.nextInt();
                System.out.print("New Price for " + newType1 + ": ");
                double newPrice1 = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                // Update the project details
                project.setType1(newType1);
                project.setUnitsType1(newUnits1);
                project.setPriceType1(newPrice1);
                System.out.println("Flat Type 1 updated successfully!");
            }
            case 2 -> {
                System.out.print("New Flat Type 2 (e.g., 3-room): ");
                String newType2 = scanner.nextLine();
                if (!newType2.equalsIgnoreCase("2-room") && !newType2.equalsIgnoreCase("3-room")) {
                    System.out.println("Invalid Flat Type 2. Only 2-room and 3-room are allowed.");
                    return;
                }
                System.out.print("New Units for " + newType2 + ": ");
                int newUnits2 = scanner.nextInt();
                System.out.print("New Price for " + newType2 + ": ");
                double newPrice2 = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                // Update the project details
                project.setType2(newType2);
                project.setUnitsType2(newUnits2);
                project.setPriceType2(newPrice2);
                System.out.println("Flat Type 2 updated successfully!");
            }
            case 3 -> {
                System.out.print("New Opening Date (YYYY-MM-DD): ");
                String openingDateStr = scanner.nextLine();
                Date newOpeningDate = parseDate(openingDateStr);

                System.out.print("New Closing Date (YYYY-MM-DD): ");
                String closingDateStr = scanner.nextLine();
                Date newClosingDate = parseDate(closingDateStr);

                // Update the project dates
                project.setOpeningDate(newOpeningDate);
                project.setClosingDate(newClosingDate);
                System.out.println("Application dates updated successfully!");
            }
            case 4 -> {
                System.out.print("New Officer Slots: ");
                int newOfficerSlots = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Update the officer slots
                project.setOfficerSlot(newOfficerSlots);
                System.out.println("Officer slots updated successfully!");
            }
            case 5 -> System.out.println("Edit cancelled.");
            default -> System.out.println("Invalid choice.");
        }
    }

    private void toggleProjectVisibility() {
        System.out.println("\n=== Toggle Project Visibility ===");
        List<BTOProject> myProjects = projectController.getProjectsByManager(manager.getNric());
        
        if (myProjects.isEmpty()) {
            System.out.println("No projects found that you manage.");
            return;
        }
        
        System.out.println("\nYour Projects:");
        for (int i = 0; i < myProjects.size(); i++) {
            System.out.printf("%d. %s (Visible: %s)\n", 
                i + 1, 
                myProjects.get(i).getName(), 
                myProjects.get(i).isVisible() ? "Yes" : "No");
        }
        
        System.out.print("Select project to toggle (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (choice == 0) return;
        if (choice < 1 || choice > myProjects.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        
        BTOProject project = myProjects.get(choice - 1);
        boolean newVisibility = !project.isVisible();
        
        if (projectController.toggleProjectVisibility(project.getName(), newVisibility)) {
            System.out.printf("Visibility for %s set to: %s\n", 
                project.getName(), 
                newVisibility ? "Visible" : "Hidden");
        } else {
            System.out.println("Failed to update visibility.");
        }
    }

    private void filterMyProjects() {
        System.out.println("\n=== My Projects ===");
        List<BTOProject> myProjects = projectController.getProjectsByManager(manager.getNric());
        
        if (myProjects.isEmpty()) {
            System.out.println("No projects found that you manage.");
            return;
        }
        
        System.out.println("\nProjects Under Your Management:");
        myProjects.forEach(project -> {
            System.out.printf("- %s (Visible: %s)\n", 
                project.getName(), 
                project.isVisible() ? "Yes" : "No");
            System.out.printf("  Types: %s (%d units), %s (%d units)\n",
                project.getType1(), project.getUnitsType1(),
                project.getType2(), project.getUnitsType2());
            System.out.printf("  Application Period: %s to %s\n",
                project.getOpeningDate(), project.getClosingDate());
        });
    }

    private void approveApplications() {
        System.out.println("\n=== Approve/Reject Applications ===");

        // Fetch projects managed by this manager
        List<BTOProject> myProjects = projectController.getProjectsByManager(manager.getNric());

        if (myProjects.isEmpty()) {
            System.out.println("No projects found that you manage.");
            return;
        }

        // Display projects for selection
        System.out.println("\nYour Projects:");
        for (int i = 0; i < myProjects.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, myProjects.get(i).getName());
        }

        System.out.print("Select project to view applications (0 to cancel): ");
        int projectChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (projectChoice == 0) return;
        if (projectChoice < 1 || projectChoice > myProjects.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        BTOProject selectedProject = myProjects.get(projectChoice - 1);

        // Fetch pending applications for the selected project
        List<Application> pendingApplications = applicationController.getApplicationsByProjectAndStatus(
            selectedProject.getName(),
            ApplicationStatus.PENDING
        );

        if (pendingApplications.isEmpty()) {
            System.out.println("No pending applications for this project.");
            return;
        }

        System.out.println("\nPending Applications:");
        for (int i = 0; i < pendingApplications.size(); i++) {
            Application app = pendingApplications.get(i);
            System.out.printf("%d. Applicant: %s (NRIC: %s)\n", 
                i + 1, 
                app.getApplicantName(), 
                app.getApplicantNric());
        }

        System.out.print("Select application to process (0 to cancel): ");
        int appChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (appChoice == 0) return;
        if (appChoice < 1 || appChoice > pendingApplications.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Application selectedApp = pendingApplications.get(appChoice - 1);

        System.out.println("\nApplication Details:");
        System.out.printf("Project: %s\n", selectedApp.getProjectName());
        System.out.printf("Flat Type Requested: %s\n", selectedApp.getFlatTypeString());
        System.out.printf("Applicant NRIC: %s\n", selectedApp.getApplicantNric());

        System.out.print("\nApprove this application? (approve/reject/cancel): ");
        String decision = scanner.nextLine().toLowerCase();

        switch (decision) {
            case "approve" -> {
                FlatType flatType = selectedApp.getFlatType();
                if (!flatType.equals(FlatType.TWO_ROOM) && !flatType.equals(FlatType.THREE_ROOM)) {
                    System.out.println("Invalid flat type. Cannot approve.");
                    return;
                }

                // Ensure availability before approving
                if (managerController.approveApplication(selectedApp, selectedProject)) {
                    System.out.println("Application approved successfully!");
                } else {
                    System.out.println("Failed to approve application (no available flats).");
                }
            }
            case "reject" -> {
                applicationController.updateApplicationStatus(
                    selectedApp.getApplicationId(),
                    ApplicationStatus.REJECTED
                );
                System.out.println("Application rejected.");
            }
            case "cancel" -> System.out.println("Operation cancelled.");
            default -> System.out.println("Invalid choice. Operation cancelled.");
        }
    }


    

    private void approveOfficerRegistrations() {
        System.out.println("\nManager Dashboard:");
    
        // Display all officer registrations from specified file path
        String filePath = "data/OfficerRegistrations.csv"; 
        displayAllOfficerRegistrations(filePath);
        
        System.out.println("1. Manage Officer Registrations");
        System.out.println("2. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        switch (choice) {
            case 1 -> {
                System.out.print("Enter officer NRIC to handle registration: ");
                String officerNric = scanner.nextLine();
                System.out.print("Approve registration? (true/false): ");
                boolean approve = scanner.nextBoolean();
                scanner.nextLine(); // Consume the newline
                managerController.handleOfficerRegistration(officerNric, approve);
            }
            case 2 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid option. Returning to dashboard.");
        }
    }

    private void ensureFileExists(String filePath) {
    File file = new File(filePath);
    if (!file.exists()) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("NRIC,Name,Status"); // Header row
            writer.println("T1234567A,John Doe,PENDING"); // Sample record
        } catch (IOException e) {
            System.out.println("Error creating registration file: " + e.getMessage());
        }
    }
}

    private void displayAllOfficerRegistrations(String filePath) {
        System.out.println("\nAll Officer Registrations:");
        System.out.println("-------------------------");
        
        // Ensure the file exists before attempting to read
        ensureFileExists(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean hasRecords = false;
            
            // Print header
            System.out.printf("%-12s %-20s %-10s%n", "NRIC", "Name", "Status");
            System.out.println("----------------------------------------");
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    hasRecords = true;
                    System.out.printf("%-12s %-20s %-10s%n", 
                        values[0], values[1], values[2]);
                }
            }
            
            if (!hasRecords) {
                System.out.println("No officer registrations found.");
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("Error: Registration file not found at " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading officer registrations: " + e.getMessage());
        }
        
        System.out.println(); // Add empty line for better formatting
    }


    private void generateReports() {
        // Implementation for generating reports
    }

    private void changePassword(Scanner scanner) {
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();
        manager.setPassword(newPassword);
        System.out.println("Password changed successfully.");
    }

    private void deleteProject() {
            System.out.println("\n=== Delete Project ===");
            List<BTOProject> myProjects = projectController.getProjectsByManager(manager.getNric());
            
            if (myProjects.isEmpty()) {
                System.out.println("No projects found that you manage.");
                return;
            }
            
            System.out.println("\nYour Projects:");
            for (int i = 0; i < myProjects.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, myProjects.get(i).getName());
            }
            
            System.out.print("Select project to delete (0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (choice == 0) return;
            if (choice < 1 || choice > myProjects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            String projectName = myProjects.get(choice - 1).getName();
            System.out.print("Are you sure you want to delete " + projectName + "? (yes/no): ");
            String confirmation = scanner.nextLine();
            
            if ("yes".equalsIgnoreCase(confirmation)) {
                if (projectController.deleteProject(projectName)) {
                    System.out.println("Project deleted successfully!");
                } else {
                    System.out.println("Failed to delete project.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        }

    
}
