package models;

import models.enums.ApplicationStatus;
import models.enums.FlatType;

public class Application {
    // Attributes
    private int applicationId;        // Unique identifier for the application
    private String applicantNric;     // NRIC of the applicant who submitted the application
    private String applicantName;     // Name of the applicant
    private String projectName;       // Name of the BTO project
    private FlatType flatType;        // FlatType (e.g., "2-Room", "3-Room")
    private ApplicationStatus status; // Current status of the application (e.g., "Pending", "Approved")

    // Constructor
    public Application(int applicationId, String applicantNric, String applicantName, String projectName, FlatType flatType, ApplicationStatus status) {
        this.applicationId = applicationId;
        this.applicantNric = applicantNric;
        this.applicantName = applicantName; 
        this.projectName = projectName;
        this.flatType = flatType; // Use FlatType enum
        this.status = status;
    }

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicantNric() {
        return applicantNric;
    }

    public void setApplicantNric(String applicantNric) {
        this.applicantNric = applicantNric;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    // Method: Update Application Status
    public void updateStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
    }

    // Override toString for Displaying Application Details
    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", applicantNric='" + applicantNric + '\'' +
                ", applicantName='" + applicantName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", flatType=" + flatType +
                ", status=" + status +
                '}';
    }

    // Add a method for getting the string representation of FlatType
    public String getFlatTypeString() {
        return flatType != null ? flatType.getDisplayName() : "Unknown";
    }
}
